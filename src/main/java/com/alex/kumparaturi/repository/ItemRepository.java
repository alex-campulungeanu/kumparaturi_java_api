package com.alex.kumparaturi.repository;

import com.alex.kumparaturi.model.Item;
import com.alex.kumparaturi.payload.projectionDTO.ItemResponseInterfaceDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    @Query(value = "select i.id, i.name, i.status_id, to_char(i.create_date, 'yyyy-MM-dd hh24:MI:SS') as create_date, to_char(i.update_date, 'yyyy-MM-dd hh24:MI:SS') as update_date, u.username, u" +
            ".photo as user_photo " +
            "from {h-schema}items i " +
            "inner join {h-schema}users u on i.user_id = u.id order by i.status_id asc, i.create_date desc", nativeQuery = true)
    List<ItemResponseInterfaceDTO> getAllItemsWithUsername();

    @Override
    void delete(Item item);

    @Override
    void deleteById(Long itemId);

    @Modifying
    @Query(value = "DELETE from Item i WHERE i.id in :ids")
    void deleteByIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query(value = "update Item i set statusId = :statusId WHERE i.id = :id")
    void updateById(@Param("id") Long id, @Param("statusId") Long statusId);

    @Modifying
    @Query(value = "DELETE from Item i WHERE i.statusId = :statusId and i.shoppingListId = :shopping_list_id")
    void deleteByStatusId(@Param("statusId") Long statusId, @Param("shopping_list_id") Long shoppingListId);

    @Modifying
    @Query(value = "DELETE from Item i WHERE i.shoppingListId = :shopping_list_id")
    void deleteAllWithQuery(@Param("shopping_list_id") Long shoppingListId);
}
