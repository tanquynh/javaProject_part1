package ra.views;

import ra.config.InputMethods;
import ra.config.Validate;

import ra.model.*;
import ra.service.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.config.Until.formatCurrency;
import static ra.config.Until.formattedPhoneNumber;
import static ra.contant.Contant.CategoryStatus.HIDE;
import static ra.contant.Contant.ProductStatus.Hide;
import static ra.contant.Contant.ProductStatus.UnHide;

public class MenuViews {
    private CartView cartView;
    private ProductService productService;
    private CartService cartService;
    private CategoryService categoryService;
    private UserService userService;
    private OrderService orderService;
    private UserViews userViews;

    public MenuViews() {
        this.cartView = new CartView();
        this.productService = new ProductService();
        this.cartService = new CartService();
        this.categoryService = new CategoryService();
        this.userService = new UserService();
        this.orderService = new OrderService();
        this.userViews = new UserViews();
    }


    public void displayUserMenuProduct() {

        int choice;

        do {
            print(BLUE);

            System.out.println(".-------------------------------------------------------------.");
            System.out.println("| WELCOME USER : " + userService.userActive().getUsername() + "                                        |");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|                 1. TÌM KIẾM SẢN PHẨM THEO TÊN               |");
            System.out.println("|                 2. HIỂN THỊ SẢN PHẨM THEO DANH MỤC          |");
            System.out.println("|                 3. DANH SÁCH SẢN PHẨM                       |");
            System.out.println("|                 4. HIỂN THỊ THEO GIÁ GIẢM DẦN               |");
            System.out.println("|                 5. THÊM VÀO GIỎ HÀNG                        |");
            System.out.println("|                 6. GIỎ HÀNG                                 |");
            System.out.println("|                 7. MY PROFILE                               |");
            System.out.println("|                 0. ĐĂNG XUẤT                                |");
            System.out.println("'-------------------------------------------------------------'\n");
            printFinish();
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            choice = getInteger();

            switch (choice) {
                case 1:
                    searchProduct();
                    break;
                case 2:
                    showProductByCategory();
                    break;
                case 3:
                    displayProductList();
                    break;
                case 4:
                    SortProduct();
                    break;
                case 5:
                    addToCart();
                    break;
                case 6:
                    new CartView().displayMenuCart();
                    break;
                case 7:
                    MyAcount();
                    break;
                case 0:
                    new UserViews().logout();
                default:
                    break;
            }
        } while (choice != 5);
    }


    public List<Product> showProductByCategory() {
        List<Product> products = productService.findAll();
        List<Category> categories = categoryService.findAll();
        List<Product> findProducts = new ArrayList<>();

        if (categories.isEmpty()) {
            System.err.println("Danh sách Category rỗng");
        } else {
            print(GREEN);
            System.out.println("\n                       DANH SÁCH CATEGORY               ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category catalog : categories) {
                if (catalog.isCategoryStatus() != HIDE) {


                    System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                            catalog.getId(), catalog.getCategoryName(), catalog.getCategoryDes(), (catalog.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
                }
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();

        }

        boolean categoryFound = false;
        int searchId = -1;

        while (!categoryFound) {
            System.out.println("Mời bạn nhập id category:");
            searchId = getInteger();

            for (Category category : categories) {
                if (category.getId().equals(searchId)) {
                    categoryFound = true;
                    break;
                }
            }

            if (!categoryFound) {
                System.err.println("ID không hợp lệ, mời nhập lại.");
            }
        }
        for (Product product : products) {
            if (product.getCategory().getId().equals(searchId)) {
                if (product.isProductStatus() != Hide) {
                    findProducts.add(product);
                }

            }
        }


        if (findProducts.isEmpty()) {
            System.err.println("Không tìm thấy sản phẩm trong danh mục này.");
        } else {
            print(GREEN);
            System.out.println("\n                                           DANH SÁCH PRODUCT            ");
            System.out.println("|----------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
            System.out.println("|----------------------------------------------------------------------------------------|");
            for (Product product : findProducts) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));

            }

            System.out.println("|----------------------------------------------------------------------------------------|");
            printFinish();
        }

        return findProducts;
    }

    private void addToCart() {
        List<Product> products = productService.findAll();
        if (products.isEmpty()) {
            printlnError("Chưa có sản phẩm");

        }
        // Hiển thị danh sách sản phẩm

        print(GREEN);
        System.out.println("\n                                           DANH SÁCH PRODUCT            ");
        System.out.println("|----------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
        System.out.println("|----------------------------------------------------------------------------------------|");
        for (Product product : products) {
            if (product.getCategory().isCategoryStatus() != HIDE && product.isProductStatus() != Hide) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));

            }
        }
        System.out.println("|----------------------------------------------------------------------------------------|");
        printFinish();


        System.out.println("Nhập vào ID sản phẩm để thêm vào giỏ hàng");
        int productId;
        while (true) {
            productId = getInteger();
            Product product = productService.findById(productId);
            if (product == null || product.isProductStatus() == Hide || product.getCategory().isCategoryStatus() == HIDE) {
                System.err.println("Không tìm thấy sản phẩm hoặc với ID " + productId);
            } else {
                // Tìm thấy sản phẩm và sản phẩm không bị ẩn, thoát khỏi vòng lặp
                break;
            }
        }


        // Tạo đối tượng Cart để lưu thông tin sản phẩm
        Cart cart = new Cart();
        cart.setProduct(productService.findById(productId));
        cart.setCartId(cartService.autoInc());

        while (true) {
            System.out.println("Nhập vào số lượng muốn thêm vào giỏ hàng: ");
            int count = getInteger();
            if (count <= 0) {
                printlnError("Nhập số lượng sản phẩm lớn hơn 0");
            } else if (count > productService.findById(productId).getStock()) {
                printlnError("Số lượng này lớn hơn hàng chúng tôi có sẵn. Vui lòng giảm số lượng xuống.");
            } else {
                cart.setQuantity(count);
                break;
            }
        }

        // Lưu đối tượng Cart vào giỏ hàng
        cartService.save(cart);
        printlnSuccess("Thêm vào giỏ hàng thành công🎈🎈!!");
        displayUserMenuProduct();

    }


    private void searchProduct() {

        List<Product> products = productService.getSerchProduct();
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống!!");

        } else {
            print(GREEN);
            System.out.println("\n                                           DANH SÁCH PRODUCT            ");
            System.out.println("|----------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
            System.out.println("|----------------------------------------------------------------------------------------|");

            for (Product product : products) {
                if (product.isProductStatus() != Hide && product.getCategory().isCategoryStatus() != HIDE) {
                    System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                            product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
                }
            }
            System.out.println("|----------------------------------------------------------------------------------------|");
            printFinish();
        }
    }

    private void displayProductList() {
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        List<Product> productList = productService.getProductList();
        if (productList.size() == 0) {
            System.out.println("Danh sách sản phẩm trống!!!");
        } else

            print(GREEN);
        System.out.println("\n                                        DANH SÁCH PRODUCT            ");
        System.out.println("|----------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
        System.out.println("|----------------------------------------------------------------------------------------|");

        for (Product product : productList) {
            if (product.isProductStatus() != Hide && product.getCategory().isCategoryStatus() != HIDE) {
                System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
        }
        System.out.println("|----------------------------------------------------------------------------------------|");
        printFinish();
    }

    private void SortProduct() {
        List<Product> sortProduct = productService.getSortPriceproducts();
        if (sortProduct.isEmpty()) {
            System.out.println("Danh sách rỗng !!!");
        } else {
            print(GREEN);
            System.out.println("\n                      DANH SÁCH PRODUCT SẮP XẾP THEO GIÁ GIẢM DẦN           ");
            System.out.println("|----------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |     PRICE   |  CATEGORY  |   STATUS " + " |");
            System.out.println("|----------------------------------------------------------------------------------------|");
            for (Product product : sortProduct) {
                if (product.isProductStatus() != Hide && product.getCategory().isCategoryStatus() != HIDE) {
                    System.out.printf("|%-5d | %-17s | %-20s | %-10s| %-11s|%-11s|%n",
                            product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
                }

            }

            System.out.println("|----------------------------------------------------------------------------------------|");
            printFinish();
        }
    }

    public void MyAcount() {
        int choice;
        do {

            print(BLUE);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("|                     MENU MY PROFILE                    |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. ĐỔI MẬT KHẨU                    |");
            System.out.println("|                     2. HIỂN THỊ THÔNG TIN              |");
            System.out.println("|                     3. CHỈNH SỬA THÔNG TIN            |");
            System.out.println("|                     4. QUAY LẠI MENU TRƯỚC             |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");

            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                    changePassword();
                    break;
                case 2:
                    showInforUser();
                    break;
                case 3:
                    changeInforUser();
                    break;
                case 4:
                    return;
                case 0:
                    new UserViews().logout();
                    break;
                default:
                    break;
            }

        } while (choice != 5);
    }

    private void changeInforUser() {
        System.out.println("Thay đổi thông tin User");
        int userId = (int) userService.userActive().getId();
        List<User> users = userService.findAll();
        User user = userService.findById(userId);
        while (true) {
            System.out.println("Hãy nhâp vào họ và tên đầy đủ :(Enter để bỏ qua) ");
            String fullName = InputMethods.scanner().nextLine();
            if (fullName.isEmpty()) {
                break;
            } else if (Validate.isValidFullName(fullName)) {
                user.setFullName(fullName);
                break;
            }
        }

        while (true) {
            System.out.println("Hãy nhập tên đăng nhập mới: (Enter để bỏ qua)");
            String username = scanner().nextLine();
            if (username.isEmpty()) {
                break;
            } else if (Validate.isValidFullName(username)) {
                boolean isUsernameAvailable = true;
                if (users != null) {
                    for (User existingUser : users) {
                        if (existingUser.getUsername().trim().equals(username)) {
                            printlnError("Tên đăng nhập đã được sử dụng, mời nhập tên đăng nhập mới.");
                            isUsernameAvailable = false;
                            break;
                        }
                    }
                } else {
                    isUsernameAvailable = false;
                }

                if (isUsernameAvailable) {
                    user.setUsername(username);
                    break; // Kết thúc vòng lặp khi tên đăng nhập hợp lệ và không trùng lặp
                }
            }
        }

        while (true) {
            System.out.println("Hãy nhập vào email mới:(Enter để bỏ qua) ");
            String email = scanner().nextLine();
            if (email.isEmpty()) {
                break;
            } else if (Validate.isValidEmail(email)) {
                boolean isEmailAvailable = true;

                if (users != null) {
                    for (User existingUser : users) {
                        if (existingUser.getEmail().trim().equals(email)) {
                            printlnError("Email đã được sử dụng, mời nhập email mới.");
                            isEmailAvailable = false;
                            break;
                        }
                    }
                } else {
                    isEmailAvailable = false;
                }

                if (isEmailAvailable) {
                    user.setEmail(email);
                    break; // Kết thúc vòng lặp khi email hợp lệ và không trùng lặp
                }
            }
        }

        while (true) {
            System.out.println("Hãy nhập vào số điện thoại: (Enter để bỏ qua) ");
            String phone = scanner().nextLine();
            if (phone.isEmpty()) {
                break;
            } else if (Validate.isValidPhone(phone)) {
                boolean isPhoneAvailable = true;
                if (users != null) {
                    for (User existingUser : userService.findAll()) {
                        if (existingUser.getPhone().trim().equals(phone)) {
                            printlnError("Số điện thoại đã được sử dụng, mời nhập số điện thoại mới.");
                            isPhoneAvailable = false;
                            break;
                        }
                    }
                } else {
                    isPhoneAvailable = false;
                }

                if (isPhoneAvailable) {
                    user.setPhone(phone);
                    break; // Kết thúc vòng lặp khi số điện thoại hợp lệ và không trùng lặp
                }
            }
        }

        // Nhập địa chỉ
        while (true) {
            System.out.println("Hãy nhập vào địa chỉ: (Enter để bỏ qua) ");
            String address = scanner().nextLine();
            if (address.isEmpty()) {
                break;
            } else if (Validate.isValidAddress(address)) {
                user.setAddress(address);
                break;
            }

        }
        user.setUpdateAt(LocalDate.now());
        userService.save(user);
        System.out.println("Thay đổi thông tin thành công!");

    }

    private void showInforUser() {

        int userId = (int) userService.userActive().getId();
        User user = userService.findById(userId);
        print(GREEN);
        System.out.println("\n                                                   THÔNG TIN USER                  ");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");
        System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |   UPDATE AT " + "|");
        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");

        System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-11s |%n",
                user.getId(), user.getUsername(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());

        System.out.println("|------------------------------------------------------------------------------------------------------------------------------|");
        printFinish();

    }

    private void changePassword() {
        System.out.println("Mời bạn nhập mật khẩu cũ:");
        String oldPassword = scanner().nextLine();

        if (userService.userActive().getPassword().equals(oldPassword)) {
            boolean newPasswordValid = false;

            while (!newPasswordValid) {
                System.out.println("Hãy nhập vào mật khẩu mới:");
                String newPassword = InputMethods.scanner().nextLine();

                if (Validate.isValidPassword(newPassword)) {
                    int userId = (int) userService.userActive().getId();
                    User user = userService.findById(userId);
                    user.setPassword(newPassword);
                    userService.save(user);
                    newPasswordValid = true;
                    System.out.println("Đổi mật khẩu thành công!");
                } else {
                    System.err.println("Mật khẩu mới không hợp lệ. Hãy thử lại.");
                }
            }
        } else {
            System.err.println("Mật khẩu cũ không chính xác. Thay đổi mật khẩu thất bại.");
        }
    }


    public void displayMenuAdminMenuProduct() {
        int choice;
        do {

            print(BLUE);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("| WELCOME ADMIN : " + userService.userActive().getUsername() + "                                  |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. THÊM MỚI SẢN PHẨM               |");
            System.out.println("|                     2. HIỂN THỊ DANH SÁCH SẢN PHẨM     |");
            System.out.println("|                     3. CHỈNH SỬA THÔNG TIN SẢN PHẨM    |");
            System.out.println("|                     4. ẨN / HIỆN SẢN PHẨM              |");
            System.out.println("|                     5. ẨN / HIỆN NHIỀU SẢN PHẨM        |");
            System.out.println("|                     6. TÌM KIẾM SẢN PHẨM               |");
            System.out.println("|                     7. QUAY LẠI MENNU TRƯỚC            |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");

            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                    addProduct();
                    break;
                case 2:
                    displayProducts();
                    break;
                case 3:
                    editProduct();
                    break;
                case 4:
                    hideProduct();
                    break;
                case 5:
                    hideAllProduct();
                    break;
                case 6:
                    searchProductByName();
                    break;
                case 7:
                    return;
                case 0:
                    new UserViews().logout();
                    break;
                default:
                    break;
            }

        } while (choice != 5);
    }

    private void displayProducts() {
        List<Product> productList = productService.getProductList();
        if (productList.isEmpty()) {
            System.err.println("Danh sách sản phẩm trống!!!");
        } else {

            print(GREEN);
            System.out.println("                                        DANH SÁCH PRODUCT            ");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STOCK  |      PRICE    |   CATEGORY  |  STATUS " + " |");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");

            for (Product product : productList) {
                System.out.printf("|%-5d | %-17s | %-20s | %-8s| %-14s| %-12s| %-9s |%n",
                        product.getId(), product.getProductName(), product.getProductDes(), product.getStock(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            printFinish();
        }
    }

    private void hideProduct() {
        List<User> users = userService.findAll();
        boolean isChange = false;

        System.out.println("Hãy nhập id sản phẩm bạn muốn thay đổi trạng thái:");
        int idProduct = getInteger();
        Product product = productService.findById(idProduct);
        if (product == null) {
            printlnError("Không tìm thấy sản phẩm bạn muốn đổi trạng thái !!");
        } else {
            for (User user : users
            ) {
                for (Cart cart : user.getCart()) {
                    if (cart.getProduct().getId().equals(idProduct)) {
                        isChange = true;
                    }
                }
            }
            if (isChange) {
                printlnError("Sản phẩm có trong giỏ hàng, nên không thể ẩn sản phẩm");

            } else {
                productService.updateProductStatus((product.isProductStatus() == Hide ? UnHide : Hide), idProduct);

                printlnSuccess("Thay đổi trạng thái thành công!");
            }

        }
    }


    private void hideAllProduct() {
        List<User> users = userService.findAll();
        System.out.println("Nhập danh sách mã sản phẩm cần ẩn/hiện (cách nhau bằng dấu phẩy):");
        String inputIds = scanner().nextLine();
        // Tách danh sách mã sản phẩm thành mảng các ID
        String[] idStrings = inputIds.split(",");
        boolean anyChanges = false;

        for (String idString : idStrings) {
            try {
                int idProduct = Integer.parseInt(idString);
                Product product = productService.findById(idProduct);

                if (product == null) {
                    System.err.println("ID " + idProduct + " không tồn tại.");
                } else {
                    boolean isChange = false;
                    for (User user : users) {
                        for (Cart cart : user.getCart()) {
                            if (cart.getProduct().getId().equals(idProduct)) {
                                isChange = true;
                                break;  // Thoát khỏi vòng lặp khi sản phẩm được tìm thấy trong giỏ hàng
                            }
                        }
                        if (isChange) {
                            System.err.println("ID sản phẩm: " + idProduct + " có trong giỏ hàng của người dùng " + user.getUsername() + ", nên không thể ẩn sản phẩm");
                            break;  // Thoát khỏi vòng lặp người dùng khi sản phẩm được tìm thấy trong giỏ hàng
                        }
                    }
                    if (!isChange) {
                        boolean newStatus = (product.isProductStatus() == Hide) ? UnHide : Hide;
                        productService.updateProductStatus(newStatus, idProduct);
                        anyChanges = true;
                        System.out.println("ID sản phẩm: " + idProduct + " Thay đổi trạng thái thành công!");
                    }
                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: " + idString + " không phải là một số nguyên hợp lệ.");
            }
        }

//        if (anyChanges) {
//            System.out.println("Các thay đổi trạng thái sản phẩm đã được áp dụng!");
//        }
    }


    private void editProduct() {
        System.out.println("Nhập ID sản phẩm cần sửa: ");
        int id = getInteger();
        List<Product> products = productService.findAll();
        int index = -1; // Khởi tạo index bằng -1 để xác định xem sản phẩm có tồn tại hay không

        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId().equals(id)) {
                index = i;
                break; // Thoát vòng lặp khi tìm thấy sản phẩm với ID tương ứng
            }
        }

        if (index != -1) {
            Product productToEdit = products.get(index);
            boolean isExit = false;

            while (true) {
                System.out.println("Nhập tên sản phẩm mới (Enter để bỏ qua):");
                String productName = scanner().nextLine();
                if (!productName.trim().isEmpty()) {
                    boolean isNameExists = false;

                    for (Product pro : products) {
                        if (!pro.getId().equals(id) && pro.getProductName().equalsIgnoreCase(productName)) {
                            isNameExists = true;
                            System.err.println("Tên sản phẩm đã tồn tại, mời nhập tên mới.");
                            break;
                        }
                    }

                    if (!isNameExists) {
                        productToEdit.setProductName(productName);
                        break; // Kết thúc vòng lặp khi tên hợp lệ và không trùng lặp
                    }
                } else {
                    break;
                }
            }

            // Nhập giá sản phẩm
            System.out.println("Nhập giá sản phẩm (Enter để bỏ qua):");
            while (true) {
                String priceInput = scanner().nextLine();
                if (priceInput.trim().isEmpty()) {
                    break;
                }
                try {
                    double price = Double.parseDouble(priceInput);
                    if (price >= 0) {
                        productToEdit.setPrice(price);
                        break;
                    } else {
                        System.err.println("Giá sản phẩm phải lớn hơn hoặc bằng 0.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi: Giá sản phẩm không hợp lệ.");
                }
            }

            System.out.println("Nhập mô tả sản phẩm (Enter để bỏ qua):");
            while (true) {
                String productDes = scanner().nextLine();
                if (productDes.isEmpty()) {
                    break;
                } else {
                    productToEdit.setProductDes(productDes);
                    break;
                }
            }


            // Nhập số lượng
            System.out.println("Nhập số lượng (Enter để bỏ qua):");
            while (true) {
                String stockInput = scanner().nextLine();
                if (stockInput.trim().isEmpty()) {
                    break;
                }
                try {
                    int stock = Integer.parseInt(stockInput);
                    if (stock >= 0) {
                        productToEdit.setStock(stock);
                        break;
                    } else {
                        System.err.println("Số lượng sản phẩm phải lớn hơn hoặc bằng 0.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Lỗi: Số lượng sản phẩm không hợp lệ.");
                }
            }

            System.out.println("Danh sách danh mục:");
            List<Category> categories = categoryService.findAll();

            print(GREEN);
            System.out.println("\n                    DANH SÁCH CATEGORY                 ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category category : categories) {
                System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                        category.getId(), category.getCategoryName(), category.getCategoryDes(), (category.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();


            System.out.println("Nhập ID danh mục mới (Enter để bỏ qua):");
            while (!isExit) {
                String st = scanner().nextLine();
                if (st.isEmpty()) {
                    break; // Người dùng bỏ qua việc nhập danh mục mới
                } else if (st.matches("\\d+")) { // Kiểm tra xem chuỗi chỉ chứa chữ số
                    int newCategoryId = Integer.parseInt(st);
                    Category newCategory = categoryService.findById(newCategoryId);
                    if (newCategory != null) {


                        productToEdit.setCategory(newCategory);
                        isExit = true; // Thoát khỏi vòng lặp sau khi nhập thành công ID danh mục
                    } else {
                        System.err.println("Danh mục không tồn tại. Mời nhập lại.");
                    }
                } else {
                    System.err.println("Hãy nhập một số nguyên hợp lệ.");
                }
            }

            productService.save(productToEdit); // Cập nhật thông tin sản phẩm
            System.out.println("Sửa sản phẩm thành công");


            List<User> users = userService.findAll();
            for (User user : users
            ) {
                for (Cart cart : user.getCart()
                ) {
                    if ((int) cart.getProduct().getId() == id) {
                        cart.setProduct(productToEdit);
                    }
                }
            }
            for (User user : users
            ) {
                userService.save(user);
            }

        } else {
            System.err.println("Không tìm thấy sản phẩm cần sửa !!!");
        }
    }


    private void addProduct() {
        System.out.println("Nhập số sản phẩm cần thêm mới:");
        int numberOfProducts = getInteger();

        if (numberOfProducts <= 0) {
            System.err.println("Số sản phẩm phải lớn hơn 0");
            return; // Thoát ngay khi số lượng không hợp lệ
        }

        for (int i = 0; i < numberOfProducts; i++) {
            List<Product> products = productService.findAll();
            System.out.println("Sản phẩm thứ " + (i + 1));
            Product product = new Product();

            // Nhập tên sản phẩm và kiểm tra xem tên đã tồn tại chưa
            while (true) {
                System.out.println("Nhập tên sản phẩm:");
                String productName = getString();
                boolean isNameExists = false;

                for (Product pro : products) {
                    if (pro.getProductName().equalsIgnoreCase(productName)) {
                        isNameExists = true;
                        System.err.println("Tên sản phẩm đã tồn tại, mời nhập tên mới.");
                        break;
                    }
                }

                if (!isNameExists) {
                    product.setProductName(productName);
                    break; // Kết thúc vòng lặp khi tên hợp lệ và không trùng lặp
                }
            }

            // Nhập giá sản phẩm
            System.out.println("Nhập giá sản phẩm:");
            double price = InputMethods.getDouble();
            product.setPrice(price);

            System.out.println("Nhập mô tả sản phẩm:");
            String productDes = getString();
            product.setProductDes(productDes);

            // Nhập số lượng
            System.out.println("Nhập số lượng:");
            int quantity = getInteger();
            product.setQuantity(quantity);

            // Hiển thị danh sách danh mục
            List<Category> categories = categoryService.findAll();
            if (categories.isEmpty()) {
                printlnError("Danh sách danh mục rỗng. Vui lòng thêm danh mục trước!!");
                return; // Thoát nếu không có danh mục
            }

            print(GREEN);
            System.out.println("\n                    DANH SÁCH CATEGORY                 ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category category : categories) {
                System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                        category.getId(), category.getCategoryName(), category.getCategoryDes(), (category.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();
//            System.out.println("Nhập id catagory :");

            while (true) {
                System.out.println("Nhập id danh mục sản phẩm:");
                int categoryId = getInteger();
                Category selectedCategory = null;

                // Tìm danh mục được chọn bởi người dùng
                for (Category category : categories) {
                    if (category.getId().equals(categoryId)) {
                        selectedCategory = category;
                        break;
                    }
                }

                if (selectedCategory != null) {
                    product.setCategory(selectedCategory);
                    product.setProductStatus(UnHide);
                    product.setId(productService.autoInc());
                    productService.save(product);
                    System.out.println("Tạo sản phẩm thành công");
                    break; // Kết thúc vòng lặp sau khi sản phẩm đã được tạo
                } else {
                    System.out.println("Id danh mục không tồn tại, mời nhập lại");
                }
            }
        }
    }

    private void searchProductByName() {
        List<Product> products = productService.getSerchProduct();
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống!!");

        } else {
            print(GREEN);
            System.out.println("                                        DANH SÁCH PRODUCT            ");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STOCK  |      PRICE    |   CATEGORY  |   STATUS " + " |");
            System.out.println("|-----------------------------------------------------------------------------------------------------|");

            for (Product product : products) {
                System.out.printf("|%-5d | %-17s | %-20s | %-8s| %-14s| %-12s| %-9s |%n",
                        product.getId(), product.getProductName(), product.getProductDes(), formatCurrency(product.getPrice()), product.getCategory().getCategoryName(), (product.isProductStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-----------------------------------------------------------------------------------------------------|");
            printFinish();
        }
    }


}

