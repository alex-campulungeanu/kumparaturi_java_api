package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.config.Constants;
import com.alex.kumparaturi.exception.ResourceNotFoundException;
import com.alex.kumparaturi.model.*;
import com.alex.kumparaturi.payload.ApiResponse;
import com.alex.kumparaturi.payload.JwtAuthenticationResponse;
import com.alex.kumparaturi.payload.LoginRequest;
import com.alex.kumparaturi.repository.RoleRepository;
import com.alex.kumparaturi.repository.SettingTypeRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.security.JwtTokenProvider;
import com.alex.kumparaturi.utils.AdministrationUtils;
import com.alex.kumparaturi.service.UserService;
import com.alex.kumparaturi.utils.AccountRegistrationUtils;
import com.alex.kumparaturi.utils.ImageToBase64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/auth")
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Value("${app.secretWordForRegistration}")
    private String secretWordEnv;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    SettingTypeRepository settingTypeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/signin")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        String jwt = "";
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            //for the moment rememberMe is false
            jwt = jwtTokenProvider.generateToken(authentication, false);

            return ResponseEntity.ok(new JwtAuthenticationResponse("ok", "Success login !", jwt));

        } catch (BadCredentialsException e) {
//            e.printStackTrace();
            return ResponseEntity.ok(new JwtAuthenticationResponse("notok", "Bad username or password !", jwt));
        } catch (DisabledException e) {
            return ResponseEntity.ok(new JwtAuthenticationResponse("notok", "Account is inactive !", jwt));
        }

    }

    @PostMapping(value = "/signup")
    public ResponseEntity<?> signupUser(HttpServletRequest request,
                                        @RequestParam("photo") MultipartFile photo,
                                        @RequestParam("name") String name,
                                        @RequestParam("username") String username,
                                        @RequestParam("email") String email,
                                        @RequestParam("password") String password,
                                        @RequestParam("secretWord") String secretWord,
                                        @RequestParam("sendActivationEmail") boolean sendActivationEmail) throws IOException {
//        logger.info("secretWord entered: " + secretWord);
        if (userRepository.existsByUsername(username)) {
            logger.info("Username exists: " + username);
            return new ResponseEntity(new ApiResponse(false, "Username exists !"), HttpStatus.OK);
        }

        if (userRepository.existsByEmail(email)) {
            logger.info("Email exists: " + email);
            return new ResponseEntity(new ApiResponse(false, "Email exists !"), HttpStatus.OK);
        }

        if (!secretWord.equals(secretWordEnv)) {
            logger.info("Secret word entered: " + secretWord);
            return new ResponseEntity(new ApiResponse(false, "You didn't guess secret word !"), HttpStatus.OK);
        }

        //save image as base64
        //TODO: check if image is only jpg or png, probably must be checekd in FE
        String photoToString = ImageToBase64.toBase64(photo.getBytes());

        //if i don't send activation email then account will be activated automatically
        Long active;
        if (sendActivationEmail) {
            active = 0L;
        } else {
            active = 1L;
        }
        String activationToken = AccountRegistrationUtils.generateActivationToken();
        String activationURL = AdministrationUtils.getAppBaseUrl(request) + Constants.ALL_ACTIVATE_ACCOUNT_URL;
        logger.info("Create account activationURL: " + activationURL);

        User user = new User(name, username, email, password, photoToString, active, activationToken);
        user.setPassword(passwordEncoder.encode(password)); //TODO: suprascriu parola din constructorul de mai sus, ar trebui sa fac un set pentru fiecare atribut al clasei

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new RuntimeException("Role not exists !"));
        user.setRoles(Collections.singleton(userRole));

        //add default setings per user
        UserSetting userSetting1 = new UserSetting();
        SettingType settingType1 =settingTypeRepository.findById(Constants.SETTING_TYPE_SEND_NOTIFICATION).orElseThrow(() -> new ResourceNotFoundException("SettingType", "id", Constants.SETTING_TYPE_SEND_NOTIFICATION));
        userSetting1.setUser(user);
        userSetting1.setSettingType(settingType1);
        userSetting1.setSettingValueNumber(1L);
        user.getUserSettings().add(userSetting1);

        UserSetting userSetting2 = new UserSetting();
        SettingType settingType2 =settingTypeRepository.findById(Constants.SETTING_TYPE_RECEIVED_NOTIFICATION).orElseThrow(() -> new ResourceNotFoundException("SettingType", "id", Constants.SETTING_TYPE_RECEIVED_NOTIFICATION));
        userSetting2.setUser(user);
        userSetting2.setSettingType(settingType2);
        userSetting2.setSettingValueNumber(1L);
        user.getUserSettings().add(userSetting2);

        userRepository.save(user);

        //send account activation email
        boolean activationEmailSent;
        if (sendActivationEmail) {
            logger.info("Will send activation email");
            activationEmailSent = userService.sendAccountActivationEmail(user.getEmail(), activationURL);
        } else {
            logger.info("Will NOT send activation email");
            activationEmailSent = false;
        }

        if (activationEmailSent) {
            return new ResponseEntity(new ApiResponse(true, "User created and mail send!"), HttpStatus.OK);
        } else {
            return new ResponseEntity(new ApiResponse(true, "User created but mail NOT send!"), HttpStatus.OK);
        }
    }

    @GetMapping(value = Constants.ACTIVATE_ACCOUNT_URL + "/{activationToken}")
    public ModelAndView activatAccount(ModelAndView modelAndView, @PathVariable("activationToken") String activationToken) {
        Boolean isAccountActivate = userService.activateAccount(activationToken);
        if (isAccountActivate) {
            logger.info("Account is activated");
            modelAndView.addObject("message", "Congratulations ! The account is activated !");
        } else {
            logger.info("Account is NOT activated");
            modelAndView.addObject("message", "The Activation Token is invalid");
        }

        modelAndView.setViewName("account_verified");
        return modelAndView;
    }
}
