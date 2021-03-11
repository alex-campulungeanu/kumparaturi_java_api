package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.AppLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppLogsRepository extends JpaRepository<AppLogs, Long> {
}
