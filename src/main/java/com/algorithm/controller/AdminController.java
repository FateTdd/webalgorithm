package com.algorithm.controller;

import com.algorithm.utils.MessageResult;
import com.algorithm.utils.SendEmail;
import com.algorithm.utils.Suggest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;

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
     * Save suggestions to a txt file, the file name is timestamp
     * @param suggest
     * @return
     */
    @RequestMapping("/saveSuggest.do")
    @ResponseBody
    public MessageResult saveSuggest(String suggest) throws GeneralSecurityException {
            SendEmail.SentEmail(suggest);
        return MessageResult.buildSuccess(true);
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
