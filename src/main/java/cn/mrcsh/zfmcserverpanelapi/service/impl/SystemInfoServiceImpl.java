package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.mrcsh.zfmcserverpanelapi.entity.vo.SysInfoVo;
import cn.mrcsh.zfmcserverpanelapi.manager.RuntimeManager;
import cn.mrcsh.zfmcserverpanelapi.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SystemInfoServiceImpl implements SystemInfoService {
    @Value("${version}")
    private String version;
    @Autowired
    private RuntimeManager runtimeManager;

    public SystemInfoServiceImpl() {
    }

    public SysInfoVo getInfos() {
        SysInfoVo sysInfoVo = new SysInfoVo();
        sysInfoVo.setDashboardVersion(this.version);
        sysInfoVo.setSysType(this.runtimeManager.getSYSTEM_TYPE());
        String var10001 = System.getProperty("java.specification.version");
        sysInfoVo.setJdkVersion(var10001 + " " + System.getProperty("java.vm.name"));
        sysInfoVo.setSysArch(System.getProperty("os.arch").toUpperCase());
        sysInfoVo.setRunUser(System.getProperty("user.name"));
        sysInfoVo.setJavaHome(System.getProperty("java.home"));
        return sysInfoVo;
    }
}
