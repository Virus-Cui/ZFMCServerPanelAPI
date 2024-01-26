package cn.mrcsh.zfmcserverpanelapi.config;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.QueueData;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class Cache {
    // 时间<HashMap<类型,次数>>
    public static Queue<QueueData<HashMap<String, Integer>>> resQueue = new LinkedList<>();

    public static HashMap<String, Integer> cacheCount = new HashMap<>();

    static {
        for (int i = 11; i > 0; i--) {
            QueueData<HashMap<String, Integer>> queueData = new QueueData<>();
            Integer minute = Calendar.getInstance().get(Calendar.MINUTE) - 5 * i;
            if (minute < 0) {
                minute += 60;
            }
            queueData.setMinute(minute);
            queueData.setData(new HashMap<>());
            resQueue.add(queueData);
        }
    }
}
