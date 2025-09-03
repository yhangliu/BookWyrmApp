DROP DATABASE IF EXISTS bookwyrm_test;
CREATE DATABASE bookwyrm_test;
USE bookwyrm_test;

-- Books Table
CREATE TABLE Books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(13) NOT NULL UNIQUE,
    description TEXT
);

-- Users Table
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    is_admin TINYINT(1) DEFAULT 0,
    created_at DATE
);

-- BookClubs Table
CREATE TABLE BookClubs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    genre VARCHAR(50),
    location VARCHAR(100),
    `online` TINYINT(1) DEFAULT 1,
    featured_book VARCHAR(13),
    created_at DATE,
    created_by INT,
    FOREIGN KEY (created_by) REFERENCES Users(id),
    FOREIGN KEY (featured_book) REFERENCES Books(isbn)
);

-- BookClubMembers Table
CREATE TABLE BookClubMembers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    bookclub_id INT NOT NULL,
    joined_at DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (bookclub_id) REFERENCES BookClubs(id),
    UNIQUE (user_id, bookclub_id)
);

-- Reviews Table
CREATE TABLE Reviews (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    isbn VARCHAR(13) NOT NULL,
    rating INT NOT NULL,
    review_text TEXT NOT NULL,
    created_at DATE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE,
    FOREIGN KEY (isbn) REFERENCES Books(isbn) ON DELETE CASCADE
);
-- DiscussionThreads Table
CREATE TABLE DiscussionThreads (
    id INT AUTO_INCREMENT PRIMARY KEY,
    bookclub_id INT,
    title VARCHAR(255) NOT NULL,
    created_by INT,
    created_at DATE,
    FOREIGN KEY (bookclub_id) REFERENCES BookClubs(id) ON DELETE CASCADE,
    FOREIGN KEY (created_by) REFERENCES Users(id) ON DELETE CASCADE
);

-- Messages Table
CREATE TABLE Messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    thread_id INT,
    user_id INT,
    message TEXT NOT NULL,
    created_at DATE,
    FOREIGN KEY (thread_id) REFERENCES DiscussionThreads(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES Users(id) ON DELETE CASCADE
);

DELIMITER //

CREATE PROCEDURE knowngoodstate()
BEGIN


    delete from Messages;
    alter table Messages auto_increment = 1;
    delete from DiscussionThreads;
    alter table DiscussionThreads auto_increment = 1;
    delete from BookClubMembers;
    alter table BookClubMembers auto_increment = 1;
	delete from BookClubs;
    alter table BookClubs auto_increment = 1;
    delete from Reviews;
    alter table Reviews auto_increment = 1;
	delete from Books;
    alter table Books auto_increment = 1;
    delete from Users;
    alter table Users auto_increment = 1;
    
 -- Insert data into Books table
    INSERT INTO Books (title, author, isbn, description) VALUES
    ('Pride and Prejudice', 'Jane Austen', '9780141040349', 'A classic novel of manners and morality.'),
    ('The Hobbit', 'J.R.R. Tolkien', '9780345339683', 'A fantasy novel about the adventures of Bilbo Baggins.'),
    ('Brave New World', 'Aldous Huxley', '9780060850524', 'A dystopian novel that explores a future society of extreme consumerism.'),
    ('The Catcher in the Rye', 'J.D. Salinger', '9780316769488', 'A novel about teenage rebellion and angst.'),
    ('Moby Dick', 'Herman Melville', '9780142437247', 'A novel about Captain Ahab’s obsession with the white whale.'),
    ('The Great Gatsby', 'F. Scott Fitzgerald', '9780743273565', 'A story about the jazz age in the United States.');

    -- Insert data into Users table
    INSERT INTO Users (username, password, email, is_admin, created_at) VALUES
    ('alice_jones', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'alice.jones@example.com', 0, '2023-01-10'),
    ('bob_martin', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'bob.martin@example.com', 1, '2023-02-12'),
    ('carol_white', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'carol.white@example.com', 0, '2023-03-15'),
    ('dave_clark', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'dave.clark@example.com', 0, '2023-04-18'),
    ('emily_smith', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'emily.smith@example.com', 1, '2023-05-20'),
    ('frank_taylor', '$2a$10$ntB7CsRKQzuLoKY3rfoAQen5nNyiC/U60wBsWnnYrtQQi8Z3IZzQa', 'frank.taylor@example.com', 0, '2023-06-22');

    -- Insert data into BookClubs table
    INSERT INTO BookClubs (name, description, genre, location, `online`, featured_book, created_at, created_by) VALUES
    ('Fantasy Readers Club', 'A club for fans of fantasy literature.', 'Fantasy', 'Seattle, WA', 1, '9780345339683', '2023-06-01', 2),
    ('Sci-Fi Enthusiasts', 'For those who love science fiction.', 'Science Fiction', 'Austin, TX', 0, '9780060850524', '2023-07-01', 3),
    ('Historical Fiction Society', 'A club focusing on historical fiction novels.', 'Historical Fiction', 'Boston, MA', 1, '9780141040349', '2023-08-01', 4),
    ('Poetry Lovers Group', 'A group for those who appreciate poetry.', 'Poetry', 'San Diego, CA', 0, '9780316769488', '2023-09-01', 1);

    -- Insert data into BookClubMembers table
    INSERT INTO BookClubMembers (user_id, bookclub_id, joined_at) VALUES
    (1, 1, '2023-06-05'),
    (2, 1, '2023-06-06'),
    (3, 2, '2023-07-02'),
    (4, 2, '2023-07-03'),
    (5, 3, '2023-08-02'),
    (6, 3, '2023-08-03'),
    (1, 4, '2023-09-05'),
    (2, 4, '2023-09-06');

    -- Insert data into Reviews table
    INSERT INTO Reviews (user_id, isbn, rating, review_text, created_at) VALUES
    (1, '9780141040349', 4, 'A captivating tale of obsession and adventure.', '2023-06-12'),
    (2, '9780345339683', 5, 'An incredible story that defines a generation.', '2023-07-13'),
    (3, '9780060850524', 5, 'An insightful and beautifully written novel.', '2023-08-14'),
    (4, '9780316769488', 4, 'A fantastic journey into a magical world.', '2023-09-15'),
    (5, '9780142437247', 3, 'A thought-provoking but sometimes slow-paced read.', '2023-10-16'),
    (6, '9780743273565', 2, 'A classic but not quite engaging for me.', '2023-11-17');

    -- Insert data into DiscussionThreads table
    INSERT INTO DiscussionThreads (bookclub_id, title, created_by, created_at) VALUES
    (1, 'Discussing "The Hobbit"', 2, '2023-06-07'),
    (2, 'Future Sci-Fi Picks?', 3, '2023-07-07'),
    (3, 'Historical Accuracy in Fiction', 4, '2023-08-07'),
    (4, 'Best Poems of the 20th Century', 1, '2023-09-07');

    -- Insert data into Messages table
    INSERT INTO Messages (thread_id, user_id, message, created_at) VALUES
    (1, 1, 'I loved the part where Bilbo...', '2023-06-07'),
    (2, 2, 'What do you think of the new release...', '2023-07-07'),
    (3, 3, 'The research in this book is outstanding...', '2023-08-07'),
    (4, 4, 'Has anyone read the latest collection by...', '2023-09-07');
    
END //

DELIMITER ;

    