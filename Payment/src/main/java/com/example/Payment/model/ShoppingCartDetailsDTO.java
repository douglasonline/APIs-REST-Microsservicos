package com.example.Payment.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShoppingCartDetailsDTO {

    private Long id;
    private int statusCode; // Atributo para armazenar o código de status

    // Construtor
    public ShoppingCartDetailsDTO(int statusCode) {
        this.statusCode = statusCode;
    }

}
