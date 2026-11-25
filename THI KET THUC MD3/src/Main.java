import database.Db;
import library.InputUtil;
import library.LibraryManagement;

public static void main(String[] args) {
    Db db = new Db();
    int choice;

    do{
        System.out.println("================= LIBRARY MANAGEMENT =================");
        System.out.println("1.Danh sách tất cả phiếu mượn");
        System.out.println("2.Thêm mới phiếu mượn");
        System.out.println("3.Cập nhật thông tin phiếu mượn");
        System.out.println("4.Xóa phiếu mượn");
        System.out.println("5.Tìm kiếm phiếu mượn theo tên độc giả");
        System.out.println("6.Tìm kiếm phiếu mượn theo tên sách");
        System.out.println("7.Thoát");

        choice = InputUtil.readInt("Nhập lựa chọn của bạn: ");

        switch (choice) {
            case 1:
                LibraryManagement.getAllBorrowCards();
                break;
            case 2:
                LibraryManagement.addBorrowCard();
                break;
            case 3:
                LibraryManagement.updateBorrowCard();
                break;
            case 4:
                LibraryManagement.deleteBorrowCard();
                break;
            case 5:
                LibraryManagement.getBorrowCardsByBorrower();
                break;
            case 6:
                LibraryManagement.searchBorrowCardsByBookTitle();;
                break;
            case 7:
                System.out.println("Bạn đã thoát chương trình");
                break;
        }
    } while (choice !=7);

}
