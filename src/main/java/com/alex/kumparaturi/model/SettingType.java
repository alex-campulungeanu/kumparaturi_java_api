package com.alex.kumparaturi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "setting_type")
public class SettingType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;

    @OneToMany(mappedBy = "settingType")
    @JsonManagedReference
    private Set<UserSetting> userSettings = new HashSet<>();

    public SettingType() {
    }

    public SettingType(String name) {
        this.id = id;
        this.name = name;
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

    public Set<UserSetting> getUserSettings() {
        return userSettings;
    }

    public void setUserSettings(Set<UserSetting> userSettings) {
        this.userSettings = userSettings;
    }
}
