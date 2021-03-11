package com.alex.kumparaturi.service;

import com.alex.kumparaturi.controller.UserController;
import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.model.MailInfo;
import com.alex.kumparaturi.model.User;
import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.payload.responseDTO.ShoppingListResponseDTO;
import com.alex.kumparaturi.payload.responseDTO.UserResponseDTO;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.security.UserPrincipal;
import com.alex.kumparaturi.utils.ImageToBase64;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    EntityManager entityManager;

    public ResponseEntity<?> getUsersForShoppingList(Long shoppingListId) {
        //TODO: maybe it's better with repository
        //list all users for sharing without users who owned current shoppingListId
        //noaptea mintii ce am facut mai jos, m-am grabit, merge si asa ...
        String sql = "select u.id, u.name, slu.shopping_list_id as shoppingListId, case when slu.shopping_list_id is not null then 1 else 0 end as isShared \n" +
                "from kumparaturi.users u \n" +
                "left join kumparaturi.shared_list_user slu on slu.user_id = u.id and slu.shopping_list_id = :shopping_list_id\n" +
                "left join kumparaturi.shopping_list sl on sl.id = :shopping_list_id \n" +
                "where u.active = 1\n" +
                "and sl.user_id <> u.id ";
        Query q = entityManager.createNativeQuery(sql);
        q.unwrap(NativeQuery.class).addScalar("id", StandardBasicTypes.LONG)
                .addScalar("id", StandardBasicTypes.LONG)
                .addScalar("name", StandardBasicTypes.STRING)
                .addScalar("shoppingListId", StandardBasicTypes.LONG)
                .addScalar("isShared", StandardBasicTypes.LONG);
        q.setParameter("shopping_list_id", shoppingListId);
        q.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(UserResponseDTO.class));
        List<UserResponseDTO> resultList = q.getResultList();
//        return resultList;
        return new ResponseEntity(new PayloadResponse(resultList, "ok", "User list retrieved successfully!"), HttpStatus.OK);
    }

    @Transactional
    public PayloadResponse changePassword(UserPrincipal currentUser, String oldPasswordEntered, String newPasswordEntered) {
        Map<String, String> responseMap = new HashMap<>();
        String errorMesasge = "";
        if(passwordEncoder.matches(oldPasswordEntered, currentUser.getPassword())) {
            String newPassword = passwordEncoder.encode(newPasswordEntered);
            User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", currentUser.getId()));
            user.setPassword(newPassword);
            userRepository.save(user); //folosesc save din JPA ca sa mearga auditul pe update_date, cu nativeQuery nu se declanseaza listenrul de audit
//            userRepository.updatePassword(newPassword, currentUser.getId());

        } else {
            errorMesasge = "Old password not match";
            logger.info("Password is NOT identical");
        }

        PayloadResponse response = new PayloadResponse();
        responseMap.put("errors", errorMesasge);
        response.setPayload(responseMap);
        logger.info("error is set: " + errorMesasge);
        if (errorMesasge.isEmpty()) {
            response.setMessage("Password is changed !");
            response.setStatus("ok");
        } else {
            response.setMessage("Password is not changed !");
            response.setStatus("notok");
        }

        return response;
    }

    @Transactional
    public void resetPassword(String password, Long userId) {
        userRepository.updatePassword(password, userId);
    };

    public PayloadResponse changeUserAvatar(UserPrincipal currentUser, MultipartFile photo) throws IOException {
        String newUserAvatar = ImageToBase64.toBase64(photo.getBytes());
        User user = userRepository.findById(currentUser.getId()).orElseThrow(() -> new ResourceNotFoundException("Item", "id", currentUser.getId()));
        user.setPhoto(newUserAvatar);
        userRepository.save(user);

        return new PayloadResponse("ok", "ok", "Photo has been changed");
    }


    public boolean activateAccount(String activationToken) {
//        User user = userRepository.findByActivationToken(activationToken).orElseThrow(() -> new ResourceNotFoundException("User", "activationToken", activationToken));
        Optional<User> user = userRepository.findByActivationToken(activationToken);
        if (user.isPresent()) {
            User user2 = user.get();
            user2.setActive(1L);
            userRepository.save(user2);
            logger.info("Activation Token is ok!");
            return true;
        } else {
            logger.info("Activation Token has not been found !");
            return false;
        }

    }

    public boolean sendAccountActivationEmail(String userEmail, String activationURL) {
        MailInfo mailInfo = new MailInfo();
        mailInfo.setFrom("no-reply@kumparaturi.com");
        mailInfo.setTo(userEmail);
        mailInfo.setSubject("Activare cont aplicatie Kumparaturi");
        Map<String, Object> model = new HashMap<>();
        model.put("activationURL", activationURL);
        mailInfo.setModel(model);

        try {
            emailSenderService.sendHtmlMail(mailInfo, "email/account_activation_email");
            return true;
        } catch (MessagingException e) {
            logger.info("error in  send activation Email " + e);
            return false;
        }
    }
}
