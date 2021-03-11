package com.alex.kumparaturi.payload.responseDTO;

import com.alex.kumparaturi.model.ShoppingList;

public class ShoppingListResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String photo;

    private Long isShared;

    public ShoppingListResponseDTO() {
    }

    public ShoppingListResponseDTO(Long id, String name, String description, String photo) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photo = photo;
    }

    public ShoppingListResponseDTO(ShoppingList shoppingList) {
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Long getIsShared() {
        return isShared;
    }

    public void setIsShared(Long isShared) {
        this.isShared = isShared;
    }
}
