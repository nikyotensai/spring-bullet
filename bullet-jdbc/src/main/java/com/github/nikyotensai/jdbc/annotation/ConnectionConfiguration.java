/*
 * Copyright 2002-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.nikyotensai.jdbc.annotation;

import com.github.nikyotensai.jdbc.DataSourceBeanPostProcessor;
import com.github.nikyotensai.jdbc.interceptor.BeanFactoryConnectionSourceAdvisor;
import com.github.nikyotensai.jdbc.interceptor.ConnectionInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurationSelector;

/**
 * {@code @Configuration} class that registers the Spring infrastructure beans
 * necessary to enable proxy-based annotation-driven transaction management.
 *
 * @author Chris Beams
 * @author Sebastien Deleuze
 * @see EnableTransactionManagement
 * @see TransactionManagementConfigurationSelector
 * @since 3.1
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ConnectionConfiguration {


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryConnectionSourceAdvisor connectionSourceAdvisor(
            ConnectionSource connectionSource, ConnectionInterceptor connectionInterceptor) {

        BeanFactoryConnectionSourceAdvisor advisor = new BeanFactoryConnectionSourceAdvisor();
        advisor.setConnectionSource(connectionSource);
        advisor.setAdvice(connectionInterceptor);
        advisor.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        return advisor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ConnectionSource connectionSource() {
        return new AnnotationConnectionSource();
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public ConnectionInterceptor connectionInterceptor(ConnectionSource connectionSource) {
        ConnectionInterceptor interceptor = new ConnectionInterceptor();
        interceptor.setConnectionSource(connectionSource);
        return interceptor;
    }

    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public DataSourceBeanPostProcessor dataSourceProcessor() {
        return new DataSourceBeanPostProcessor();
    }

}
