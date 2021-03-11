package com.alex.kumparaturi.service;

import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.model.ShoppingList;
import com.alex.kumparaturi.model.User;
import com.alex.kumparaturi.payload.PayloadResponse;
import com.alex.kumparaturi.payload.responseDTO.ShoppingListResponseDTO;
import com.alex.kumparaturi.repository.ShoppingListRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.utils.ImageToBase64;
import com.alex.kumparaturi.utils.SecurityUtils;
import org.hibernate.query.NativeQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShoppingListService {
    public static final Logger logger = LoggerFactory.getLogger(ShoppingListService.class);

    @Autowired
    ShoppingListRepository shoppingListRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    EntityManager entityManager;

    public Optional<ShoppingList> findForId(Long id) {return shoppingListRepository.findById(id);}

    public List<ShoppingListResponseDTO> getShoppingListForUser() {
        Long currentUser = SecurityUtils.getCurrentUserLogin().getId();
        String sql = "select id, name, description, photo, 0 isShared\n" +
                    "from kumparaturi.shopping_list \n" +
                    "where user_id = :user_id\n" +
                    "union\n" +
                    "select sl.id, sl.name, sl.description, sl.photo , 1 is_shared\n" +
                    "from kumparaturi.shared_list_user slu\n" +
                    "inner join kumparaturi.shopping_list sl on sl.id = slu.shopping_list_id\n" +
                    "where slu.user_id = :user_id\n" +
                    "and sl.user_id != :user_id\n" +
                    "order by isShared";
        Query q = entityManager.createNativeQuery(sql);
        q.unwrap(NativeQuery.class).addScalar("id", StandardBasicTypes.LONG)
                                    .addScalar("name", StandardBasicTypes.STRING)
                                    .addScalar("description", StandardBasicTypes.STRING)
                                    .addScalar("photo", StandardBasicTypes.STRING)
                                    .addScalar("isShared", StandardBasicTypes.LONG);
        q.setParameter("user_id", currentUser);
        q.unwrap(NativeQuery.class).setResultTransformer(Transformers.aliasToBean(ShoppingListResponseDTO.class));
        List<ShoppingListResponseDTO> resultList = q.getResultList();
        return resultList;
    }

    public ShoppingListResponseDTO addShoppingList(String name, String description, MultipartFile photo) {
        String photoToString = "";
        try {
            photoToString = ImageToBase64.toBase64(photo.getBytes());
        } catch (IOException e) {
            logger.info("Photo exception: " + e.getMessage());
        }
        ShoppingList shoppingList = new ShoppingList();
        shoppingList.setName(name);
        shoppingList.setDescription(description);
        shoppingList.setPhoto(photoToString);
        shoppingList.setUser(new User(SecurityUtils.getCurrentUserLogin().getId()));
        shoppingListRepository.save(shoppingList);

        ShoppingListResponseDTO shoppingListResponseDTO = new ShoppingListResponseDTO(shoppingList.getId(), shoppingList.getName(), shoppingList.getDescription(), shoppingList.getPhoto());

        return shoppingListResponseDTO;
    }

    public PayloadResponse editShoppingList(Long id, String shoppingName) {
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ShoppingList", "id", id));
            if (checkShoppingListAccess(shoppingList.getId())) {
                logger.info("User has acces on id: " + id);
                shoppingList.setName(shoppingName);
                shoppingListRepository.save(shoppingList);

                ShoppingListResponseDTO shoppingListResponseDTO = new ShoppingListResponseDTO();
//                shoppingList.getId(), shoppingList.getName();
                shoppingListResponseDTO.setId(shoppingList.getId());
                shoppingListResponseDTO.setName(shoppingList.getName());

                return new PayloadResponse(shoppingListResponseDTO, "ok", "Shopping list have been edited !");
            } else {
                return new PayloadResponse("", "notok", "You don't have the rights to edit !");
            }
        } catch (ResourceNotFoundException r) {
            return new PayloadResponse("", "notok", "Resource not found: " + r.getResourceName());
        }
    }

    public PayloadResponse deleteShoppingList(Long id) {
        if (checkShoppingListAccess(id)) {
            logger.info("User has acces on id: " + id);
            shoppingListRepository.deleteById(id);
            return new PayloadResponse("", "ok", "Shopping list have been edited !");
        } else {
            logger.info("User NOT has acces for delete on id: " + id);
            return new PayloadResponse("", "notok", "You don't have the rights to delete !");
        }
    }

    public String addShareShoppingList(Long id, Long userId) {
        //TODO: need to check if shoppingListId is not owned by the userId, in this case must not add to shared_list
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ShoppingList", "id", id));
            if (checkShoppingListAccess(shoppingList.getId())) {
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                shoppingList.addSharedUser(user);
                shoppingListRepository.save(shoppingList);
                return "success";
            } else {
                return "dont have rights";
            }
        } catch (ResourceNotFoundException r) {
            return "Resource not found";
        }
    }

    public String removeShareShoppingList(Long id, Long userId) {
        try {
            ShoppingList shoppingList = shoppingListRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("ShoppingList", "id", id));
            if (checkShoppingListAccess(shoppingList.getId())) {
                User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
                shoppingList.removeSharedUsers(user);
                shoppingListRepository.save(shoppingList);
                return "success";
            } else {
                return "dont have rights";
            }
        } catch (ResourceNotFoundException r) {
            return "Resource not found";
        }
    }

    public Boolean checkShoppingListAccess(Long shoppingListId) {
        //TODO: de pus aici verificare ca userul sa fie STAPAN pe lista pe care incearca sa o modifice, altfel nu il las
        List<ShoppingListResponseDTO> shoppingListResponseDTOS = this.getShoppingListForUser();

        return shoppingListResponseDTOS.stream().anyMatch(list -> list.getId().equals(shoppingListId));
    }

}
