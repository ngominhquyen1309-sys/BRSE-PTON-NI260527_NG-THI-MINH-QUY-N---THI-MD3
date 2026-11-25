package library;

import database.Db;

import java.sql.*;

public class BorrowCard {

    private long card_id;
    private String book_title;
    private String borrower_name;
    private Timestamp borrow_date;
    private Timestamp return_deadline;
    private int quantity;
    private String status;

    public BorrowCard() {
    }

    public BorrowCard(long card_id, String book_title, String borrower_name,
                      Timestamp borrow_date, Timestamp return_deadline,
                      int quantity, String status) {
        this.card_id = card_id;
        this.book_title = book_title;
        this.borrower_name = borrower_name;
        this.borrow_date = borrow_date;
        this.return_deadline = return_deadline;
        this.quantity = quantity;
        this.status = status;
    }

    public long getCard_id() {
        return card_id;
    }

    public void setCard_id(long card_id) {
        this.card_id = card_id;
    }

    public String getBook_title() {
        return book_title;
    }

    public void setBook_title(String book_title) {
        this.book_title = book_title;
    }

    public String getBorrower_name() {
        return borrower_name;
    }

    public void setBorrower_name(String borrower_name) {
        this.borrower_name = borrower_name;
    }

    public Timestamp getBorrow_date() {
        return borrow_date;
    }

    public void setBorrow_date(Timestamp borrow_date) {
        this.borrow_date = borrow_date;
    }

    public Timestamp getReturn_deadline() {
        return return_deadline;
    }

    public void setReturn_deadline(Timestamp return_deadline) {
        this.return_deadline = return_deadline;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class LibraryManagement {

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
    }
}
