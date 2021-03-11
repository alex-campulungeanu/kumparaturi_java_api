package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasswordResetTokenRepository  extends JpaRepository<PasswordResetToken, Long> {

    PasswordResetToken findByToken(String token);

}
