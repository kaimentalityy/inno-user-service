package com.innowise.util.dto.rq;// RegisterRequestDTO.java
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(

    @NotBlank
    @Size(min = 3, max = 20)
    String username,

    @NotBlank
    @Size(min = 6)
    String password

){
}
