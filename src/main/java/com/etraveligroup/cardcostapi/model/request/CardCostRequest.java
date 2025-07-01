package com.etraveligroup.cardcostapi.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data; // Requires Lombok dependency

@Data // Generates getters, setters, toString, equals, hashCode
public class CardCostRequest {

    @NotBlank(message = "Card number cannot be empty")
    //[cite_start]
    @Size(min = 8, max = 19, message = "Card number must be between 8 and 19 digits") //[cite: 13]
    @Pattern(regexp = "^[0-9]+$", message = "Card number must contain only digits")
    private String cardNumber;

    // You might add a custom validation here for Luhn algorithm if desired.
}