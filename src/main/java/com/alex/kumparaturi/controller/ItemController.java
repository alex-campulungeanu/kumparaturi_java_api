package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.payload.projectionDTO.ItemResponseInterfaceDTO;
import com.alex.kumparaturi.payload.requestDTO.ItemRequestDTO;
import com.alex.kumparaturi.payload.responseDTO.ItemResponseDTO;
import com.alex.kumparaturi.security.CurrentUser;
import com.alex.kumparaturi.security.UserPrincipal;
import com.alex.kumparaturi.service.ItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api")
public class ItemController {
    public static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    ItemService itemService;

    @GetMapping(value = "/items/{shopping_list_id}")
    public PayloadResponse<ItemResponseDTO> getAllItems(@PathVariable("shopping_list_id") Long shoppingListId) {
        return itemService.getAllItems(shoppingListId);
    }
//    public ResponseEntity<?> getAllItems() {
//        return ResponseEntity.badRequest().body("bad request");
//    }

    @PostMapping(value = "/add_item")
    public PayloadResponse addItem(@Valid @RequestBody ItemRequestDTO itemRequestDTO, @CurrentUser UserPrincipal currentUser) {
        return itemService.addItem(itemRequestDTO, currentUser);
    }

    //TODO: check if item with id exists and show proper Payload response
    @PostMapping(value = "/edit_item_name")
    public PayloadResponse editItemName(@Valid @RequestBody Map<String, String> itemRequest) {
        Long itemId = Long.parseLong(itemRequest.get("itemId"));
        String itemName = itemRequest.get("itemName");

        return itemService.editItemName(itemId, itemName);

    }

    //TODO: check if item with id exists and show proper Payload response
    @PostMapping(value = "/delete_item")
    public PayloadResponse deleteItem(@RequestBody Map<String, List<Long>> itemRequest) {
        List<Long> itemIds = itemRequest.get("ids");

        if (!itemIds.isEmpty()) {
            itemService.deleteItem(itemIds);
            return new PayloadResponse(itemRequest, "ok", "Item has been deleted");
        } else {
            Map<String, String> responseError = new HashMap<>();
            responseError.put("error", "Not item has been selected!");
            return new PayloadResponse(responseError, "notok", "No item has been selected");
        }
    }

    @PostMapping(value = "/update_item_status")
    public PayloadResponse updateItemStatus(@RequestBody Map<String, Long> itemInfo) {
        Long itemId = itemInfo.get("itemId");
        Long statusId = itemInfo.get("statusId");

        return itemService.updateItemStatus(itemId, statusId);
    }

    @PostMapping(value = "/delete_all_items")
    public PayloadResponse deleteAllItems(@RequestBody Map<String, Long> statusIdRequest) {
        Long statusId = statusIdRequest.get("statusId");
        Long shoppingListId = statusIdRequest.get("shoppingListId");

        itemService.deleteAllItems(statusId, shoppingListId);

        return new PayloadResponse(statusIdRequest, "ok", "Items has been deleted");

    }


}
