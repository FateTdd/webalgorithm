package com.algorithm.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class AdminController {
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    /**
     * Set system homepage
     * @param m
     * @return
     */
    @RequestMapping("/home")
    public String home(Model m, HttpServletRequest request) {

        return "admin/home";
    }

    /**
     * gs homepage
     * @return
     */
    @RequestMapping("/gsHome")
    public String gsHome() {
        return "admin/gs-home";
    }

    /**
     * ges homepage
     * @return
     */
    @RequestMapping("/egsHome")
    public String egsHome() {
        return "admin/egs-home";
    }
}
