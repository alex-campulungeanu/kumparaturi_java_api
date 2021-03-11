package com.alex.kumparaturi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_setting_mn")
public class UserSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "setting_value_number")
    private Long settingValueNumber;

    @Column(name = "setting_value_string")
    private String settingValueString;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "setting_type_id")
    private SettingType settingType;

    public UserSetting() {

    }

    public UserSetting(User user, SettingType settingType) {
        this.user = user;
        this.settingType = settingType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SettingType getSettingType() {
        return settingType;
    }

    public void setSettingType(SettingType settingType) {
        this.settingType = settingType;
    }

    public Long getSettingValueNumber() {
        return settingValueNumber;
    }

    public void setSettingValueNumber(Long settingValueNumber) {
        this.settingValueNumber = settingValueNumber;
    }

    public String getSettingValueString() {
        return settingValueString;
    }

    public void setSettingValueString(String settingValueString) {
        this.settingValueString = settingValueString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, settingType);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        } else if (other == null || this.getClass() != other.getClass()) {
            return false;
        } else {
            UserSetting otherEntity = (UserSetting) other;
            return Objects.equals(user, otherEntity.user) && Objects.equals(settingType, otherEntity.settingType);
        }
    }
}
