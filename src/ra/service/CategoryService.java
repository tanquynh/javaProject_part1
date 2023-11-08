package ra.service;

import ra.contant.Contant;
import ra.model.Category;
import ra.repository.FileRepo;

import java.util.List;

public class CategoryService implements IShop<Category> {
    FileRepo<Category, Integer> categoryFileRepo;

    public CategoryService() {
        this.categoryFileRepo = new FileRepo<>(Contant.FilePath.CATEGORY_FILE);
    }
    @Override
    public void save(Category category) {
        categoryFileRepo.save(category);
    }

    @Override
    public List<Category> findAll() {
        return categoryFileRepo.findAll();
    }

    @Override
    public Category findById(int id) {
        return categoryFileRepo.findById(id);
    }

    @Override
    public int findIndex(int id) {
        return categoryFileRepo.findByIndex(id);
    }

    @Override
    public int autoInc() {
        return categoryFileRepo.autoInc();
    }

    public void updateCategoryStatus(boolean Status , int id) {
        List<Category> categories = findAll();
        for (Category category : categories) {
            if (category.getId().equals(id) ) {
                category.setCategoryStatus(Status);
                save(category);
                break;
            }
        }

    }


}

