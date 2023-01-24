package com.github.nikyotensai.jdbc;

import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import javax.sql.DataSource;

public class DataSourceBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DataSource) {
            ProxyFactory proxyFactory = new ProxyFactory(bean);
            proxyFactory.addAdvice(new DataSourceAdvisor());
            proxyFactory.setProxyTargetClass(true);
            return proxyFactory.getProxy();
        }
        return bean;
    }
}