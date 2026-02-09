package com.ecommerce.project.controller;

import com.ecommerce.project.config.AppConstants;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    public ProductResponse getAllProducts(
            @RequestParam(name = "pageNumber", defaultValue = AppConstants.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = AppConstants.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(name = "sortBy", defaultValue = AppConstants.SORT_PRODUCTS_BY, required = false) String sortBy,
            @RequestParam(name = "sortOrder", defaultValue = AppConstants.SORT_DIR, required = false) String sortOrder
    ){

        return productService.getAll(pageNumber, pageSize, sortBy, sortOrder);
    }

    @GetMapping("/admin/{categoryId}/product")
    public ResponseEntity<ProductResponse> getProductByCategory(@PathVariable Long categoryId){

        ProductResponse productResponse = productService.getProductByCategory(categoryId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @GetMapping("/admin/product/{keywords}")
    public ResponseEntity<List<ProductDto>> getProductsByKeyword(@PathVariable String keywords){

        List<ProductDto> productDto = productService.getProdutByKeyword(keywords);
        return new ResponseEntity<>(productDto, HttpStatus.OK);
    }

    @DeleteMapping("/admin/product")
    public ResponseEntity<String> deleteAllProducts(){
        productService.deleteAll();
        return new ResponseEntity<>("All products deleted successfully", HttpStatus.OK);
    }
    
    @DeleteMapping("/admin/product/{productId}")
    public ResponseEntity<String> deleteProductById(@PathVariable Long productId){
        
        productService.deleteProduct(productId);
        return new ResponseEntity<>("Product with "+productId+" deleted successfully", HttpStatus.OK);
    }

    @PutMapping("/admin/product/{productId}")
    public ResponseEntity<ProductDto> updateProduct(@RequestBody ProductDto productDto,
                                                    @PathVariable Long productId){

        ProductDto updatedProductdto = productService.updateProduct(productDto, productId);
        return new ResponseEntity<>(updatedProductdto, HttpStatus.OK);
    }

    @PostMapping("/admin/product/{productId}/image")
    public ResponseEntity<ProductDto> updateProductImage(@PathVariable Long productId,
                                                         @RequestParam("image")MultipartFile image) throws IOException {

        ProductDto updatedProduct = productService.updateProductImage(productId, image);
        return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
    }
}
