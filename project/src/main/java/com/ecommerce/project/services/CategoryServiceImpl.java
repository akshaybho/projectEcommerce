package com.ecommerce.project.services;

import com.ecommerce.project.categoryRepository.CategoryRepository;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {

        List <Category> categories = categoryRepository.findAll();
        if(categories.isEmpty())
            throw new APIException("No category created till now.");
        return categories;
    }

    @Override
    public void createCategory(Category category) {

        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory != null)
            throw new APIException("Category with this name "+category.getCategoryName()+" already exists !!");
        categoryRepository.save(category);
    }

    @Override
    public String deleteCategory(Long categoryId) {

        List <Category> categories = categoryRepository.findAll();

        Category catagory = categories.stream()
                .filter(c->c.getCategoryId().equals(categoryId))
                .findFirst().orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Resource not found"));
        if(catagory==null)
            return "Category not found";

        categoryRepository.delete(catagory);
        return "Category with "+categoryId+" deleted successfully";
    }

    @Override
    public Category updateCategory(Category category, Long categoryId) {

        Category savedCategory = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category with this Id not found"));

        category.setCategoryId(categoryId);
       savedCategory = categoryRepository.save(category);
       return savedCategory;
    }
}
