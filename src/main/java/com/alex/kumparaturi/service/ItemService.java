package com.alex.kumparaturi.service;

import com.alex.kumparaturi.config.Constants;
import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.model.Item;
import com.alex.kumparaturi.model.User;
import com.alex.kumparaturi.model.UserSetting;
import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.payload.requestDTO.ItemRequestDTO;
import com.alex.kumparaturi.payload.responseDTO.ItemResponseDTO;
import com.alex.kumparaturi.repository.ItemRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.security.UserPrincipal;
import com.alex.kumparaturi.utils.DateFormatterUtils;
import com.alex.kumparaturi.utils.SecurityUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.time.Instant;
import java.util.*;

@Service
public class ItemService {
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    Environment environment;

    @Autowired
    EntityManager entityManager;

    @Autowired
    SendNotificationService sendNotificationService;

    @Autowired
    ShoppingListService shoppingListService;

    //controller services
    public PayloadResponse<ItemResponseDTO> getAllItems(Long shoppingListId) {
        //this is for learning only, it's better with sql
        /*List<Item> items = itemRepository.findAll();
        List<ItemResponse> itemResponses = items.stream().map(item -> {
            return new ItemResponse(item.getId(), item.getName(), item.getStatusId(), item.getCreateDate(),
                    getUserForItem(item.getUser().getId())
            );
        }).collect(Collectors.toList());*/
        /* old way
        List<ItemResponseDTO> itemResponses = itemRepository.getAllItemsWithUsername();
        return new PayloadResponse(itemResponses,"ok", "Items list loaded succesfuly");
        */
        Long currentUser = SecurityUtils.getCurrentUserLogin().getId();
        logger.info("Current user login: " + currentUser);
//        Long shopping_list_default_id = SecurityUtils.getCurrentUserLogin().getShopppingList_id();
//        Long shoppingListId = 6L;

        if (shoppingListService.checkShoppingListAccess(shoppingListId)) {

            String sqlItems = "select i.id, i.name, i.status_id, to_char(i.create_date, 'yyyy-MM-dd hh24:MI:SS') as create_date, to_char(i.update_date, 'yyyy-MM-dd hh24:MI:SS') as update_date, u.username, u.photo as user_photo\n" +
                    "from kumparaturi.items i\n" +
                    "inner join kumparaturi.users u on i.user_id = u.id \n" +
                    "where i.shopping_list_id = :shopping_list_id\n" +
                    "order by i.status_id asc, i.create_date desc";
            Query q = entityManager.createNativeQuery(sqlItems);
            q.unwrap(NativeQuery.class)
                    .addScalar("id", StandardBasicTypes.LONG)
                    .addScalar("name", StandardBasicTypes.STRING)
                    .addScalar("status_id", StandardBasicTypes.LONG)
                    .addScalar("create_date", StandardBasicTypes.STRING)
                    .addScalar("update_date", StandardBasicTypes.STRING)
                    .addScalar("username", StandardBasicTypes.STRING)
                    .addScalar("user_photo", StandardBasicTypes.STRING);
            q.setParameter("shopping_list_id", shoppingListId);
            q.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(ItemResponseDTO.class));
            List<ItemResponseDTO> resultList = q.getResultList();

            return new PayloadResponse(resultList, "ok", "Items list loaded succesfuly !");
        } else {
            return new PayloadResponse("", "notok", "NOT have access to shopping list !");
        }

    }

    public PayloadResponse addItem(ItemRequestDTO itemRequestDTO, UserPrincipal userPrincipal) {
        if (shoppingListService.checkShoppingListAccess(itemRequestDTO.getShoppingListId())) {

            Item item = new Item();
            User user = userRepository.getOne(userPrincipal.getId());
            item.setName(itemRequestDTO.getItemName());
            item.setStatusId(Constants.ACTIVE_STATUS_ITEM);
            item.setCreateDate(Instant.now());
            item.setUser(user);
            item.setShoppingListId(itemRequestDTO.getShoppingListId());

            itemRepository.save(item);
            //TODO: ar trebui sa fac un DTO si sa il folosesc si la getAllItems si la adItem
            Map<String, Object> mapResponse = new HashMap<>();
            mapResponse.put("id", item.getId());
            mapResponse.put("name", item.getName());
            mapResponse.put("status_id", item.getStatusId());
            mapResponse.put("create_date", DateFormatterUtils.getNowFormatted());
            mapResponse.put("update_date", DateFormatterUtils.getNowFormatted());
            mapResponse.put("username", userPrincipal.getUsername());
            mapResponse.put("user_photo", user.getPhoto());
            mapResponse.put("shopping_list_id", item.getShoppingListId());

            List<Map> addedItem = new ArrayList<>(); //TODO: ar trebui sa modific aici, nu mai este nevoie de vector, pot sa pun direct
            addedItem.add(mapResponse);

            //send push notification only on prod, for now
            if (canSendNotificationStatus(userPrincipal) && environment.getActiveProfiles()[0].equals("prod")) {
                logger.info("Sending push notification");
                sendNotificationService.sendPushNotification(item.getName());
            }

            return new PayloadResponse(addedItem, "ok", "Item added succsefully");
        } else {
            return new PayloadResponse("addedItem", "notok", "NOT have access to shopping list !");
        }

    }

    public PayloadResponse editItemName(Long itemId, String itemName) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
        if (shoppingListService.checkShoppingListAccess(item.getShoppingListId())) {
            item.setName(itemName);
            itemRepository.save(item);
            ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
            itemResponseDTO.setId(item.getId());
            itemResponseDTO.setName(item.getName());
            return new PayloadResponse(itemResponseDTO, "ok", "Item edited succsefully");
        } else {
            return new PayloadResponse("", "notok", "NOT have access to shopping list !");
        }
    }

    @Transactional
    public void deleteItem(List<Long> itemIds) {
        itemRepository.deleteByIds(itemIds);
    }

    @Transactional
    public PayloadResponse updateItemStatus(Long itemId, Long statusId) {
        try {
            Item item = itemRepository.findById(itemId).orElseThrow(() -> new ResourceNotFoundException("Item", "id", itemId));
            if (shoppingListService.checkShoppingListAccess(item.getShoppingListId())) {
                item.setStatusId(statusId);
                itemRepository.save(item);
                ItemResponseDTO itemResponseDTO = new ItemResponseDTO();
                itemResponseDTO.setId(item.getId());
                itemResponseDTO.setStatus_id(item.getStatusId());
                return new PayloadResponse(itemResponseDTO, "ok", "Item completed succsefully");
            } else {
                return new PayloadResponse("", "notok", "NOT have access to shopping list !");
            }
        }catch (ResourceNotFoundException r) {
            return new PayloadResponse("", "notok", "Resource not found: " + r.getResourceName());
        }
    }

    @Transactional
    public void deleteAllItems(Long statusId, Long shioppingListId) {
        if (statusId == -1) {
            itemRepository.deleteAllWithQuery(shioppingListId);
        } else if(statusId == 1 || statusId == 0) {
            itemRepository.deleteByStatusId(statusId, shioppingListId);
        }
    }


    //helpers
    public String getUserForItem(Long userId){
        return userRepository.getUsernameFromId(userId);
    }

    public List<Item> getAllItemsComp() {
        return itemRepository.findAll();
    }

    public boolean canSendNotificationStatus(UserPrincipal userPrincipal) {
        Boolean sendNotificationStatus = false;
        User user = userRepository.getOne(userPrincipal.getId());
        Set<UserSetting> userSettings = user.getUserSettings();

        for (UserSetting userSetting: userSettings) {
            if(userSetting.getSettingType().getId() == Constants.SEND_NOTIFICATION_SETTING_TYPE_ID
                    && userSetting.getSettingValueNumber() == Constants.SEND_NOTIFICATION_SETTING_VALUE_NUMBER) {
                sendNotificationStatus = true;
            }
        }

        return sendNotificationStatus;
    }
}
