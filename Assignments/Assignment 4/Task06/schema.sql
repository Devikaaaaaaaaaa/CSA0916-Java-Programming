CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;

CREATE TABLE IF NOT EXISTS registrations (
    registration_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(20) NOT NULL,
    event_name VARCHAR(100) NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uq_student_event UNIQUE (student_id, event_name)
);

-- Example paginated query; bind event name, page size and offset with PreparedStatement.
SELECT student_id, event_name, registered_at
FROM registrations
WHERE event_name = ?
ORDER BY registration_id
LIMIT ? OFFSET ?;
