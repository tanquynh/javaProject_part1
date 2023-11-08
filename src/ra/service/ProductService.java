package ra.service;
import ra.contant.Contant;
import ra.model.Product;
import ra.repository.FileRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ra.config.InputMethods.scanner;

public class ProductService implements IShop<Product> {
    FileRepo<Product, Integer> productFileRepo;
    public ProductService() {
        this.productFileRepo = new FileRepo<>(Contant.FilePath.PRODUCT_FILE);
    }

    @Override
    public void save(Product product) {
        productFileRepo.save(product);
    }

    @Override
    public List<Product> findAll() {
        return productFileRepo.findAll();
    }

    @Override
    public Product findById(int id) {
        return productFileRepo.findById(id);
    }

    @Override
    public int findIndex(int id) {
        return productFileRepo.findByIndex(id);
    }

    @Override
    public int autoInc() {
        return productFileRepo.autoInc();
    }

    public List<Product> getSerchProduct() {
        List<Product> products = findAll();
        List<Product> findProduct = new ArrayList<>();
        System.out.println("Mời nhập tên sản phẩm cần tìm kiếm!!");
        String searchName = scanner().nextLine();
        if(products.isEmpty()) {
            return new ArrayList<>();
        } else {
            for (Product product: products) {
                if(product.getProductName().toLowerCase().trim().contains(searchName.trim().toLowerCase())) {
                    findProduct.add(product);
                }
            }
            return findProduct;
        }
    }

    public List<Product> getProductList() {
        List<Product> products = findAll();
        return products;
    }

    public List<Product> getSortPriceproducts() {
        List<Product> sortProducts = findAll();
        sortProducts.sort((o1, o2) -> Double.compare(o2.getPrice(), o1.getPrice()));
        return sortProducts;
    }

    public void updateProductStatus(boolean Status , int id) {
        List<Product> productList = findAll();
        for (Product product : productList) {
            if (product.getId().equals(id) ) {
                product.setProductStatus(Status);
                save(product);
            }
        }


    }

    public void updateQuantity(Product product) {
        List<Product> allMenu = findAll();
        // Tìm sản phẩm trong danh sách
        for (Product existingProduct : allMenu) {
            if (Objects.equals(existingProduct.getId(), product.getId())) {
                // Cập nhật số lượng
                existingProduct.setStock(product.getStock());
                save(product);
                break;
            }
        }

    }
}

