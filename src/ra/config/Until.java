package ra.config;

import java.text.NumberFormat;
import java.util.Locale;

public class Until {
    public static String formatCurrency(double amount) {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        return currencyFormatter.format(amount);
    }

    public static String formattedPhoneNumber(String phoneNumber) {
       return   phoneNumber.substring(0, 3) + "-" + phoneNumber.substring(3, 6) + "-" + phoneNumber.substring(6);
    }
}
