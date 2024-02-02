package cn.mrcsh.zfmcserverpanelapi.config;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.QueueData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class Cache {
    // 时间<HashMap<类型,次数>>
    public static Queue<QueueData<HashMap<String, Integer>>> resQueue = new LinkedList<>();

    public static HashMap<String, Integer> cacheCount = new HashMap<>();

    public static Queue<Integer> CPUUsage = new ArrayDeque<>();

    public static Queue<Integer> ContainerAlive = new ArrayDeque<>();

    public static Queue<Integer> MemUsage = new ArrayDeque<>();

    static {
        for (int i = 11; i > 0; i--) {
            QueueData<HashMap<String, Integer>> queueData = new QueueData<>();
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE,-5*i);
            queueData.setMinute(new SimpleDateFormat("hh:mm").format(calendar.getTime()));
            queueData.setData(new HashMap<>());
            resQueue.add(queueData);
            CPUUsage.add(0);
            ContainerAlive.add(0);
            MemUsage.add(0);
        }
    }

    public static LinkedList<String> types = new LinkedList<>();


}
