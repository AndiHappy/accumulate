package com.spring.learn.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class WelcomeController {
    @RequestMapping("/")
    public ModelAndView index(@RequestParam(value = "name", defaultValue = "World") String name) {
        ModelAndView view = new ModelAndView("index");
        view.addObject("searchName", "searchValue" + Math.random() + " " + name);
        return view;
    }
}
