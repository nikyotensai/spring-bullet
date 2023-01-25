package com.github.nikyotensai.bean.config;

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