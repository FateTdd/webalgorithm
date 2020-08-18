package com.algorithm.controller;

import com.algorithm.utils.*;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.*;
import java.util.*;

@ServerEndpoint("/gsServer")
@Component
public class GsServer {
    Session currentSession=null;//The current link
    GsApplication gsApplication=null;
    /**
     * The connection is successful
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        currentSession=session;
        System.out.println("The connection is successful");
    }

    /**
     * Connection is closed
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {

        System.out.println("Connection is closed");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException {
        System.out.println(message);
        JSONObject jsonObject = JSONObject.parseObject(message);
        int code=jsonObject.getInteger("code");
        switch (code){
            case 1:
                //Initializing
                String manStr=jsonObject.getString("manStr");
                String womanStr=jsonObject.getString("womanStr");
                gsApplication=new GsApplication(manStr,womanStr);
                JSONObject msg=new JSONObject();
                msg.put("code",2);
                sendMessage(msg);
                break;
            case 3:
                //Start the cycle of single man
                while (true){
                    Man freeMan=gsApplication.findFreedomMan();
                    if(freeMan!=null){
                        JSONObject msg3=new JSONObject();
                        msg3.put("code",4);
                        msg3.put("freeMan", freeMan.getName());
                        Thread.sleep(1200l);
                        sendMessage(msg3);
                        searchPartner(freeMan,gsApplication.allwoman);
                    }else{
                        System.out.println("END=====================All the men have partners");
                        break;
                    }
                }
                gsApplication.initAgain();
                if(CheckUtil.hasBlockMatch(gsApplication.allman)==false){
                    //No blocking pairs.

                }else{
                    //There are blocking pairs.

                }


                String filePath="D:\\"+System.currentTimeMillis()+".txt";

                try {
                    File writename = new File(filePath);
                    if(!writename.exists()){
                        writename.createNewFile();
                    }
                    BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                    StringBuilder sb=new StringBuilder("");
                    for(int k=0;k<gsApplication.txtList.size();k++){
                        sb.append(gsApplication.txtList.get(k));
                        sb.append("\r\n");
                    }
                    for(Man man:gsApplication.allman){
                        sb.append(man.getName()+"===========engaged=========="+man.getPartner().getName());
                        sb.append("\r\n");
                    }
                    out.write(sb.toString());
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                JSONObject endMsg=new JSONObject();
                endMsg.put("code",99);
                endMsg.put("filePath",filePath);
                sendMessage(endMsg);
                break;
        }

    }

    public void match(Man man, Woman woman){
        try {
            Thread.sleep(1200l);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(woman.isFreedom()){

            man.setPartner(woman);
            man.setFreedom(false);
            woman.setPartner(man);
            woman.setFreedom(false);
            JSONObject msg=new JSONObject();
            msg.put("code",8);
            msg.put("msg", man.getName()+">>>"+woman.getName()+","+man.getName()+" and "+woman.getName()+" is engaged " );
            msg.put("man",man.getCode());
            msg.put("woman",woman.getCode());
            msg.put("colorMan",man.getCode());
            msg.put("colorWoman",woman.getCode());
            msg.put("codeLine",5);
            sendMessage(msg);
        }else{

            man.setPartner(woman);
            man.setFreedom(false);
            Man preMan=woman.getPartner();
            preMan.setFreedom(true);
            preMan.setPartner(null);
            woman.setPartner(man);
            JSONObject msg=new JSONObject();
            msg.put("code",9);
            msg.put("msg", man.getName()+">>>"+woman.getName()+","+man.getName()+" and "+woman.getName()+" is engaged ,and "+preMan.getName() +" is free." );
            msg.put("man",man.getCode());
            msg.put("woman",woman.getCode());
            msg.put("oldman",preMan.getCode());
            msg.put("colorMan",man.getCode());
            msg.put("colorWoman",woman.getCode());
            msg.put("codeLine",9);
            sendMessage(msg);
        }

    }
    //Search for partners
    public void searchPartner(Man man, List<Woman> womanTotal) throws InterruptedException {
        for(Woman tempWoman:man.getPreferWoman()){
            if(tempWoman.isFreedom()){
                //Freedom.together
                JSONObject msg=new JSONObject();
                msg.put("code",7);
                msg.put("msg", man.getName()+">>>"+tempWoman.getName()+","+tempWoman.getName()+" is free" );
                msg.put("codeLine",4);
                msg.put("colorMan",man.getCode());
                msg.put("colorWoman",tempWoman.getCode());
                Thread.sleep(1200l);
                sendMessage(msg);
                match(man,tempWoman);
                break;
            }else{
                //Not free.In a boyfriend
                Man currentMan=tempWoman.getPartner();
                JSONObject msg=new JSONObject();
                msg.put("code",7);
                msg.put("msg", man.getName()+">>>"+tempWoman.getName()+","+tempWoman.getName()+" and "+tempWoman.getPartner().getName()+" is engaged " );
                msg.put("codeLine",6);
                msg.put("colorMan",man.getCode());
                msg.put("colorWoman",tempWoman.getCode());
                Thread.sleep(1200l);
                sendMessage(msg);
                int manOrder=0;
                int currentOrder=0;
                for(int i=0;i<tempWoman.getPreferMan().length;i++){
                    if(tempWoman.getPreferMan()[i].equals(man)){
                        manOrder=i;
                    }
                    if(tempWoman.getPreferMan()[i].equals(currentMan)){
                        currentOrder=i;
                    }
                }
                if(manOrder<currentOrder){
                    //Change partners
                    JSONObject msg1=new JSONObject();
                    msg1.put("code",7);
                    msg1.put("msg", man.getName()+">>>"+tempWoman.getName()+","+man.getName()+"  beat "+tempWoman.getPartner().getName()+"" );
                    msg1.put("codeLine",8);
                    Thread.sleep(1200l);
                    sendMessage(msg1);
                    match(man,tempWoman);
                    break;
                }else{
                    JSONObject msg1=new JSONObject();
                    msg1.put("code",7);
                    msg1.put("msg", man.getName()+">>>"+tempWoman.getName()+","+tempWoman.getPartner().getName()+"  beat "+man.getName()+"" );
                    msg1.put("codeLine",11);
                    Thread.sleep(1200l);
                    sendMessage(msg1);
                }
            }
        }
    }
    //The method of sending a message
    public void sendMessage(JSONObject msg){
        try {
            currentSession.getBasicRemote().sendText(msg.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
