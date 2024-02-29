package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.mrcsh.zfmcserverpanelapi.entity.vo.SysInfoVo;
import cn.mrcsh.zfmcserverpanelapi.service.SystemInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import oshi.SystemInfo;

@RestController
@RequestMapping("/sys")
@CrossOrigin
public class SysInfoController extends ABaseController {

    @Autowired
    private SystemInfoService systemInfo;

    @GetMapping("info")
    public response sysInfo(){
        SysInfoVo sysInfoVo = systemInfo.getInfos();
        return success(sysInfoVo);
    }
}
