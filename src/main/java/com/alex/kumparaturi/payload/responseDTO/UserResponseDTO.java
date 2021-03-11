package com.alex.kumparaturi.payload.responseDTO;

public class UserResponseDTO {

    private Long id;
    private String name;
    private Long shoppingListId;
    private Long isShared;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public Long getIsShared() {
        return isShared;
    }

    public void setIsShared(Long isShared) {
        this.isShared = isShared;
    }
}
