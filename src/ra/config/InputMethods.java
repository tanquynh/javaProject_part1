package ra.config;

import java.util.Scanner;

public class InputMethods {
    public static Scanner scanner() {
        return new Scanner(System.in);
    }
    public static String getInput() {
        return scanner().nextLine();
    }
public static String getString() {
        while (true) {
            String result = getInput();
            if(result.trim().equals("")) {
                System.err.println("Dữ liệu nhập vào không được để trống: ");
            continue;
            }
                return result;
        }
}

    public static int getInteger() {
        while (true) {
         try {
             return Integer.parseInt(getString());
         } catch (NumberFormatException exception) {
             System.err.println("Dữ liệu nhập vào phải là 1 só: ");
         }
        }
    }
    public static double getDouble() {
        while (true) {
            try {
                return Double.parseDouble(getString());
            } catch (NumberFormatException errException) {
                System.err.println("Dữ liệu nhập vào phải là 1 só: ");
            }
        }
    }
}
