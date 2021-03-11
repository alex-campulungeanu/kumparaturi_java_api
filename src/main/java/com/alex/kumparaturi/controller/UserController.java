package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.model.SettingType;
import com.alex.kumparaturi.model.User;
import com.alex.kumparaturi.model.UserSetting;
import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.repository.SettingTypeRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.security.CurrentUser;
import com.alex.kumparaturi.security.UserPrincipal;
import com.alex.kumparaturi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    SettingTypeRepository settingTypeRepository;

    @GetMapping(value = "/get_users_for_list/{shopping_list_id}")
    public ResponseEntity<?> getUsers(@PathVariable("shopping_list_id") Long shoppingListId) {
        return new ResponseEntity(userService.getUsersForShoppingList(shoppingListId), HttpStatus.OK);
    }

    @RequestMapping(value = "/get_user_details")
    public PayloadResponse getUserDetails(@CurrentUser UserPrincipal userPrincipal) {
        Map<String, Object> userDetails = new HashMap<>();
        userDetails.put("name", userPrincipal.getName());
        userDetails.put("username", userPrincipal.getUsername());
        userDetails.put("email", userPrincipal.getEmail());
        userDetails.put("userImage", userPrincipal.getPhoto());

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));
        //TODO: make this array
//        {
//            "status": "ok",
//                "message": "succes",
//                "payload":{
//            "username": "barosanu",
//                    "email": "barosanu@test.com",
//                    "userImage": "",
//                    "setting":{
//                            "1":{
//                                "setting_value_string": null,
//                                 "setting_value_number": "0"
//                                },
//                            "2":{
//                                "setting_value_string": null,
//                                 "setting_value_number": "0"
//                                }
//                      }
//        }
//        }

        //TODO: aolooo Struto-camila asta sa o fac dinamic. merge si asa deocamdata
        Map<Long, Map<String, Object>> settings = new HashMap<>();
        for (UserSetting userSetting: user.getUserSettings()) {
            Map<String, Object> settingtype = new HashMap<>();
            settingtype.put("setting_value_string", userSetting.getSettingValueString());
            settingtype.put("setting_value_number", userSetting.getSettingValueNumber().toString());

            settings.put(userSetting.getSettingType().getId(), settingtype);
        }

        userDetails.put("setting", settings);

        return new PayloadResponse(userDetails, "ok", "Details have been fetched !");

    }

    @PostMapping(value = "/change_password")
    public PayloadResponse changePassword(@CurrentUser UserPrincipal currentUser, @RequestBody Map<String, String> passwordRequest) {
        return userService.changePassword(currentUser, passwordRequest.get("oldPassword"), passwordRequest.get("newPassword"));
    }

    @PostMapping(value = "/change_user_avatar")
    public PayloadResponse changeUserAvatar(@CurrentUser UserPrincipal currentUser, @RequestParam("photo") MultipartFile photo) throws IOException {
        return userService.changeUserAvatar(currentUser, photo);
    }

    @PostMapping(value = "change_notification_settings")
    public PayloadResponse changeNotificationSettings(@CurrentUser UserPrincipal userPrincipal, @RequestBody Map<String, Long> notificationSettingRequest) {

        Long settingTypeId =notificationSettingRequest.get("setting_type");
        Long newStatus =notificationSettingRequest.get("newStatus");

        User user = userRepository.findById(userPrincipal.getId()).orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        SettingType settingType = settingTypeRepository.findById(settingTypeId).orElseThrow(() -> new ResourceNotFoundException("SettingType", "id", settingTypeId));
        user.removeSettingType(settingType);

        UserSetting userSetting = new UserSetting();
        userSetting.setUser(user);
        userSetting.setSettingType(settingType);
        userSetting.setSettingValueNumber(newStatus);

        user.getUserSettings().add(userSetting);

        userRepository.save(user);

        return new PayloadResponse("ok", "ok", "Notification setting has been changed");
    }
}
