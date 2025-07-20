CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_email VARCHAR(100),
    card_number VARCHAR(20),
    cvc VARCHAR(10),
    exp_month VARCHAR(10),
    exp_year VARCHAR(10),
    FOREIGN KEY (user_email) REFERENCES users(email)
); 