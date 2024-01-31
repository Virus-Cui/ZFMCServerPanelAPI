package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.WSMessage;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@ServerEndpoint("/message")
@Slf4j
public class GlobalMessagingManage {

    /*
     * 存储在线连接数
     * */
    public static final List<Session> SESSION_POOL = new CopyOnWriteArrayList<>();

    @OnOpen
    public void onOpen(Session session) {
        addToSessionPool(session);
    }


    @OnClose
    public void onClose(Session session) {
        SESSION_POOL.remove(session);
    }

    public void addToSessionPool(Session session) {

        SESSION_POOL.add(session);
    }

    public void sendToPanel(String msg, WSMessageType type){
        WSMessage wsMessage = new WSMessage();
        wsMessage.setData(msg);
        wsMessage.setTime(System.currentTimeMillis());
        wsMessage.setType(type.getCode());
        for (Session session : SESSION_POOL) {
            session.getAsyncRemote().sendText(JSON.toJSONString(wsMessage));
        }
    }
}
