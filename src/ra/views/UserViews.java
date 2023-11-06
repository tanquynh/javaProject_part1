package ra.views;

import ra.config.InputMethods;
import ra.config.Validate;
import ra.model.User;
import ra.service.*;

import java.time.LocalDate;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.config.Validate.isValidFullName;
import static ra.config.Validate.isValidPassword;
import static ra.constant.Contant.ADMIN_CODE;
import static ra.constant.Contant.Importance.BLOCK;
import static ra.constant.Contant.Importance.OPEN;
import static ra.constant.Contant.Role.ADMIN;
import static ra.constant.Contant.Status.ACTIVE;


public class UserViews {

    private UserService userService;

    private CartService cartService;
    private ProductService productService;
    private OrderService orderService;
    private CategoryService categoryService;

    public UserViews() {

        this.userService = new UserService();

        this.productService = new ProductService();
        this.orderService = new OrderService();
        this.categoryService = new CategoryService();
    }
    static String userName;



    public UserService getUserService() {
        return userService;
    }

    public CartService getCartService() {
        return cartService;
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
    public static void main(String[] args) {
        UserViews userViews = new UserViews();
        userViews.loginOrRegister();
    }


    public  void loginOrRegister() {
        print(BLUE);
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   😍🧡  QUẢN LÝ CỬA HÀNG KARA 😍😍  ║");
        System.out.println("╟────────┬─────────────────────────────╢");
        System.out.println("║   1    │       Đăng nhập             ║");
        System.out.println("║   2    │       Đăng ký               ║");
        System.out.println("║   0    │       Thoát                 ║");
        System.out.println("╚════════╧═════════════════════════════╝");
        System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");

        int choice = InputMethods.getInteger();
        switch (choice) {
            case 1:
               User user = login();
                break;
            case 2:
                User user1 = registerUser();
                userService.save(user1);
                printlnSuccess("Đăng ký thành công !");
                loginOrRegister();
                break;
            case 0:
                break;
    }
    }
    private User registerUser() {
        List<User> users = userService.findAll();
        User user = new User();
        user.setId(userService.autoInc());
        printlnMess("Vui lòng đăng ký tài khoản !!");
        // Chọn role của người dùng
        System.out.println("Hãy chọn role của bạn: ");
        System.out.println("1: ADMIN");
        System.out.println("2: USER");
        int role = getInteger();
        if (role == ADMIN) {
            // Nếu là ADMIN, yêu cầu nhập mã xác nhận ADMIN
            printlnMess("Nhập vào mã xác nhận ADMIN: ");
            String adminCode = getString();

            if (!adminCode.equals(ADMIN_CODE)) {
                printlnError("Mã xác thực không đúng, vui lòng nhập lại.");
                return registerUser(); // Gọi lại phương thức để người dùng nhập lại
            }
        }

        user.setRole(role);

        // Nhập họ và tên đầy đủ
        while (true) {
            System.out.println("Hãy nhâp vào họ và tên đầy đủ: ");
            String fullName = InputMethods.scanner().nextLine();

            if (Validate.isValidFullName(fullName)) {
                user.setFullName(fullName);
                break;
            }
        }

        // Nhập tên đăng nhập
        while (true) {
            System.out.println("Hãy nhập tên đăng nhập: ");
            String username = InputMethods.scanner().nextLine();

            if (Validate.isValidFullName(username)) {
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

        // Nhập mật khẩu
        while (true) {
            System.out.println("Hãy nhập vào mật khẩu: ");
            String password = InputMethods.scanner().nextLine();

            if (Validate.isValidPassword(password)) {
                user.setPassword(password);
                break;
            }
        }

        // Nhập email
        while (true) {
            System.out.println("Hãy nhập vào email đăng ký: ");
            String email = InputMethods.scanner().nextLine();

            if (Validate.isValidEmail(email)) {
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

        // Nhập số điện thoại
        while (true) {
            System.out.println("Hãy nhập vào số điện thoại: ");
            String phone = InputMethods.scanner().nextLine();

            if (Validate.isValidPhone(phone)) {
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
            System.out.println("Hãy nhập vào địa chỉ: ");
            String address = InputMethods.scanner().nextLine();

            if (Validate.isValidAddress(address)) {
                user.setAddress(address);
                break;
            }
        }
        user.setCreateAt(LocalDate.now());

        // Đăng ký hoàn thành, trả về đối tượng User đã tạo
        return user;
    }

    private User login() {
        String pass;
        String userName;
        printlnMess("Thực hiện đăng nhập 🧡😍:");
        while (true) {
            System.out.println("UserName: ");
            userName = getString();
            if (isValidFullName(userName)) {
                break;
            }
        }
        while (true) {
            System.out.println("Password: ");
            pass = scanner().nextLine();
            if (isValidPassword(pass)) {
                break;
            }
        }
        User user;
        user = userService.login(userName, pass);

        if (user != null) {
            if (user.isImportance()) {
                userService.setStatusLogin(userName, ACTIVE);
                if (user.getRole() == ADMIN) {

                    displayAdminMenu();

                } else {
                    displayUserMenu();
                }

            } else {
                printlnError("Tài khoản của bạn đã bị khóa😂😂 !!");
                loginOrRegister();
            }


        } else {
            printlnError("Đăng nhập thấy bại,Mật khẩu hoặc UserName ko trùng hợp!!! ");
            loginOrRegister();

        }
        return user;
    }
    private void displayUserMenu() {
        int choice;
        do {
            print(PURPLE);
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║       😍🧡  QUẢN LÝ USER 😍😍       ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │    Trang chủ                ║");
            System.out.println("║   2    │    Giỏi hàng                ║");
            System.out.println("║   3    │    Đăng xuất                ║");
            System.out.println("╚════════╧═════════════════════════════╝");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                   new ProductViews().displayUserMenuProduct();
                    break;

                case 2:
                    new CartView().displayMenuCart();
                    break;
                case 3:
                    logout();
                    break;
            }
        } while (choice != 5);
    }

    public void logout() {
        System.out.println("Bạn chắc chắn muốn thoát chứ ??");
        System.out.println("1. Có                2.Không");
        int choice = InputMethods.getInteger();
        if(choice == 1) {
            loginOrRegister();
        }
    }

    public void displayAdminMenu() {
        int choice;
        do {
            print(PURPLE);
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          😍🧡  ADMIN 😍😍           ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │    Quản lý người dùng       ║");
            System.out.println("║   2    │    Quản lý danh mục         ║");
            System.out.println("║   3    │    Quản lý sản phẩm         ║");
            System.out.println("║   4    │    Quản lý Đơn hàng         ║");
            System.out.println("║   5    │    Quay lại menu trước      ║");
            System.out.println("║   6    │    Đăng xuất                ║");
            System.out.println("╚════════╧═════════════════════════════╝");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            printFinish();

            choice = getInteger();
            switch (choice) {
                case 1:
                    userManagement();
                    break;

                case 2:
                   new CategoryView().displayAdminCategory();
                    break;
                case 3:
                   new ProductViews().displayMenuAdminMenuProduct();
                    break;
                case 4:
//                   new ProductViews().menuAdminOrder();
                    break;
                case 5:
                    return;
                case 6:
                    logout();
                    break;
                default:
                    break;
            }
        } while (choice != 7);
    }
    private void userManagement() {
        int choice;

        do {
            print(CYAN);
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║       😍🧡  QUẢN LÝ USER 😍😍       ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │    Danh sách user           ║");
            System.out.println("║   2    │    Tìm kiếm user theo tên   ║");
            System.out.println("║   3    │    Khóa/ mở user            ║");
            System.out.println("║   4    │    Quay lại menu trước      ║");
            System.out.println("║   5    │    Đăng xuất                ║");
            System.out.println("╚════════╧═════════════════════════════╝");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 : ");
            choice = getInteger();
            printFinish();

            switch (choice) {
                case 1:
                    displayUserList();
                    break;
                case 2:
                    displayUserByUserName();

                    break;
                case 3:
                    changeUserImportance();
                    break;
                case 4:
                    return;
                case 5:
                    logout();
                    break;
                default:
                    break;
            }
        } while (true);
    }

    private void changeUserImportance() {
        System.out.println("Hãy nhập username bạn muốn thay đổi trạng thái:");
        String username = getString();
        User user = userService.getUserByUsename(username);
        if(user == null) {
            System.err.println("Không tìm thấy username bạn muốn đổi trạng thái !!\"");

        } else  {
            if(user.getRole() == ADMIN) {
                printlnError("Không thể khóa user ADMIN !!");
            } else {
                userService.updateImportance((user.isImportance() == OPEN ? BLOCK : OPEN), username);
                printlnSuccess("Thay đổi trạng thái thành công!");
            }
        }
    }

    private void displayUserByUserName() {
        System.out.println("Nhập vào tên User cần tìm kiếm");
        String username = getString();
        List<User> fitterUsers = userService.getFitterUsers(username);
        if(fitterUsers.size() != 0) {
            System.out.println("Danh sách User:");
            for (User user: fitterUsers) {
                user.display();
            }
        } else  {
            printlnError("Không thể khóa user ADMIN !!");

        }
    }

    private void displayUserList() {
        List<User> sortUsers = userService.getSortUsersList();
       if(sortUsers.size() != 0) {
           System.out.println("Danh sách User sắp xếp theo tên");
           for (User user: sortUsers) {
               user.display();
           }
       } else  {
           printlnError("Không thể khóa user ADMIN !!");

       }

    }
}


