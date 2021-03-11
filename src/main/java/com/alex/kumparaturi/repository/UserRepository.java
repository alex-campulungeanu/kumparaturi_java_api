package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByEmail(String email);
    Optional<User> findByActivationToken(String activationToken);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Query(value = "select username from kumparaturi.users where id = :id", nativeQuery = true)
    String getUsernameFromId(@Param("id") Long userId);

    @Query(value = "select password from kumparaturi.users where id = :id", nativeQuery = true)
    String getPasswordFromId(@Param("id") Long userId);

    @Modifying
    @Query(value = "update User u set u.password = :password where u.id = :userId")
    void updatePassword(@Param("password") String password, @Param("userId") Long userId);

}
