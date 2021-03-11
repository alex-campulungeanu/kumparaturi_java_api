package com.alex.kumparaturi.payload.requestDTO;

import javax.validation.Valid;
import javax.validation.constraints.Size;

public class ItemRequestDTO {

    @Valid
    @Size(min = 1, max = 100, message = "Itemul trebuie sa aiba min:1 - max:100")
    private String itemName;

    private Long shoppingListId;

    public String getItemName() {
        return itemName;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }
}
