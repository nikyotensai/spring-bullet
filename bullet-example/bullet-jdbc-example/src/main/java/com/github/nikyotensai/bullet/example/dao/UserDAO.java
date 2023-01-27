package com.github.nikyotensai.bullet.example.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.nikyotensai.bullet.example.entity.User;
import com.github.nikyotensai.bullet.example.mapper.UserMapper;
import com.github.nikyotensai.bullet.jdbc.annotation.Connectional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author nikyotensai
 */
@Component
@Slf4j
public class UserDAO extends ServiceImpl<UserMapper, User> {


    @Connectional(readOnly = true)
    public void testReadOnly() {
        log.info("testReadOnly");
        updateOperation();
    }

    @Connectional
    public void testWrite() {
        log.info("testWrite");
        updateOperation();
    }

    private void updateOperation() {
        try {
            User user = new User();
            user.setId(1);
            user.setName("bullet-jdbc");
            baseMapper.updateById(user);
            log.info("update success");
        } catch (Exception ex) {
            log.error("updateOperation#failed", ex);
        }
    }

}
