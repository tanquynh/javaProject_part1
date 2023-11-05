package ra.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ra.constant.OrderStatus.getStatusByCode;

public class Order extends Entity {
//    private int id;
    private int idUser;
    private double total;
    private LocalDate buyDate;
    private String receiver;
    private String numberPhone;
    private String address;
    private byte status = 0;
    public static List<Cart> OrderDetail =new ArrayList<>();

    public Order() {}

    public Order(Integer id, int idUser, double total, LocalDate buyDate, String receiver, String numberPhone, String address, byte status) {
        this.id = id;
        this.idUser = idUser;
        this.total = total;
        this.buyDate = buyDate;
        this.receiver = receiver;
        this.numberPhone = numberPhone;
        this.address = address;
        this.status = status;
    }

//    public int getId() {
//        return id;
//    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public LocalDate getBuyDate() {
        return buyDate;
    }

    public void setBuyDate(LocalDate buyDate) {
        this.buyDate = buyDate;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getNumberPhone() {
        return numberPhone;
    }

    public void setNumberPhone(String numberPhone) {
        this.numberPhone = numberPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public static List<Cart> getOrderDetail() {
        return OrderDetail;
    }

    public static void setOrderDetail(List<Cart> orderDetail) {
        OrderDetail = orderDetail;
    }

    public void display(){
        System.out.println("Id : " +id + " | Tên người nhận : "+receiver+ " |Số điện thoại : "+ numberPhone);
        System.out.println("Địa chỉ" + this.address+"| Trạng thái :" + getStatusByCode(status));

    }
}
