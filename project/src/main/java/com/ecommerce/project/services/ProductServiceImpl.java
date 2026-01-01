package com.ecommerce.project.services;

import com.ecommerce.project.categoryRepository.CategoryRepository;
import com.ecommerce.project.categoryRepository.ProductRepository;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.CategoryDto;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public ProductDto addProduct(Long categoryId, Product product) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category"+"categroyId:"+categoryId));

        product.setCategory(category);
        double specialPrice = product.getPrice() -
                (product.getDiscount()*0.01*product.getPrice());
        product.setImage("default.png");
        product.setSpecialPrice(specialPrice);

        Product savedProduct = productRepository.save(product);
        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
   public List<ProductDto> getAll() {

        List <Product> list = productRepository.findAll();

        List <ProductDto> listDto = list.stream().map(n -> entityToDto(n))
                .collect(Collectors.toList());
        return listDto;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category"+"categoryId"+categoryId));


        List <Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List <ProductDto> productsDto = products.stream()
                .map(n -> entityToDto(n)).collect(Collectors.toList());

        ProductResponse pr = new ProductResponse();
        pr.setContent(productsDto);
        return pr;
    }

    //entity To Dto
    public ProductDto entityToDto(Product savedProduct){

        return mapper.map(savedProduct, ProductDto.class);
    }
}
