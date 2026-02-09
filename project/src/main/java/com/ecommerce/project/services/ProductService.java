package com.ecommerce.project.services;

import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    ProductDto addProduct(Long categoryId, Product product);

    ProductResponse getProductByCategory(Long categoryId);

    List<ProductDto> getProdutByKeyword(String keyword);

    void deleteAll();

    ProductDto deleteProduct(Long productId);

    ProductDto updateProduct(ProductDto productDto, Long productId);

    ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException;

    ProductResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
}
