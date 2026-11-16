package LTWeb.services;

import java.util.List;
import LTWeb.models.Category;

public interface CategoryService {
    void insert(Category category);
    void edit(Category category);
    void delete(int id);
    Category findById(int id);
    List<Category> findAll();
    List<Category> findByCategoryname(String catename);
    List<Category> findAll(int page, int pagesize);
    int count();
}