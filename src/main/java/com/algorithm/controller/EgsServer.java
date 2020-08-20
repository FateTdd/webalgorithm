package com.algorithm.controller;

import com.algorithm.utils.EgsApplication;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/egsServer")
@Component
public class EgsServer {
    Session currentSession=null;//The current link
    EgsApplication egsApplication=null;
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
}
