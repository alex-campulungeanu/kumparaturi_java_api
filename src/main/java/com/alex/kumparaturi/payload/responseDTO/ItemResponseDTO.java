package com.alex.kumparaturi.payload.responseDTO;

public class ItemResponseDTO {
    private Long id;
    private String name;
    private Long status_id;
    private String create_date;
    private String update_date;
    private String username;
    private String user_photo;
    private Long shopping_list_id;

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

    public Long getStatus_id() {
        return status_id;
    }

    public void setStatus_id(Long status_id) {
        this.status_id = status_id;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public void setShopping_list_id(Long shopping_list_id) {
        this.shopping_list_id = shopping_list_id;
    }

    public Long getShopping_list_id() {
        return shopping_list_id;
    }



}
