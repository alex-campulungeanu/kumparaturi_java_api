package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.repository.ItemRepository;
import com.alex.kumparaturi.repository.RoleRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.security.JwtTokenProvider;
import com.alex.kumparaturi.service.EmailSenderService;
import com.alex.kumparaturi.service.ItemService;
import com.alex.kumparaturi.service.SendNotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

@RestController
@RequestMapping("/test")
public class TestController  {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemService itemService;

    @Autowired
    Environment environment;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    SendNotificationService sendNotificationService;

    @RequestMapping(value = "/get_app_properties")
    public String getAppProperties() throws UnknownHostException {
        logger.info("App name: " + environment.getProperty("spring.application.name"));
        logger.info("Active profiles: " + Arrays.toString(environment.getActiveProfiles()));
        logger.info("\n----------------------------------------------------------\n\t" +
                    "Application '{}' is running! Access URLs:\n\t" +
                    "Local: \t\t//localhost:{}\n\t" +
                    "External: \t//{}:{}\n\t" +
                    "Profile(s): \t{}\n----------------------------------------------------------",
                environment.getProperty("spring.application.name"),

                environment.getProperty("server.port"),

                InetAddress.getLocalHost().getHostAddress(),
                environment.getProperty("server.port"),

                environment.getActiveProfiles());
        String appName =environment.getProperty("spring.application.name");
        String serverPort = environment.getProperty("server.port");
        String externalHost = InetAddress.getLocalHost().getHostAddress();
        String activeProfiles = Arrays.toString(environment.getActiveProfiles());
        return "----------------------------------------------------------</br>" +
                "Application " + appName+ " is running!</br>" +
                "Local: //localhost:" + serverPort + " </br>" +
                "External: //" + externalHost + " :"+ serverPort+ "</br>" +
                "Profile(s): " + activeProfiles + "" +
                "</br>----------------------------------------------------------";
    }

    @RequestMapping(value = "/encrypt_password/{password}")
    public String encryptPassword(@PathVariable String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(password);
        return hashedPassword;
    }

    @RequestMapping(value = "/test2")
    public String test1() {
       return "from test12222";
    }

//    @RequestMapping(value = "/get_users")
//    public List<User> getAllUsers() {
//        return userRepository.findAll();
//    }
//
//    @RequestMapping(value = "/get_roles")
//    public Optional<Role> getAllRoles() {
//        return roleRepository.findByName(RoleName.ROLE_ADMIN);
//    }

//    @RequestMapping(value = "/get_users_by_role")
//    public List<User> getUsersByRole(){
//        User user = new User("mircea","mircea","mircea","mircea", "sdsd");
//        return Arrays.asList(user);
//    }

//    @RequestMapping(value = "/get_jwt/{user_id}")
//    public String getJwt(String token, @PathVariable("user_id") Long userId) {
//        String fakeToken = jwtTokenProvider.generateFakeToken(userId);
//        logger.info("fakeToken: " + fakeToken);
//        return fakeToken;
//    }

//    @RequestMapping("/send_notification")
//    public void sendNotification() {
//        sendNotificationService.sendPushNotification("mancare");
//    }


//    @RequestMapping("/get_all_items")
//    public List<Item> getAllItems() {
//        return itemRepository.findAll();
//    }

//    @RequestMapping("/getitems")
//    public List<ItemResponse> getItems() {
//        List<Item> items = itemService.getAllItemsComp();
//        List<ItemResponse> itemResponses = items.stream().map(item -> {
//            return new ItemResponse(item.getId(), item.getName(), item.getStatusId(), item.getCreateDate(),
//                    getUserForItem(item.getUser().getId())
//            );
//        }).collect(Collectors.toList());
//        return itemResponses;
//    }

    public String getUserForItem(Long userId){
        logger.info(userRepository.getUsernameFromId(userId));
        logger.info("userId:" + userId);
        return userRepository.getUsernameFromId(userId);
    }

}
