package com.algorithm.controller;

import com.algorithm.utils.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ServerEndpoint("/egsServer")
@Component
public class EgsServer {
    Session currentSession=null;
    EgsApplication egsApplication=null;
    /**
     * connection succeeded
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        currentSession=session;
        System.out.println("connection succeeded");
    }

    /**
     * Connection closed
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {
        System.out.println("Connection closed");
    }

    @OnMessage
    public void onMessage(String message, Session session) throws InterruptedException {
        System.out.println(message);
        JSONObject jsonObject = JSONObject.parseObject(message);
//        try {
        int code=jsonObject.getInteger("code");
        switch (code){
            case 1:
                //Start initialization
                String manStr=jsonObject.getString("manStr");
                String womanStr=jsonObject.getString("womanStr");
                egsApplication=new EgsApplication(manStr,womanStr);
                JSONObject msg=new JSONObject();
                msg.put("code",2);
                sendMessage(msg);
                break;
            case 3:
                //Start cycle single man
                while (true){
                    Man freeMan=egsApplication.findFreedomMan();
                    if(freeMan!=null){
                        //Single male found.
                        JSONObject msg3=new JSONObject();
                        msg3.put("code",4);
                        msg3.put("freeMan", freeMan.getName());
                        Thread.sleep(1200l);
                        sendMessage(msg3);
                        searchPartner(freeMan);
                    }else{
                        System.out.println("END=====================All men have partners");
                        break;
                    }
                }
                egsApplication.initAgain();
                if(CheckUtil.hasBlockMatch(egsApplication.allman)==false){
                    //No blocking right

                }else{
                    //Blocking pair

                }

                String filePath="D:\\"+System.currentTimeMillis()+".txt";

                try {
                    File writename = new File(filePath);// Relative path, if not, create a new output. txt file
                    if(!writename.exists()){
                        writename.createNewFile(); // Create new file
                    }
                    BufferedWriter out = new BufferedWriter(new FileWriter(writename));
                    StringBuilder sb=new StringBuilder("");
                    for(int k=0;k<egsApplication.txtList.size();k++){
                        sb.append(egsApplication.txtList.get(k));
                        sb.append("\r\n");
                    }
                    for(Man man:egsApplication.allman){
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
////            session.getBasicRemote().sendText("123333333333333444444444");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
    //match
    public void match(Man man,Woman woman){
        man.setPartner(woman);
        man.setFreedom(false);
        woman.setPartner(man);
        woman.setFreedom(false);

    }
    //Search for a spouse.
    public void searchPartner(Man man) throws InterruptedException {
        Woman firstWoman=man.getPreferWoman()[0];
        JSONObject msg=new JSONObject();
        msg.put("code",7);
        msg.put("msg", man.getName()+" prefer ﬁrst woman>>>"+firstWoman.getName() );
        msg.put("colorMan",man.getCode());
        msg.put("colorWoman",firstWoman.getCode());
        msg.put("codeLine",2);
        Thread.sleep(1200l);
        sendMessage(msg);
        System.out.println(man.getName()+"=========================="+firstWoman.getName());
        if(!firstWoman.isFreedom()){
            //Male m’s favorite woman is not free, and breaks up to return to being single.
            JSONObject msg1=new JSONObject();
            msg1.put("code",7);
            msg1.put("msg", man.getName()+" prefer ﬁrst woman>>>"+firstWoman.getName() );

            msg1.put("codeLine",3);
            Thread.sleep(1200l);
            sendMessage(msg1);
            Man oldMan=firstWoman.getPartner();
            oldMan.setFreedom(true);
            oldMan.setPartner(null);
            firstWoman.setFreedom(true);
            firstWoman.setPartner(null);
            JSONObject msg2=new JSONObject();
            msg2.put("code",8);
            msg2.put("msg", man.getName()+" prefer ﬁrst woman>>>"+firstWoman.getName() );
            msg2.put("codeLine",4);
            msg2.put("man",oldMan.getCode());
            msg2.put("woman",firstWoman.getCode());
            Thread.sleep(1200l);
            sendMessage(msg2);
        }
        //Male m’s favorite female has no spouse.
        JSONObject msg3=new JSONObject();
        msg3.put("code",9);
        msg3.put("msg", man.getName()+" engaged with "+firstWoman.getName() );
        msg3.put("codeLine",5);
        msg3.put("colorMan",man.getCode());
        msg3.put("colorWoman",firstWoman.getCode());
        msg3.put("man",man.getCode());
        msg3.put("woman",firstWoman.getCode());
        Thread.sleep(1200l);
        sendMessage(msg3);
        match(man,firstWoman);//match
        Man[] preferMen=firstWoman.getPreferMan();
        Man[] newPreferMen=new Man[preferMen.length];
        boolean isBack=false;
        JSONObject msg4=new JSONObject();
        msg4.put("code",7);
        msg4.put("msg"," for each  "+firstWoman.getName()+" prefer list " );
        msg4.put("codeLine",6);
        Thread.sleep(1200l);
        sendMessage(msg4);
        for(int i=0;i<preferMen.length;i++){
            if(preferMen[i]==null){
                continue;
            }
            if(isBack){
                //It is the man behind m.
                Woman[] tempWomen=preferMen[i].getPreferWoman();
                System.out.println(man.getName()+"The man behind is being deleted."+preferMen[i].getName());
                List<Woman> newPreWomen=new ArrayList<Woman>();
                for(int j=0;j<tempWomen.length;j++){
                    //Delete firstWoman
                    if(!firstWoman.equals(tempWomen[j])){
                        newPreWomen.add(tempWomen[j]);
                    }
                }
                Woman[] resultWomen=new Woman[newPreWomen.size()];
                preferMen[i].setPreferWoman(newPreWomen.toArray(resultWomen));
                JSONObject msg5=new JSONObject();
                msg5.put("code",11);
                msg5.put("msg","Delete the Man behind "+ man.getName()+ " in the "+ firstWoman.getName()+ " prefer list. And delete " +firstWoman.getName()+" from their prefer list.");
                msg5.put("man", preferMen[i].getCode());
                String preStr="";
                for(int j=0;j<newPreWomen.size();j++){
                    if(j==newPreWomen.size()-1){
                        preStr+=newPreWomen.get(j).getCode();
                    }else{
                        preStr+=newPreWomen.get(j).getCode()+",";
                    }
                }
                msg5.put("preferList", preStr);
                msg5.put("codeLine",7);
                Thread.sleep(1200l);
                sendMessage(msg5);

                //
            }else{
                //Delete the man behind man m
                newPreferMen[i]=preferMen[i];
            }
            if(preferMen[i].equals(man)){

                isBack=true;
            }
        }
        //Update
        firstWoman.setPreferMan(newPreferMen);
        JSONObject msg5=new JSONObject();
        msg5.put("code",10);
        msg5.put("msg"," update  "+firstWoman.getName()+" prefer list " );
        msg5.put("codeLine",7);
        msg5.put("woman",firstWoman.getCode());
        msg5.put("prelist",getCodeStr(firstWoman.getPreferMan()));
        Thread.sleep(1200l);
        sendMessage(msg5);
    }
    public String getCodeStr(Man[] preferMan){
        String codeStr="";
        for(Man tempMan:preferMan){
            if(null!=tempMan){
                codeStr+=tempMan.getCode()+",";
            }
        }
        return codeStr.substring(0, codeStr.length() - 1);
    }
    //Method of sending message
    public void sendMessage(JSONObject msg){
        try {
            currentSession.getBasicRemote().sendText(msg.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
