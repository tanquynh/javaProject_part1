package ra.views;

import ra.config.InputMethods;
import ra.config.Validate;
import ra.model.User;
import ra.service.*;

import java.time.LocalDate;
import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.config.Until.formattedPhoneNumber;
import static ra.config.Validate.isValidFullName;
import static ra.config.Validate.isValidPassword;
import static ra.contant.Contant.ADMIN_CODE;
import static ra.contant.Contant.Importance.BLOCK;
import static ra.contant.Contant.Importance.OPEN;
import static ra.contant.Contant.Role.ADMIN;
import static ra.contant.Contant.Status.ONLINE;


public class UserViews {

    private UserService userService;
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


    public void loginOrRegister() {
        print(BLUE);


        System.out.println("                                         .-------------------------------------------------------------------------------.");
        System.out.println("                                         |        ღ ღ (¯`◕‿◕´¯) ღ ღ       CỬA HÀNG KARA       ღ ღ (¯`◕‿◕´¯) ღ ღ         |");
        System.out.println("                                         |-------------------------------------------------------------------------------|");
        System.out.println("                                         |                                                                               |");
        System.out.println("                                         |                                 1. ĐĂNG NHẬP                                  |");
        System.out.println("                                         |                                 2. ĐĂNG KÝ                                    |");
        System.out.println("                                         |                                 0. THOÁT                                      |");
        System.out.println("                                         |                                                                               |");
        System.out.println("                                         '-------------------------------------------------------------------------------'\n");


        System.out.print("Nhập vào lựa chọn của bạn 🧡🧡 :   ");
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
            System.out.print("UserName: ");
            userName = getString();
            if (isValidFullName(userName)) {
                break;
            }
        }
        while (true) {
            System.out.print("Password: ");
            pass = scanner().nextLine();
            if (isValidPassword(pass)) {
                break;
            }
        }
        User user;
        user = userService.login(userName, pass);

        if (user != null) {
            if (user.isImportance()) {
                userService.setStatusLogin(userName, ONLINE);
                if (user.getRole() == ADMIN) {

                    displayAdminMenu();

                } else {
                   new MenuViews().displayUserMenuProduct();
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


    public void logout() {
        System.err.println("Bạn chắc chắn muốn thoát chứ ??");
        System.err.println("1. Có                2.Không");
        int choice = InputMethods.getInteger();
        if (choice == 1) {
//            userService.setStatusLogin(userName, INACTIVE);
            loginOrRegister();
        }
    }

    public void displayAdminMenu() {
        int choice;
        do {
            print(PURPLE);

            System.out.println(".--------------------------------------------------------.");
            System.out.println("| WELCOME ADMIN : "+ userService.userActive().getUsername()+"                                  |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. QUẢN LÝ NGƯỜI DÙNG              |");
            System.out.println("|                     2. QUẢN LÝ DANH MỤC                |");
            System.out.println("|                     3. QUẢN LÝ SẢN PHẨM                |");
            System.out.println("|                     4. QUẢN LÝ ĐƠN HÀNG                |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");
            System.out.println("Nhập vào lựa chọn của bạn 🧡🧡 :   ");
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
                    new MenuViews().displayMenuAdminMenuProduct();
                    break;
                case 4:
                    new OrderView().menuAdminOrder();
                    break;
                case 0:
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

            System.out.println(".--------------------------------------------------------.");
            System.out.println("|           🧡🧡        QUẢN LÝ USER        🧡🧡        |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                     1. DANH SÁCH USER                  |");
            System.out.println("|                     2. TÌM KIẾM USER THEO TÊN          |");
            System.out.println("|                     3. KHÓA / MỞ TÀI KHOẢN USER        |");
            System.out.println("|                     4. QUAY LẠI MENU TRƯỚC             |");
            System.out.println("|                     0. ĐĂNG XUẤT                       |");
            System.out.println("'--------------------------------------------------------'\n");
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
                case 0:
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
        if (user == null) {
            System.err.println("Không tìm thấy username bạn muốn đổi trạng thái !!\"");

        } else {
            if (user.getRole() == ADMIN) {
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
        if (fitterUsers.size() != 0) {
            print(GREEN);
            System.out.println("\n                                                  DANH SÁCH USER THEO NAME                  ");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |    UPDATE AT  " + "|");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");

            for (User user : fitterUsers) {
                System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-13s |%n",
                        user.getId(), user.getUsername(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());
            }
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            printlnError("Không có user phù hợp!!");

        }
    }

    private void displayUserList() {
        List<User> sortUsers = userService.getSortUsersList();
        if (sortUsers.size() != 0) {

            print(GREEN);
            System.out.println("\n                                                   DANH SÁCH USER SẮP XẾP THEO NAME                  ");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |    UPDATE AT  " + "|");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");

            for (User user : sortUsers) {
                System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-13s |%n",
                        user.getId(), user.getUsername(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());
            }
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            printlnError("Không có user !!");

        }

    }
}


