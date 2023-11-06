package ra.config;

import static ra.config.ConsoleColor.printlnError;

public class Validate {
    public static boolean isValidFullName(String fullName) {
        if (fullName.isEmpty()) {
            printlnError("Họ và tên không được để trống");
            return false;
        }
        return true;
    }
    public static boolean isValidPassword(String password) {
        String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&*!])[A-Za-z\\d@#$%^&*!]{8,}$";
        if (password.isEmpty()) {
            printlnError("Password không được để trống.");
            return false;
        } else if (password.length() < 8) {
            printlnError("Password phải có ít nhất 8 ký tự. ");
            return false;
        } else if (!password.matches(passwordRegex)) {
            printlnError("Password chứa ký tự hoa, ký tự đặc biệt và số.");
            return false;
        }
        return true;
    }

    public  static  boolean isValidEmail(String email) {
        String emailRegex ="^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
        if(email.isEmpty()) {
            printlnError("Email không được để trống");
            return false;
        } else if (!email.matches(emailRegex)) {
            printlnError("Email không đúng định dạng!!");
            return false;
        }
        return true;
    }

    public static boolean isValidPhone(String phone) {
        String phoneRegex = "^(\\+\\d{1,3}[- ]?)?\\d{10}$";

        if (phone.isEmpty()) {
            printlnError("Số điện thoại không được để trống.");
            return false;
        } else if (!phone.matches(phoneRegex)) {
            printlnError("Số điện thoại không hợp lệ.");
            return false;
        }

        return true;
    }

    public static boolean isValidAddress(String address) {
        if (address.isEmpty()) {
            printlnError("Địa chỉ không để trống");
            return false;
        }
        return true;
    }
}
