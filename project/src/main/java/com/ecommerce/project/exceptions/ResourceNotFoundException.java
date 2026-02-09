package com.ecommerce.project.exceptions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String message)
    {
        super(message);
    }

}
