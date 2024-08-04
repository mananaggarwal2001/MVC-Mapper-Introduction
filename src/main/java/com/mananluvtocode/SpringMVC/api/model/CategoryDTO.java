package com.mananluvtocode.SpringMVC.api.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;
// Mapstruct will do the mapping automatically.
@Data
public class CategoryDTO {
    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String name;
}
