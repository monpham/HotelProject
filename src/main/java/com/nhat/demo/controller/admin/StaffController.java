package com.nhat.demo.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/staff")
public class StaffController {

    @RequestMapping(method = RequestMethod.GET)
    public String showStaffPage() {
        return "/employee/staffPage";
    }
}
