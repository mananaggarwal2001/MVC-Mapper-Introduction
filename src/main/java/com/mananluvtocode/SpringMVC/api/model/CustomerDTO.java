package com.mananluvtocode.SpringMVC.api.model;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CustomerDTO {
    @NotNull
    private  Long id;
    @Size(min = 2, max = 50)
    private  String firstName;
    @Size(min = 2, max = 50)
    private  String lastName;
    @NotNull
    private  String customer_url;
}
