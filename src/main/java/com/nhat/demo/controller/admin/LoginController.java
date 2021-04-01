package com.nhat.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

  @GetMapping("/login")
  public String login(Model model) {
    return "/manage/login";
  }

  @GetMapping("/403")
  public String accessDenied() {
    return "/manage/403";
  }

  @GetMapping("/logoutSuccessful")
  public String logoutSuccessful(Model model) {
    return "/manage/login";
  }
}

