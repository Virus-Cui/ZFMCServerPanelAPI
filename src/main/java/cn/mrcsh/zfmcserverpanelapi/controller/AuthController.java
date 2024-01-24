package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.stp.StpUtil;
import cn.mrcsh.zfmcserverpanelapi.entity.dto.UserDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin
public class AuthController extends ABaseController{

    @PostMapping("/login")
    public response login(@RequestBody UserDTO userDTO){
        StpUtil.login(userDTO.getUsername());
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        return success(tokenValue);
    }
}
