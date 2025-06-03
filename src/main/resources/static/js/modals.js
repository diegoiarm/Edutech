// Modal para agregar/editar contenido
function showAddContentModal(content = null) {
    const modalHtml = `
        <div class="modal fade" id="contentModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${content ? 'Editar' : 'Nuevo'} Contenido</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="contentForm">
                            <input type="hidden" id="contentId" value="${content?.id || ''}">
                            <div class="mb-3">
                                <label class="form-label">Curso</label>
                                <select class="form-select" id="courseId" required>
                                    <option value="">Seleccione un curso</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Título</label>
                                <input type="text" class="form-control" id="title" required
                                    value="${content?.title || ''}" maxlength="200">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Tipo de Contenido</label>
                                <select class="form-select" id="contentType" required>
                                    <option value="">Seleccione un tipo</option>
                                    <option value="VIDEO" ${content?.contentType === 'VIDEO' ? 'selected' : ''}>Video</option>
                                    <option value="DOCUMENT" ${content?.contentType === 'DOCUMENT' ? 'selected' : ''}>Documento</option>
                                    <option value="QUIZ" ${content?.contentType === 'QUIZ' ? 'selected' : ''}>Quiz</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">URL</label>
                                <input type="url" class="form-control" id="url" required
                                    value="${content?.url || ''}" maxlength="800">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Orden</label>
                                <input type="number" class="form-control" id="orderIndex" required
                                    value="${content?.orderIndex || ''}">
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveContent()">Guardar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    // Agregar el modal al body
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Inicializar el modal
    const modal = new bootstrap.Modal(document.getElementById('contentModal'));
    modal.show();

    // Cargar cursos
    loadCoursesForSelect();

    // Limpiar el modal cuando se cierre
    document.getElementById('contentModal').addEventListener('hidden.bs.modal', function () {
        this.remove();
    });
}

// Modal para agregar/editar comentario
function showAddCommentModal(comment = null) {
    const modalHtml = `
        <div class="modal fade" id="commentModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${comment ? 'Editar' : 'Nuevo'} Comentario</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="commentForm">
                            <input type="hidden" id="commentId" value="${comment?.id || ''}">
                            <div class="mb-3">
                                <label class="form-label">Curso</label>
                                <select class="form-select" id="commentCourseId" required>
                                    <option value="">Seleccione un curso</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Usuario</label>
                                <select class="form-select" id="userId" required>
                                    <option value="">Seleccione un usuario</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Comentario</label>
                                <textarea class="form-control" id="commentText" required
                                    maxlength="800" rows="3">${comment?.commentText || ''}</textarea>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Calificación</label>
                                <select class="form-select" id="rating" required>
                                    <option value="">Seleccione una calificación</option>
                                    ${[1,2,3,4,5].map(num => 
                                        `<option value="${num}" ${comment?.rating === num ? 'selected' : ''}>${num}</option>`
                                    ).join('')}
                                </select>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveComment()">Guardar</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    // Agregar el modal al body
    document.body.insertAdjacentHTML('beforeend', modalHtml);
    
    // Inicializar el modal
    const modal = new bootstrap.Modal(document.getElementById('commentModal'));
    modal.show();

    // Cargar cursos y usuarios
    loadCoursesForSelect('commentCourseId');
    loadUsers();

    // Limpiar el modal cuando se cierre
    document.getElementById('commentModal').addEventListener('hidden.bs.modal', function () {
        this.remove();
    });
}

// Funciones auxiliares
async function loadCoursesForSelect(selectId = 'courseId') {
    try {
        const courses = await dataCache.loadCourses();
        const select = document.getElementById(selectId);
        if (select) {  // Verificar si el elemento existe
            select.innerHTML = '<option value="">Seleccione un curso</option>' +
                courses.map(course => 
                    `<option value="${course.id}">${course.title}</option>`
                ).join('');
        }
    } catch (error) {
        console.error('Error al cargar cursos para selector:', error);
    }
}

async function loadUsers(selectId = 'userId', role = null) {
    try {
        console.log(`Cargando usuarios para ${selectId} con rol ${role}`); // Log de depuración
        // Forzar recarga de usuarios para asegurar que tenemos los roles actualizados
        const users = await dataCache.loadUsers(true);
        console.log('Usuarios obtenidos del servidor:', users.length); // Log de depuración
        
        const select = document.getElementById(selectId);
        if (!select) {
            console.error(`Elemento select con id ${selectId} no encontrado`);
            return;
        }

        // Mapeo de roles del frontend a roles del backend
        const roleMapping = {
            'INSTRUCTOR': 'Instructor',
            'MANAGER': 'Manager',
            'SUPPORT': 'Support',
            'STUDENT': 'Student',
            'ADMINISTRATOR': 'Administrator'
        };

        // Filtrar usuarios por rol si se especifica
        const filteredUsers = role ? users.filter(user => {
            console.log(`Verificando usuario ${user.firstName} ${user.lastName} con rol ${user.role}`); // Log de depuración
            return user.role === roleMapping[role];
        }) : users;
        
        console.log(`Usuarios filtrados para rol ${role}:`, filteredUsers.length); // Log de depuración
        
        if (filteredUsers.length === 0) {
            console.warn(`No se encontraron usuarios con el rol ${role}. Roles disponibles:`, 
                [...new Set(users.map(u => u.role))]); // Log de advertencia con roles disponibles
        }

        select.innerHTML = '<option value="">Seleccione un usuario</option>' +
            filteredUsers.map(user => 
                `<option value="${user.id}">${user.firstName} ${user.lastName} (${user.role || 'Sin rol'})</option>`
            ).join('');

        console.log(`Usuarios cargados en ${selectId}:`, filteredUsers.length); // Log de depuración
    } catch (error) {
        console.error('Error al cargar usuarios:', error);
    }
}

// Funciones para guardar
async function saveContent() {
    const form = document.getElementById('contentForm');
    const contentId = document.getElementById('contentId').value;
    
    const content = {
        courseId: parseInt(document.getElementById('courseId').value),
        title: document.getElementById('title').value,
        contentType: document.getElementById('contentType').value,
        url: document.getElementById('url').value,
        orderIndex: parseInt(document.getElementById('orderIndex').value)
    };

    try {
        const url = contentId ? 
            `/api/courses/content/${contentId}` : 
            '/api/courses/content';
        
        const response = await fetch(url, {
            method: contentId ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(content)
        });

        if (response.ok) {
            bootstrap.Modal.getInstance(document.getElementById('contentModal')).hide();
            loadCourseContent();
        } else {
            throw new Error('Error al guardar el contenido');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar el contenido');
    }
}

async function saveComment() {
    const form = document.getElementById('commentForm');
    const commentId = document.getElementById('commentId').value;
    
    const comment = {
        courseId: parseInt(document.getElementById('commentCourseId').value),
        userId: parseInt(document.getElementById('userId').value),
        commentText: document.getElementById('commentText').value,
        rating: parseInt(document.getElementById('rating').value)
    };

    try {
        const url = commentId ? 
            `/api/courses/comments/${commentId}` : 
            '/api/courses/comments';
        
        const response = await fetch(url, {
            method: commentId ? 'PUT' : 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(comment)
        });

        if (response.ok) {
            bootstrap.Modal.getInstance(document.getElementById('commentModal')).hide();
            loadCourseComments();
        } else {
            throw new Error('Error al guardar el comentario');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al guardar el comentario');
    }
}

// Modal para agregar ticket de soporte
function showAddTicketModal() {
    const modalHtml = `
        <div class="modal fade" id="ticketModal" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Nuevo Ticket de Soporte</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="ticketForm">
                            <div class="mb-3">
                                <label class="form-label">Usuario</label>
                                <select class="form-select" id="ticketUserId" required>
                                    <option value="">Seleccione un usuario</option>
                                </select>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Asunto</label>
                                <input type="text" class="form-control" id="ticketSubject" required
                                    maxlength="200" placeholder="Ingrese un asunto descriptivo">
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Descripción</label>
                                <textarea class="form-control" id="ticketDescription" required
                                    rows="4" maxlength="1000" placeholder="Describa el problema o consulta"></textarea>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveTicket()">Crear Ticket</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modal = new bootstrap.Modal(document.getElementById('ticketModal'));
    
    // Cargar usuarios
    loadUsers('ticketUserId');
    
    modal.show();

    document.getElementById('ticketModal').addEventListener('hidden.bs.modal', function () {
        this.remove();
    });
}

async function saveTicket() {
    const userId = document.getElementById('ticketUserId').value;
    const subject = document.getElementById('ticketSubject').value;
    const description = document.getElementById('ticketDescription').value;

    if (!userId || !subject || !description) {
        alert('Por favor complete todos los campos');
        return;
    }

    try {
        const response = await fetch('/api/support/tickets', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId: parseInt(userId),
                subject: subject,
                description: description
            })
        });

        if (response.ok) {
            const ticket = await response.json();
            dataCache.tickets.set(ticket.id, ticket);
            updateTicketsTable(Array.from(dataCache.tickets.values()));
            bootstrap.Modal.getInstance(document.getElementById('ticketModal')).hide();
        } else {
            throw new Error('Error al crear el ticket');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al crear el ticket');
    }
}

async function showAddCourseModal() {
    const modalHtml = `
        <div class="modal fade" id="courseModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Nuevo Curso</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <form id="courseForm">
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Título</label>
                                    <input type="text" class="form-control" id="courseTitle" required>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Categoría</label>
                                    <select class="form-select" id="courseCategory" required>
                                        <option value="">Seleccione una categoría</option>
                                    </select>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">Descripción</label>
                                <textarea class="form-control" id="courseDescription" rows="3" required></textarea>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-4">
                                    <label class="form-label">Instructor</label>
                                    <select class="form-select" id="courseInstructor" required>
                                        <option value="">Seleccione un instructor</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Gestor</label>
                                    <select class="form-select" id="courseManager" required>
                                        <option value="">Seleccione un gestor</option>
                                    </select>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Estado</label>
                                    <select class="form-select" id="courseStatus" required>
                                        <option value="DRAFT">Borrador</option>
                                        <option value="PUBLISHED">Publicado</option>
                                        <option value="ARCHIVED">Archivado</option>
                                    </select>
                                </div>
                            </div>
                            <div class="row mb-3">
                                <div class="col-md-6">
                                    <label class="form-label">Precio</label>
                                    <div class="input-group">
                                        <span class="input-group-text">$</span>
                                        <input type="number" class="form-control" id="coursePrice" 
                                               step="0.01" min="0" required>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <label class="form-label">Fecha de Publicación</label>
                                    <input type="date" class="form-control" id="coursePublishDate" required>
                                </div>
                            </div>
                            <div class="mb-3">
                                <label class="form-label">URL de la Imagen</label>
                                <input type="url" class="form-control" id="courseImage" required>
                                <div class="form-text">Ingrese la URL de una imagen para el curso</div>
                            </div>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                        <button type="button" class="btn btn-primary" onclick="saveCourse()">Guardar Curso</button>
                    </div>
                </div>
            </div>
        </div>
    `;

    document.body.insertAdjacentHTML('beforeend', modalHtml);
    const modal = new bootstrap.Modal(document.getElementById('courseModal'));
    
    // Cargar categorías
    try {
        const response = await fetch('/api/course-categories');
        const categories = await response.json();
        const categorySelect = document.getElementById('courseCategory');
        categorySelect.innerHTML = '<option value="">Seleccione una categoría</option>' +
            categories.map(category => 
                `<option value="${category.id}">${category.name}</option>`
            ).join('');
    } catch (error) {
        console.error('Error al cargar categorías:', error);
    }

    // Cargar usuarios específicos por rol
    await loadUsers('courseInstructor', 'INSTRUCTOR');
    await loadUsers('courseManager', 'MANAGER');

    // Establecer fecha actual como valor predeterminado
    document.getElementById('coursePublishDate').valueAsDate = new Date();

    modal.show();

    document.getElementById('courseModal').addEventListener('hidden.bs.modal', function () {
        this.remove();
    });
}

async function editCourse(id) {
    try {
        const response = await fetch(`/api/courses/${id}`);
        const course = await response.json();
        
        const modalHtml = `
            <div class="modal fade" id="courseModal" tabindex="-1">
                <div class="modal-dialog modal-lg">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">Editar Curso</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body">
                            <form id="courseForm">
                                <input type="hidden" id="courseId" value="${course.id}">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Título</label>
                                        <input type="text" class="form-control" id="courseTitle" 
                                               value="${course.title}" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Categoría</label>
                                        <select class="form-select" id="courseCategory" required>
                                            <option value="">Seleccione una categoría</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Descripción</label>
                                    <textarea class="form-control" id="courseDescription" 
                                              rows="3" required>${course.description}</textarea>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-4">
                                        <label class="form-label">Instructor</label>
                                        <select class="form-select" id="courseInstructor" required>
                                            <option value="">Seleccione un instructor</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">Gestor</label>
                                        <select class="form-select" id="courseManager" required>
                                            <option value="">Seleccione un gestor</option>
                                        </select>
                                    </div>
                                    <div class="col-md-4">
                                        <label class="form-label">Estado</label>
                                        <select class="form-select" id="courseStatus" required>
                                            <option value="DRAFT" ${course.status === 'DRAFT' ? 'selected' : ''}>Borrador</option>
                                            <option value="PUBLISHED" ${course.status === 'PUBLISHED' ? 'selected' : ''}>Publicado</option>
                                            <option value="ARCHIVED" ${course.status === 'ARCHIVED' ? 'selected' : ''}>Archivado</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Precio</label>
                                        <div class="input-group">
                                            <span class="input-group-text">$</span>
                                            <input type="number" class="form-control" id="coursePrice" 
                                                   value="${course.price}" step="0.01" min="0" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Fecha de Publicación</label>
                                        <input type="date" class="form-control" id="coursePublishDate" 
                                               value="${course.publishDate}" required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">URL de la Imagen</label>
                                    <input type="url" class="form-control" id="courseImage" 
                                           value="${course.image}" required>
                                    <div class="form-text">Ingrese la URL de una imagen para el curso</div>
                                </div>
                            </form>
                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancelar</button>
                            <button type="button" class="btn btn-primary" onclick="updateCourse()">Actualizar Curso</button>
                        </div>
                    </div>
                </div>
            </div>
        `;

        document.body.insertAdjacentHTML('beforeend', modalHtml);
        const modal = new bootstrap.Modal(document.getElementById('courseModal'));
        
        // Cargar categorías
        const categoryResponse = await fetch('/api/course-categories');
        const categories = await categoryResponse.json();
        const categorySelect = document.getElementById('courseCategory');
        categorySelect.innerHTML = '<option value="">Seleccione una categoría</option>' +
            categories.map(category => 
                `<option value="${category.id}" ${category.id === course.categoryId ? 'selected' : ''}>
                    ${category.name}
                </option>`
            ).join('');

        // Cargar usuarios específicos por rol
        await loadUsers('courseInstructor', 'INSTRUCTOR');
        await loadUsers('courseManager', 'MANAGER');

        // Establecer los valores seleccionados
        if (course.instructorId) {
            document.getElementById('courseInstructor').value = course.instructorId;
        }
        if (course.managerId) {
            document.getElementById('courseManager').value = course.managerId;
        }

        modal.show();

        document.getElementById('courseModal').addEventListener('hidden.bs.modal', function () {
            this.remove();
        });
    } catch (error) {
        console.error('Error:', error);
        alert('Error al cargar los datos del curso');
    }
}

async function saveCourse() {
    const courseData = {
        title: document.getElementById('courseTitle').value,
        description: document.getElementById('courseDescription').value,
        categoryId: parseInt(document.getElementById('courseCategory').value),
        instructorId: parseInt(document.getElementById('courseInstructor').value),
        managerId: parseInt(document.getElementById('courseManager').value),
        status: document.getElementById('courseStatus').value,
        price: parseFloat(document.getElementById('coursePrice').value),
        publishDate: document.getElementById('coursePublishDate').value,
        image: document.getElementById('courseImage').value
    };

    try {
        const response = await fetch('/api/courses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(courseData)
        });

        if (response.ok) {
            const newCourse = await response.json();
            dataCache.courses.set(newCourse.id, newCourse);
            bootstrap.Modal.getInstance(document.getElementById('courseModal')).hide();
            loadCoursesForSelect();
        } else {
            throw new Error('Error al crear el curso');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al crear el curso');
    }
}

async function updateCourse() {
    const courseId = document.getElementById('courseId').value;
    const courseData = {
        title: document.getElementById('courseTitle').value,
        description: document.getElementById('courseDescription').value,
        categoryId: parseInt(document.getElementById('courseCategory').value),
        instructorId: parseInt(document.getElementById('courseInstructor').value),
        managerId: parseInt(document.getElementById('courseManager').value),
        status: document.getElementById('courseStatus').value,
        price: parseFloat(document.getElementById('coursePrice').value),
        publishDate: document.getElementById('coursePublishDate').value,
        image: document.getElementById('courseImage').value
    };

    try {
        const response = await fetch(`/api/courses/${courseId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(courseData)
        });

        if (response.ok) {
            const updatedCourse = await response.json();
            dataCache.courses.set(updatedCourse.id, updatedCourse);
            bootstrap.Modal.getInstance(document.getElementById('courseModal')).hide();
            loadCoursesForSelect();
        } else {
            throw new Error('Error al actualizar el curso');
        }
    } catch (error) {
        console.error('Error:', error);
        alert('Error al actualizar el curso');
    }
} 