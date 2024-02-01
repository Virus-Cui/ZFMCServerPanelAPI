package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.config.Cache;
import cn.mrcsh.zfmcserverpanelapi.entity.enums.WSMessageType;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.QueueData;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.EchartsVo;
import cn.mrcsh.zfmcserverpanelapi.manager.ContainerManager;
import cn.mrcsh.zfmcserverpanelapi.manager.GlobalMessagingManage;
import cn.mrcsh.zfmcserverpanelapi.utils.SysOccUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/supervisory")
public class APISupervisoryController extends ABaseController {

    @Autowired
    private GlobalMessagingManage globalMessagingManage;

    @Autowired
    private ContainerManager containerManager;

    @GetMapping("/load")
    @APISupervisory("监控接口")
    public response load() {
        Map<String, Object> resultMap = new HashMap<>();
        Integer minute = Calendar.getInstance().get(Calendar.MINUTE);
        EchartsVo echartsVo = new EchartsVo();
        Queue<QueueData<HashMap<String, Integer>>> queueData = new LinkedList<>(Cache.resQueue);
        QueueData<HashMap<String, Integer>> qd = new QueueData<>();
        qd.setData(Cache.cacheCount);
        qd.setMinute(minute);
        queueData.add(qd);
        LinkedList<String> xAxis = new LinkedList<>();
        LinkedList<String> types = new LinkedList<>();
        for (QueueData<HashMap<String, Integer>> queueDatum : queueData) {

            xAxis.add(String.valueOf(queueDatum.getMinute())+"分钟前");
            types.addAll(queueDatum.getData().keySet());
        }

        xAxis.removeLast();
        xAxis.add("现在");


        // 某个接口 全部时间点所有数据
        List<EchartsVo.DataStructure> dataStructures = new ArrayList<>();
        LinkedHashMap<String, List<Integer>> hashMap = new LinkedHashMap<>();

        for (QueueData<HashMap<String, Integer>> queueDatum : queueData) {
            for (Map.Entry<String, Integer> entry : queueDatum.getData().entrySet()) {
                hashMap.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());
                hashMap.get(entry.getKey()).add(entry.getValue());
            }
        }

        for (Map.Entry<String, List<Integer>> entry : hashMap.entrySet()) {
            EchartsVo.DataStructure dataStructure = new EchartsVo.DataStructure();
            Queue<Integer> queue = new ArrayDeque<>();
            if (entry.getValue().size() <= 12) {
                for (int i = 0; i < 12; i++) {
                    queue.add(0);
                }
            }
            for (Integer i : entry.getValue()) {
                queue.remove();
                queue.add(i);
            }
            dataStructure.setName(entry.getKey());
            dataStructure.setData(queue);
            dataStructure.setType("line");
            dataStructures.add(dataStructure);
        }


        echartsVo.setXAxis(xAxis);
        echartsVo.setTypes(types);
        echartsVo.setData(dataStructures);

        resultMap.put("apis", echartsVo);
        // CPU占用
        EchartsVo cpu = new EchartsVo();
        cpu.setXAxis(xAxis);
        cpu.setTypes(List.of("CPU"));
        EchartsVo.DataStructure dataStructure = new EchartsVo.DataStructure();
        dataStructure.setType("line");
        dataStructure.setName("CPU占用率");
        dataStructure.setAreaStyle("");
        Queue<Integer> cpuUsage = new ArrayDeque<>(Cache.CPUUsage);
        //TODO 获取CPU实时占用
        cpuUsage.add(SysOccUtils.getCPU());
        dataStructure.setData(cpuUsage);
        cpu.setData(List.of(dataStructure));
        resultMap.put("cpu",cpu);
        // 内存占用
        EchartsVo mem = new EchartsVo();
        mem.setXAxis(xAxis);
        mem.setTypes(List.of("MEM"));
        EchartsVo.DataStructure memData = new EchartsVo.DataStructure();
        memData.setType("line");
        memData.setName("内存占用率");
        memData.setAreaStyle("");
        Queue<Integer> memUsage = new ArrayDeque<>(Cache.MemUsage);
        //TODO 获取内存实时占用
        memUsage.add(SysOccUtils.getMem());
        memData.setData(memUsage);
        mem.setData(List.of(memData));
        resultMap.put("mem", mem);
        // 存活实例
        EchartsVo ac = new EchartsVo();
        ac.setXAxis(xAxis);
        ac.setTypes(List.of("实例"));
        EchartsVo.DataStructure acData = new EchartsVo.DataStructure();
        acData.setType("line");
        acData.setName("在线实例实例");
        acData.setAreaStyle("");
        Queue<Integer> acAliveQueue = new ArrayDeque<>(Cache.ContainerAlive);
        acAliveQueue.add(containerManager.getRunningContainer().size());
        acData.setData(acAliveQueue);
        ac.setData(List.of(acData));
        resultMap.put("container", ac);
        return success(resultMap);
    }

    @GetMapping("test")
    public void test() {
        Cache.cacheCount.put("监控接口", Cache.cacheCount.get("监控接口") + 1);
        globalMessagingManage.sendToPanel("更新图表", WSMessageType.UPDATE_CHART);
    }

}
