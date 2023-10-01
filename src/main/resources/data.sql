INSERT INTO roles(description) VALUES('Admin');
INSERT INTO roles(description) VALUES('Manager');
INSERT INTO roles(description) VALUES('Employee');

-- Insert User 1
INSERT INTO users (first_name, last_name, user_name, pass_word, enabled, phone, role_id, gender)
VALUES ('John', 'Doe', 'john@example.com', 'Password123', true, '1234567890', 1, 'MALE');

-- Insert User 2
INSERT INTO users (first_name, last_name, user_name, pass_word, enabled, phone, role_id, gender)
VALUES ('Jane', 'Smith', 'jane@example.com', 'Password456', true, '9876543210', 1, 'FEMALE');

-- Insert User 3
INSERT INTO users (first_name, last_name, user_name, pass_word, enabled, phone, role_id, gender)
VALUES ('Bob', 'Johnson', 'bob@example.com', 'Password789', true, '4567891230', 1, 'MALE');