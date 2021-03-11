package com.alex.kumparaturi.model;

import com.alex.kumparaturi.model.audit.DateAudit;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username"}),
        @UniqueConstraint(columnNames = "email")
})
public class User extends DateAudit<Instant> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 140)
    private String name;

    @NotBlank
    @Size(max = 140)
    private String username;

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    private String photo;

//    @NotBlank
    private Long active;

    @Column(name = "activation_token")
    private String activationToken;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name="user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserSetting> userSettings = new HashSet<>();

    @ManyToMany(mappedBy = "sharedUsers")
    private Set<ShoppingList> shoppingLists = new HashSet<>();

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(String name, String username, String email, String password, String photo, Long active, String activationToken) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.photo = photo;
        this.active = active;
        this.activationToken = activationToken;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Set<UserSetting> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(Set<UserSetting> userSettings) {
        this.userSettings = userSettings;
    }

    public Long getActive() {
        return active;
    }

    public void setActive(Long active) {
        this.active = active;
    }

//    public void addSettingType(SettingType settingType) {
//        UserSetting userSetting = new UserSetting(this, settingType);
//        userSettings.remove(userSetting);
//        settingType.getUserSettings().remove(userSetting);
//    }
//
    public void removeSettingType(SettingType settingType) {
        UserSetting userSetting = new UserSetting(this, settingType);
        userSettings.remove(userSetting);
        settingType.getUserSettings().remove(userSetting);
//        userSettings.stream().forEach(usr -> {
//            System.out.println("id is : " + usr.getId());
//        })
//        ;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || this.getClass() != other.getClass()) {
            return false;
        } else {
            User otherEntity = (User) other;
            return Objects.equals(id, otherEntity.id);
        }
    }

    public String getActivationToken() {
        return activationToken;
    }

    public Set<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void setShoppingLists(Set<ShoppingList> shoppingLists) {
        this.shoppingLists = shoppingLists;
    }
}
