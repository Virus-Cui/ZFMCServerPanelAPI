package cn.mrcsh.zfmcserverpanelapi.manager;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@ServerEndpoint("/log/{id}")
@Slf4j
public class WebsocketManager {

    /*
     * 存储在线连接数
     * */
    public static final Map<String, List<Session>> SESSION_POOL = new HashMap<>();

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("创建连接:{}",session.getId());
        addToSessionPool(session, id);
    }



    @OnClose
    public void onClose(Session session){
        log.warn("连接断开:{}",session.getId());
        for (Map.Entry<String, List<Session>> entry : SESSION_POOL.entrySet()) {
            entry.getValue().remove(session);
        }
    }

    public void addToSessionPool(Session session, String id) {
        List<Session> sessions = SESSION_POOL.get(id);
        if (sessions != null && !sessions.isEmpty()) {
            sessions.add(session);
        } else {
            sessions = new ArrayList<>();
            sessions.add(session);
            SESSION_POOL.put(id, sessions);
        }
    }
}
