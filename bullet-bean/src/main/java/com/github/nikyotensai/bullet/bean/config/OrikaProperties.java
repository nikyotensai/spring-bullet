package com.github.nikyotensai.bullet.bean.config;

import lombok.Data;
import ma.glasnost.orika.impl.DefaultMapperFactory.MapperFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * orika配置，默认值使用
 *
 * @author nikyotensai
 * @see MapperFactoryBuilder
 */
@ConfigurationProperties(prefix = "bullet.orika")
@Data
public class OrikaProperties {
    private boolean useBuiltinConverters = true;
    private boolean useAutoMapping = true;
    private boolean mapNulls = true;
    private boolean dumpStateOnException = false;
    private boolean favorExtension = false;
    private boolean captureFieldContext = false;
}
