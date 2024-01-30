package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
import cn.mrcsh.zfmcserverpanelapi.config.Cache;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.QueueData;
import cn.mrcsh.zfmcserverpanelapi.entity.vo.EchartsVo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@CrossOrigin
@RequestMapping("/supervisory")
public class APISupervisoryController extends ABaseController {

    @GetMapping("/load")
    @APISupervisory("监控接口")
    public response load() {
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

            xAxis.add(String.valueOf(queueDatum.getMinute()));
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


        return success(echartsVo);
    }

    @GetMapping("test")
    public void test() {
        Cache.cacheCount.put("监控接口", Cache.cacheCount.get("监控接口") + 1);
    }

}
