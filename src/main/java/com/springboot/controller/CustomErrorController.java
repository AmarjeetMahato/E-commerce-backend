package com.springboot.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError() {
        // Provide custom error handling here
        return "customErrorPage";  // Return a view name
    }

    public String getErrorPath() {
        return "/error";
    }
}
