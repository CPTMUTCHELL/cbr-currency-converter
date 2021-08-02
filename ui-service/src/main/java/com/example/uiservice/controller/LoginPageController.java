package com.example.uiservice.controller;

import com.example.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ui")

public class LoginPageController   {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/login")
    public String getLoginPage(Model model){
        model.addAttribute("userToLogin", new User());

        model.addAttribute("userToRegister", new User());
        return "startPage";
    }

    @PostMapping("/registration")
    private String registration(@ModelAttribute(name = "userToRegister") User userToRegister,
                                RedirectAttributes ra) {
        try {
            ResponseEntity<User> userResponseEntity = restTemplate.postForEntity("http://localhost:8081/auth/registration", userToRegister, User.class);
        }
        catch (HttpClientErrorException e){
            ra.addFlashAttribute("exist", true);
            return "redirect:/ui/login";
        }

         ra.addFlashAttribute("success", true);

        return "redirect:/ui/login";
    }
//    @PostMapping("/login")
//    private String login(@ModelAttribute(name = "userToLogin") User userToLogin,
//                                RedirectAttributes ra) {
//        System.out.println(userToLogin);
//        try {
//            ResponseEntity<User> userResponseEntity = restTemplate.postForEntity("http://localhost:8081/auth/login", userToLogin, User.class);
//        }
//        catch (HttpClientErrorException e){
//            ra.addFlashAttribute("exist", true);
//            return "redirect:/ui/login";
//        }
//
//        ra.addFlashAttribute("success", true);
//
//        return "redirect:/ui/login";
//    }
}
