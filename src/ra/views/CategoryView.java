package ra.views;


import ra.config.Validate;
import ra.model.Cart;
import ra.model.Category;
import ra.model.Product;
import ra.model.User;
import ra.service.*;

import java.util.ArrayList;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.contant.Contant.CategoryStatus.HIDE;
import static ra.contant.Contant.CategoryStatus.UNHIDE;


public class CategoryView {
    private UserViews userViews;
    private UserService userService;
    private CartService cartService;
    private ProductService productService;
    private OrderService orderService;
    private CategoryService categoryService;

    public CategoryView() {
        this.userViews = new UserViews();
        this.userService = new UserService();
        this.cartService = new CartService();
        this.productService = new ProductService();
        this.orderService = new OrderService();
        this.categoryService = new CategoryService();
    }


    public void displayAdminCategory() {
        int choice;

        do {

            print(YELLOW);

            System.out.println(".-----------------------------------------------------------.");
            System.out.println("|                            ADMIN-CATEGORY                 |");
            System.out.println("|-----------------------------------------------------------|");
            System.out.println("|                     1. THÊM MỚI DANH MỤC                  |");
            System.out.println("|                     2. DANH SÁCH DANH MỤC                 |");
            System.out.println("|                     3. TÌM KIẾM DANH MỤC THEO TÊN         |");
            System.out.println("|                     4. CHỈNH SỬA THÔNG TIN DANH MUC       |");
            System.out.println("|                     5. ẨN / HIỆN DANH MỤC                 |");
            System.out.println("|                     6. ẨN / HIỆN NHIỀU DANH MỤC           |");
            System.out.println("|                     7. QUAY LẠI MENU TRƯỚC                |");
            System.out.println("|                     0. ĐĂNG XUẤT                          |");
            System.out.println("'-----------------------------------------------------------'");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();

            choice = getInteger();

            switch (choice) {
                case 1:
                    addCategory();
                    break;
                case 2:
                    displayAllCategorys();
                    break;
                case 3:
                    searchCategoryByName();
                    break;
                case 4:
                    editCategory();
                    break;
                case 5:
                    hideCategory();
                    break;
                case 6:
                    hideAllCategory();
                    break;
                case 7:
                    return;
                case 0:
                    if (userViews != null) {
                        userViews.logout();
                    }
                    break;
                default:
                    break;
            }

        } while (true);

    }

    private void hideAllCategory() {
        List<Category> categories = categoryService.findAll();
        List<User> users = userService.findAll();
        System.out.println("Nhập danh sách mã danh mục cần ẩn/hiện (cách nhau bằng dấu phẩy):");
        String inputIds = scanner().nextLine();

        // Tách danh sách mã danh mục thành mảng các ID
        String[] idStrings = inputIds.split(",");
        boolean anyChanges = false;

        for (String idString : idStrings) {
            try {
                int idCategory = Integer.parseInt(idString);
                Category category = categoryService.findById(idCategory);

                if (category == null) {
                    System.err.println("ID " + idCategory + " không tồn tại.");

                } else {
                    boolean isChange = false;
                    for (User user : users) {
                        for (Cart cart : user.getCart()) {
                            if (cart.getProduct().getCategory().getId().equals(idCategory)) {
                                isChange = true;
                                break;  // Thoát khỏi vòng lặp khi sản phẩm được tìm thấy trong giỏ hàng
                            }
                        }
                        if (isChange) {
                            System.err.println("ID sản phẩm: " + idCategory + " có trong giỏ hàng của người dùng " + user.getUsername() + ", nên không thể ẩn sản phẩm");
                            break;  // Thoát khỏi vòng lặp người dùng khi sản phẩm được tìm thấy trong giỏ hàng
                        }
                    }
                    if (!isChange) {
                        boolean newStatus = (category.isCategoryStatus() == HIDE) ? UNHIDE : HIDE;
                        categoryService.updateCategoryStatus(newStatus, idCategory);
                        anyChanges = true;
                        System.out.println("ID danh mục: " + idCategory + " Thay đổi trạng thái thành công!");
                    }

                }
            } catch (NumberFormatException e) {
                System.err.println("Lỗi: " + idString + " không phải là một số nguyên hợp lệ.");
            }
        }

        if (anyChanges) {
            printlnSuccess("Thay đổi trạng thái thành công!");
            // Lưu trạng thái của danh mục sau khi thay đổi

        }
    }

    private void hideCategory() {
        List<User> users = userService.findAll();
        boolean isChange = false;
        System.out.println("Hãy nhập id Category bạn muốn thay đổi trạng thái:");
        int idCategory = getInteger();
        Category category = categoryService.findById(idCategory);
        if (category == null) {
            printlnError("Không tìm thấy category bạn muốn đổi trạng thái !!");
        } else {
            for (User user : users
            ) {
                for (Cart cart : user.getCart()) {
                    if (cart.getProduct().getCategory().getId().equals(idCategory)) {
                        isChange = true;
                    }
                }
            }
            if (isChange) {
                printlnError("Sản phẩm có trong giỏ hàng, nên không thể ẩn Category");
            } else {

                categoryService.updateCategoryStatus((category.isCategoryStatus() == HIDE ? UNHIDE : HIDE), idCategory);
                printlnSuccess("Thay đổi trạng thái thành công!");
            }
        }
    }

    private void editCategory() {
        boolean isNameExists = true;
        System.out.println("Nhập vào id danh mục cần sửa: ");
        int id = getInteger();
        List<Category> allCategory = categoryService.findAll();

        int index = categoryService.findIndex(id);
        if (index != -1) {
            Category categoryToEdit = new Category();
            categoryToEdit.setId(id);
            System.out.println("Hãy nhập tên danh mục mới: (Enter để bỏ qua)");
            while (true) {
                String newName = scanner().nextLine();
                if (newName.isEmpty()) {
                    categoryToEdit.setCategoryName(categoryService.findById(id).getCategoryName());
                    break;
                } else if (Validate.isValidFullName(newName)) {
                    boolean isUsernameAvailable = true;
                    if (allCategory != null) {
                        for (Category category : allCategory) {
                            if (category.getCategoryName().trim().equals(newName)) {
                                printlnError("Tên danh mục đã được sử dụng, mời nhập tên danh mục mới mới.");
                                isUsernameAvailable = false;
                            }
                        }
                    } else {
                        isUsernameAvailable = false;
                    }

                    if (isUsernameAvailable) {
                        categoryToEdit.setCategoryName(newName);
                        break; // Kết thúc vòng lặp khi tên đăng nhập hợp lệ và không trùng lặp
                    }
                }
            }


            //
            System.out.println("Nhập vào mô tả danh mục mới (Enter để bỏ qua):");
            while (true) {
                String newDes = scanner().nextLine();
                if (!newDes.isEmpty()) {
                    categoryToEdit.setCategoryDes(newDes);
                    break;
                } else {
                    categoryToEdit.setCategoryDes(categoryService.findById(id).getCategoryDes());
                    break;
                }
            }
            categoryToEdit.setCategoryStatus(UNHIDE);
            categoryService.save(categoryToEdit);
            List<Product> products = productService.findAll();
            List<User> users = userService.findAll();
            int idProduct = -1;
            Product newProduct = new Product();

            for (int i = 0; i < products.size(); i++) {

                if (products.get(i).getCategory().getId().equals(id)) {
                    products.get(i).setCategory(categoryToEdit);
                    newProduct = products.get(i);
                    productService.save(newProduct);
                    idProduct = (int) products.get(i).getId();
                }
            }
            List<Cart> carts = cartService.findAll();
            Cart newCart = new Cart();
            for (int i = 0; i < carts.size(); i++) {
                if (carts.get(i).getProduct().getId().equals(idProduct)) {
                    carts.get(i).setProduct(newProduct);
                    newCart = carts.get(i);
                    cartService.save(newCart);
                }
            }

        } else {
            printlnError("Không tìm thấy mã danh mục cần sửa !!!");
        }
    }


    private void searchCategoryByName() {
        System.out.println("Nhập tên danh mục muốn tìm kiếm");
        String searchName = getString();
        List<Category> categories = categoryService.findAll();
        List<Category> category = new ArrayList<>();
        boolean flag = false;
        for (Category cate : categories) {
            if (cate.getCategoryName().contains(searchName.trim())) {
                category.add(cate);
                flag = true;
            }
        }
        if (flag) {
            print(GREEN);
            System.out.println("\n            DANH SÁCH CATEGORY THEO TÊN              ");
            System.out.println("|-------------------------------------------------------------|");
            System.out.println("|" + "  ID  |       NAME        |      DESCRIPTION     |   STATUS " + " |");
            System.out.println("|-------------------------------------------------------------|");

            for (Category catalog : category) {
                System.out.printf("|%-5d | %-17s | %-20s | %-9s |%n",
                        catalog.getId(), catalog.getCategoryName(), catalog.getCategoryDes(), (catalog.isCategoryStatus() ? "ĐANG BÁN" : "TẠM DỪNG"));
            }
            System.out.println("|-------------------------------------------------------------|");
            printFinish();
        } else {
            System.err.println("Không tìm thấy danh mục phù hợp");
        }
    }

    private void displayAllCategorys() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            System.err.println("Danh sách Category rỗng");
        } else {
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
        }
    }

    private void addCategory() {
        System.out.println("Nhập số danh mục cần thêm mới");
        int numberOfCategories = getInteger();
        if (numberOfCategories <= 0) {
            printlnError("Số danh mục phải lớn hơn 0");
            return;
        }
        for (int i = 0; i < numberOfCategories; i++) {
            List<Category> categories = categoryService.findAll();
            System.out.println("Danh mục thứ " + (i + 1));
            Category category = new Category();

            // Nhập tên danh mục và kiểm tra xem tên đã tồn tại chưa
            while (true) {
                System.out.println("Nhập tên danh mục");
                String categoryName = getString();
                boolean isNameExists = false;
                for (Category cate : categories) {
                    if (cate.getCategoryName().equalsIgnoreCase(categoryName)) {
                        isNameExists = true;
                        System.err.println("Tên danh mục đã tồn tại, mời nhập tên mới.");
                        break;
                    }
                }
                if (!isNameExists) {
                    category.setCategoryName(categoryName);
                    break; // Kết thúc vòng lặp khi tên hợp lệ và không trùng lặp
                }

            }
            System.out.println("Nhập mô tả danh mục:");
            String categoryDes = getString();
            category.setCategoryDes(categoryDes);
            category.setCategoryStatus(UNHIDE);
            category.setId(categoryService.autoInc());
            categoryService.save(category);

        }
        System.out.println("Tạo category thành công");
    }


}

