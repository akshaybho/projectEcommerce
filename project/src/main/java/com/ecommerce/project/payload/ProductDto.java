package com.ecommerce.project.payload;

import com.ecommerce.project.model.Category;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    private Long productId;

    @NotBlank(message = "productName is required")
    @Size(max = 255, message = "productName must be at most 255 characters")
    private String productName;

    @Size(max = 255, message = "name must be at most 255 characters")
    private String name;

    @Size(max = 2048, message = "image URL must be at most 2048 characters")
    private String image;

    @Size(max = 2000, message = "description must be at most 2000 characters")
    private String description;

    @NotNull(message = "quantity is required")
    @Min(value = 0, message = "quantity must be zero or positive")
    private Integer quantity;

    @NotNull(message = "price is required")
    @Positive(message = "price must be greater than 0")
    private double price;

    @DecimalMin(value = "0.0", inclusive = true, message = "discount must be >= 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "discount must be <= 100")
    private double discount;

    @DecimalMin(value = "0.0", inclusive = true, message = "specialPrice must be >= 0")
    private double specialPrice;

    @NotNull(message = "category is required")
    @Valid
    private Category category;

}
