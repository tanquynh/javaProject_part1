package ra.views;

import ra.config.InputMethods;
import ra.model.User;
import ra.service.*;
import ra.model.Cart;
import ra.model.Order;

import java.util.List;

import static ra.config.ConsoleColor.*;
import static ra.config.InputMethods.getInteger;
import static ra.constant.OrderStatus.*;


public class OrderView {
    private OrderService orderService;
    private CartView cartView;
    private ProductService productService;
    private CartService cartService;
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

    public OrderService getOrderService() {
        return orderService;
    }

    public CartView getCartView() {
        return cartView;
    }

    public ProductService getProductService() {
        return productService;
    }

    public CartService getCartService() {
        return cartService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserViews getUserViews() {
        return userViews;
    }

    public void OrderMenuHistory() {

        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║            LỊCH SỬ ĐẶT HÀNG          ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │  Hiển thị tất cả đơn hàng   ║");
            System.out.println("║   2    │  Đơn hàng đang chờ          ║");
            System.out.println("║   3    │  Đơn hàng đã xác nhận       ║");
            System.out.println("║   4    │  Hủy đơn hàng               ║");
            System.out.println("║   5    │  Chi tiết đơn hàng          ║");
            System.out.println("║   6    │  Quay lại menu trước        ║");
            System.out.println("║   7    │  Đăng xuất                  ║");
            System.out.println("╚════════╧═════════════════════════════╝");

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
                    cancelOrder();
                    break;
                case 5:
                    System.out.print("Nhập ID đơn hàng: ");
                    int orderId = getInteger();
                    showOrderDetail(orderId);
                    break;
                case 6:
                    return;
                case 7:
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
                printlnMess("Đã hủy đơn hàng.");
            } else {
                printlnMess("Đơn hàng của bạn đã được chấp nhận nên không thể hủy.");
            }
        } else {
            printlnError("Không tìm thấy id đơn hàng bạn muốn hủy.");
        }
    }

    private void showOrder() {
        printlnMess("________________________ LỊCH SỬ ĐẶT HÀNG________________________");
        for (Order order : orderService.findAll()) {

            if (order.getIdUser() == (int)userService.userActive().getId()) {
                order.display();
                printlnMess("══════════════════════════════════════════════════════════════");
            }

        }

    }

    private void showOrderByStatus(byte statusCode) {

        int count = 0;
        for (Order order : orderService.getOrdersByStatus(statusCode)) {
            if (order.getIdUser() == (int)userService.userActive().getId()) {
                count ++;
                order.display();
            }
        }
        if(count == 0) {
            System.err.println("Danh sách trống");
        }

    }

    private void showOrderDetail(int orderId) {
        Order order = orderService.findById(orderId);
        if (order != null) {
            System.out.println("______________________ CHI TIẾT ĐƠN HÀNG ______________________");

            System.out.println("Người nhận       : " + order.getReceiver());
            System.out.println("Số điện thoại    : " + order.getNumberPhone());
            System.out.println("Địa chỉ          : " + order.getAddress());
            System.out.println("Thời gian        : " + order.getBuyDate());
            System.out.println("______________________ THÔNG TIN ĐƠN HÀNG _______________________");

            for (Cart ca : order.getOrderDetail()) {
                ca.display();
            }
            System.out.println("Tong tien : " + order.getTotal());
        } else {
            System.out.println("Không tìm thấy đơn hàng.");


        }

    }

    public void menuAdminOrder() {
        while (true) {
            System.out.println("╔══════════════════════════════════════╗");
            System.out.println("║          ADMIN-QUẢN LÝ ĐƠN HÀNG      ║");
            System.out.println("╟────────┬─────────────────────────────╢");
            System.out.println("║   1    │  Hiển thị tất cả đơn hàng   ║");
            System.out.println("║   2    │  xác nhận các đơn hàng      ║");
            System.out.println("║   3    │  Quay lại menu trước        ║");
            System.out.println("║   4    │  Đămg xuất                  ║");
            System.out.println("╚════════╧═════════════════════════════╝");

            int choice = getInteger();
            switch (choice) {
                case 1:
                    showAllOrder();
                    break;
                case 2:
                    orderConfirmation();
                    break;
                case 3:
                    return;
                case 4:
                    new UserViews().logout();
                    break;
                default:
                    printlnError("--->> Lua chon khong phu hop. Vui long chon lai ❤ ");
            }

        }
    }

    private void orderConfirmation() {
        List<Order> orders = orderService.findAll();
        showAllOrder();
        System.out.println("Nhập vào id order cần xác nhận: ");
        int id = getInteger();
        Order order = orderService.findById(id);

        if (order != null) {
            if (order.getStatus() == WAITING) {
                System.out.println("Xác nhận:     1.Chấp nhận      . 2.Hủy :");
                int x = getInteger();


                order.setStatus((byte) x);
                int index = orderService.findIndex(id);
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


    private void showAllOrder() {

        for (Order order : orderService.findAll()) {
            order.display();
        }
    }

}
