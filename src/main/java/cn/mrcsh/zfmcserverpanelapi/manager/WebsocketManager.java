package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;

@Component
@ServerEndpoint("/log/{id}")
@Slf4j
public class WebsocketManager {

    /*
     * 存储在线连接数
     * */
    public static final Map<String, List<Session>> SESSION_POOL = new HashMap<>();

    private ContainerManager containerManager = (ContainerManager) BeanManager.getBean("containerManager");

    private ContainerMapper containerMapper = (ContainerMapper) BeanManager.getBean("containerMapper");

    @OnOpen
    public void onOpen(Session session, @PathParam("id") String id) {
        log.info("创建连接:{}", session.getId());
        addToSessionPool(session, id);
        Container container = null;
        // 在存活实例列表中寻找
        container = containerManager.getContainerByContainerId(id);
        if (container == null) {
            container = containerMapper.selectById(id);
            container.setQueue(JSON.parseObject(container.getOldlog(), new TypeReference<LinkedList<String>>() {
            }));
        }
        if (container.getQueue() == null) {
            container.setQueue(new LinkedList<>());
        }
        for (String s : container.getQueue()) {
            try {
                containerManager.sendToSimpleWS(session, s, WSMessageType.LOG);
                containerManager.sendToSimpleWS(session, "stop", WSMessageType.STATUS);
            } catch (Exception e) {

            }
        }
    }


    @OnClose
    public void onClose(Session session) {
        log.warn("连接断开:{}", session.getId());
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
