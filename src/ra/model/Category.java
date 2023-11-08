package ra.model;

import java.io.Serializable;

public class Category extends Entity {

//    private int id;
    private String categoryName;
    private String categoryDes;
    private boolean categoryStatus;

    public Category() {

    }

    public Category(Integer id, String categoryName, String categoryDes, boolean categoryStatus) {
        this.id = id;
        this.categoryName = categoryName;
        this.categoryDes = categoryDes;
        this.categoryStatus = categoryStatus;
    }

//    public int getId() {
//        return id;
//    }

//    public void setId(int categoryId) {
//        this.id = id;
//    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryDes() {
        return categoryDes;
    }

    public void setCategoryDes(String categoryDes) {
        this.categoryDes = categoryDes;
    }

    public boolean isCategoryStatus() {
        return categoryStatus;
    }

    public void setCategoryStatus(boolean categoryStatus) {
        this.categoryStatus = categoryStatus;
    }

}
