package ra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ra.contant.OrderStatus.getStatusByCode;

public class Order extends Entity {
//    private int id;
    private int idUser;
    private double total;
    private LocalDate buyDate;
    private LocalDateTime confirmTime;
    private String receiver;
    private String numberPhone;
    private String address;
    private byte status = 0;
    public static List<Cart> orderDetail =new ArrayList<>();

    public Order() {}

    public Order(int idUser, double total, LocalDate buyDate, LocalDateTime confirmTime, String receiver, String numberPhone, String address, byte status, List<Cart> orderDetail) {
        this.idUser = idUser;
        this.total = total;
        this.buyDate = buyDate;
        this.confirmTime = confirmTime;
        this.receiver = receiver;
        this.numberPhone = numberPhone;
        this.address = address;
        this.status = status;
        this.orderDetail = orderDetail;
    }

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

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
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

    public  List<Cart> getOrderDetail() {
        return orderDetail;
    }

    public  void setOrderDetail(List<Cart> orderDetail) {
       this.orderDetail = orderDetail;
    }



}
