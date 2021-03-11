package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.SettingType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingTypeRepository extends JpaRepository<SettingType, Long> {
}
