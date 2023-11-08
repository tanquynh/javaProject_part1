package ra.model;

import java.io.Serializable;

public class Product extends Entity {

//    private int id;
    private String productName;
    private double price;
    private int stock;
    private String productDes;
    private Category category;
    boolean productStatus;
    public Product() {}

    public Product(Integer id, String productName, double price, int stock, String productDes, Category category, boolean productStatus) {
        this.id = id;
        this.productName = productName;
        this.price = price;
        this.stock = stock;
        this.productDes = productDes;
        this.category = category;
        this.productStatus = productStatus;
    }


    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getProductDes() {
        return productDes;
    }

    public void setProductDes(String productDes) {
        this.productDes = productDes;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }
    public void setQuantity(int stock) {
        this.stock = stock;
    }

}
