package com.example.vtravelwebapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @GetMapping("/error")
    public ModelAndView handleError() {
        ModelAndView modelAndView = new ModelAndView("error");
        // Add any additional attributes you want to display on the error page
        modelAndView.addObject("errorCode", "404");
        modelAndView.addObject("errorMessage", "An unexpected error occurred.");
        return modelAndView;
    }
}
