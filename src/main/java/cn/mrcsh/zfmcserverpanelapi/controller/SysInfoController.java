package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.mrcsh.zfmcserverpanelapi.annotation.APISupervisory;
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
    @SaCheckLogin
    @APISupervisory("信息接口")
    public response sysInfo(){
        SysInfoVo sysInfoVo = systemInfo.getInfos();
        return success(sysInfoVo);
    }


    @GetMapping("/settings")
    @SaCheckLogin
    @APISupervisory("信息接口")
    public response sysSettings(){
        return null;
    }
}
