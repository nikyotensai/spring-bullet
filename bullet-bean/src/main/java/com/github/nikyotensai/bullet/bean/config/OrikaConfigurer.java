package com.github.nikyotensai.bullet.bean.config;

import ma.glasnost.orika.MapperFactory;

/**
 * @author nikyotensai
 */
public interface OrikaConfigurer {

    /**
     * 配置mapperFactory
     */
    void configureMapperFactory(MapperFactory mapperFactory);

}