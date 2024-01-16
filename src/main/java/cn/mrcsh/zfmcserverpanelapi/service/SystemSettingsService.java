package cn.mrcsh.zfmcserverpanelapi.service;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.SystemSettings;

public interface SystemSettingsService {
    SystemSettings getSettings();

    void saveSettings(SystemSettings systemSettings);
}
