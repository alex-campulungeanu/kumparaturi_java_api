package com.alex.kumparaturi.payload.requestDTO;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class ShoppingListRequestDTO {

    private Long id;
    private String name;
    private String description;
    private String photo;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }
}
