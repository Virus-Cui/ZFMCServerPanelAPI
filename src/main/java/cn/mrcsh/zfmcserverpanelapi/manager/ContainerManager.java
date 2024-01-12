package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.hutool.core.util.IdUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.WSMessage;
import com.alibaba.fastjson2.JSON;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;

@Component
@Slf4j
public class ContainerManager {
    // 实例ID 实例
    private LinkedHashMap<String, Container> containerLinkedHashMap = new LinkedHashMap<>();

    public void exec(Container container) throws IOException {
        Process exec = Runtime.getRuntime().exec(container.getCmd(), new String[]{}, new File(container.getWorkdir()));
        log.info("运行实例：{}", container.getContainerId());
        container.setProcess(exec);
        container.initStream();
        containerLinkedHashMap.put(container.getContainerId(), container);
        new Thread(() -> {
            try {
                InputStream inputStream = container.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, System.getProperty("sun.jnu.encoding")));
                String str;
                while ((str = reader.readLine()) != null) {
                    sendToWS(container.getContainerId(), str,WSMessageType.LOG);
                }
            } catch (Exception e) {
                log.error("错误", e);
            }

        }).start();
        new Thread(()->{
            while (true){
                if(!exec.isAlive()){
                    sendToWS(container.getContainerId(), "进程退出", WSMessageType.STATUS);
                    break;
                }
            }
        }).start();
    }

    public Container getContainerByContainerId(String id) {
        return containerLinkedHashMap.get(id);
    }

    public void sendToWS(String containerId, String msg, WSMessageType wsMessageType){
        try {
            List<Session> sessions = WebsocketManager.SESSION_POOL.get(containerId);
            if (sessions != null) {
                for (Session session : sessions) {
                    if(session != null){
                        WSMessage wsMessage = new WSMessage();
                        wsMessage.setData(msg);
                        wsMessage.setType(wsMessageType.getCode());
                        session.getAsyncRemote().sendText(JSON.toJSONString(wsMessage));
                        session.getAsyncRemote().flushBatch();
                    }
                }
            }
        }catch (Exception e){
            log.error("向ws客户端发送消息失败",e);
        }

    }
}
