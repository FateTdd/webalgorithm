package com.algorithm.controller;

import com.algorithm.utils.FileUtils;
import com.algorithm.utils.MessageResult;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
    public MessageResult saveSuggest(String suggest) {
        FileUtils.saveSuggest(suggest);
        return MessageResult.buildSuccess(true);
    }
    /**
     * Save suggestions to a txt file, the file name is timestamp
     * @param suggest
     * @return
     */
    @RequestMapping("/getAllSuggest.do")
    @ResponseBody
    public MessageResult getAllSuggest(String suggest) {
        return MessageResult.buildSuccess( FileUtils.getAllSuggest());
    }
    /**
     *
     * @param msgStr
     * @return
     */
    @RequestMapping("/saveLog")
    @ResponseBody
    public MessageResult saveLog(String msgStr) {
        return MessageResult.buildSuccess(FileUtils.saveLog(msgStr));
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

    /**
     * download
     * @param fileName
     * @param savePath
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value ="/download.do",method = RequestMethod.POST)
    public String download( String fileName ,String savePath, HttpServletRequest request, HttpServletResponse response){
        response.setContentType("text/html;charset=utf-8");
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        String downLoadPath = savePath;
        //	String downLoadPath =filePath.replaceAll("/", "\\\\\\\\");
        try {
            long fileLength = new File(downLoadPath).length();
            response.setContentType("application/x-msdownload;");
            response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
            response.setHeader("Content-Length", String.valueOf(fileLength));
            bis = new BufferedInputStream(new FileInputStream(downLoadPath));
            bos = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bis != null)
                try {
                    bis.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            if (bos != null)
                try {
                    bos.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
        }
        return null;
    }
}