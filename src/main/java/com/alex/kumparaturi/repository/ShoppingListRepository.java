package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    @Query(value = "select id from {h-schema}shopping_list where user_id = :user_id", nativeQuery = true)
    List<Long> getShoppingListIdsUserHasAccess(@Param("user_id") Long userId);

}
