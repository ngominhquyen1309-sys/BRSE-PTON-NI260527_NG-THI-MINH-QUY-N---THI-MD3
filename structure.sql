CREATE DATABASE db_library_management;
USE db_library_management;
CREATE TABLE borrow_cards (
    card_id SERIAL PRIMARY KEY AUTO_INCREMENT,
    book_title VARCHAR(150) NOT NULL,
    borrower_name VARCHAR(100) NOT NULL,
    borrow_date TIMESTAMP NOT NULL,
    return_deadline TIMESTAMP NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(30) NOT NULL
    CHECK (status IN ('Borrowing', 'Returned', 'Overdue'))
);
INSERT INTO borrow_cards (
    book_title,
    borrower_name,
    borrow_date,
    return_deadline,
    quantity,
    status
)
VALUES
('Java Programming', 'Nguyen Van An', '2026-11-01 09:00:00', '2026-11-15 09:00:00', 2, 'Borrowing'),
('Learning MySQL', 'Tran Thi Binh', '2026-10-20 10:30:00', '2026-11-03 10:30:00', 1, 'Returned'),
('JavaScript Basic', 'Le Van Cuong', '2026-10-25 14:00:00', '2026-11-08 14:00:00', 3, 'Overdue'),
('HTML and CSS', 'Pham Thi Dung', '2026-11-10 08:00:00', '2026-11-24 08:00:00', 1, 'Borrowing'),
('Python for Beginners', 'Hoang Van Nam', '2026-10-15 13:00:00', '2026-10-29 13:00:00', 2, 'Returned');

SELECT * FROM borrow_cards;

-- stored procedures lấy danh sách tất cả các phiếu mượn--

DELIMITER //
CREATE PROCEDURE get_all_borrow_cards()
BEGIN
    SELECT * FROM borrow_cards;
END //
DELIMITER ;

-- stored procedures thêm mới một phiếu mượn--

DELIMITER //
CREATE PROCEDURE add_borrow_cards(
	IN p_book_title VARCHAR(150),
    IN P_borrower_name VARCHAR(100),
    IN P_borrow_date TIMESTAMP,
    IN p_return_deadline TIMESTAMP,
    IN p_quantity INT,
    IN p_status VARCHAR(30)
)
BEGIN
    INSERT INTO borrow_cards (
    book_title,
    borrower_name,
    borrow_date,
    return_deadline,
    quantity,
    status
    )
    VALUES (
    p_book_title,
    P_borrower_name,
    P_borrow_date,
    p_return_deadline,
    p_quantity,
    p_status
    );
END //
DELIMITER ;

-- stored procedures lấy danh sách phiếu mượn theo tên độc giả borrower_name--

DELIMITER //
CREATE PROCEDURE get_borrow_cards_by_borrower(
    IN p_borrower_name VARCHAR(100)
)
BEGIN
    SELECT *
    FROM borrow_cards
    WHERE borrower_name = p_borrower_name;
END //
DELIMITER ;

-- stored procedures cập nhật thông tin phiếu mượn theo card_id --

DELIMITER //
CREATE PROCEDURE update_borrow_card(
    IN p_card_id BIGINT UNSIGNED,
    IN p_book_title VARCHAR(150),
    IN p_borrower_name VARCHAR(100),
    IN p_borrow_date TIMESTAMP,
    IN p_return_deadline TIMESTAMP,
    IN p_quantity INT,
    IN p_status VARCHAR(30)
)
BEGIN
    UPDATE borrow_cards
    SET
        book_title = p_book_title,
        borrower_name = p_borrower_name,
        borrow_date = p_borrow_date,
        return_deadline = p_return_deadline,
        quantity = p_quantity,
        status = p_status
    WHERE card_id = p_card_id;
END //
DELIMITER ;

-- stored procedures xóa phiếu mượn theo card_id --

DELIMITER //
CREATE PROCEDURE delete_borrow_card(
    IN p_card_id BIGINT UNSIGNED
)
BEGIN
    DELETE FROM borrow_cards
    WHERE card_id = p_card_id;
END //
DELIMITER ;

-- stored procedures tìm kiếm phiếu mượn theo tên sách book_title --

DELIMITER //
CREATE PROCEDURE search_borrow_cards_by_book_title(
    IN p_book_title VARCHAR(150)
)
BEGIN
    SELECT *
    FROM borrow_cards
    WHERE LOWER(book_title) LIKE CONCAT('%', LOWER(p_book_title), '%');
END //
DELIMITER ;
