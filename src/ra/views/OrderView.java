package ra.views;


import ra.config.InputMethods;
import ra.config.Validate;
import ra.model.Product;
import ra.model.User;
import ra.service.*;
import ra.model.Cart;
import ra.model.Order;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.*;
import static ra.config.Until.formatCurrency;
import static ra.config.Until.formattedPhoneNumber;
import static ra.contant.OrderStatus.*;


public class OrderView {
    private OrderService orderService;
    private CartView cartView;
    private ProductService productService;

    private CategoryService categoryService;
    private UserService userService;
    private UserViews userViews;

    public OrderView() {
        this.orderService = new OrderService();
        this.cartView = new CartView();
        this.productService = new ProductService();

        this.categoryService = new CategoryService();
        this.userService = new UserService();
        this.userViews = new UserViews();
    }

    public void OrderMenuHistory() {

        while (true) {

            print(BLUE);
            System.out.println(".--------------------------------------------------------.");
            System.out.println("|                       GIỎ HÀNG                         |");
            System.out.println("|--------------------------------------------------------|");
            System.out.println("|                 1. HIỂN THỊ TẤT CẢ ĐƠN HÀNG            |");
            System.out.println("|                 2. ĐƠN HÀNG ĐANG CHỜ XÁC NHẬN          |");
            System.out.println("|                 3. ĐƠN HÀNG ĐÃ XÁC NHẬN                |");
            System.out.println("|                 4. ĐƠN HÀNG ĐANG GIAO                  |");
            System.out.println("|                 5. ĐƠN HÀNG ĐÃ HOÀN THÀNH              |");
            System.out.println("|                 6. HỦY ĐƠN HÀNG                        |");
            System.out.println("|                 7. CHI TIẾT ĐƠN HÀNG                   |");
            System.out.println("|                 8. QUAY LẠI MENU TRƯỚC                 |");
            System.out.println("|                 0. ĐĂNG XUẤT                           |");
            System.out.println("'--------------------------------------------------------'\n");
            System.out.println("Mời nhập lựa chọn của bạn");
            printFinish();
            int choice = getInteger();
            switch (choice) {
                case 1:
                    showOrder();
                    break;
                case 2:
                    showOrderByStatus(WAITING);
                    break;
                case 3:
                    showOrderByStatus(ACCEPT);
                    break;
                case 4:
                    showOrderByStatus(DELIVERY);
                    break;
                case 5:
                    showOrderByStatus(SUCCESS);
                    break;
                case 6:
                    cancelOrder();
                    break;
                case 7:
                    showOrderDetail();
                    break;
                case 8:
                    return;
                case 0:
                    new UserViews().logout();
                    break;
                default:
                    printlnError("--->> Lua chon khong phu hop. Vui long chon lai ❤ ");
            }
        }

    }

    private void cancelOrder() {
        List<Order> orders = orderService.findAll();
        System.out.println("Nhập vào id đơn hàng bạn muốn hủy.");
        int id = getInteger();
        int index = orderService.findIndex(id);

        if (index != -1) {
            Order order = orders.get(index);
            if (order.getStatus() == WAITING) {
                order.setStatus(CANCEL);
                orderService.save(order);
                Order order1 = orderService.findById(id);
                for (Cart ca : order1.getOrderDetail()) {
                    Product product = productService.findById((int) ca.getProduct().getId());
                    product.setStock(product.getStock() + ca.getQuantity());
                    productService.save(product);
                }

                printlnMess("Đã hủy đơn hàng.");
            } else {
                printlnMess("Đơn hàng của bạn đã được chấp nhận nên không thể hủy.");
            }
        } else {
            printlnError("Không tìm thấy id đơn hàng bạn muốn hủy.");
        }
    }

    private void showOrder() {

        List<Order> orders = new ArrayList<>();
        for (Order order : orderService.findAll()) {
            if (Objects.equals(order.getIdUser(), userService.userActive().getId())) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            printlnError("KHÔNG CÓ LỊCH SỬ ĐẶT HÀNG");
        } else {
            print(GREEN);
            printlnMess("________________________ LỊCH SỬ ĐẶT HÀNG________________________");
            System.out.println("|-------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |    NAME   |      PHONE    |    ĐỊA CHỈ   |     STATUS      " + "|");
            System.out.println("|-------------------------------------------------------------------|");
            for (Order order : orders) {
                System.out.printf("|%-5d | %-9s |  %-12s | %-12s | %-15s |%n",
                        order.getId(), order.getReceiver(), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ HOÀN THÀNH"))));

            }
            System.out.println("|-------------------------------------------------------------------|");
            printFinish();
        }

    }

    private int selectOrderId(List<Order> orders) {
        while (true) {
            System.out.println("Nhập ID order cần thay đổi thông tin:");
            int id = getInteger();

            for (Order order : orders) {
                if ((int) order.getId() == id) {
                    return id;
                }
            }

            System.out.println("Id bạn nhập không tồn tại, mời nhập lại.");
        }
    }

    private void showOrderByStatus(byte statusCode) {
        List<Order> orders = new ArrayList<>();
        for (Order order : orderService.getOrdersByStatus(statusCode)) {
            if (Objects.equals(order.getIdUser(), userService.userActive().getId())) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            System.err.println("DANH SÁCH TRỐNG");
        } else {
            print(GREEN);
            System.out.println("\n                        ĐƠN HÀNG ");
            System.out.println("|-------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |    NAME   |      PHONE    |    ĐỊA CHỈ   |     STATUS      " + "|");
            System.out.println("|-------------------------------------------------------------------|");

            for (Order order : orders) {
                System.out.printf("|%-5d | %-9s |  %-12s | %-12s | %-15s |%n",
                        order.getId(), order.getReceiver(), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ HOÀN THÀNH"))));

            }
            System.out.println("|-------------------------------------------------------------------|\n");
            printFinish();
            if(statusCode == WAITING) {
                System.out.println("Bạn có muốn thay đổi thông tin đơn hàng không ??");
                System.err.println("1. Có                2.Không");
                int choice = InputMethods.getInteger();
                if (choice == 2) {
                    OrderMenuHistory();
                } else {
                    int selectedOrderId = selectOrderId(orders);
                    while (true) {
                        print(BLUE);
                        System.out.println(".--------------------------------------------------------.");
                        System.out.println("|                 1. CHỈNH SỬA THÔNG TIN NGƯỜI NHẬN      |");
                        System.out.println("|                 2. QUAY LẠI MENU TRƯỚC                 |");
                        System.out.println("|                 0. ĐĂNG XUẤT                           |");
                        System.out.println("'--------------------------------------------------------'\n");
                        printFinish();

                        System.out.println("Mời nhập lựa chọn của bạn");
                        int choice1 = getInteger();

                        switch (choice1) {
                            case 1:
                                changeOrderReceiver(selectedOrderId);
                                break;

                            case 2:
                                OrderMenuHistory();
                                return;
                            case 0:
                                new UserViews().logout();
                                return;
                            default:
                                printlnError("--->> Lựa chọn không phù hợp. Vui lòng chọn lại ❤ ");
                        }
                    }
                }
            }



        }
    }

    private void changeOrderReceiver(int id) {
        Order newOrder = orderService.findById(id);

        while (true) {
            System.out.println("Hãy nhập vào tên người nhận: (Enter để bỏ qua) ");
            String name = scanner().nextLine();
            if (name.isEmpty()) {
                break;
            } else if (Validate.isValidAddress(name)) {
                newOrder.setReceiver(name);
                break;
            }

        }
        System.out.println("Nhập số điện thoại người nhận: (Enter để bỏ qua)");
        while (true) {
            String phone = scanner().nextLine();
            if (phone.isEmpty()) {
                break;
            } else {
                Validate.isValidPhone(phone);
                newOrder.setNumberPhone(phone);
                break;
            }
        }

        while (true) {
            System.out.println("Hãy nhập vào địa chỉ: (Enter để bỏ qua) ");
            String address = scanner().nextLine();
            if (address.isEmpty()) {
                break;
            } else if (Validate.isValidAddress(address)) {
                newOrder.setAddress(address);
                break;
            }

        }
        newOrder.setBuyDate(LocalDate.now());
        orderService.save(newOrder);
    }


    private void showOrderDetail() {
        System.out.print("Nhập ID đơn hàng: ");
        int orderId = getInteger();
        Order order = orderService.findById(orderId);
        if (order != null && Objects.equals(order.getIdUser(), userService.userActive().getId())) {
            System.out.println("______________________ CHI TIẾT ĐƠN HÀNG ______________________");
            System.out.println("Người nhận       : " + order.getReceiver());
            System.out.println("Số điện thoại    : " + formattedPhoneNumber(order.getNumberPhone()));
            System.out.println("Địa chỉ          : " + order.getAddress());
            System.out.println("Thời gian        : " + order.getBuyDate());

            print(GREEN);
            System.out.println("_________________---_____ THÔNG TIN ĐƠN HÀNG __________---_____________");
            System.out.println("|-------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |        PRODUCT    |    QUANTITY  |       PRIME    |      TOTAL    |     STATUS    " + " |");
            System.out.println("|-------------------------------------------------------------------------------------------|");

            for (Cart ca : order.getOrderDetail()) {
                System.out.printf("|%-5d | %-17s | %-12d | %-15s|%-15s| %-15s | %n",
                        ca.getCartId(), ca.getProduct().getProductName(), ca.getQuantity(), formatCurrency(ca.getProduct().getPrice()), formatCurrency(ca.getQuantity() * ca.getProduct().getPrice()), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|--------------------------------------------------------------------------------------------|");
            printFinish();


        } else {
            System.err.println("Không tìm thấy đơn hàng.");
        }

    }

    public void menuAdminOrder() {

        while (true) {

            print(BLUE);
            System.out.println(".---------------------------------------------------------------------.");
            System.out.println("|                      ADMIN-QUẢN LÝ ĐƠN HÀNG                         |");
            System.out.println("|---------------------------------------------------------------------|");
            System.out.println("|                     1. HIỂN THỊ TẤT CẢ ĐƠN HÀNG                     |");
            System.out.println("|                     2. TÌM KIẾM ĐƠN HÀNG THEO USER                  |");
            System.out.println("|                     3. DANH SÁCH ĐƠN HÀNG ĐANG CHỜ XÁC NHẬN         |");
            System.out.println("|                     4. DANH SÁCH ĐƠN HÀNG ĐÃ XÁC NHẬN               |");
            System.out.println("|                     5. DANH SÁCH ĐƠN HÀNG ĐÃ BỊ HỦY                 |");
            System.out.println("|                     6. DANH SÁCH ĐƠN HÀNG ĐANG GIAO                 |");
            System.out.println("|                     7. DANH SÁCH ĐƠN HÀNG ĐÃ GIAO                   |");
            System.out.println("|                     8. HIỂN THỊ CHI TIẾT ĐƠN HÀNG                   |");
            System.out.println("|                     9. QUAY LẠI MENU TRƯỚC                          |");
            System.out.println("|                     0. ĐĂNG XUẤT                                    |");
            System.out.println("'---------------------------------------------------------------------'");
            System.out.println("Nhập lựa chọn của bạn :");
            printFinish();

            int choice = getInteger();
            switch (choice) {
                case 1:
                    showAllOrder();
                    break;
                case 2:
                    sortOrderByUserName();
                    break;
                case 3:
                    orderConfirmation();
                    break;
                case 4:
                    orderStatus(ACCEPT);

                    break;
                case 5:
                    orderStatus(CANCEL);
                    break;
                case 6:
                    orderStatus(DELIVERY);
                    break;
                case 7:
                    orderStatus(SUCCESS);
                    break;
                case 8:
                    orderDetailAdmin();
                    break;
                case 9:
                    return;
                case 0:
                    new UserViews().logout();
                    break;
                default:
                    printlnError("--->> Lua chon khong phu hop. Vui long chon lai ❤ ");
            }

        }
    }

    private void sortOrderByUserName() {
        List<User> sortUsers = userService.findAll();

        if (sortUsers.size() != 0) {

            print(GREEN);
            System.out.println("\n                                                   DANH SÁCH USER                   ");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   NAME  |       EMAIL      |     PHONE    |    ĐỊA CHỈ   |   STATUS  |  ROLE | IMPORTANCE |  CREATE AT  |    UPDATE AT  " + "|");
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");

            for (User user : sortUsers) {
                System.out.printf("|%-5d | %-7s | %-16s | %-12s | %-12s | %-9s | %-5s | %-10s | %-11s | %-13s |%n",
                        user.getId(), user.getUsername(), user.getEmail(), formattedPhoneNumber(user.getPhone()), user.getAddress(), (user.isStatus() ? "ONLINE" : "OFFLINE"), (user.getRole() == 1 ? "ADMIN" : "USER"), user.isImportance() ? "OPEN" : "BLOCK", user.getCreateAt(), (user.getUpdateAt()) == null ? "Chưa cập nhật" : user.getUpdateAt());
            }
            System.out.println("|--------------------------------------------------------------------------------------------------------------------------------|");
            printFinish();

        }
        else {
            System.err.println("Chưa có User");
        }

        System.out.println("Mời ban nhập ID của User");
        int id = getInteger();
        boolean flag =true;
        while (true) {
            for (User user: sortUsers
                 ) {
                if((int)user.getId() == id) {
                    flag = flag;
                }
            }
            if(flag) {
                printlnError("ID user không tồn tại, mời nhập lại");
            } else {
                break;
            }
        }
        List<Order> orders = new ArrayList<>();
        for (Order order : orderService.findAll()) {
            if (Objects.equals(order.getIdUser(), userService.findById(id))) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            printlnError("KHÔNG CÓ LỊCH SỬ ĐẶT HÀNG");
        } else {
            print(GREEN);
            printlnMess("________________________ LỊCH SỬ ĐẶT HÀNG________________________");
            System.out.println("|-------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |    NAME   |      PHONE    |    ĐỊA CHỈ   |     STATUS      " + "|");
            System.out.println("|-------------------------------------------------------------------|");
            for (Order order : orders) {
                System.out.printf("|%-5d | %-9s |  %-12s | %-12s | %-15s |%n",
                        order.getId(), order.getReceiver(), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ HOÀN THÀNH"))));

            }
            System.out.println("|-------------------------------------------------------------------|");
            printFinish();
        }
    }

    private void orderDetailAdmin() {
        System.out.print("Nhập ID đơn hàng: ");
        int orderId = getInteger();
        Order order = orderService.findById(orderId);
        if (order != null) {
            System.out.println("______________________ CHI TIẾT ĐƠN HÀNG ______________________");
            System.out.println("Người nhận       : " + order.getReceiver());
            System.out.println("Số điện thoại    : " + formattedPhoneNumber(order.getNumberPhone()));
            System.out.println("Địa chỉ          : " + order.getAddress());
            System.out.println("Thời gian        : " + order.getBuyDate());

            print(GREEN);
            System.out.println("______________________ THÔNG TIN ĐƠN HÀNG _______________________");
            System.out.println("|-------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |        PRODUCT    |    QUANTITY  |       PRIME    |      TOTAL    |     STATUS    " + " |");
            System.out.println("|-------------------------------------------------------------------------------------------|");

            for (Cart ca : order.getOrderDetail()) {
                System.out.printf("|%-5d | %-17s | %-12d | %-15s|%-15s| %-15s | %n",
                        ca.getCartId(), ca.getProduct().getProductName(), ca.getQuantity(), formatCurrency(ca.getProduct().getPrice()), formatCurrency(ca.getQuantity() * ca.getProduct().getPrice()), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|--------------------------------------------------------------------------------------------|");
            printFinish();
        } else {
            System.err.println("Không tìm thấy đơn hàng.");
        }

    }

    private void orderStatus(byte orderStatus) {
        List<Order> oderList = orderService.findAll();
        List<Order> orders = new ArrayList<>();
        for (Order order : oderList) {
            if (order.getStatus() == orderStatus) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            printlnError("Danh sách đơn hàng đã xác nhận rỗng");
        } else {
            print(GREEN);
            System.out.println("\n                                                 ĐƠN HÀNG ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");

            for (Order order : orders
            ) {
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userService.findById(order.getIdUser()).getUsername(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();
        }


    }


    private void orderConfirmation() {
        List<Order> oderList = orderService.findAll();
        List<Order> orders = new ArrayList<>();
        for (Order order : oderList) {
            if (order.getStatus() == WAITING) {
                orders.add(order);
            }
        }
        if (orders.isEmpty()) {
            System.err.println("Đơn hàng chờ xác nhận rỗng");
        } else {
            print(GREEN);
            System.out.println("\n                                                 ĐƠN HÀNG                ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");

            for (Order order : orders
            ) {
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userService.findById(order.getIdUser()).getUsername(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));
            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();

            System.out.println("Nhập vào id order cần xác nhận: ");
            int id = getInteger();
            Order order = orderService.findById(id);

            if (order != null) {
                if (order.getStatus() == WAITING) {
                    System.out.println("Xác nhận:     1.Chấp nhận      . 2.Hủy :");
                    int x = getInteger();

                    order.setStatus((byte) x);
//
                    if ((byte) x == 2) {
                        Order order1 = orderService.findById(id);
                        for (Cart ca : order1.getOrderDetail()) {
                            Product product = productService.findById((int) ca.getProduct().getId());
                            product.setStock(product.getStock() + ca.getQuantity());
                            productService.save(product);
                        }
                    }
//
                    int index = orderService.findIndex(id);
                    // Đặt thời gian xác nhận đơn hàng
                    order.setConfirmTime(LocalDateTime.now());
                    orderService.save(order);
                    printlnMess("Xác thực đơn hàng thành công");

                } else if (order.getStatus() == CANCEL) {
                    printlnError("Đơn hàng đã bị hủy không thể xác thực !!.");
                } else {
                    printlnError("Đơn hàng đã được xác nhận, không thể thay đổi trạng thái.");
                }
            } else {
                printlnError("Không tìm thấy đơn hàng.");
            }
        }
    }


    private void showAllOrder() {
        List<Order> orders = orderService.findAll();
        if (!orders.isEmpty()) {
            print(GREEN);
            System.out.println("\n                                                ĐƠN HÀNG                ");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            System.out.println("|" + "  ID  |   USERNAME   |   RECEIVER    |      TOTAL      |     PHONE    |    ĐỊA CHỈ   |    STATUS       " + "|");
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            for (Order order : orderService.findAll()) {
                // Kiểm tra và chuyển trạng thái đơn hàng
                if (order.getConfirmTime() != null) {
                    if (ChronoUnit.MINUTES.between(order.getConfirmTime(), LocalDateTime.now()) >= 5) {
                        order.setStatus((byte) 3);
                        orderService.save(order);
                    }
                }

                if (order.getConfirmTime() != null) {
                    if (ChronoUnit.MINUTES.between(order.getConfirmTime(), LocalDateTime.now()) >= 10) {
                        order.setStatus((byte) 4);
                        orderService.save(order);
                    }
                }
                System.out.printf("|%-5d | %-12s |  %-12s | %-15s | %-12s | %-12s | %-15s |%n",
                        order.getId(), userService.findById(order.getIdUser()).getUsername(), order.getReceiver(), formatCurrency(order.getTotal()), formattedPhoneNumber(order.getNumberPhone()), order.getAddress(), (order.getStatus() == 0) ? "ĐANG XÁC NHẬN" : ((order.getStatus() == 1) ? "ĐÃ XÁC NHẬN" : ((order.getStatus() == 2) ? "ĐÃ BỊ HỦY" : ((order.getStatus() == 3) ? "ĐANG GIAO" : "ĐÃ GIAO"))));

            }
            System.out.println("|-------------------------------------------------------------------------------------------------------|");
            printFinish();

        } else {
            System.err.println("Đơn hàng rỗng");
        }
    }
}
