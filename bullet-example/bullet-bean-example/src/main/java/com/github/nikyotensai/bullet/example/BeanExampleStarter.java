package com.github.nikyotensai.bullet.example;

import com.github.nikyotensai.bullet.bean.util.OrikaBeanUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.Date;


@SpringBootApplication
public class BeanExampleStarter {

    public static void main(final String[] args) {
        try (ConfigurableApplicationContext applicationContext = SpringApplication.run(BeanExampleStarter.class, args)) {
            process(applicationContext);
        }
    }

    private static void process(final ConfigurableApplicationContext applicationContext) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setName("nikyotensai");
        userDTO.setBirthday(new Date());
        UserVO userVO = OrikaBeanUtil.map(userDTO, UserVO.class);
        System.out.println(userVO);
    }

}
