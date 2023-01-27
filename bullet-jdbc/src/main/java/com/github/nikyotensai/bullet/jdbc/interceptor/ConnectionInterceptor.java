package com.github.nikyotensai.bullet.jdbc.interceptor;

import com.github.nikyotensai.bullet.jdbc.ConnectionManager;
import com.github.nikyotensai.bullet.jdbc.annotation.ConnectionAttr;
import com.github.nikyotensai.bullet.jdbc.annotation.ConnectionSource;
import lombok.Getter;
import lombok.Setter;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.support.AopUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.io.Serializable;


@SuppressWarnings("serial")
@Getter
@Setter
public class ConnectionInterceptor implements MethodInterceptor, Serializable {

    @Nullable
    private ConnectionSource connectionSource;


    public ConnectionInterceptor() {
    }


    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Class<?> targetClass = (invocation.getThis() != null ? AopUtils.getTargetClass(invocation.getThis()) : null);
        ConnectionSource cs = getConnectionSource();
        Assert.notNull(cs, "ConnectionSource must not be null");
        ConnectionAttr connAttr = cs.getConnectionConfig(invocation.getMethod(), targetClass);
        ConnectionManager.setCurrentConnectionAttrIfAbsent(connAttr);
        try {
            return invocation.proceed();
        } finally {
            ConnectionManager.removeCurrentConnectionAttr();
        }
    }


}
