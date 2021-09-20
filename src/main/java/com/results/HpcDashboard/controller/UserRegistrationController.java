package com.results.HpcDashboard.controller;

import com.results.HpcDashboard.dto.UserRegistrationDto;
import com.results.HpcDashboard.models.User;
import com.results.HpcDashboard.services.UserService;
import com.results.HpcDashboard.services.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/register")
public class UserRegistrationController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm(Model model) {
        return "register";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user") @Valid UserRegistrationDto userDto,
                                      BindingResult result, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {

        User existingEmail = userService.findByEmail(userDto.getEmail());
        if (existingEmail != null) {
            result.rejectValue("email", null, "There is already an account registered with this email");
        }
        User existingUserName = userService.findByUserName(userDto.getUserName());
        if (existingUserName != null) {
            result.rejectValue("userName", null, "There is already an account registered with this username");
        }

        if (result.hasErrors()) {
            return "register";
        }
        userServiceImpl.register(userDto, getSiteURL(request));
//        userService.save(userDto);
        return "redirect:/register?success";
    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }

    @GetMapping("/verify")
    public String verifyUser(@Param("code") String code) {
        if (userServiceImpl.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }

    @GetMapping("/approve")
    public String approveUser(@Param("code") String code, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        if (userServiceImpl.approve(code, getSiteURL(request))) {
            return "approval_success";
        } else {
            return "approval_fail";
        }
    }
}
