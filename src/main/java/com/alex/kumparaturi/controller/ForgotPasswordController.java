package com.alex.kumparaturi.controller;

import com.alex.kumparaturi.config.Constants;
import com.alex.kumparaturi.model.PasswordResetToken;
import com.alex.kumparaturi.model.User;
import com.alex.kumparaturi.payload.ApiResponse;
import com.alex.kumparaturi.payload.ResetPasswordDTO;
import com.alex.kumparaturi.repository.PasswordResetTokenRepository;
import com.alex.kumparaturi.repository.UserRepository;
import com.alex.kumparaturi.utils.AdministrationUtils;
import com.alex.kumparaturi.service.ForgotPasswordService;
import com.alex.kumparaturi.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@CrossOrigin("*")
@RestController
@RequestMapping(value = "/api/rp")
public class ForgotPasswordController {
    public static final Logger logger = LoggerFactory.getLogger(ForgotPasswordController.class);

    @Autowired UserRepository userRepository;
    @Autowired UserService userService;
    @Autowired PasswordResetTokenRepository passwordResetTokenRepository;
    @Autowired ForgotPasswordService forgotPasswordService;
    @Autowired PasswordEncoder passwordEncoder;

    @ModelAttribute("resetPasswordForm")
    public ResetPasswordDTO resetPasswordDTO() {
        return new ResetPasswordDTO();
    }

    @PostMapping(value = "/forgot_password")
    public ResponseEntity processForgotPasswordFromMobile(HttpServletRequest request, @RequestBody Map<String, String> requestEmail) {
        String email = requestEmail.get("email");
        Optional<User> user = userRepository.findByEmail(email);

        if (!user.isPresent()) {
            logger.info("Reset token is not valid");
            return new ResponseEntity(new ApiResponse(false, "Email not found in database"), HttpStatus.OK);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken();
            String resetToken = UUID.randomUUID().toString();
            passwordResetToken.setToken(resetToken);
            User userPresent = user.get();
            passwordResetToken.setUser(userPresent);
            passwordResetToken.setExpiryDate(30);
            passwordResetTokenRepository.save(passwordResetToken);

            String resetPasswordURL = AdministrationUtils.getAppBaseUrl(request) + Constants.ALL_RESET_PASSWORD_URL + "?token=" + resetToken;

            forgotPasswordService.sendResetPasswordEmail(userPresent.getEmail(), resetPasswordURL);

            return new ResponseEntity(new ApiResponse(true, "Email sent !"), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/reset_password")
    public ModelAndView displayResetPasswordForm(@RequestParam(name = "token", required = false) String token) {
        ModelAndView modelAndView = new ModelAndView();
        logger.info("Token: " + token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            modelAndView.addObject("error", "The token is not found !");
        } else  if (passwordResetToken.isExired()) {
            modelAndView.addObject("error", "The token is expired !");
        } else {
            modelAndView.addObject("token", passwordResetToken.getToken());
        }

        modelAndView.setViewName("forgot_password/reset_password");
        return modelAndView;
    }

    @PostMapping(value = "/reset_password")
    public ModelAndView handleResetPasswordForm(ModelAndView modelAndView,
                                                @ModelAttribute("resetPasswordForm") @Valid ResetPasswordDTO form,
                                                BindingResult result,
                                                RedirectAttributes redirectAttributes) {
        if (result.hasErrors()){
            logger.info("Has errors");
            redirectAttributes.addFlashAttribute(BindingResult.class.getName() + ".resetPasswordForm", result);
            redirectAttributes.addFlashAttribute("resetPasswordForm", form);
            logger.info("form.getToken(): " + form.getToken());
            String redirect = "redirect:" + Constants.ALL_RESET_PASSWORD_URL + "?token=" + form.getToken();
            modelAndView.setViewName(redirect);
            return modelAndView;
        }
        logger.info("Token from form: " + form.getToken());
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(form.getToken());
        if (passwordResetToken != null) {
            logger.info("PasswordResetToken: " + passwordResetToken.getToken());
            User user = passwordResetToken.getUser();
            String updatedPassword = passwordEncoder.encode(form.getPassword());

            userService.resetPassword(updatedPassword, user.getId());
            passwordResetTokenRepository.delete(passwordResetToken);

            modelAndView.setViewName("forgot_password/success");
        } else {
            modelAndView.addObject("error", "The token is not found in database!");
            modelAndView.setViewName("forgot_password/reset_password");
        }
//        return "redirect:/login?resetSuccess";
        return modelAndView;

    }

}
