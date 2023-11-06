package ra.constant;

public class OrderStatus {
    public static byte WAITING = 0;
    public static byte ACCEPT = 1;
    public static byte CANCEL = 2;

    public static String getStatusByCode (byte codeStatus) {
        switch (codeStatus) {
            case 0:
                return "Đang xác nhận";
            case 1 :
                return "Đã được xác nhận";
            case 2:
                return "Đã bị hủy";
            default:
                return "Không hợp lệ";
        }
    }
}


