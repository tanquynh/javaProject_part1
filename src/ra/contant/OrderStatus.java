package ra.contant;

public class OrderStatus {
    public static byte WAITING = 0;
    public static byte ACCEPT = 1;
    public static byte CANCEL = 2;
    public static byte DELIVERY = 3;
    public static byte SUCCESS = 4;

    public static String getStatusByCode (byte codeStatus) {
        switch (codeStatus) {
            case 0:
                return "Đang xác nhận";
            case 1 :
                return "Đã được xác nhận";
            case 2:
                return "Đã bị hủy";
            case 3:
                return "Đang giao";
            case 4:
                return "Đã hoàn thành";
            default:
                return "Không hợp lệ";
        }
    }
}


