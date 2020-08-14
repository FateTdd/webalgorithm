package com.algorithm.controller;

import com.algorithm.utils.MessageResult;
import com.algorithm.utils.SendEmail;
import com.algorithm.utils.Suggest;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.GeneralSecurityException;
import java.util.*;

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

    /**
     * create
     * @return
     */
    @RequestMapping("/doCreate")
    @ResponseBody
    public MessageResult doCreate(Integer manNum,Integer womanNum) {
        Map<String,String> retNums=new HashMap<String,String>();
        StringBuilder mansb=new StringBuilder("manNum="+manNum);
        mansb.append("-");
        StringBuilder womansb=new StringBuilder("womanNum="+womanNum);
        womansb.append("-");
        List<Integer> manList=new ArrayList<Integer>();
        for (int i =1;i<=manNum;i++){
            manList.add(i);
        }
        List<Integer> womanList=new ArrayList<Integer>();
        for (int i =1;i<=womanNum;i++){
            womanList.add(i);
        }
        for (int i =0;i<manList.size();i++){
            Collections.shuffle(womanList);
            mansb.append("man"+manList.get(i)+"="+ Joiner.on(",").join(womanList));
            mansb.append("-");
        }
        Collections.sort(womanList);//reorder
        for (int i =0;i<womanList.size();i++){
            Collections.shuffle(manList);
            womansb.append("woman"+womanList.get(i)+"="+Joiner.on(",").join(manList));
            womansb.append("-");
        }
        retNums.put("man",mansb.toString());
        retNums.put("woman", womansb.toString());
        return  MessageResult.buildSuccess(retNums);
    }
}
