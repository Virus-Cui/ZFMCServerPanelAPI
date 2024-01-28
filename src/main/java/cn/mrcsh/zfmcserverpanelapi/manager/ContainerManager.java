package cn.mrcsh.zfmcserverpanelapi.manager;

import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.ContainerStatus;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.Container;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.WSMessage;
import cn.mrcsh.zfmcserverpanelapi.mapper.ContainerMapper;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

@Component
@Slf4j
public class ContainerManager {
    // 实例ID 实例

    @Autowired
    private ContainerMapper containerMapper;
    private final LinkedHashMap<String, Container> containerLinkedHashMap = new LinkedHashMap<>();

    private final String INFO = "<span style='color: green'>%s</span><span style='color: #FFF'>%s</span>";
    private final String WARN = "<span style='color: orange'>%s</span><span style='color: #FFF'>%s</span>";
    private final String ERROR = "<span style='color: red'>%s</span><span style='color: #FFF'>%s</span>";
    private final String NOMALE = "<span style='color: #FFF'>%s</span>";

    public void exec(Container container) throws IOException {
        Container containerByContainerId = getContainerByContainerId(container.getContainerId());
        if(containerByContainerId != null && (containerByContainerId.getStatus().equals(ContainerStatus.STARTING) || containerByContainerId.getStatus().equals(ContainerStatus.RUNNING))){
            return;
        }
        container.setStatus(ContainerStatus.STARTING);
        sendToWS(container.getContainerId(), "STARTING", WSMessageType.STATUS);
        Process exec = Runtime.getRuntime().exec(container.getCmd(), new String[]{}, new File(container.getWorkdir()));
        new Thread(() -> {
            try {
                Thread.sleep(3000);
                container.setStatus(ContainerStatus.RUNNING);
                sendToWS(container.getContainerId(), "RUNNING", WSMessageType.STATUS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        log.info("运行实例：{}", container.getContainerId());
        container.setProcess(exec);
        container.initStream();
        container.setPid(exec.pid());
        container.setQueue(JSON.parseObject(container.getOldlog(), new TypeReference<LinkedList<String>>() {
        }));
        containerLinkedHashMap.put(container.getContainerId(), container);
        if (container.getQueue() == null) {
            container.setQueue(new LinkedList<>());
        }
        new Thread(() -> {
            try {
                InputStream inputStream = container.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, System.getProperty("sun.jnu.encoding")));
                String str;
                while ((str = reader.readLine()) != null) {
                    String log = "";
                    if (str.contains("INFO]:")) {
                        String[] split = str.split("INFO]:");
                        log = String.format(INFO, split[0] + "INFO]:", split[1]);
                        container.setLastType(INFO);
                    } else if (str.contains("WARN]:")) {
                        String[] split = str.split("WARN]:");
                        log = String.format(WARN, split[0] + "WARN]:", split[1]);
                        container.setLastType(WARN);
                    } else if (str.contains("ERROR]:")) {
                        String[] split = str.split("ERROR]:");
                        log = String.format(ERROR, split[0] + "ERROR]:", split[1]);
                        container.setLastType(ERROR);
                    } else {
                        if (container.getLastType() == null) {
                            container.setLastType(NOMALE);
                        }
                        if (container.getLastType().equals(NOMALE)) {
                            log = String.format(NOMALE, str);
                        } else {
                            log = String.format(container.getLastType(), "", str);
                        }
                    }
                    if (container.getQueue().size() >= 500) {
                        container.getQueue().remove();
                    }
                    container.getQueue().add(log);
                    sendToWS(container.getContainerId(), log, WSMessageType.LOG);
                }
            } catch (Exception e) {
                log.error("错误", e);
            }

        }).start();
        new Thread(() -> {
            while (true) {
                if (!exec.isAlive()) {
                    container.setOldlog(JSON.toJSONString(container.getQueue()));
                    containerMapper.updateById(container);
                    containerLinkedHashMap.remove(container.getContainerId());
                    sendToWS(container.getContainerId(), "进程退出", WSMessageType.STATUS);
                    break;
                }

            }
        }).start();
    }

    public Container getContainerByContainerId(String id) {
        return containerLinkedHashMap.get(id);
    }

    @APISupervisory("ws接口")
    public void sendToWS(String containerId, String msg, WSMessageType wsMessageType) {
        try {
            List<Session> sessions = WebsocketManager.SESSION_POOL.get(containerId);
            if (sessions != null) {
                for (Session session : sessions) {
                    if (session != null) {
                        synchronized (session) {
                            WSMessage wsMessage = new WSMessage();
                            wsMessage.setData(msg);
                            wsMessage.setType(wsMessageType.getCode());
                            session.getAsyncRemote().sendText(JSON.toJSONString(wsMessage));
                            session.getAsyncRemote().flushBatch();
                        }
                    }
                }
            }
        } catch (Exception e) {
//            log.error("向ws客户端发送消息失败", e);
        }

    }

    @APISupervisory("ws接口")
    public void sendToSimpleWS(Session session, String msg, WSMessageType wsMessageType) {
        try {
            WSMessage wsMessage = new WSMessage();
            wsMessage.setData(msg);
            wsMessage.setType(wsMessageType.getCode());
            session.getAsyncRemote().sendText(JSON.toJSONString(wsMessage));
            session.getAsyncRemote().flushBatch();
        } catch (Exception e) {
//            log.error("向ws客户端发送消息失败", e);
        }

    }

    public LinkedHashMap<String, Container> getRunningContainer() {
        return containerLinkedHashMap;
    }

}
