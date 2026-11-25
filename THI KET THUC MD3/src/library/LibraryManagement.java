package library;

import database.Db;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryManagement {

    private static Connection conn = Db.getConn();

    public static void getAllBorrowCards() {

        String sql = "{CALL get_all_borrow_cards()}";

        try {
            CallableStatement cs = conn.prepareCall(sql);
            ResultSet rs = cs.executeQuery();

            while (rs.next()) {

                BorrowCard card = new BorrowCard();

                card.setCard_id(rs.getLong("card_id"));
                card.setBook_title(rs.getString("book_title"));
                card.setBorrower_name(rs.getString("borrower_name"));
                card.setBorrow_date(rs.getTimestamp("borrow_date"));
                card.setReturn_deadline(rs.getTimestamp("return_deadline"));
                card.setQuantity(rs.getInt("quantity"));
                card.setStatus(rs.getString("status"));

                System.out.println("----------------------------------");
                System.out.println("Mã phiếu: " + card.getCard_id());
                System.out.println("Tên sách: " + card.getBook_title());
                System.out.println("Tên độc giả: " + card.getBorrower_name());
                System.out.println("Ngày mượn: " + card.getBorrow_date());
                System.out.println("Hạn trả: " + card.getReturn_deadline());
                System.out.println("Số lượng: " + card.getQuantity());
                System.out.println("Trạng thái: " + card.getStatus());
            }

            rs.close();
            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy danh sách phiếu mượn!");
            e.printStackTrace();
        }
    }

    public static void addBorrowCard() {

        System.out.println("\n========== THÊM PHIẾU MƯỢN ==========");

        String bookTitle = InputUtil.readString("Nhập tên sách: ");

        String borrowerName = InputUtil.readString("Nhập tên độc giả: ");

        java.time.LocalDateTime borrowDate =
                InputUtil.readLocalDateTime("Nhập ngày mượn (yyyy-MM-dd HH:mm:ss): ");

        java.time.LocalDateTime returnDeadline =
                InputUtil.readLocalDateTime("Nhập hạn trả (yyyy-MM-dd HH:mm:ss): ");

        while (!returnDeadline.isAfter(borrowDate)) {
            System.out.println("Hạn trả phải sau ngày mượn!");

            returnDeadline = InputUtil.readLocalDateTime(
                    "Nhập lại hạn trả (yyyy-MM-dd HH:mm:ss): "
            );
        }

        int quantity = InputUtil.readInt("Nhập số lượng: ", 1, Integer.MAX_VALUE);

        System.out.println("1. Borrowing");
        System.out.println("2. Returned");
        System.out.println("3. Overdue");

        int statusChoice = InputUtil.readInt("Chọn trạng thái: ", 1, 3);

        String status = "";

        switch (statusChoice) {
            case 1:
                status = "Borrowing";
                break;
            case 2:
                status = "Returned";
                break;
            case 3:
                status = "Overdue";
                break;
        }

        BorrowCard card = new BorrowCard();

        card.setBook_title(bookTitle);
        card.setBorrower_name(borrowerName);
        card.setBorrow_date(java.sql.Timestamp.valueOf(borrowDate));
        card.setReturn_deadline(java.sql.Timestamp.valueOf(returnDeadline));
        card.setQuantity(quantity);
        card.setStatus(status);

        insertBorrowCard(card);
    }

    private static void insertBorrowCard(BorrowCard card) {

        String sql = "{CALL add_borrow_card(?, ?, ?, ?, ?, ?)}";

        try {
            if (conn == null || conn.isClosed()) {
                System.out.println("Không có kết nối đến database!");
                return;
            }

            CallableStatement cs = conn.prepareCall(sql);

            cs.setString(1, card.getBook_title());
            cs.setString(2, card.getBorrower_name());
            cs.setTimestamp(3, card.getBorrow_date());
            cs.setTimestamp(4, card.getReturn_deadline());
            cs.setInt(5, card.getQuantity());
            cs.setString(6, card.getStatus());

            cs.executeUpdate();

            System.out.println("Thêm phiếu mượn thành công!");

            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm phiếu mượn!");
            e.printStackTrace();
        }
    }

    private static boolean checkBorrowCard(long cardId) {

        String sql = "SELECT * FROM borrow_cards WHERE card_id = ?";

        try {
            var ps = conn.prepareStatement(sql);

            ps.setLong(1, cardId);

            var rs = ps.executeQuery();

            boolean exists = rs.next();

            rs.close();
            ps.close();

            return exists;

        } catch (SQLException e) {
            System.out.println("Lỗi kiểm tra phiếu mượn!");
            e.printStackTrace();
            return false;
        }
    }

    public static void updateBorrowCard() {

        System.out.println("\n========== CẬP NHẬT PHIẾU MƯỢN ==========");

        long cardId = InputUtil.readInt("Nhập mã phiếu cần cập nhật: ");

        if (!checkBorrowCard(cardId)) {
            System.out.println("Không tìm thấy phiếu mượn!");
            return;
        }

        System.out.println("Nhập thông tin mới:");

        String bookTitle = InputUtil.readString("Tên sách: ");

        String borrowerName = InputUtil.readString("Tên độc giả: ");

        java.time.LocalDateTime borrowDate =
                InputUtil.readLocalDateTime("Ngày mượn (yyyy-MM-dd HH:mm:ss): ");

        java.time.LocalDateTime returnDeadline =
                InputUtil.readLocalDateTime("Hạn trả (yyyy-MM-dd HH:mm:ss): ");

        while (!returnDeadline.isAfter(borrowDate)) {
            System.out.println("Hạn trả phải sau ngày mượn!");

            returnDeadline = InputUtil.readLocalDateTime(
                    "Nhập lại hạn trả (yyyy-MM-dd HH:mm:ss): "
            );
        }

        int quantity = InputUtil.readInt(
                "Số lượng: ", 1, Integer.MAX_VALUE
        );

        System.out.println("1. Borrowing");
        System.out.println("2. Returned");
        System.out.println("3. Overdue");

        int statusChoice = InputUtil.readInt("Chọn trạng thái: ", 1, 3);

        String status = "";

        switch (statusChoice) {
            case 1:
                status = "Borrowing";
                break;
            case 2:
                status = "Returned";
                break;
            case 3:
                status = "Overdue";
                break;
        }

        BorrowCard card = new BorrowCard();

        card.setCard_id(cardId);
        card.setBook_title(bookTitle);
        card.setBorrower_name(borrowerName);
        card.setBorrow_date(java.sql.Timestamp.valueOf(borrowDate));
        card.setReturn_deadline(java.sql.Timestamp.valueOf(returnDeadline));
        card.setQuantity(quantity);
        card.setStatus(status);

        updateBorrowCardData(card);
    }

    private static void updateBorrowCardData(BorrowCard card) {

        String sql = "{CALL update_borrow_card(?, ?, ?, ?, ?, ?, ?)}";

        try {
            CallableStatement cs = conn.prepareCall(sql);

            cs.setLong(1, card.getCard_id());
            cs.setString(2, card.getBook_title());
            cs.setString(3, card.getBorrower_name());
            cs.setTimestamp(4, card.getBorrow_date());
            cs.setTimestamp(5, card.getReturn_deadline());
            cs.setInt(6, card.getQuantity());
            cs.setString(7, card.getStatus());

            cs.executeUpdate();

            System.out.println("Cập nhật phiếu mượn thành công!");

            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật phiếu mượn!");
            e.printStackTrace();
        }
    }

    public static void deleteBorrowCard() {
        long cardId = InputUtil.readInt("Nhập mã phiếu mượn cần xóa: ");

        if (!checkBorrowCard(cardId)) {
            System.out.println("Không tìm thấy phiếu mượn!");
            return;
        }

        String confirm = InputUtil.readString(
                "Bạn có chắc chắn muốn xóa phiếu mượn này không? (Y/N): "
        );

        if (!confirm.equalsIgnoreCase("Y")) {
            System.out.println("Đã hủy thao tác xóa.");
            return;
        }

        deleteBorrowCardData(cardId);
    }

    private static void deleteBorrowCardData(long cardId) {

        String sql = "{CALL delete_borrow_card(?)}";

        try {
            CallableStatement cs = conn.prepareCall(sql);

            cs.setLong(1, cardId);

            cs.executeUpdate();

            System.out.println("Xóa phiếu mượn thành công!");

            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi xóa phiếu mượn!");
            e.printStackTrace();
        }
    }

    public static void getBorrowCardsByBorrower() {

        String borrowerName = InputUtil.readString(
                "Nhập tên độc giả cần tìm: "
        );

        String sql = "{CALL get_borrow_cards_by_borrower(?)}";

        try {
            CallableStatement cs = conn.prepareCall(sql);

            cs.setString(1, borrowerName);

            ResultSet rs = cs.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                BorrowCard card = new BorrowCard();

                card.setCard_id(rs.getLong("card_id"));
                card.setBook_title(rs.getString("book_title"));
                card.setBorrower_name(rs.getString("borrower_name"));
                card.setBorrow_date(rs.getTimestamp("borrow_date"));
                card.setReturn_deadline(rs.getTimestamp("return_deadline"));
                card.setQuantity(rs.getInt("quantity"));
                card.setStatus(rs.getString("status"));

                System.out.println("----------------------------------------");
                System.out.println("Mã phiếu mượn: " + card.getCard_id());
                System.out.println("Tên sách: " + card.getBook_title());
                System.out.println("Tên độc giả: " + card.getBorrower_name());
                System.out.println("Ngày mượn: " + card.getBorrow_date());
                System.out.println("Hạn trả: " + card.getReturn_deadline());
                System.out.println("Số lượng: " + card.getQuantity());
                System.out.println("Trạng thái: " + card.getStatus());
            }

            if (!found) {
                System.out.println("Không tìm thấy phiếu mượn phù hợp!");
            }

            rs.close();
            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm phiếu mượn!");
            e.printStackTrace();
        }
    }

    public static void searchBorrowCardsByBookTitle() {

        String bookTitle = InputUtil.readString(
                "Nhập tên sách hoặc từ khóa cần tìm: "
        );

        String sql = "{CALL search_borrow_cards_by_book_title(?)}";

        try {
            CallableStatement cs = conn.prepareCall(sql);

            cs.setString(1, bookTitle);

            ResultSet rs = cs.executeQuery();

            boolean found = false;

            while (rs.next()) {
                found = true;

                BorrowCard card = new BorrowCard();

                card.setCard_id(rs.getLong("card_id"));
                card.setBook_title(rs.getString("book_title"));
                card.setBorrower_name(rs.getString("borrower_name"));
                card.setBorrow_date(rs.getTimestamp("borrow_date"));
                card.setReturn_deadline(rs.getTimestamp("return_deadline"));
                card.setQuantity(rs.getInt("quantity"));
                card.setStatus(rs.getString("status"));

                System.out.println("----------------------------------------");
                System.out.println("Mã phiếu mượn: " + card.getCard_id());
                System.out.println("Tên sách: " + card.getBook_title());
                System.out.println("Tên độc giả: " + card.getBorrower_name());
                System.out.println("Ngày mượn: " + card.getBorrow_date());
                System.out.println("Hạn trả: " + card.getReturn_deadline());
                System.out.println("Số lượng: " + card.getQuantity());
                System.out.println("Trạng thái: " + card.getStatus());
            }

            if (!found) {
                System.out.println("Không tìm thấy phiếu mượn phù hợp!");
            }

            rs.close();
            cs.close();

        } catch (SQLException e) {
            System.out.println("Lỗi khi tìm kiếm phiếu mượn theo tên sách!");
            e.printStackTrace();
        }
    }
}
