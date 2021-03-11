package com.alex.kumparaturi.model;

import com.alex.kumparaturi.model.audit.DateAuditUpdate;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;

@Entity
@Table(name = "items")
public class Item extends DateAuditUpdate<Instant> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Valid
    @Size(min = 1, max = 100)
    private String name;

    private Long statusId;

    private Instant createDate;

    private Long shoppingListId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @Transient //to ignore returning child Entity User
    private User user;

    public Item() {
    }

    public Item(String name, Long status_id, Instant create_date, Long shoppingListId) {
        this.name = name;
        this.statusId = status_id;
        this.createDate = create_date;
        this.shoppingListId = shoppingListId;
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

    public Long getStatusId() {
        return statusId;
    }

    public void setStatusId(Long statusId) {
        this.statusId = statusId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    public void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }
}
