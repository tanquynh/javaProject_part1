package ra.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ra.constant.Contant.Importance.OPEN;
import static ra.constant.Contant.Role.ADMIN;
import static ra.constant.Contant.Status.INACTIVE;

public class User extends Entity {
//    private int id;
    private String username;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private boolean status;
    private boolean importance;
    private LocalDate createAt;
    private LocalDate updateAt;
    private String address;
    private int role;
    private List<Cart> cart = new ArrayList<>();

    public User() {
        status = INACTIVE;
        importance = OPEN;
    }

    public User(Integer id, String username, String fullName, String password, String email, String phone, boolean status, boolean importance, LocalDate createAt, LocalDate updateAt, String address, int role , List<Cart> cart) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.status = status;
        this.importance = importance;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.address = address;
        this.role = role;
        this.cart = cart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isImportance() {
        return importance;
    }

    public void setImportance(boolean importance) {
        this.importance = importance;
    }

    public LocalDate getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDate createAt) {
        this.createAt = createAt;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public List<Cart> getCart() {
        return cart;
    }

    public void setCarts(List<Cart> cart) {
        this.cart = cart;
    }

    public void display() {
        System.out.println("ID:" + this.id + " -Username: " + this.username + " - Email: " + this.email);
        System.out.println("Status: " + (this.status ? "ACTIVE" : "INACTIVE") + " - Role: " + (this.role == ADMIN ? "ADMIN" : "USER") + " -Trạng thái khóa/mở:" + (this.importance == OPEN ? "MỞ" : "KHÓA"));
        System.out.println("Thời gian tạo: " + this.createAt + " -- Thời gian cập nhật: " +(this.updateAt == null ? "Chưa cập nhật" : this.updateAt) );
        System.out.println("|------------------------------------------------------|");
    }


}
