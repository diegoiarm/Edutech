DROP DATABASE IF EXISTS edutech;

-- Crear la BD
CREATE DATABASE edutech
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_0900_ai_ci;
  
USE edutech;

-- Tabla: role
-- Descripción: Define los distintos roles que pueden tener los usuarios del sistema.
CREATE TABLE role (
    id          INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador único del rol
    name        VARCHAR(50)     NOT NULL,                           -- Nombre del rol (Administrator: administrador, Manager: coordinador, Instructor: instructor, Support: soporte, Student: alumno)
    description VARCHAR(800)    NOT NULL                            -- Descripción del rol
);

-- Tabla: user
-- Descripción: Almacena los usuarios del sistema, incluyendo: administradores, coordinadores, instructores, alumnos y personal de soporte.
CREATE TABLE user (
    id            INT             AUTO_INCREMENT PRIMARY KEY,          -- Identificador único del usuario
    first_name    VARCHAR(100)    NOT NULL,                            -- Nombre del usuario
    last_name     VARCHAR(100)    NOT NULL,                            -- Apellido del usuario
    email         VARCHAR(255)    NOT NULL UNIQUE,                     -- Correo electrónico del usuario
    password_hash VARCHAR(255)    NOT NULL,                            -- Contraseña en formato encriptado
    role_id       INT             NOT NULL,                            -- ID del rol asignado
    is_active     BOOLEAN         NOT NULL DEFAULT TRUE,               -- Indica si la cuenta está activa
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- Fecha de creación del usuario
    updated_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, -- Fecha de última actualización
    FOREIGN KEY (role_id)         REFERENCES role(id)
);

-- Tabla: course_category
-- Descripción: Agrupa los cursos por temáticas o áreas de conocimiento.
CREATE TABLE course_category (
    id          INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador único de la categoría
    name        VARCHAR(100)    NOT NULL,                           -- Nombre de la categoría
    description VARCHAR(800)    NOT NULL                            -- Descripción de la categoría
);

-- Tabla: course
-- Descripción: Representa los cursos ofrecidos en la plataforma.
CREATE TABLE course (
    id            INT             AUTO_INCREMENT PRIMARY KEY,       -- Identificador único del curso
    title         VARCHAR(200)    NOT NULL,                         -- Título del curso
    description   VARCHAR(800)    NOT NULL,                         -- Descripción general del curso
    category_id   INT             NOT NULL,                         -- ID de la categoría del curso
    manager_id    INT             NOT NULL,                         -- ID del coordinador a cargo
    instructor_id INT             NOT NULL,                         -- ID del instructor a cargo
    publish_date  DATE            NOT NULL,                         -- Fecha publicación del curso
    price         DECIMAL(15,3)   NOT NULL,                         -- Pecio del curso
    image         VARCHAR(255)    NOT NULL,                         -- Imagen del curso
    status        VARCHAR(50)     NOT NULL,                         -- Indica el estado del curso (Draft: borrador, Approved: aprobado, Enabled: habilitado para su publicación, Disabled: desactivado)
    FOREIGN KEY (category_id)     REFERENCES course_category(id),
    FOREIGN KEY (manager_id)      REFERENCES user(id),
    FOREIGN KEY (instructor_id)   REFERENCES user(id)
);

-- Tabla: enrollment
-- Descripción: Registra las inscripciones de estudiantes a los cursos.
CREATE TABLE enrollment (
    id           INT              AUTO_INCREMENT PRIMARY KEY,          -- Identificador único de la inscripción
    student_id   INT              NOT NULL,                            -- ID del estudiante inscrito
    course_id    INT              NOT NULL,                            -- ID del curso inscrito
    enrolled_at  DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- Fecha de inscripción
    status       VARCHAR(20)      NOT NULL,                            -- Estado de la inscripción (Enabled: habilitado, Completed: completado, Disabled: deshabilitado)
    FOREIGN KEY (student_id) REFERENCES user(id),
    FOREIGN KEY (course_id)  REFERENCES course(id)
);

-- Tabla: course_content
-- Descripción: Contiene los contenidos o materiales educativos de cada curso.
CREATE TABLE course_content (
    id            INT             AUTO_INCREMENT PRIMARY KEY,       -- Identificador del contenido
    course_id     INT             NOT NULL,                         -- ID del curso asociado
    title         VARCHAR(200)    NOT NULL,                         -- Título del contenido
    content_type  VARCHAR(50)     NOT NULL,                         -- Tipo de contenido (Video: video, PDF: archivo .pdf, Link: enlace a sitio web, Word: documento .docx, Audio: archivo de audio .mp3)
    url           VARCHAR(800)    NOT NULL,                         -- Ubicación del recurso
    order_index   INT             NOT NULL,                         -- Orden del contenido en el curso
    FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Tabla: course_quiz
-- Descripción: Contiene las pruebas de evaluación dentro de un curso, como quizzes de alternativas, preguntas escritas o trabajos entregables.
CREATE TABLE course_quiz (
    id          INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador único del quiz
    course_id   INT             NOT NULL,                           -- ID del curso asociado
    title       VARCHAR(200)    NOT NULL,                           -- Título del quiz
    description VARCHAR(800)    NOT NULL,                           -- Descripción general del quiz
    quiz_type   VARCHAR(50)     NOT NULL,                           -- Tipo de quiz (Multiple Choice: alternativas, Written Answer: respuesta escrita, Assignment: entrega de encargo)
    created_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha de creación del quiz
    FOREIGN KEY (course_id) REFERENCES course(id)
);

-- Tabla: course_quiz_question
-- Descripción: Almacena las preguntas de un quiz de un curso, incluyendo el texto de la pregunta, las alternativas de respuesta (A, B, C, D, E) y la indicación de cuál de ellas es la correcta.
CREATE TABLE course_quiz_question (
    id              INT              AUTO_INCREMENT PRIMARY KEY,         -- Identificador único de la pregunta del quiz.
    quiz_id         INT              NULL,                               -- ID del quiz al que pertenece la pregunta.
    question_text   VARCHAR(800)     NULL,                               -- Texto de la pregunta.
    option_a        VARCHAR(800)     NULL,                               -- Texto de la alternativa A.
    option_b        VARCHAR(800)     NULL,                               -- Texto de la alternativa B.
    option_c        VARCHAR(800)     NULL,                               -- Texto de la alternativa C.
    option_d        VARCHAR(800)     NULL,                               -- Texto de la alternativa D.
    option_e        VARCHAR(800)     NULL,                               -- Texto de la alternativa E.
    correct_answer  VARCHAR(800)     NULL,                               -- Texto de la respuesta correcta si la pregunta es de “respuesta escrita”.
    correct_option  VARCHAR(1)       NULL,                               -- Letra de la alternativa correcta.
    order_index     INT              NOT NULL,                           -- Orden de la pregunta en el quiz
    created_at      DATETIME         NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha de creación de la pregunta.
    FOREIGN KEY (quiz_id) REFERENCES course_quiz(id)
);

-- Tabla: quiz_response
-- Descripción: Respuestas de los estudiantes a las preguntas de un quiz.
CREATE TABLE quiz_response (
    id               INT             AUTO_INCREMENT PRIMARY KEY,           -- Identificador de la respuesta
    quiz_id          INT             NOT NULL,                             -- ID del quiz
    student_id       INT             NOT NULL,                             -- ID del estudiante
    selected_option  VARCHAR(1)      NULL,                                 -- Letra de la alternativa seleccionada.
    response_content VARCHAR(800)    NULL,                                 -- Contenido de la respuesta si la pregunta es de “respuesta escrita”.
    assignment_url   VARCHAR(800)    NULL,                                 -- Link del archivo del encargo si la pregunta es de “entrega de encargo”.
    submitted_at     DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,   -- Fecha de envío
    FOREIGN KEY (quiz_id)    REFERENCES course_quiz(id),
    FOREIGN KEY (student_id) REFERENCES user(id)
);

-- Tabla: student_mark
-- Descripción: Registra las calificaciones obtenidas por los estudiantes en cada quiz individual.
CREATE TABLE student_mark (
    id         INT             AUTO_INCREMENT PRIMARY KEY,          -- Identificador de la calificación
    quiz_id    INT             NOT NULL,                            -- ID del quiz evaluado
    student_id INT             NOT NULL,                            -- ID del estudiante evaluado
    mark       DECIMAL(5,2)    NOT NULL,                            -- Calificación del estudiante de 0% a 100%
    comments   VARCHAR(800)    NULL,                                -- Comentarios del profesor sobre el desempeño del estudiante
    graded_at  DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP,  -- Fecha en que fue evaluado
    FOREIGN KEY (quiz_id)    REFERENCES course_quiz(id),
    FOREIGN KEY (student_id) REFERENCES user(id)
);

-- Tabla: support_ticket
-- Descripción: Gestiona las solicitudes de soporte técnico o incidencias.
CREATE TABLE support_ticket (
    id              INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador del ticket
    user_id         INT             NOT NULL,                           -- ID del usuario que reporta
    support_user_id INT             NULL,                               -- ID del usuario de soporte que atiende
    subject         VARCHAR(200)    NOT NULL,                           -- Asunto del ticket
    description     VARCHAR(800)    NOT NULL,                           -- Descripción del problema
    status          VARCHAR(20)     NOT NULL,                           -- Estado del ticket (Open, Closed, Active)
    created_at      DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha de creación del ticket
    closed_at       DATETIME        NULL,                               -- Fecha de cierre del ticket
    FOREIGN KEY (user_id)         REFERENCES user(id),
    FOREIGN KEY (support_user_id) REFERENCES user(id)
);

-- Tabla: course_comment
-- Descripción: Comentarios y calificaciones dejadas por los estudiantes en los cursos.
CREATE TABLE course_comment (
    id            INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador del comentario
    course_id     INT             NOT NULL,                           -- ID del curso comentado
    user_id       INT             NOT NULL,                           -- ID del autor del comentario
    comment_text  VARCHAR(800)    NOT NULL,                           -- Texto del comentario
    rating        INT             NOT NULL,                           -- Valoración del curso (1 a 5)
    created_at    DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha del comentario
    FOREIGN KEY (course_id) REFERENCES course(id),
    FOREIGN KEY (user_id)   REFERENCES user(id)
);

-- Tabla: discount_coupon
-- Descripción: Registra cupones de descuento aplicables a las inscripciones en cursos.
CREATE TABLE discount_coupon (
    id                  INT             AUTO_INCREMENT PRIMARY KEY, -- Identificador del cupón
    code                VARCHAR(50)     NOT NULL UNIQUE,            -- Código del cupón
    description         VARCHAR(800)    NOT NULL,                   -- Descripción del cupón
    discount_percentage DECIMAL(5,2)    NOT NULL,                   -- Porcentaje de descuento
    valid_from          DATE            NOT NULL,                   -- Inicio de validez
    valid_until         DATE            NOT NULL,                   -- Fin de validez
    is_active           BOOLEAN         NOT NULL DEFAULT TRUE       -- Estado de activación del cupón
);

-- Tabla: payment
-- Descripción: Almacena los pagos realizados por los estudiantes para acceder a los cursos.
CREATE TABLE payment (
    id                  INT             AUTO_INCREMENT PRIMARY KEY,         -- Identificador del pago
    user_id             INT             NOT NULL,                           -- ID del usuario que paga
    amount              DECIMAL(15,3)   NOT NULL,                           -- Monto pagado
    payment_date        DATETIME        NOT NULL DEFAULT CURRENT_TIMESTAMP, -- Fecha del pago
    payment_method      VARCHAR(100)    NOT NULL,                           -- Método de pago (Credit Card: tarjeta de crédito, Debit Card: tarjeta de débito, Electronic Payment: pago electrónico)
    payment_institution VARCHAR(200)    NOT NULL,                           -- Nombre de la plataforma o banco utilizado (ej: Banco Estado, WebPay, PayPal, etc.)
    transaction_id      VARCHAR(200)    NOT NULL,                           -- ID de la transacción
    status              VARCHAR(20)     NOT NULL,                           -- Estado del pago (Completed: completado, Failed: fallido)
    FOREIGN KEY (user_id) REFERENCES user(id)
);

INSERT INTO role (id, name, description) VALUES (1, 'Administrator', 'Administrator del sistema');
INSERT INTO role (id, name, description) VALUES (2, 'Manager', 'Manager del sistema');
INSERT INTO role (id, name, description) VALUES (3, 'Instructor', 'Instructor del sistema');
INSERT INTO role (id, name, description) VALUES (4, 'Support', 'Support del sistema');
INSERT INTO role (id, name, description) VALUES (5, 'Student', 'Student del sistema');



INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (1, 'Admin', 'Principal', 'admin@edu.cl', 'hashedpassword', 1, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (2, 'Patrick', 'Blair', 'manager2@edu.cl', 'hashedpassword', 2, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (3, 'Blake', 'Clark', 'manager3@edu.cl', 'hashedpassword', 2, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (4, 'Christina', 'Spencer', 'manager4@edu.cl', 'hashedpassword', 2, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (5, 'Faith', 'Jimenez', 'manager5@edu.cl', 'hashedpassword', 2, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (6, 'Mary', 'Terry', 'manager6@edu.cl', 'hashedpassword', 2, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (7, 'Brian', 'Woods', 'instructor7@edu.cl', 'hashedpassword', 3, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (8, 'Elizabeth', 'Diaz', 'instructor8@edu.cl', 'hashedpassword', 3, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (9, 'Katherine', 'Park', 'instructor9@edu.cl', 'hashedpassword', 3, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (10, 'Janet', 'Church', 'instructor10@edu.cl', 'hashedpassword', 3, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (11, 'Dustin', 'Williams', 'instructor11@edu.cl', 'hashedpassword', 3, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (12, 'Jacob', 'Newton', 'support12@edu.cl', 'hashedpassword', 4, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (13, 'Donald', 'Guzman', 'support13@edu.cl', 'hashedpassword', 4, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (14, 'Randy', 'Kane', 'support14@edu.cl', 'hashedpassword', 4, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (15, 'Christine', 'Foley', 'student15@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (16, 'Kimberly', 'Cowan', 'student16@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (17, 'Rachel', 'Wright', 'student17@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (18, 'Daniel', 'Mcclain', 'student18@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (19, 'Elizabeth', 'Rodriguez', 'student19@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (20, 'Kevin', 'Hurley', 'student20@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (21, 'Gary', 'Andersen', 'student21@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (22, 'Rodney', 'Evans', 'student22@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (23, 'Catherine', 'Page', 'student23@edu.cl', 'hashedpassword', 5, TRUE);
INSERT INTO user (id, first_name, last_name, email, password_hash, role_id, is_active) VALUES (24, 'Alyssa', 'Gates', 'student24@edu.cl', 'hashedpassword', 5, TRUE);



INSERT INTO course_category (id, name, description) VALUES (1, 'Programación', 'Categoría de Programación');
INSERT INTO course_category (id, name, description) VALUES (2, 'Inteligencia Artificial', 'Categoría de Inteligencia Artificial');
INSERT INTO course_category (id, name, description) VALUES (3, 'Robótica', 'Categoría de Robótica');



INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (1, 'Introducción a la Robótica', 'Fundamentos de sensores, actuadores y microcontroladores.', 1, 2, 7, '2024-01-01', 100000, '0001.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (2, 'Fundamentos de Inteligencia Artificial', 'Curso teórico-práctico sobre IA clásica y moderna.', 2, 2, 7, '2024-01-01', 100000, '0002.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (3, 'Robótica con Arduino y Raspberry Pi', 'Diseño e implementación de proyectos robóticos básicos.', 3, 2, 7, '2024-01-01', 100000, '0003.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (4, 'Robótica Aplicada en Educación', 'Uso de kits robóticos en contextos educativos.', 1, 2, 7, '2024-01-01', 100000, '0004.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (5, 'IA Generativa y Chatbots', 'Implementación de modelos como GPT y asistentes conversacionales.', 2, 2, 7, '2024-01-01', 100000, '0005.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (6, 'Programación en Python', 'Curso intensivo de desarrollo con Python desde cero.', 3, 2, 7, '2024-01-01', 100000, '0006.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (7, 'Automatización con Robótica Industrial', 'Control de procesos automatizados con PLCs y brazos robóticos.', 2, 2, 7, '2024-01-01', 100000, '0007.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (8, 'Programación Web con JavaScript', 'Desarrollo de sitios interactivos con HTML, CSS y JS.', 1, 2, 7, '2024-01-01', 100000, '0008.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (9, 'Taller de Robótica Educativa', 'Aplicaciones prácticas para niños y jóvenes en robótica.', 2, 2, 7, '2024-01-01', 100000, '0009.jpg', 'Enabled');
INSERT INTO course (id, title, description, category_id, manager_id, instructor_id, publish_date, price, image, status) VALUES (10, 'Full Stack 1', 'Microservicios con Spring Boot, REST APIs y bases de datos.', 1, 2, 7, '2024-01-01', 100000, '0010.jpg', 'Enabled');



INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (1, 1, 'Quiz 1 Curso 1', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (2, 1, 'Quiz 2 Curso 1', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (3, 1, 'Quiz 3 Curso 1', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (4, 2, 'Quiz 1 Curso 2', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (5, 2, 'Quiz 2 Curso 2', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (6, 2, 'Quiz 3 Curso 2', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (7, 3, 'Quiz 1 Curso 3', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (8, 3, 'Quiz 2 Curso 3', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (9, 3, 'Quiz 3 Curso 3', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (10, 4, 'Quiz 1 Curso 4', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (11, 4, 'Quiz 2 Curso 4', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (12, 4, 'Quiz 3 Curso 4', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (13, 5, 'Quiz 1 Curso 5', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (14, 5, 'Quiz 2 Curso 5', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (15, 5, 'Quiz 3 Curso 5', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (16, 6, 'Quiz 1 Curso 6', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (17, 6, 'Quiz 2 Curso 6', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (18, 6, 'Quiz 3 Curso 6', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (19, 7, 'Quiz 1 Curso 7', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (20, 7, 'Quiz 2 Curso 7', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (21, 7, 'Quiz 3 Curso 7', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (22, 8, 'Quiz 1 Curso 8', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (23, 8, 'Quiz 2 Curso 8', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (24, 8, 'Quiz 3 Curso 8', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (25, 9, 'Quiz 1 Curso 9', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (26, 9, 'Quiz 2 Curso 9', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (27, 9, 'Quiz 3 Curso 9', 'Evaluación 3', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (28, 10, 'Quiz 1 Curso 10', 'Evaluación 1', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (29, 10, 'Quiz 2 Curso 10', 'Evaluación 2', 'Multiple Choice', '2024-01-15');
INSERT INTO course_quiz (id, course_id, title, description, quiz_type, created_at) VALUES (30, 10, 'Quiz 3 Curso 10', 'Evaluación 3', 'Multiple Choice', '2024-01-15');



INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (1, 1, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (2, 1, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (3, 1, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (4, 2, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (5, 2, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (6, 2, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (7, 3, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (8, 3, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (9, 3, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (10, 4, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (11, 4, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (12, 4, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (13, 5, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (14, 5, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (15, 5, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (16, 6, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (17, 6, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (18, 6, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (19, 7, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (20, 7, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (21, 7, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (22, 8, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (23, 8, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (24, 8, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (25, 9, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (26, 9, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (27, 9, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (28, 10, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (29, 10, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (30, 10, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (31, 11, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (32, 11, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (33, 11, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (34, 12, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (35, 12, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (36, 12, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (37, 13, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (38, 13, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (39, 13, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (40, 14, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (41, 14, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (42, 14, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (43, 15, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (44, 15, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (45, 15, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (46, 16, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (47, 16, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (48, 16, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (49, 17, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (50, 17, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (51, 17, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (52, 18, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (53, 18, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (54, 18, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (55, 19, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (56, 19, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (57, 19, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (58, 20, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (59, 20, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (60, 20, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (61, 21, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (62, 21, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (63, 21, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (64, 22, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (65, 22, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (66, 22, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (67, 23, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (68, 23, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (69, 23, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (70, 24, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (71, 24, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (72, 24, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (73, 25, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (74, 25, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (75, 25, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (76, 26, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (77, 26, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (78, 26, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (79, 27, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (80, 27, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (81, 27, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (82, 28, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (83, 28, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (84, 28, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (85, 29, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (86, 29, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (87, 29, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (88, 30, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 1, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (89, 30, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 2, '2024-01-16');
INSERT INTO course_quiz_question (id, quiz_id, question_text, option_a, option_b, option_c, option_d, option_e, correct_answer, correct_option, order_index, created_at) VALUES (90, 30, '¿Cuál es la respuesta correcta?', 'Opción A', 'Opción B', 'Opción C', 'Opción D', 'Opción E', 'Texto correcto', 'A', 3, '2024-01-16');



INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (1, 1, '¿Qué es un robot?', 'Video', 'https://www.youtube.com/watch?v=-wED6SbZnY0', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (2, 1, 'Herramientas para electrónica', 'Video', 'https://www.youtube.com/watch?v=Qm4GooGNaf4', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (3, 1, 'Introducción a la robótica', 'Video', 'https://www.youtube.com/watch?v=VLaV9Oyi9SI', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (4, 1, 'Robótica educativa', 'Video', 'https://www.youtube.com/watch?v=7OmRyY2ke6s', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (5, 1, 'Curso de robótica para principiantes', 'Video', 'https://www.youtube.com/watch?v=eYpi4J8Cfss', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (6, 2, 'Curso de Inteligencia Artificial para todos', 'Video', 'https://www.youtube.com/watch?v=gYrfGnYob5Q', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (7, 2, 'Inteligencia Artificial - Clase 01: Introducción y herramientas', 'Video', 'https://www.youtube.com/watch?v=kTejsfOU8gs', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (8, 2, 'Curso gratis: Inteligencia Artificial para todos', 'Video', 'https://www.youtube.com/watch?v=ZnQKdhUhNeI', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (9, 2, 'Curso de inteligencia artificial GRATIS 2025', 'Video', 'https://www.youtube.com/watch?v=M8XsxjcYc5U', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (10, 2, 'Aprende Inteligencia Artificial en 2025 Gratis', 'Video', 'https://www.youtube.com/watch?v=4F15v-qC-0M', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (11, 3, 'Curso completo de informática básica', 'Video', 'https://www.youtube.com/watch?v=o-78Ue-EUeI', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (12, 3, 'Curso completo de computación desde cero', 'Video', 'https://www.youtube.com/watch?v=dyud7aCLUcs', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (13, 3, 'Curso completo de computación para adultos', 'Video', 'https://www.youtube.com/watch?v=n0PpVlYqrBY', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (14, 3, 'Curso de computación básica para principiantes', 'Video', 'https://www.youtube.com/watch?v=lJ0oRGIkKQs', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (15, 3, 'Curso de computación básica en Windows 7', 'Video', 'https://www.youtube.com/watch?v=MrdDCOW25Us', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (16, 4, 'Aprende HTML ahora! curso completo GRATIS desde cero', 'Video', 'https://www.youtube.com/watch?v=UB1O30fR-EE', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (17, 4, 'Curso completo de CSS desde cero', 'Video', 'https://www.youtube.com/watch?v=1Rs2ND1ryYc', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (18, 4, 'Curso de HTML y CSS', 'Video', 'https://www.youtube.com/watch?v=3JluqTojuME', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (19, 4, 'Tutorial de HTML y CSS para principiantes', 'Video', 'https://www.youtube.com/watch?v=HGTJBPNC-Gw', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (20, 4, 'Curso de HTML5 y CSS3 desde cero', 'Video', 'https://www.youtube.com/watch?v=4RS0C4c0gkY', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (21, 5, 'Aprende JavaScript Ahora! curso completo desde cero para principiantes', 'Video', 'https://www.youtube.com/watch?v=hdI2bqOjy3c', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (22, 5, 'Curso completo de JavaScript desde cero', 'Video', 'https://www.youtube.com/watch?v=PkZNo7MFNFg', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (23, 5, 'Tutorial de JavaScript para principiantes', 'Video', 'https://www.youtube.com/watch?v=W6NZfCO5SIk', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (24, 5, 'Curso de JavaScript moderno', 'Video', 'https://www.youtube.com/watch?v=PtQiiknWUcI', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (25, 5, 'Curso de JavaScript avanzado', 'Video', 'https://www.youtube.com/watch?v=1PnVor36-40', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (26, 6, 'Curso de Python desde cero', 'Video', 'https://www.youtube.com/watch?v=rfscVS0vtbw', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (27, 6, 'Tutorial de Python para principiantes', 'Video', 'https://www.youtube.com/watch?v=-uQrJ0TkZlc', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (28, 6, 'Curso completo de Python', 'Video', 'https://www.youtube.com/watch?v=YYXdXT2l-Gg', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (29, 6, 'Aprende Python en una hora', 'Video', 'https://www.youtube.com/watch?v=kqtD5dpn9C8', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (30, 6, 'Curso de Python intermedio', 'Video', 'https://www.youtube.com/watch?v=HGOBQPFzWKo', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (31, 7, 'Curso de MySQL desde cero', 'Video', 'https://www.youtube.com/watch?v=7S-tz1z-5bA', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (32, 7, 'Tutorial de MySQL para principiantes', 'Video', 'https://www.youtube.com/watch?v=HXV3zeQKqGY', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (33, 7, 'Curso completo de MySQL', 'Video', 'https://www.youtube.com/watch?v=9ylj9NR0Lcg', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (34, 7, 'Aprende MySQL en una hora', 'Video', 'https://www.youtube.com/watch?v=9Pzj7Aj25lw', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (35, 7, 'Curso de MySQL avanzado', 'Video', 'https://www.youtube.com/watch?v=3vZ0E2eL1nU', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (36, 8, 'Curso de Java desde cero', 'Video', 'https://www.youtube.com/watch?v=eIrMbAQSU34', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (37, 8, 'Tutorial de Java para principiantes', 'Video', 'https://www.youtube.com/watch?v=grEKMHGYyns', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (38, 8, 'Curso completo de Java', 'Video', 'https://www.youtube.com/watch?v=GoXwIVyNvX0', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (39, 8, 'Aprende Java en una hora', 'Video', 'https://www.youtube.com/watch?v=Hl-zzrqQoSE', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (40, 8, 'Curso de Java avanzado', 'Video', 'https://www.youtube.com/watch?v=8cm1x4bC610', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (41, 9, 'Curso de C# desde cero', 'Video', 'https://www.youtube.com/watch?v=GhQdlIFylQ8', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (42, 9, 'Tutorial de C# para principiantes', 'Video', 'https://www.youtube.com/watch?v=0QUgvfuKvWU', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (43, 9, 'Curso completo de C#', 'Video', 'https://www.youtube.com/watch?v=gfkTfcpWqAY', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (44, 9, 'Aprende C# en una hora', 'Video', 'https://www.youtube.com/watch?v=GcFJjpMFJvI', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) values (45, 9, 'Curso de C# avanzado', 'Video', 'https://www.youtube.com/watch?v=0QUgvfuKvWU', 5);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) VALUES (46, 10, 'Curso de Spring Boot desde cero', 'Video', 'https://www.youtube.com/watch?v=vtPkZShrvXQ', 1);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) VALUES (47, 10, 'Tutorial de Spring Boot para principiantes', 'Video', 'https://www.youtube.com/watch?v=35EQXmHKZYs', 2);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) VALUES (48, 10, 'Curso completo de Spring Boot', 'Video', 'https://www.youtube.com/watch?v=vtPkZShrvXQ', 3);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) VALUES (49, 10, 'Aprende Spring Boot en una hora', 'Video', 'https://www.youtube.com/watch?v=vtPkZShrvXQ', 4);
INSERT INTO course_content (id, course_id, title, content_type, url, order_index) VALUES (50, 10, 'Spring Boot REST API con MySQL y JPA', 'Video', 'https://www.youtube.com/watch?v=9SGDpanrc8U', 5);



INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (1, 23, 1, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (2, 15, 1, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (3, 16, 1, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (4, 17, 1, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (5, 19, 1, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (6, 24, 2, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (7, 16, 2, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (8, 18, 2, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (9, 22, 2, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (10, 17, 2, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (11, 16, 3, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (12, 22, 3, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (13, 20, 3, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (14, 23, 3, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (15, 18, 3, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (16, 23, 4, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (17, 24, 4, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (18, 18, 4, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (19, 21, 4, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (20, 16, 4, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (21, 18, 5, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (22, 20, 5, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (23, 16, 5, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (24, 22, 5, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (25, 17, 5, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (26, 21, 6, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (27, 18, 6, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (28, 16, 6, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (29, 22, 6, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (30, 20, 6, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (31, 21, 7, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (32, 23, 7, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (33, 22, 7, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (34, 24, 7, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (35, 18, 7, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (36, 20, 8, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (37, 18, 8, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (38, 22, 8, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (39, 19, 8, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (40, 21, 8, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (41, 20, 9, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (42, 22, 9, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (43, 21, 9, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (44, 24, 9, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (45, 23, 9, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (46, 21, 10, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (47, 17, 10, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (48, 22, 10, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (49, 18, 10, '2024-01-10', 'Enabled');
INSERT INTO enrollment (id, student_id, course_id, enrolled_at, status) VALUES (50, 16, 10, '2024-01-10', 'Enabled');



INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (1, 1, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega1', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (2, 1, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega2', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (3, 1, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega3', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (4, 1, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega4', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (5, 1, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega5', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (6, 1, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega6', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (7, 1, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega7', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (8, 1, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega8', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (9, 1, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega9', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (10, 1, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega10', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (11, 1, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega11', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (12, 1, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega12', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (13, 1, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega13', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (14, 1, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega14', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (15, 1, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega15', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (16, 2, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega16', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (17, 2, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega17', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (18, 2, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega18', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (19, 2, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega19', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (20, 2, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega20', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (21, 2, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega21', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (22, 2, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega22', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (23, 2, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega23', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (24, 2, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega24', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (25, 2, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega25', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (26, 2, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega26', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (27, 2, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega27', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (28, 2, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega28', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (29, 2, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega29', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (30, 2, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega30', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (31, 3, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega31', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (32, 3, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega32', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (33, 3, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega33', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (34, 3, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega34', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (35, 3, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega35', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (36, 3, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega36', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (37, 3, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega37', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (38, 3, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega38', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (39, 3, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega39', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (40, 3, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega40', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (41, 3, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega41', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (42, 3, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega42', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (43, 3, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega43', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (44, 3, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega44', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (45, 3, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega45', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (46, 4, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega46', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (47, 4, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega47', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (48, 4, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega48', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (49, 4, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega49', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (50, 4, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega50', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (51, 4, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega51', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (52, 4, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega52', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (53, 4, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega53', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (54, 4, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega54', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (55, 4, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega55', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (56, 4, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega56', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (57, 4, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega57', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (58, 4, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega58', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (59, 4, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega59', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (60, 4, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega60', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (61, 5, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega61', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (62, 5, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega62', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (63, 5, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega63', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (64, 5, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega64', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (65, 5, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega65', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (66, 5, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega66', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (67, 5, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega67', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (68, 5, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega68', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (69, 5, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega69', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (70, 5, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega70', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (71, 5, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega71', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (72, 5, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega72', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (73, 5, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega73', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (74, 5, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega74', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (75, 5, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega75', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (76, 6, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega76', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (77, 6, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega77', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (78, 6, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega78', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (79, 6, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega79', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (80, 6, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega80', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (81, 6, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega81', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (82, 6, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega82', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (83, 6, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega83', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (84, 6, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega84', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (85, 6, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega85', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (86, 6, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega86', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (87, 6, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega87', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (88, 6, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega88', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (89, 6, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega89', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (90, 6, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega90', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (91, 7, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega91', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (92, 7, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega92', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (93, 7, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega93', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (94, 7, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega94', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (95, 7, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega95', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (96, 7, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega96', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (97, 7, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega97', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (98, 7, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega98', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (99, 7, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega99', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (100, 7, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega100', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (101, 7, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega101', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (102, 7, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega102', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (103, 7, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega103', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (104, 7, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega104', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (105, 7, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega105', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (106, 8, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega106', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (107, 8, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega107', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (108, 8, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega108', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (109, 8, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega109', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (110, 8, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega110', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (111, 8, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega111', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (112, 8, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega112', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (113, 8, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega113', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (114, 8, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega114', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (115, 8, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega115', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (116, 8, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega116', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (117, 8, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega117', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (118, 8, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega118', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (119, 8, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega119', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (120, 8, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega120', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (121, 9, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega121', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (122, 9, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega122', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (123, 9, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega123', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (124, 9, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega124', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (125, 9, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega125', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (126, 9, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega126', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (127, 9, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega127', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (128, 9, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega128', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (129, 9, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega129', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (130, 9, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega130', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (131, 9, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega131', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (132, 9, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega132', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (133, 9, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega133', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (134, 9, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega134', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (135, 9, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega135', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (136, 10, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega136', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (137, 10, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega137', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (138, 10, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega138', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (139, 10, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega139', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (140, 10, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega140', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (141, 10, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega141', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (142, 10, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega142', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (143, 10, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega143', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (144, 10, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega144', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (145, 10, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega145', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (146, 10, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega146', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (147, 10, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega147', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (148, 10, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega148', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (149, 10, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega149', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (150, 10, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega150', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (151, 11, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega151', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (152, 11, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega152', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (153, 11, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega153', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (154, 11, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega154', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (155, 11, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega155', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (156, 11, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega156', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (157, 11, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega157', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (158, 11, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega158', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (159, 11, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega159', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (160, 11, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega160', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (161, 11, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega161', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (162, 11, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega162', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (163, 11, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega163', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (164, 11, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega164', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (165, 11, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega165', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (166, 12, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega166', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (167, 12, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega167', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (168, 12, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega168', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (169, 12, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega169', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (170, 12, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega170', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (171, 12, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega171', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (172, 12, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega172', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (173, 12, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega173', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (174, 12, 22, 'A', 'Respuesta escrita', 'http://archivo.com/entrega174', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (175, 12, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega175', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (176, 12, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega176', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (177, 12, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega177', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (178, 12, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega178', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (179, 12, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega179', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (180, 12, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega180', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (181, 13, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega181', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (182, 13, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega182', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (183, 13, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega183', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (184, 13, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega184', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (185, 13, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega185', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (186, 13, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega186', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (187, 13, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega187', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (188, 13, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega188', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (189, 13, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega189', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (190, 13, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega190', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (191, 13, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega191', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (192, 13, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega192', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (193, 13, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega193', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (194, 13, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega194', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (195, 13, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega195', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (196, 14, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega196', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (197, 14, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega197', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (198, 14, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega198', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (199, 14, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega199', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (200, 14, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega200', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (201, 14, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega201', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (202, 14, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega202', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (203, 14, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega203', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (204, 14, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega204', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (205, 14, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega205', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (206, 14, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega206', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (207, 14, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega207', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (208, 14, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega208', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (209, 14, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega209', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (210, 14, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega210', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (211, 15, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega211', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (212, 15, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega212', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (213, 15, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega213', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (214, 15, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega214', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (215, 15, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega215', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (216, 15, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega216', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (217, 15, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega217', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (218, 15, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega218', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (219, 15, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega219', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (220, 15, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega220', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (221, 15, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega221', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (222, 15, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega222', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (223, 15, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega223', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (224, 15, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega224', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (225, 15, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega225', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (226, 16, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega226', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (227, 16, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega227', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (228, 16, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega228', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (229, 16, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega229', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (230, 16, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega230', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (231, 16, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega231', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (232, 16, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega232', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (233, 16, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega233', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (234, 16, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega234', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (235, 16, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega235', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (236, 16, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega236', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (237, 16, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega237', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (238, 16, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega238', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (239, 16, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega239', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (240, 16, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega240', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (241, 17, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega241', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (242, 17, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega242', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (243, 17, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega243', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (244, 17, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega244', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (245, 17, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega245', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (246, 17, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega246', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (247, 17, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega247', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (248, 17, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega248', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (249, 17, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega249', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (250, 17, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega250', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (251, 17, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega251', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (252, 17, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega252', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (253, 17, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega253', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (254, 17, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega254', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (255, 17, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega255', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (256, 18, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega256', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (257, 18, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega257', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (258, 18, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega258', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (259, 18, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega259', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (260, 18, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega260', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (261, 18, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega261', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (262, 18, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega262', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (263, 18, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega263', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (264, 18, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega264', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (265, 18, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega265', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (266, 18, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega266', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (267, 18, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega267', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (268, 18, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega268', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (269, 18, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega269', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (270, 18, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega270', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (271, 19, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega271', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (272, 19, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega272', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (273, 19, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega273', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (274, 19, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega274', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (275, 19, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega275', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (276, 19, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega276', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (277, 19, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega277', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (278, 19, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega278', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (279, 19, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega279', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (280, 19, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega280', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (281, 19, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega281', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (282, 19, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega282', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (283, 19, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega283', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (284, 19, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega284', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (285, 19, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega285', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (286, 20, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega286', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (287, 20, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega287', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (288, 20, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega288', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (289, 20, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega289', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (290, 20, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega290', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (291, 20, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega291', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (292, 20, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega292', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (293, 20, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega293', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (294, 20, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega294', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (295, 20, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega295', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (296, 20, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega296', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (297, 20, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega297', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (298, 20, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega298', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (299, 20, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega299', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (300, 20, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega300', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (301, 21, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega301', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (302, 21, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega302', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (303, 21, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega303', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (304, 21, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega304', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (305, 21, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega305', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (306, 21, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega306', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (307, 21, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega307', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (308, 21, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega308', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (309, 21, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega309', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (310, 21, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega310', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (311, 21, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega311', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (312, 21, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega312', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (313, 21, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega313', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (314, 21, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega314', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (315, 21, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega315', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (316, 22, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega316', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (317, 22, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega317', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (318, 22, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega318', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (319, 22, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega319', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (320, 22, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega320', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (321, 22, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega321', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (322, 22, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega322', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (323, 22, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega323', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (324, 22, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega324', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (325, 22, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega325', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (326, 22, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega326', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (327, 22, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega327', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (328, 22, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega328', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (329, 22, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega329', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (330, 22, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega330', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (331, 23, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega331', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (332, 23, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega332', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (333, 23, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega333', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (334, 23, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega334', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (335, 23, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega335', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (336, 23, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega336', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (337, 23, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega337', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (338, 23, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega338', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (339, 23, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega339', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (340, 23, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega340', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (341, 23, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega341', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (342, 23, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega342', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (343, 23, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega343', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (344, 23, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega344', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (345, 23, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega345', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (346, 24, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega346', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (347, 24, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega347', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (348, 24, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega348', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (349, 24, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega349', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (350, 24, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega350', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (351, 24, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega351', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (352, 24, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega352', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (353, 24, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega353', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (354, 24, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega354', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (355, 24, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega355', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (356, 24, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega356', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (357, 24, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega357', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (358, 24, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega358', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (359, 24, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega359', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (360, 24, 15, 'A', 'Respuesta escrita', 'http://archivo.com/entrega360', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (361, 25, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega361', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (362, 25, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega362', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (363, 25, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega363', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (364, 25, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega364', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (365, 25, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega365', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (366, 25, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega366', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (367, 25, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega367', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (368, 25, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega368', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (369, 25, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega369', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (370, 25, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega370', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (371, 25, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega371', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (372, 25, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega372', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (373, 25, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega373', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (374, 25, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega374', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (375, 25, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega375', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (376, 26, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega376', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (377, 26, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega377', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (378, 26, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega378', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (379, 26, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega379', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (380, 26, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega380', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (381, 26, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega381', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (382, 26, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega382', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (383, 26, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega383', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (384, 26, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega384', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (385, 26, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega385', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (386, 26, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega386', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (387, 26, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega387', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (388, 26, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega388', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (389, 26, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega389', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (390, 26, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega390', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (391, 27, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega391', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (392, 27, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega392', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (393, 27, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega393', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (394, 27, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega394', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (395, 27, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega395', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (396, 27, 16, 'A', 'Respuesta escrita', 'http://archivo.com/entrega396', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (397, 27, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega397', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (398, 27, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega398', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (399, 27, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega399', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (400, 27, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega400', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (401, 27, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega401', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (402, 27, 20, 'A', 'Respuesta escrita', 'http://archivo.com/entrega402', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (403, 27, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega403', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (404, 27, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega404', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (405, 27, 23, 'A', 'Respuesta escrita', 'http://archivo.com/entrega405', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (406, 28, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega406', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (407, 28, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega407', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (408, 28, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega408', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (409, 28, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega409', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (410, 28, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega410', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (411, 28, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega411', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (412, 28, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega412', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (413, 28, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega413', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (414, 28, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega414', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (415, 28, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega415', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (416, 28, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega416', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (417, 28, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega417', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (418, 28, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega418', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (419, 28, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega419', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (420, 28, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega420', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (421, 29, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega421', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (422, 29, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega422', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (423, 29, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega423', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (424, 29, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega424', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (425, 29, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega425', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (426, 29, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega426', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (427, 29, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega427', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (428, 29, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega428', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (429, 29, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega429', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (430, 29, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega430', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (431, 29, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega431', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (432, 29, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega432', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (433, 29, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega433', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (434, 29, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega434', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (435, 29, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega435', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (436, 30, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega436', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (437, 30, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega437', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (438, 30, 24, 'A', 'Respuesta escrita', 'http://archivo.com/entrega438', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (439, 30, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega439', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (440, 30, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega440', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (441, 30, 17, 'A', 'Respuesta escrita', 'http://archivo.com/entrega441', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (442, 30, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega442', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (443, 30, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega443', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (444, 30, 18, 'A', 'Respuesta escrita', 'http://archivo.com/entrega444', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (445, 30, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega445', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (446, 30, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega446', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (447, 30, 19, 'A', 'Respuesta escrita', 'http://archivo.com/entrega447', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (448, 30, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega448', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (449, 30, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega449', '2024-01-20');
INSERT INTO quiz_response (id, quiz_id, student_id, selected_option, response_content, assignment_url, submitted_at) VALUES (450, 30, 21, 'A', 'Respuesta escrita', 'http://archivo.com/entrega450', '2024-01-20');



INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (1, 1, 19, 87.07, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (2, 1, 15, 62.39, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (3, 1, 17, 80.96, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (4, 1, 23, 94.93, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (5, 1, 22, 96.96, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (6, 2, 19, 75.76, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (7, 2, 15, 93.11, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (8, 2, 17, 74.58, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (9, 2, 23, 82.57, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (10, 2, 22, 96.16, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (11, 3, 19, 77.85, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (12, 3, 15, 94.83, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (13, 3, 17, 75.32, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (14, 3, 23, 80.71, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (15, 3, 22, 72.66, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (16, 4, 17, 63.97, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (17, 4, 20, 71.41, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (18, 4, 19, 92.21, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (19, 4, 23, 67.62, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (20, 4, 15, 70.89, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (21, 5, 17, 65.21, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (22, 5, 20, 76.19, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (23, 5, 19, 66.01, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (24, 5, 23, 82.33, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (25, 5, 15, 99.73, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (26, 6, 17, 77.09, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (27, 6, 20, 60.37, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (28, 6, 19, 98.51, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (29, 6, 23, 98.96, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (30, 6, 15, 97.55, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (31, 7, 23, 85.59, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (32, 7, 17, 87.57, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (33, 7, 16, 86.36, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (34, 7, 20, 97.02, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (35, 7, 22, 96.57, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (36, 8, 23, 98.48, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (37, 8, 17, 66.61, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (38, 8, 16, 68.96, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (39, 8, 20, 87.69, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (40, 8, 22, 99.38, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (41, 9, 23, 63.04, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (42, 9, 17, 61.35, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (43, 9, 16, 61.84, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (44, 9, 20, 94.56, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (45, 9, 22, 62.53, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (46, 10, 21, 95.9, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (47, 10, 20, 71.92, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (48, 10, 22, 69.19, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (49, 10, 18, 70.66, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (50, 10, 17, 90.4, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (51, 11, 21, 63.92, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (52, 11, 20, 77.39, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (53, 11, 22, 74.11, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (54, 11, 18, 83.5, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (55, 11, 17, 97.36, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (56, 12, 21, 77.14, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (57, 12, 20, 98.65, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (58, 12, 22, 98.59, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (59, 12, 18, 73.47, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (60, 12, 17, 81.31, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (61, 13, 20, 87.15, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (62, 13, 21, 64.89, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (63, 13, 17, 91.92, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (64, 13, 23, 81.36, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (65, 13, 18, 87.72, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (66, 14, 20, 77.94, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (67, 14, 21, 80.83, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (68, 14, 17, 71.66, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (69, 14, 23, 72.66, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (70, 14, 18, 60.73, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (71, 15, 20, 86.91, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (72, 15, 21, 92.82, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (73, 15, 17, 88.09, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (74, 15, 23, 88.16, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (75, 15, 18, 78.29, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (76, 16, 21, 71.08, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (77, 16, 20, 60.43, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (78, 16, 19, 69.13, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (79, 16, 15, 91.41, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (80, 16, 24, 62.54, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (81, 17, 21, 96.52, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (82, 17, 20, 84.14, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (83, 17, 19, 99.82, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (84, 17, 15, 62.3, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (85, 17, 24, 67.41, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (86, 18, 21, 82.37, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (87, 18, 20, 74.14, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (88, 18, 19, 78.98, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (89, 18, 15, 64.41, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (90, 18, 24, 78.15, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (91, 19, 20, 66.79, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (92, 19, 21, 68.2, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (93, 19, 19, 85.63, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (94, 19, 24, 87.92, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (95, 19, 23, 89.53, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (96, 20, 20, 61.13, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (97, 20, 21, 79.6, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (98, 20, 19, 81.69, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (99, 20, 24, 80.06, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (100, 20, 23, 98.77, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (101, 21, 20, 94.21, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (102, 21, 21, 77.97, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (103, 21, 19, 95.12, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (104, 21, 24, 66.36, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (105, 21, 23, 96.83, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (106, 22, 18, 97.76, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (107, 22, 19, 63.31, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (108, 22, 17, 89.83, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (109, 22, 24, 68.08, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (110, 22, 15, 75.2, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (111, 23, 18, 83.28, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (112, 23, 19, 77.55, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (113, 23, 17, 70.62, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (114, 23, 24, 81.28, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (115, 23, 15, 66.02, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (116, 24, 18, 71.3, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (117, 24, 19, 97.53, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (118, 24, 17, 86.15, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (119, 24, 24, 64.08, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (120, 24, 15, 65.37, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (121, 25, 18, 90.64, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (122, 25, 16, 89.97, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (123, 25, 19, 80.79, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (124, 25, 20, 91.69, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (125, 25, 23, 77.68, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (126, 26, 18, 85.09, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (127, 26, 16, 63.11, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (128, 26, 19, 69.8, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (129, 26, 20, 99.03, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (130, 26, 23, 88.08, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (131, 27, 18, 61.84, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (132, 27, 16, 93.85, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (133, 27, 19, 98.53, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (134, 27, 20, 76.07, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (135, 27, 23, 60.26, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (136, 28, 24, 77.91, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (137, 28, 17, 94.07, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (138, 28, 18, 89.8, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (139, 28, 19, 87.69, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (140, 28, 21, 62.07, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (141, 29, 24, 94.51, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (142, 29, 17, 75.36, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (143, 29, 18, 65.02, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (144, 29, 19, 67.47, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (145, 29, 21, 64.52, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (146, 30, 24, 68.47, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (147, 30, 17, 66.67, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (148, 30, 18, 75.31, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (149, 30, 19, 74.55, 'Buen trabajo', '2024-01-21');
INSERT INTO student_mark (id, quiz_id, student_id, mark, comments, graded_at) VALUES (150, 30, 21, 85.13, 'Buen trabajo', '2024-01-21');



INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (1, 20, 16, 'Problema 1', 'Descripción del problema', 'Closed', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (2, 17, 17, 'Problema 2', 'Descripción del problema', 'Closed', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (3, 19, 18, 'Problema 3', 'Descripción del problema', 'Active', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (4, 24, 18, 'Problema 4', 'Descripción del problema', 'Open', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (5, 23, 17, 'Problema 5', 'Descripción del problema', 'Open', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (6, 22, 18, 'Problema 6', 'Descripción del problema', 'Closed', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (7, 24, 18, 'Problema 7', 'Descripción del problema', 'Active', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (8, 20, 16, 'Problema 8', 'Descripción del problema', 'Closed', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (9, 23, 18, 'Problema 9', 'Descripción del problema', 'Open', '2024-02-01', '2024-02-02');
INSERT INTO support_ticket (id, user_id, support_user_id, subject, description, status, created_at, closed_at) VALUES (10, 17, 18, 'Problema 10', 'Descripción del problema', 'Active', '2024-02-01', '2024-02-02');



INSERT INTO discount_coupon (id, code, description, discount_percentage, valid_from, valid_until, is_active) VALUES (1, 'DESC01', 'Cupón de prueba', 26, '2024-01-01', '2024-12-31', TRUE);
INSERT INTO discount_coupon (id, code, description, discount_percentage, valid_from, valid_until, is_active) VALUES (2, 'DESC02', 'Cupón de prueba', 12, '2024-01-01', '2024-12-31', TRUE);
INSERT INTO discount_coupon (id, code, description, discount_percentage, valid_from, valid_until, is_active) VALUES (3, 'DESC03', 'Cupón de prueba', 13, '2024-01-01', '2024-12-31', TRUE);
INSERT INTO discount_coupon (id, code, description, discount_percentage, valid_from, valid_until, is_active) VALUES (4, 'DESC04', 'Cupón de prueba', 14, '2024-01-01', '2024-12-31', TRUE);
INSERT INTO discount_coupon (id, code, description, discount_percentage, valid_from, valid_until, is_active) VALUES (5, 'DESC05', 'Cupón de prueba', 16, '2024-01-01', '2024-12-31', TRUE);



INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (1, 23, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00001', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (2, 22, 100.00, '2024-02-10', 'Debit Card', 'PayPal', 'TXN00002', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (3, 15, 100.00, '2024-02-10', 'Electronic Payment', 'PayPal', 'TXN00003', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (4, 17, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00004', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (5, 16, 100.00, '2024-02-10', 'Electronic Payment', 'PayPal', 'TXN00005', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (6, 16, 100.00, '2024-02-10', 'Electronic Payment', 'WebPay', 'TXN00006', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (7, 17, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00007', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (8, 24, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00008', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (9, 21, 100.00, '2024-02-10', 'Electronic Payment', 'PayPal', 'TXN00009', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (10, 18, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00010', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (11, 24, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00011', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (12, 16, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00012', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (13, 21, 100.00, '2024-02-10', 'Electronic Payment', 'WebPay', 'TXN00013', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (14, 18, 100.00, '2024-02-10', 'Electronic Payment', 'PayPal', 'TXN00014', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (15, 20, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00015', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (16, 21, 100.00, '2024-02-10', 'Electronic Payment', 'WebPay', 'TXN00016', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (17, 15, 100.00, '2024-02-10', 'Debit Card', 'PayPal', 'TXN00017', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (18, 16, 100.00, '2024-02-10', 'Electronic Payment', 'PayPal', 'TXN00018', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (19, 24, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00019', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (20, 23, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00020', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (21, 19, 100.00, '2024-02-10', 'Debit Card', 'WebPay', 'TXN00021', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (22, 20, 100.00, '2024-02-10', 'Debit Card', 'WebPay', 'TXN00022', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (23, 18, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00023', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (24, 22, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00024', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (25, 17, 100.00, '2024-02-10', 'Debit Card', 'WebPay', 'TXN00025', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (26, 24, 100.00, '2024-02-10', 'Credit Card', 'WebPay', 'TXN00026', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (27, 15, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00027', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (28, 19, 100.00, '2024-02-10', 'Electronic Payment', 'WebPay', 'TXN00028', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (29, 21, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00029', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (30, 23, 100.00, '2024-02-10', 'Credit Card', 'WebPay', 'TXN00030', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (31, 23, 100.00, '2024-02-10', 'Electronic Payment', 'WebPay', 'TXN00031', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (32, 19, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00032', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (33, 20, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00033', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (34, 18, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00034', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (35, 17, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00035', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (36, 22, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00036', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (37, 20, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00037', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (38, 18, 100.00, '2024-02-10', 'Debit Card', 'PayPal', 'TXN00038', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (39, 21, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00039', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (40, 17, 100.00, '2024-02-10', 'Debit Card', 'WebPay', 'TXN00040', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (41, 24, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00041', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (42, 16, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00042', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (43, 19, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00043', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (44, 22, 100.00, '2024-02-10', 'Credit Card', 'WebPay', 'TXN00044', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (45, 20, 100.00, '2024-02-10', 'Credit Card', 'WebPay', 'TXN00045', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (46, 17, 100.00, '2024-02-10', 'Credit Card', 'BancoEstado', 'TXN00046', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (47, 19, 100.00, '2024-02-10', 'Electronic Payment', 'BancoEstado', 'TXN00047', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (48, 24, 100.00, '2024-02-10', 'Credit Card', 'PayPal', 'TXN00048', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (49, 18, 100.00, '2024-02-10', 'Debit Card', 'WebPay', 'TXN00049', 'Completed');
INSERT INTO payment (id, user_id, amount, payment_date, payment_method, payment_institution, transaction_id, status) VALUES (50, 21, 100.00, '2024-02-10', 'Debit Card', 'BancoEstado', 'TXN00050', 'Completed');



INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (1, 1, 18, 'Share government exist we industry behavior my down growth might.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (2, 1, 22, 'Use oil interest sign arm career quality specific share clear particular career section away buy.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (3, 1, 16, 'Commercial chair term modern soon paper include create eye perform road.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (4, 1, 24, 'Hear represent open pass line she policy material push better likely yeah activity adult.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (5, 1, 15, 'Couple especially none agree today mind run keep field edge again box smile defense.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (6, 2, 19, 'Government man how material simple situation loss lawyer play suggest law so leave mouth miss.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (7, 2, 20, 'Six local past nice now hot data film local garden travel fire.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (8, 2, 17, 'Agent we art interview follow newspaper character share.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (9, 2, 18, 'Address argue talk morning American quite market or man cell task.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (10, 2, 22, 'Fly scientist husband seven read reason seven middle particularly standard.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (11, 3, 16, 'Chance around suffer miss past how project.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (12, 3, 15, 'If hospital trip need study analysis tough.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (13, 3, 22, 'Which business society north including girl worry finally fight prepare church involve voice direction believe test.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (14, 3, 20, 'Health set believe past share property peace shoulder budget attack rock.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (15, 3, 23, 'Model decision also heart way sometimes their from heart shake.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (16, 4, 17, 'Business suffer argue force sister language something that situation represent.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (17, 4, 18, 'Present during reason cold expect play activity.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (18, 4, 15, 'Sure behind PM bag movement hair create traditional age key today.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (19, 4, 23, 'Father yard much fire southern maintain claim moment boy world nature.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (20, 4, 21, 'Career get simple religious effort television speech instead generation.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (21, 5, 16, 'Focus represent poor mother white generation weight treatment realize focus.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (22, 5, 20, 'Own change value message hundred way thank gas when along.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (23, 5, 23, 'Why simply oil tough source including increase capital college over black per middle argue.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (24, 5, 17, 'Moment treatment market physical add moment listen rule enjoy follow buy some that.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (25, 5, 21, 'Concern worry change quickly both identify Republican approach future force less against age dark mother sell.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (26, 6, 16, 'New think might radio car new continue miss born glass Congress loss.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (27, 6, 24, 'Research whether become effect item decision baby within education daughter off.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (28, 6, 19, 'Miss get claim idea film authority increase yet arrive customer.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (29, 6, 21, 'Top pay finally stay section will street truth notice attorney affect sister happen court.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (30, 6, 18, 'Price foreign represent cut imagine everybody ability work out number use car police blue.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (31, 7, 16, 'Middle who article degree end able everything specific staff purpose specific maybe often environment court.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (32, 7, 24, 'That thought everything decision reason area despite person risk.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (33, 7, 21, 'Edge leader difference front bed wall family himself discuss possible note a.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (34, 7, 18, 'Manage social pull face begin truth heart write rise glass senior talk against business.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (35, 7, 20, 'By such travel produce address page minute instead.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (36, 8, 23, 'Protect art maybe man report front type.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (37, 8, 17, 'The school black wall respond off position there from rise.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (38, 8, 18, 'Stock prove lot listen feeling rate during mean wear service include dark foot.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (39, 8, 19, 'Break city animal factor with spend need.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (40, 8, 21, 'Center box establish part tend bring group green none kitchen chance city.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (41, 9, 18, 'High machine laugh guy magazine evening old that executive spring wind.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (42, 9, 19, 'News different reveal particular market home drive century surface surface price make show note writer push.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (43, 9, 23, 'Miss ago carry foot see bill if whatever skill third Republican this others positive building.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (44, 9, 24, 'Environmental scientist apply add poor environmental series.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (45, 9, 22, 'Like power which production with now American safe good simply really.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (46, 10, 24, 'Admit better evidence after onto leg worker record would important.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (47, 10, 21, 'Itself film receive need consumer reason investment ask hospital lay actually former back fill time old.', 4, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (48, 10, 23, 'After green cover industry industry price could.', 3, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (49, 10, 18, 'Join college doctor seat budget mind worry international song.', 5, '2024-02-15');
INSERT INTO course_comment (id, course_id, user_id, comment_text, rating, created_at) VALUES (50, 10, 17, 'Help dark time thus boy spring remember rock can member night themselves.', 4, '2024-02-15');
