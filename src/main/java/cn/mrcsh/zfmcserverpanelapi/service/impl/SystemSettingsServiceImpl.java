package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.mrcsh.zfmcserverpanelapi.config.Constance;
import cn.mrcsh.zfmcserverpanelapi.entity.structure.SystemSettings;
import cn.mrcsh.zfmcserverpanelapi.mapper.MapMapper;
import cn.mrcsh.zfmcserverpanelapi.service.MapService;
import cn.mrcsh.zfmcserverpanelapi.service.SystemSettingsService;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemSettingsServiceImpl implements SystemSettingsService {

    @Autowired
    private MapService mapService;

    @Override
    public SystemSettings getSettings() {
        String s = mapService.get(Constance.SYSTEM_SETTINGS);
        return JSON.parseObject(s, SystemSettings.class);
    }

    @Override
    public void saveSettings(SystemSettings systemSettings) {
        mapService.set(Constance.SYSTEM_SETTINGS, JSON.toJSONString(systemSettings));
    }
}
