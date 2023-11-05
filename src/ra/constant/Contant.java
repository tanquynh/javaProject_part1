package ra.constant;

public class Contant {
    public static final String ADMIN_CODE = "111111";


    public static class Role {
        public static final int ADMIN = 1;
        public static final int USER = 2;

    }

    public static class Status {
        public static final boolean ACTIVE = true;
        public static final boolean INACTIVE = false;
    }

    public static class Importance {
        public static final boolean BLOCK = false;
        public static final boolean OPEN = true;
    }
    public static class FilePath{
        public static final String COMMON_PATH = "data";
        public static final String USER_FILE = "/user.dat";
        public static final String CATEGORY_FILE ="/category.dat";
        public static final String PRODUCT_FILE = "/product.dat";
        public static final String ORDER_FILE = "/order.dat";
        public static final String CART_FILE = "/cart.dat";

    }
    public static class ProductStatus {
        public  static final boolean Hide = false;
        public  static final boolean UnHide = true;
    }
    public static class CategoryStatus {
        public  static final boolean HIDE = false;
        public  static final boolean UNHIDE = true;
    }
}
