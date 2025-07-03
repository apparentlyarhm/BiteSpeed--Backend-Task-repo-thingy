CREATE TABLE contact (
    id INT AUTO_INCREMENT PRIMARY KEY,
    created_at DATETIME(6) NOT NULL,
    updated_at DATETIME(6) NOT NULL,
    deleted_at DATETIME(6) NULL,

    contact_id VARCHAR(36) NOT NULL,
    phone_number VARCHAR(255) NULL,
    email VARCHAR(255) NULL,
    linked_id INT NULL,
    link_precedence VARCHAR(20) NOT NULL,

    CONSTRAINT uk_contactId UNIQUE (contact_id),

    INDEX idx_email (email),
    INDEX idx_phone_number (phone_number),
    INDEX idx_linked_id (linked_id)
)