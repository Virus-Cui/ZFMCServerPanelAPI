package cn.mrcsh.zfmcserverpanelapi.task;

import cn.mrcsh.zfmcserverpanelapi.config.Cache;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.QueueData;
import cn.mrcsh.zfmcserverpanelapi.manager.GlobalMessagingManage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.HashMap;

@Component

public class APIExecuteSupervisoryTask {

    @Autowired
    private GlobalMessagingManage globalMessagingManage;

    @Scheduled(cron = "0 */5 * * * ?")
    public void exec(){
        int size = Cache.resQueue.size();
        if(size >= 11){
            Cache.resQueue.remove();
        }
        QueueData<HashMap<String, Integer>> queueData = new QueueData<>();
        queueData.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
        queueData.setData(Cache.cacheCount);
        Cache.resQueue.add(queueData);
        Cache.cacheCount = new HashMap<>();
        for (String type : Cache.types) {
            Cache.cacheCount.put(type, 0);
        }
        globalMessagingManage.sendToPanel("UPDATE_CHART", WSMessageType.UPDATE_CHART);
    }
}
