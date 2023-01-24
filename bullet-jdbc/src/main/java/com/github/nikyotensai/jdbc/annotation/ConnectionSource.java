package com.github.nikyotensai.jdbc.annotation;

import org.springframework.lang.Nullable;

import java.lang.reflect.Method;

public interface ConnectionSource {

    default boolean isCandidateClass(Class<?> targetClass) {
        return true;
    }

    ConnectionAttr getConnectionConfig(Method method, @Nullable Class<?> targetClass);
}
