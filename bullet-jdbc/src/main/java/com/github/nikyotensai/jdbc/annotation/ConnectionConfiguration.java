package com.github.nikyotensai.jdbc.annotation;

import com.github.nikyotensai.jdbc.DataSourceBeanPostProcessor;
import com.github.nikyotensai.jdbc.interceptor.BeanFactoryConnectionSourceAdvisor;
import com.github.nikyotensai.jdbc.interceptor.ConnectionInterceptor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.annotation.Role;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

/**
 * @author nikyotensai
 */
@Configuration(proxyBeanMethods = false)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class ConnectionConfiguration implements ImportAware {

    @Nullable
    protected AnnotationAttributes enableConnectionManagement;

    @Override
    public void setImportMetadata(AnnotationMetadata importMetadata) {
        this.enableConnectionManagement = AnnotationAttributes.fromMap(
                importMetadata.getAnnotationAttributes(EnableConnectionManagement.class.getName(), false));
        if (this.enableConnectionManagement == null) {
            throw new IllegalArgumentException(
                    "@EnableConnectionManagement is not present on importing class " + importMetadata.getClassName());
        }
    }


    @Bean
    @Role(BeanDefinition.ROLE_INFRASTRUCTURE)
    public BeanFactoryConnectionSourceAdvisor connectionSourceAdvisor(
            ConnectionSource connectionSource, ConnectionInterceptor connectionInterceptor) {

        BeanFactoryConnectionSourceAdvisor advisor = new BeanFactoryConnectionSourceAdvisor();
        advisor.setConnectionSource(connectionSource);
        advisor.setAdvice(connectionInterceptor);
        if (this.enableConnectionManagement != null) {
            advisor.setOrder(this.enableConnectionManagement.<Integer>getNumber("order"));
        } else {
            advisor.setOrder(Ordered.LOWEST_PRECEDENCE - 1);
        }
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
