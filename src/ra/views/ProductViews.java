package ra.views;

import ra.config.InputMethods;
import ra.config.Validate;

import ra.model.Cart;
import ra.model.Category;
import ra.model.Product;
import ra.model.User;
import ra.service.CartService;
import ra.service.CategoryService;
import ra.service.ProductService;
import ra.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.constant.Contant.CategoryStatus.HIDE;
import static ra.constant.Contant.ProductStatus.Hide;
import static ra.constant.Contant.ProductStatus.UnHide;

public class ProductViews {
    private CartView cartView;
    private ProductService productService;
    private CartService cartService;
    private CategoryService categoryService;
    private UserService userService;
    private UserViews userViews;

    public ProductViews() {
        this.cartView = new CartView();
        this.productService = new ProductService();
        this.cartService = new CartService();
        this.categoryService = new CategoryService();
        this.userService = new UserService();
        this.userViews = new UserViews();
    }

//    public CartView getCartView() {
//        return cartView;
//    }
//
//    public ProductService getProductService() {
//        return productService;
//    }
//
//    public CartService getCartService() {
//        return cartService;
//    }
//
//    public CategoryService getCategoryService() {
//        return categoryService;
//    }
//
//    public UserService getUserService() {
//        return userService;
//    }
//
//    public UserViews getUserViews() {
//        return userViews;
//    }

    public void displayUserMenuProduct() {
        int choice;

        do {
            print(BLUE);
            System.out.println("╔═══════════════════════════════════════════════════╗");
            System.out.println("║                😍🧡USER-PRODUCT😍😍              ║");
            System.out.println("╟────────┬──────────────────────────────────────────║");
            System.out.println("║   1    │    Tìm kiếm sản phẩm                     ║");
            System.out.println("║   2    │    Danh sách sản phẩm                    ║");
            System.out.println("║   3    │    Hiển thị theo giá giảm dần            ║");
            System.out.println("║   4    │    Thêm vào  giỏ hàng                    ║");
            System.out.println("║   5    │    Giỏ hàng                              ║");
            System.out.println("║   6    │    Quay lại menu trước                   ║");
            System.out.println("║   7    │    Đăng xuất                             ║");
            System.out.println("╚════════╧══════════════════════════════════════════╝");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡: ");
            printFinish();
            choice = getInteger();

            switch (choice) {
                case 1:
                    searchProduct();
                    break;
                case 2:
                    displayProductList();
                    break;
                case 3:
                    SortProduct();
                    break;
                case 4:
                    addToCart();
                    break;
                case 5:
                    new CartView().displayMenuCart();
                    break;
                case 6:
                    return;
                case 7:
                    new UserViews().logout();
                default:
                    break;
            }

        } while (choice != 5);
    }




    private void addToCart() {
        List<Product> products = productService.findAll();
        if (products.isEmpty()) {
            printlnError("Chưa có sản phẩm");

        }

        // Hiển thị danh sách sản phẩm
        for (Product product : products) {
                System.out.println("ID: " + product.getId() + ", Name: " + product.getProductName());

        }

        System.out.println("Nhập vào ID sản phẩm để thêm vào giỏ hàng");
        int productId;
        while (true) {
            productId = getInteger();
            Product product = productService.findById(productId);

                break;

        }


        // Tạo đối tượng Cart để lưu thông tin sản phẩm
        Cart cart = new Cart();
        cart.setProduct(productService.findById(productId));
        cart.setCartId(cartService.autoInc());

        while (true) {
            System.out.println("Nhập vào số lượng muốn thêm vào giỏ hàng: ");
            int count = getInteger();

            if (count > productService.findById(productId).getStock()) {
                printlnError("Số lượng này lớn hơn hàng chúng tôi có sẵn. Vui lòng giảm số lượng xuống.");
            } else {
                cart.setQuantity(count);
                break;
            }
        }

        // Lưu đối tượng Cart vào giỏ hàng
        cartService.save(cart);

        printlnSuccess("Thêm vào giỏ hàng thành công🎈🎈!!");
    }


    private void searchProduct() {
        List<Product> products = productService.getSerchProduct();
        if (products.isEmpty()) {
            System.out.println("Danh sách sản phẩm trống!!");

        } else {
            System.out.println("Danh sách sản phẩm");
            for (Product product : products
            ) {
                    product.display();
                }

        }
    }

    private void displayProductList() {
        List<Product> productList = productService.getProductList();
        if (productList.size() == 0) {
            System.out.println("Danh sách sản phẩm trống!!!");
        } else
            System.out.println("Danh sách sản phẩm!!!");
        for (Product product : productList) {
                product.display();
        }
//
    }

    private void SortProduct() {
        List<Product> sortProduct = productService.getSortPriceproducts();
        if (sortProduct.isEmpty()) {
            System.out.println("Danh sách rỗng !!!");
        } else {
            System.out.println("Danh sách đã được sắp xếp theo giá:");
            for (Product product : sortProduct) {
                    product.display();


            }
        }
    }


    public void displayMenuAdminMenuProduct() {
        int choice;
        do {

            print(BLUE);
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          😍🧡ADMIN-PRODUCT😍😍      ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │    Thêm mới sản phẩm        ║");
            System.out.println("║   2    │    Hiển thị ds sản phẩm     ║");
            System.out.println("║   3    │    Sửa sản phẩm             ║");
            System.out.println("║   4    │    Tìm kiếm sản phẩm        ║");
            System.out.println("║   5    │    Quay lại menu trước      ║");
            System.out.println("║   6    │    Đăng xuất                ║");
            System.out.println("╚════════╧═════════════════════════════╝");
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
                    searchProduct();
                    break;
                case 5:
                    return;
                case 6:
                    new UserViews().logout();
                    break;
                default:
                    break;
            }

        } while (choice != 5);
    }

    private void displayProducts() {
        List<Product> productList = productService.getProductList();
        if (productList.size() == 0) {
            System.out.println("Danh sách sản phẩm trống!!!");
        } else
            System.out.println("Danh sách sản phẩm!!!");
        for (Product product : productList) {

            product.display();

        }
//
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
            for (Category category : categories) {
                category.displayCategory();
            }

            System.out.println("Nhập ID danh mục mới (Enter để bỏ qua):");
            while (!isExit) {
                int newCategoryId = getInteger();
                if (newCategoryId == 0) {
                    break; // Người dùng bỏ qua việc nhập danh mục mới
                } else {
                    Category newCategory = categoryService.findById(newCategoryId);
                    if (newCategory != null) {
                        productToEdit.setCategory(newCategory);
                        isExit = true; // Thoát khỏi vòng lặp sau khi nhập thành công ID danh mục
                    } else {
                        System.err.println("Danh mục không tồn tại. Mời nhập lại.");
                    }
                }
            }

            productService.save(productToEdit); // Cập nhật thông tin sản phẩm
            System.out.println("Sửa sản phẩm thành công");
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

            System.out.println("Chọn danh mục cho sản phẩm:");
            for (Category category : categories) {
                category.displayCategory();
            }

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


}

