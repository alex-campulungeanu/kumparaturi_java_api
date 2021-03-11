package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.payload.responseDTO.ShoppingListResponseDTO;
import com.alex.kumparaturi.service.ShoppingListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ShoppingListController {
    public static final Logger logger = LoggerFactory.getLogger(ShoppingListController.class);

    @Autowired
    ShoppingListService shoppingListService;

    @GetMapping("/shopping_lists")
    public ResponseEntity<?> getShoppingListForUser() {
        return new ResponseEntity(new PayloadResponse(shoppingListService.getShoppingListForUser(), "ok", "Shopping list get succesfully"), HttpStatus.OK);
    }

    @PostMapping("/add_shopping_list")
    public ResponseEntity<?> addItemList(@RequestParam("name") String name,
                                         @RequestParam("description") String description,
                                         @RequestParam("photo") MultipartFile photo) {
        logger.info("enter add_shopping_list method sdas sd ");
        ShoppingListResponseDTO shoppingListResponseDTO = shoppingListService.addShoppingList(name, description, photo);

        return new ResponseEntity(new PayloadResponse(shoppingListResponseDTO, "ok", "Shopping list added succesfully"), HttpStatus.OK);

    }

    @PostMapping("/edit_shopping_list")
    public ResponseEntity<?> editShoppingList(@RequestBody Map<String, String> listRequest) {
        return new ResponseEntity(shoppingListService.editShoppingList(Long.parseLong(listRequest.get("id")), listRequest.get("name")), HttpStatus.OK);
    }

    @PostMapping ("/delete_shopping_list/{id}")
    public ResponseEntity<?> deleteShoppingList(@PathVariable("id") Long listId) {
        return new ResponseEntity(shoppingListService.deleteShoppingList(listId), HttpStatus.OK);
    }

    @PostMapping("/add_share_shopping_list")
    public ResponseEntity<?> addShareShoppingList(@RequestBody Map<String, Long> shareRequest) {
        Long shoppingListId = shareRequest.get("shoppingListId");
        Long userId = shareRequest.get("userId");
        return new ResponseEntity(shoppingListService.addShareShoppingList(shoppingListId, userId), HttpStatus.OK);
    }

    @PostMapping("/remove_share_shopping_list")
    public ResponseEntity<?> removeShareShoppingList(@RequestBody Map<String, Long> shareRequest) {
        Long shoppingListId = shareRequest.get("shoppingListId");
        Long userId = shareRequest.get("userId");
        return new ResponseEntity(shoppingListService.removeShareShoppingList(shoppingListId, userId), HttpStatus.OK);
    }


}
