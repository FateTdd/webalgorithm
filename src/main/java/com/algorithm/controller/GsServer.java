package com.algorithm.controller;

import com.algorithm.utils.GsApplication;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/gsServer")
@Component
public class GsServer {
    Session currentSession=null;//The current link
    GsApplication gsApplication=null;
    /**
     * 连接成功
     *
     * @param session
     */
    @OnOpen
    public void onOpen(Session session) {
        currentSession=session;
        System.out.println("The connection is successful");
    }

    /**
     * 连接关闭
     *
     * @param session
     */
    @OnClose
    public void onClose(Session session) {

        System.out.println("Connection is closed");
    }
}
