package com.ecommerce.project.controller;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/admin/categories/{categoryId}/product")
    public ResponseEntity<ProductDto> addProduct(@RequestBody Product product,
                                                 @PathVariable Long categoryId){

        ProductDto productDto = productService.addProduct(categoryId, product);
        return new ResponseEntity<>(productDto, HttpStatus.CREATED);
    }

    @GetMapping("/admin/product")
    public ResponseEntity<List<ProductDto>> getAllProducts(){

        List<ProductDto> productDto = productService.getAll();
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @GetMapping("/admin/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId){

        ProductResponse productResponse = productService.getProductByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }
}
