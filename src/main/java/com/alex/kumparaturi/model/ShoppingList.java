package com.alex.kumparaturi.model;

import com.alex.kumparaturi.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "shopping_list")
public class ShoppingList extends DateAudit<Instant> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @Size(min = 0, max = 20)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    private String description;

    private String photo;

    @ManyToMany
    @JoinTable(name = "SHARED_LIST_USER",
                joinColumns = @JoinColumn(name = "SHOPPING_LIST_ID"),
                inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    private Set<User> sharedUsers = new HashSet<>();

    public ShoppingList() {
    }

    public ShoppingList(String name, String description, String photo) {
        this.name = name;
        this.description = description;
        this.photo = photo;
//        this.userId = userId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public void addSharedUser(User user) {
        sharedUsers.add(user);
        user.getShoppingLists().add(this);
    }

    public void removeSharedUsers(User user) {
        sharedUsers.remove(user);
        user.getShoppingLists().remove(this);
    }

    public Set<User> getSharedUsers() {
        return sharedUsers;
    }

    public void setSharedUsers(Set<User> users) {
        this.sharedUsers = users;
    }

//    public Long getUserId() {
//        return userId;
//    }
//
//    public void setUserId(Long userId) {
//        this.userId = userId;
//    }

}
