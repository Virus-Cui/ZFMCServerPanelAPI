package cn.mrcsh.zfmcserverpanelapi.controller;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController extends ABaseController{

    @PostMapping("/login")
    public response login(String username, String password){
        StpUtil.login(username);
        String tokenValue = StpUtil.getTokenInfo().getTokenValue();
        return success(tokenValue);
    }
}
