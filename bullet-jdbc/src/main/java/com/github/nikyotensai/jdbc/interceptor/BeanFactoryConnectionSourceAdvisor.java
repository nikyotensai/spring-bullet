package com.github.nikyotensai.jdbc.interceptor;

import com.github.nikyotensai.jdbc.annotation.ConnectionSource;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.Nullable;

/**
 * @author nikyotensai
 */
public class BeanFactoryConnectionSourceAdvisor extends AbstractBeanFactoryPointcutAdvisor {


    @Nullable
    private ConnectionSource connectionSource;

    private final ConnectionPointcut pointcut = new ConnectionPointcut() {
        @Override
        protected ConnectionSource getConnectionSource() {
            return connectionSource;
        }
    };


    public void setConnectionSource(@Nullable ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }


    public void setClassFilter(ClassFilter classFilter) {
        this.pointcut.setClassFilter(classFilter);
    }

    @Override
    public Pointcut getPointcut() {
        return this.pointcut;
    }

}
