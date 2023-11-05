package ra.views;

import ra.model.Category;
import ra.service.*;

import java.util.ArrayList;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.constant.Contant.CategoryStatus.HIDE;
import static ra.constant.Contant.CategoryStatus.UNHIDE;

public class CategoryView {
    private UserViews userViews;
    private UserService userService;

    private ProductService productService;
    private OrderService orderService;
    private CategoryService categoryService;

    public CategoryView() {
        this.userViews = new UserViews();
        this.userService = new UserService();

        this.productService = new ProductService();
        this.orderService = new OrderService();
        this.categoryService = new CategoryService();
    }


    public UserViews getUserViews() {
        return userViews;
    }

    public UserService getUserService() {
        return userService;
    }


    public ProductService getProductService() {
        return productService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void displayAdminCategory() {
        int choice;

        do {

            print(YELLOW);
            System.out.println("╔════════════════════════════════════════════╗");
            System.out.println("║             😍🧡  ADMIN-CATEGORY 😍😍     ║");
            System.out.println("╟────────┬───────────────────────────────────╢");
            System.out.println("║   1    │    Thêm mới danh mục              ║");
            System.out.println("║   2    │    Hiển thị danh mục              ║");
            System.out.println("║   3    │    Tìm danh mục theo tên          ║");
            System.out.println("║   4    │    Chỉnh sửa danh mục             ║");
            System.out.println("║   5    │    Ẩn danh mục theo mã            ║");
            System.out.println("║   6    │    Ẩn nhiều danh mục theo mã      ║");
            System.out.println("║   7    │    Quay lại menu trước            ║");
            System.out.println("║   8    │    Đăng xuất                      ║");
            System.out.println("╚════════╧═══════════════════════════════════╝");
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
                case 8:
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
                    boolean newStatus = (category.isCategoryStatus() == HIDE) ? UNHIDE : HIDE;
                    categoryService.updateCategoryStatus(newStatus, idCategory);
                    anyChanges = true;
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
        System.out.println("Hãy nhập id Category bạn muốn thay đổi trạng thái:");
        int idCategory = getInteger();
        Category category = categoryService.findById(idCategory);
        if (category == null) {
            printlnError("Không tìm thấy category bạn muốn đổi trạng thái !!");
        } else {
            categoryService.updateCategoryStatus((category.isCategoryStatus() == HIDE ? UNHIDE : HIDE), idCategory);
            printlnSuccess("Thay đổi trạng thái thành công!");
        }
    }

    private void editCategory() {
        System.out.println("Nhập vào id danh mục cần sửa: ");
        int id = getInteger();
        List<Category> allCategory = categoryService.findAll();

        int index = categoryService.findIndex(id);
        if (index != -1) {
            Category categoryToEdit = new Category();
            boolean isExit = true;
            categoryToEdit.setId(id);
            System.out.println("Nhập vào tên danh mục mới (Enter để bỏ qua):");
            String newName = scanner().nextLine();
            if (!newName.trim().isEmpty()) {
                categoryToEdit.setCategoryName(newName);
            }

            System.out.println("Nhập vào mô tả danh mục mới (Enter để bỏ qua):");
            String newDes = scanner().nextLine();
            if (!newDes.trim().isEmpty()) {
                categoryToEdit.setCategoryDes(newDes);
            }
            categoryToEdit.setCategoryStatus(UNHIDE);
            categoryService.save(categoryToEdit);
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
        for (Category cate: categories) {
            if (cate.getCategoryName().contains(searchName.trim())) {
                category.add(cate);
                flag = true;
            }
        }
        if (flag) {
            System.out.println("Danh sách danh mục: ");
            for (Category catalog: category) {
                catalog.displayCategory();
            }
        } else {
            System.err.println("Không tìm thấy danh mục phù hợp");
        }
    }

    private void displayAllCategorys() {
        List<Category> categories = categoryService.findAll();
        if (categories.isEmpty()) {
            System.err.println("Danh sách Category rỗng");
        } else {
            System.out.println("Danh sách Category");
            for (Category category : categories) {
                category.displayCategory();
            }
        }
    }

    private void addCategory() {
        System.out.println("Nhập số danh mục cần thm mới");
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

