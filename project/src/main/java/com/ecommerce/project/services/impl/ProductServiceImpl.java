package com.ecommerce.project.services.impl;

import com.ecommerce.project.categoryRepository.CategoryRepository;
import com.ecommerce.project.categoryRepository.ProductRepository;
import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDto;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.services.FileService;
import com.ecommerce.project.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper mapper;

    @Value("${product.images.path.images}")
    private String imageUploadPath;

    @Autowired
    private FileService fileService;


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
   public ProductResponse getAll(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("asc") ?
        Sort.by(sortBy).ascending() :
        Sort.by(sortBy).descending();

        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page <Product> productPage = productRepository.findAll(pageDetails);

        List <Product> products = productPage.getContent();

        if(products.isEmpty())
            throw new APIException("No product created till now");

            List <ProductDto> productDtos = products.stream()
                    .map(product -> mapper.map(product, ProductDto.class))
                    .toList();

            ProductResponse productResponse = new ProductResponse();
            productResponse.setContent(productDtos);
            productResponse.setPageNumber(productPage.getNumber());
            productResponse.setPageSize(productPage.getSize());
            productResponse.setTotalElements(productPage.getTotalElements());
            productResponse.setTotalPages(productPage.getTotalPages());
            productResponse.setLastPage(productPage.isLast());

        return productResponse;
    }

    @Override
    public ProductResponse getProductByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category"+"categoryId"+categoryId));


        List <Product> products = productRepository.findByCategoryOrderByPriceAsc(category);
        List <ProductDto> productsDto = products.stream()
                .map(this::entityToDto).collect(Collectors.toList());

        ProductResponse pr = new ProductResponse();
        pr.setContent(productsDto);
        return pr;
    }

    @Override
    public List<ProductDto> getProdutByKeyword(String keyword) {

        List<Product> products = productRepository.findByNameContaining(keyword);
        return products.stream().map(this::entityToDto).toList();
    }

    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    @Override
    public ProductDto deleteProduct(Long productId) {

        List<Product> products = productRepository.findAll();

        Product product = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product with this Id not found"));

        productRepository.deleteById(productId);
        return mapper.map(product, ProductDto.class);
    }

    @Override
    public ProductDto updateProduct(ProductDto productDto, Long productId) {

         //get the product
        //update
        // save to db
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with "+productId+" not found."));

        productFromDB.setProductName(productDto.getProductName());
        productFromDB.setDescription(productDto.getDescription());
        productFromDB.setQuantity(productDto.getQuantity());
        productFromDB.setDiscount(productDto.getDiscount());
        productFromDB.setPrice(productDto.getPrice());
        productFromDB.setSpecialPrice(productDto.getSpecialPrice());

        //save
        Product savedProduct = productRepository.save(productFromDB);

        return mapper.map(savedProduct, ProductDto.class);
    }

    @Override
    public ProductDto updateProductImage(Long productId, MultipartFile image) throws IOException {

        //Get the product from DB
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with "+productId+" not found."));

        //Upload image to server
        //Get the filename of uploaded image
        String fileName = fileService.uploadImage(image, imageUploadPath);

        //Updating new file name to the product
        productFromDb.setImage(fileName);

        //Save the updated product
        Product updatedProduct = productRepository.save(productFromDb);

        //Return DTO after mapping the product DTO
        return mapper.map(updatedProduct, ProductDto.class);
    }

    //entity To Dto
    public ProductDto entityToDto(Product savedProduct){

        return mapper.map(savedProduct, ProductDto.class);
    }
}
