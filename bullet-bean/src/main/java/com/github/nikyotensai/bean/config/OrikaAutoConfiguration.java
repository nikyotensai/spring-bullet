package com.github.nikyotensai.bean.config;

import lombok.val;
import ma.glasnost.orika.Converter;
import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author nikyotensai
 */
@Configuration
@EnableConfigurationProperties(OrikaProperties.class)
public class OrikaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(MapperFactory.class)
    public MapperFactory mapperFactory(OrikaProperties orikaProperties,
                                       List<Converter<?, ?>> orikaConverters,
                                       ObjectProvider<OrikaConfigurer> configurerObjectProvider) {
        val orikaMapperFactoryBuilder = new DefaultMapperFactory.Builder();
        configureBuilder(orikaMapperFactoryBuilder, orikaProperties);

        MapperFactory mapperFactory = orikaMapperFactoryBuilder.build();
        // 自动注册converter
        if (!CollectionUtils.isEmpty(orikaConverters)) {
            orikaConverters.forEach(converter -> {
                mapperFactory.getConverterFactory().registerConverter(converter);
            });
        }
        // 支持自定义配置
        configurerObjectProvider.ifAvailable(orikaConfigurer -> orikaConfigurer.configureMapperFactory(mapperFactory));
        return mapperFactory;
    }


    @Bean
    @ConditionalOnMissingBean(MapperFacade.class)
    public MapperFacade mapperFacade(MapperFactory mapperFactory) {
        return mapperFactory.getMapperFacade();
    }


    private void configureBuilder(DefaultMapperFactory.Builder orikaMapperFactoryBuilder, OrikaProperties orikaProperties) {
        orikaMapperFactoryBuilder.useBuiltinConverters(orikaProperties.isUseBuiltinConverters());
        orikaMapperFactoryBuilder.useAutoMapping(orikaProperties.isUseAutoMapping());
        orikaMapperFactoryBuilder.mapNulls(orikaProperties.isMapNulls());
        orikaMapperFactoryBuilder.dumpStateOnException(orikaProperties.isDumpStateOnException());
        orikaMapperFactoryBuilder.favorExtension(orikaProperties.isFavorExtension());
        orikaMapperFactoryBuilder.captureFieldContext(orikaProperties.isCaptureFieldContext());
    }

}
