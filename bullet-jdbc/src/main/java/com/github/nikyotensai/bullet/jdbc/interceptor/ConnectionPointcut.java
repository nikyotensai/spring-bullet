package com.github.nikyotensai.bullet.jdbc.interceptor;

import com.github.nikyotensai.bullet.jdbc.annotation.ConnectionSource;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author nikyotensai
 */
@SuppressWarnings("serial")
public abstract class ConnectionPointcut extends StaticMethodMatcherPointcut implements Serializable {

    protected ConnectionPointcut() {
        setClassFilter(new ConnectionClassFilter());
    }


    @Override
    public boolean matches(Method method, Class<?> targetClass) {
        ConnectionSource connectionSource = getConnectionSource();
        return connectionSource != null && connectionSource.getConnectionConfig(method, targetClass) != null;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ConnectionPointcut)) {
            return false;
        }
        ConnectionPointcut otherPc = (ConnectionPointcut) other;
        return ObjectUtils.nullSafeEquals(getConnectionSource(), otherPc.getConnectionSource());
    }

    @Override
    public int hashCode() {
        return ConnectionPointcut.class.hashCode();
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + getConnectionSource();
    }


    @Nullable
    protected abstract ConnectionSource getConnectionSource();


    private class ConnectionClassFilter implements ClassFilter {

        @Override
        public boolean matches(Class<?> clazz) {
            ConnectionSource connectionSource = getConnectionSource();
            return connectionSource != null && connectionSource.isCandidateClass(clazz);
        }
    }

}
