package cn.mrcsh.zfmcserverpanelapi.service.impl;

import cn.mrcsh.zfmcserverpanelapi.entity.structure.XMap;
import cn.mrcsh.zfmcserverpanelapi.mapper.MapMapper;
import cn.mrcsh.zfmcserverpanelapi.service.MapService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MapServiceImpl implements MapService {

    @Autowired
    private MapMapper mapMapper;

    @Override
    public String get(String key) {
        QueryWrapper<XMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("x_key",key);
        XMap result = mapMapper.selectOne(queryWrapper);
        return result==null?null: result.getXValue();
    }

    @Override
    public void set(String key, String value) {
        String s = get(key);
        if(s == null || s.isEmpty()){
            XMap map = new XMap();
            map.setXKey(key);
            map.setXValue(value);
            mapMapper.insert(map);
        }
    }

    @Override
    public void del(String key) {
        QueryWrapper<XMap> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("x_key",key);
        mapMapper.delete(queryWrapper);
    }
}
