// Cache para almacenar datos
const dataCache = {
    courses: new Map(),
    users: new Map(),
    tickets: new Map(),
    async loadCourses() {
        if (this.courses.size === 0) {
            const response = await fetch('/api/courses');
            const courses = await response.json();
            courses.forEach(course => this.courses.set(course.id, course));
        }
        return Array.from(this.courses.values());
    },
    async loadUsers(forceReload = false) {
        console.log('Iniciando carga de usuarios...'); // Log de depuración
        if (this.users.size === 0 || forceReload) {
            try {
                console.log('Haciendo petición a /api/users...'); // Log de depuración
                const response = await fetch('/api/users');
                console.log('Respuesta recibida:', response.status); // Log de depuración
                
                // Verificar el tipo de contenido
                const contentType = response.headers.get('content-type');
                console.log('Tipo de contenido:', contentType);
                
                const users = await response.json();
                console.log('Respuesta completa del servidor:', users); // Log detallado
                
                if (!Array.isArray(users)) {
                    console.error('La respuesta no es un array de usuarios:', users);
                    return [];
                }

                // Limpiar el caché si es una recarga forzada
                if (forceReload) {
                    this.users.clear();
                }

                users.forEach(user => {
                    console.log('Procesando usuario:', {
                        id: user.id,
                        firstName: user.firstName,
                        lastName: user.lastName,
                        role: user.role,
                        email: user.email
                    }); // Log detallado
                    this.users.set(user.id, user);
                });
                
                console.log('Total de usuarios cargados:', this.users.size); // Log de depuración
            } catch (error) {
                console.error('Error al cargar usuarios:', error);
                return [];
            }
        } else {
            console.log('Usando usuarios en caché:', this.users.size); // Log de depuración
        }
        return Array.from(this.users.values());
    },
    async loadTickets() {
        if (this.tickets.size === 0) {
            try {
                const response = await fetch('/api/support/tickets');
                const tickets = await response.json();
                console.log('Tickets cargados:', tickets); // Log para depuración
                tickets.forEach(ticket => {
                    // Asegurar que los campos existan
                    const processedTicket = {
                        ...ticket,
                        subject: ticket.subject || ticket.title || 'Sin asunto', // Fallback para compatibilidad
                        description: ticket.description || 'Sin descripción'
                    };
                    this.tickets.set(ticket.id, processedTicket);
                });
            } catch (error) {
                console.error('Error al cargar tickets:', error);
                throw error;
            }
        }
        return Array.from(this.tickets.values());
    },
    getCourseName(id) {
        return this.courses.get(id)?.title || `Curso #${id}`;
    },
    getUserName(id) {
        const user = this.users.get(id);
        return user ? `${user.firstName} ${user.lastName}` : `Usuario #${id}`;
    },
    getTicketStatus(status) {
        const statusMap = {
            'ABIERTO': { text: 'Abierto', class: 'badge bg-danger' },
            'ASIGNADO': { text: 'Asignado', class: 'badge bg-info' },
            'EN_PROCESO': { text: 'En Proceso', class: 'badge bg-warning' },
            'RESUELTO': { text: 'Resuelto', class: 'badge bg-success' },
            'CERRADO': { text: 'Cerrado', class: 'badge bg-secondary' }
        };
        return statusMap[status] || { text: status, class: 'badge bg-light' };
    }
};

// Función para limpiar las clases active del menú
function clearActiveMenu() {
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
}

// Función para cargar el contenido según la sección seleccionada
function loadContent(section) {
    console.log('Intentando cargar sección:', section);
    const mainContent = document.getElementById('mainContent');
    console.log('Elemento mainContent encontrado:', !!mainContent);
    
    // Limpiar clases active y establecer la nueva
    clearActiveMenu();
    const activeLink = document.querySelector(`.nav-link[onclick*="loadContent('${section}')"]`);
    if (activeLink) {
        activeLink.classList.add('active');
    }
    
    // Precargar datos necesarios
    console.log('Iniciando precarga de datos...');
    Promise.all([
        dataCache.loadCourses(),
        dataCache.loadUsers(),
        dataCache.loadTickets()
    ]).then(() => {
        console.log('Datos precargados exitosamente');
        switch(section) {
            case 'courses':
                console.log('Cargando sección de cursos...');
                loadCourses();
                break;
            case 'content':
                console.log('Cargando sección de contenido...');
                loadCourseContent();
                break;
            case 'comments':
                console.log('Cargando sección de comentarios...');
                loadCourseComments();
                break;
            case 'support':
                console.log('Cargando sección de soporte...');
                loadSupportTickets();
                break;
            default:
                console.log('Sección no reconocida:', section);
                mainContent.innerHTML = '<h2>Sección en desarrollo</h2>';
        }
    }).catch(error => {
        console.error('Error al precargar datos:', error);
        mainContent.innerHTML = '<div class="alert alert-danger">Error al cargar los datos necesarios</div>';
    });
}

// Funciones para manejar los cursos
async function loadCourses() {
    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Cursos</h2>
            <div>
                <button class="btn btn-outline-primary me-2" onclick="showFilterModal()">
                    <i class="bi bi-funnel"></i> Filtrar
                </button>
                <button class="btn btn-primary" onclick="showAddCourseModal()">
                    <i class="bi bi-plus"></i> Nuevo Curso
                </button>
            </div>
        </div>
        <div class="row mb-3">
            <div class="col">
                <div class="input-group">
                    <input type="text" class="form-control" id="courseSearch" 
                           placeholder="Buscar cursos..." onkeyup="filterCourses()">
                    <button class="btn btn-outline-secondary" type="button" onclick="clearSearch()">
                        <i class="bi bi-x"></i>
                    </button>
                </div>
            </div>
        </div>
        <div id="coursesList" class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <!-- Los cursos se cargarán aquí -->
        </div>
    `;

    try {
        const response = await fetch('/api/courses');
        const courses = await response.json();
        displayCourses(courses);
    } catch (error) {
        console.error('Error:', error);
        mainContent.innerHTML += '<div class="alert alert-danger">Error al cargar los cursos</div>';
    }
}

function displayCourses(courses) {
    const coursesList = document.getElementById('coursesList');
    if (!coursesList) return;

    coursesList.innerHTML = courses.map(course => `
        <div class="col">
            <div class="card h-100">
                <img src="${course.image}" class="card-img-top" alt="${course.title}" 
                     style="height: 200px; object-fit: cover;">
                <div class="card-body">
                    <h5 class="card-title">${course.title}</h5>
                    <p class="card-text text-truncate">${course.description}</p>
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <span class="badge bg-primary">${course.status}</span>
                        <span class="text-primary fw-bold">$${course.price}</span>
                    </div>
                    <div class="small text-muted mb-2">
                        <div><i class="bi bi-person"></i> Instructor: ${dataCache.getUserName(course.instructorId)}</div>
                        <div><i class="bi bi-calendar"></i> Publicado: ${new Date(course.publishDate).toLocaleDateString()}</div>
                    </div>
                </div>
                <div class="card-footer bg-transparent">
                    <div class="btn-group w-100">
                        <button class="btn btn-outline-primary" onclick="viewCourse(${course.id})">
                            <i class="bi bi-eye"></i> Ver
                        </button>
                        <button class="btn btn-outline-secondary" onclick="editCourse(${course.id})">
                            <i class="bi bi-pencil"></i> Editar
                        </button>
                        <button class="btn btn-outline-danger" onclick="deleteCourse(${course.id})">
                            <i class="bi bi-trash"></i>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    `).join('');
}

function filterCourses() {
    const searchTerm = document.getElementById('courseSearch').value.toLowerCase();
    const courses = Array.from(dataCache.courses.values());
    
    const filteredCourses = courses.filter(course => 
        course.title.toLowerCase().includes(searchTerm) ||
        course.description.toLowerCase().includes(searchTerm) ||
        dataCache.getUserName(course.instructorId).toLowerCase().includes(searchTerm)
    );
    
    displayCourses(filteredCourses);
}

function clearSearch() {
    document.getElementById('courseSearch').value = '';
    displayCourses(Array.from(dataCache.courses.values()));
}

async function viewCourse(id) {
    try {
        const response = await fetch(`/api/courses/${id}`);
        const course = await response.json();
        
        const modalHtml = `
            <div class="modal fade" id="courseModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${course.title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-6">
                                    <img src="${course.image}" class="img-fluid rounded mb-3" alt="${course.title}">
                                </div>
                                <div class="col-md-6">
                                    <h6>Detalles del Curso</h6>
                                    <p><strong>Descripción:</strong></p>
                                    <p>${course.description}</p>
                                    <p><strong>Instructor:</strong> ${dataCache.getUserName(course.instructorId)}</p>
                                    <p><strong>Gestor:</strong> ${dataCache.getUserName(course.managerId)}</p>
                                    <p><strong>Precio:</strong> $${course.price}</p>
                                    <p><strong>Estado:</strong> <span class="badge bg-primary">${course.status}</span></p>
                                    <p><strong>Fecha de Publicación:</strong> ${new Date(course.publishDate).toLocaleDateString()}</p>
                                </div>
                            </div>
                            <div class="mt-4">
                                <h6>Contenido del Curso</h6>
                                <div id="courseContent" class="list-group">
                                    <!-- El contenido se cargará aquí -->
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                            <button type="button" class="btn btn-primary" onclick="editCourse(${course.id})">Editar Curso</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHtml);
        const modal = new bootstrap.Modal(document.getElementById('courseModal'));
        
        // Cargar el contenido del curso
        const contentResponse = await fetch(`/api/courses/content/course/${id}`);
        const content = await contentResponse.json();
        const contentList = document.getElementById('courseContent');
        
        if (content.length === 0) {
            contentList.innerHTML = '<p class="text-muted">No hay contenido disponible.</p>';
        } else {
            contentList.innerHTML = content.map(item => `
                <div class="list-group-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <h6 class="mb-1">${item.title}</h6>
                            <small class="text-muted">Tipo: ${item.contentType}</small>
                        </div>
                        <a href="${item.url}" target="_blank" class="btn btn-sm btn-outline-primary">
                            <i class="bi bi-box-arrow-up-right"></i> Ver
                        </a>
                    </div>
                </div>
            `).join('');
        }

        modal.show();

        document.getElementById('courseModal').addEventListener('hidden.bs.modal', function () {
            this.remove();
        });
    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar los detalles del curso');
    }
}

async function deleteCourse(id) {
    if (confirm('¿Estás seguro de eliminar este curso?')) {
        try {
            const response = await fetch(`/api/courses/${id}`, { method: 'DELETE' });
            if (response.ok) {
                dataCache.courses.delete(id);
                loadCourses();
            } else {
                throw new Error('Error al eliminar el curso');
            }
        } catch (error) {
            console.error('Error:', error);
            alert('Error al eliminar el curso');
        }
    }
}

// Funciones para manejar el contenido de cursos
async function loadCourseContent() {
    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Contenido de Cursos</h2>
            <button class="btn btn-primary" onclick="showAddContentModal()">
                <i class="bi bi-plus"></i> Nuevo Contenido
            </button>
        </div>
        <div id="contentList" class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Título</th>
                        <th>Tipo</th>
                        <th>Curso</th>
                        <th>Orden</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="contentTableBody"></tbody>
            </table>
        </div>
    `;

    try {
        const response = await fetch('/api/courses/content');
        const content = await response.json();
        const tbody = document.getElementById('contentTableBody');
        tbody.innerHTML = content.map(item => `
            <tr>
                <td>${item.id}</td>
                <td>${item.title}</td>
                <td>${item.contentType}</td>
                <td>${dataCache.getCourseName(item.courseId)}</td>
                <td>${item.orderIndex}</td>
                <td>
                    <button class="btn btn-sm btn-info" onclick="editContent(${item.id})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteContent(${item.id})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error:', error);
        mainContent.innerHTML += '<div class="alert alert-danger">Error al cargar el contenido</div>';
    }
}

// Funciones para manejar los comentarios de cursos
async function loadCourseComments() {
    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Comentarios de Cursos</h2>
            <button class="btn btn-primary" onclick="showAddCommentModal()">
                <i class="bi bi-plus"></i> Nuevo Comentario
            </button>
        </div>
        <div id="commentsList" class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Curso</th>
                        <th>Usuario</th>
                        <th>Comentario</th>
                        <th>Calificación</th>
                        <th>Fecha</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="commentsTableBody"></tbody>
            </table>
        </div>
    `;

    try {
        const response = await fetch('/api/courses/comments');
        const comments = await response.json();
        const tbody = document.getElementById('commentsTableBody');
        tbody.innerHTML = comments.map(comment => `
            <tr>
                <td>${comment.id}</td>
                <td>${dataCache.getCourseName(comment.courseId)}</td>
                <td>${dataCache.getUserName(comment.userId)}</td>
                <td>${comment.commentText}</td>
                <td>
                    <div class="d-flex align-items-center">
                        ${comment.rating}
                        <div class="ms-2">
                            ${'★'.repeat(comment.rating)}${'☆'.repeat(5-comment.rating)}
                        </div>
                    </div>
                </td>
                <td>${new Date(comment.createdAt).toLocaleString()}</td>
                <td>
                    <button class="btn btn-sm btn-info" onclick="editComment(${comment.id})">
                        <i class="bi bi-pencil"></i>
                    </button>
                    <button class="btn btn-sm btn-danger" onclick="deleteComment(${comment.id})">
                        <i class="bi bi-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
    } catch (error) {
        console.error('Error:', error);
        mainContent.innerHTML += '<div class="alert alert-danger">Error al cargar los comentarios</div>';
    }
}

// Funciones CRUD para contenido
async function showAddContentModal() {
    // Implementar modal para agregar contenido
    alert('Función en desarrollo');
}

async function editContent(id) {
    // Implementar edición de contenido
    alert('Función en desarrollo');
}

async function deleteContent(id) {
    if (confirm('¿Estás seguro de eliminar este contenido?')) {
        try {
            await fetch(`/api/courses/content/${id}`, { method: 'DELETE' });
            loadCourseContent();
        } catch (error) {
            console.error('Error:', error);
            alert('Error al eliminar el contenido');
        }
    }
}

// Funciones CRUD para comentarios
async function showAddCommentModal() {
    // Implementar modal para agregar comentario
    alert('Función en desarrollo');
}

async function editComment(id) {
    // Implementar edición de comentario
    alert('Función en desarrollo');
}

async function deleteComment(id) {
    if (confirm('¿Estás seguro de eliminar este comentario?')) {
        try {
            await fetch(`/api/courses/comments/${id}`, { method: 'DELETE' });
            loadCourseComments();
        } catch (error) {
            console.error('Error:', error);
            alert('Error al eliminar el comentario');
        }
    }
}

// Agregar función para cargar tickets de soporte
async function loadSupportTickets() {
    console.log('Cargando tickets de soporte...'); // Log para depuración
    const mainContent = document.getElementById('mainContent');
    mainContent.innerHTML = `
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Tickets de Soporte</h2>
            <button class="btn btn-primary" onclick="showAddTicketModal()">
                <i class="bi bi-plus"></i> Nuevo Ticket
            </button>
        </div>
        <div class="row mb-3">
            <div class="col">
                <select class="form-select" id="statusFilter" onchange="filterTickets()">
                    <option value="">Todos los estados</option>
                    <option value="ABIERTO">Abiertos</option>
                    <option value="ASIGNADO">Asignados</option>
                    <option value="EN_PROCESO">En Proceso</option>
                    <option value="RESUELTO">Resueltos</option>
                    <option value="CERRADO">Cerrados</option>
                </select>
            </div>
        </div>
        <div id="ticketsList" class="table-responsive">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Asunto</th>
                        <th>Descripción</th>
                        <th>Usuario</th>
                        <th>Soporte</th>
                        <th>Estado</th>
                        <th>Fecha</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody id="ticketsTableBody"></tbody>
            </table>
        </div>
    `;

    try {
        const tickets = await dataCache.loadTickets();
        console.log('Tickets a mostrar:', tickets); // Log para depuración
        updateTicketsTable(tickets);
    } catch (error) {
        console.error('Error al cargar los tickets:', error);
        mainContent.innerHTML += '<div class="alert alert-danger">Error al cargar los tickets</div>';
    }
}

function updateTicketsTable(tickets) {
    console.log('Actualizando tabla con tickets:', tickets); // Log para depuración
    const tbody = document.getElementById('ticketsTableBody');
    const statusFilter = document.getElementById('statusFilter')?.value;
    
    const filteredTickets = statusFilter ? 
        tickets.filter(ticket => ticket.status === statusFilter) : 
        tickets;

    console.log('Tickets filtrados:', filteredTickets); // Log para depuración

    tbody.innerHTML = filteredTickets.map(ticket => {
        const status = dataCache.getTicketStatus(ticket.status);
        console.log('Procesando ticket:', ticket); // Log para depuración
        return `
            <tr>
                <td>${ticket.id}</td>
                <td>${ticket.subject || 'Sin asunto'}</td>
                <td>
                    <div class="text-truncate" style="max-width: 200px;" title="${ticket.description || 'Sin descripción'}">
                        ${ticket.description || 'Sin descripción'}
                    </div>
                </td>
                <td>${dataCache.getUserName(ticket.userId)}</td>
                <td>${ticket.supportUserId ? dataCache.getUserName(ticket.supportUserId) : 'Sin asignar'}</td>
                <td><span class="${status.class}">${status.text}</span></td>
                <td>${new Date(ticket.createdAt).toLocaleString()}</td>
                <td>
                    <button class="btn btn-sm btn-info" onclick="viewTicket(${ticket.id})">
                        <i class="bi bi-eye"></i>
                    </button>
                    <button class="btn btn-sm btn-warning" onclick="updateTicketStatus(${ticket.id})">
                        <i class="bi bi-arrow-clockwise"></i>
                    </button>
                    ${!ticket.supportUserId ? `
                        <button class="btn btn-sm btn-success" onclick="assignSupportUser(${ticket.id})">
                            <i class="bi bi-person-plus"></i>
                        </button>
                    ` : ''}
                </td>
            </tr>
        `;
    }).join('');
}

function filterTickets() {
    const tickets = Array.from(dataCache.tickets.values());
    updateTicketsTable(tickets);
}

async function viewTicket(id) {
    try {
        const response = await fetch(`/api/support/tickets/${id}`);
        const ticket = await response.json();
        
        const modalHtml = `
            <div class="modal fade" id="ticketModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Ticket #${ticket.id}: ${ticket.subject}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <div class="mb-3">
                                <h6>Detalles del Ticket</h6>
                                <p><strong>Usuario:</strong> ${dataCache.getUserName(ticket.userId)}</p>
                                <p><strong>Estado:</strong> <span class="${dataCache.getTicketStatus(ticket.status).class}">${dataCache.getTicketStatus(ticket.status).text}</span></p>
                                <p><strong>Fecha:</strong> ${new Date(ticket.createdAt).toLocaleString()}</p>
                                <p><strong>Descripción:</strong></p>
                                <div class="border p-2 rounded">${ticket.description}</div>
                            </div>
                            <div class="mb-3">
                                <h6>Respuestas</h6>
                                <div id="ticketResponses" class="border rounded p-2">
                                    ${await loadTicketResponses(ticket.id)}
                                </div>
                            </div>
                            <div class="mb-3">
                                <h6>Agregar Respuesta</h6>
                                <form id="responseForm">
                                    <div class="mb-3">
                                        <textarea class="form-control" id="responseText" rows="3" required></textarea>
                                    </div>
                                    <div class="form-check mb-3">
                                        <input class="form-check-input" type="checkbox" id="isInternal">
                                        <label class="form-check-label" for="isInternal">
                                            Respuesta interna (solo visible para soporte)
                                        </label>
                                    </div>
                                </form>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cerrar</button>
                            <button type="button" class="btn btn-primary" onclick="saveResponse(${ticket.id})">Enviar Respuesta</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHtml);
        const modal = new bootstrap.Modal(document.getElementById('ticketModal'));
        modal.show();

        document.getElementById('ticketModal').addEventListener('hidden.bs.modal', function () {
            this.remove();
        });
    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar el ticket');
    }
}

async function loadTicketResponses(ticketId) {
    try {
        const response = await fetch(`/api/support/tickets/${ticketId}/responses`);
        const responses = await response.json();
        
        if (responses.length === 0) {
            return '<p class="text-muted">No hay respuestas aún.</p>';
        }

        return responses.map(resp => `
            <div class="border-bottom mb-2 pb-2">
                <div class="d-flex justify-content-between">
                    <strong>${dataCache.getUserName(resp.userId)}</strong>
                    <small class="text-muted">${new Date(resp.createdAt).toLocaleString()}</small>
                </div>
                ${resp.isInternal ? '<span class="badge bg-info">Interno</span>' : ''}
                <p class="mb-0 mt-1">${resp.responseText}</p>
            </div>
        `).join('');
    } catch (error) {
        console.error('Error:', error);
        return '<p class="text-danger">Error al cargar las respuestas</p>';
    }
}

async function saveResponse(ticketId) {
    const responseText = document.getElementById('responseText').value;
    const isInternal = document.getElementById('isInternal').checked;

    if (!responseText.trim()) {
        alert('Por favor ingrese una respuesta');
        return;
    }

    try {
        const response = await fetch('/api/support/responses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                ticketId: ticketId,
                responseText: responseText,
                isInternal: isInternal
            })
        });

        if (response.ok) {
            document.getElementById('responseText').value = '';
            document.getElementById('isInternal').checked = false;
            document.getElementById('ticketResponses').innerHTML = await loadTicketResponses(ticketId);
        } else {
            throw new Error('Error al guardar la respuesta');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar la respuesta');
    }
}

async function updateTicketStatus(ticketId) {
    const ticket = dataCache.tickets.get(ticketId);
    if (!ticket) return;

    const statusMap = {
        'ABIERTO': 'ASIGNADO',
        'ASIGNADO': 'EN_PROCESO',
        'EN_PROCESO': 'RESUELTO',
        'RESUELTO': 'CERRADO',
        'CERRADO': 'ABIERTO'
    };

    const newStatus = statusMap[ticket.status];
    if (!newStatus) return;

    try {
        const response = await fetch(`/api/support/tickets/${ticketId}/status/${newStatus}`, {
            method: 'PUT'
        });

        if (response.ok) {
            ticket.status = newStatus;
            dataCache.tickets.set(ticketId, ticket);
            updateTicketsTable(Array.from(dataCache.tickets.values()));
        } else {
            throw new Error('Error al actualizar el estado');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el estado del ticket');
    }
}

async function assignSupportUser(ticketId) {
    const modalHtml = `
        <div class="modal fade" id="assignModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Asignar Usuario de Soporte</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <select class="form-select" id="supportUserId">
                            <option value="">Seleccione un usuario de soporte</option>
                        </select>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveSupportUser(${ticketId})">Asignar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modal = new bootstrap.Modal(document.getElementById('assignModal'));
    
    // Cargar usuarios de soporte
    const users = await dataCache.loadUsers();
    const supportUsers = users.filter(user => user.role === 'SUPPORT');
    const select = document.getElementById('supportUserId');
    select.innerHTML = '<option value="">Seleccione un usuario de soporte</option>' +
        supportUsers.map(user => 
            `<option value="${user.id}">${user.firstName} ${user.lastName}</option>`
        ).join('');

    modal.show();

    document.getElementById('assignModal').addEventListener('hidden.bs.modal', function () {
        this.remove();
    });
}

async function saveSupportUser(ticketId) {
    const supportUserId = document.getElementById('supportUserId').value;
    if (!supportUserId) {
        alert('Por favor seleccione un usuario de soporte');
        return;
    }

    try {
        const response = await fetch(`/api/support/tickets/${ticketId}/assign/${supportUserId}`, {
            method: 'PUT'
        });

        if (response.ok) {
            const ticket = dataCache.tickets.get(ticketId);
            ticket.supportUserId = parseInt(supportUserId);
            dataCache.tickets.set(ticketId, ticket);
            updateTicketsTable(Array.from(dataCache.tickets.values()));
            bootstrap.Modal.getInstance(document.getElementById('assignModal')).hide();
        } else {
            throw new Error('Error al asignar el usuario de soporte');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al asignar el usuario de soporte');
    }
} 