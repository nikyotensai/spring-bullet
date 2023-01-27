package com.github.nikyotensai.bullet.jdbc.annotation;

import org.springframework.aop.support.AopUtils;
import org.springframework.core.MethodClassKey;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author nikyotensai
 */
public class AnnotationConnectionSource implements ConnectionSource, Serializable {


    private final boolean publicMethodsOnly;

    private final Set<ConnectionAnnotationParser> annotationParsers;

    private final Map<MethodClassKey, ConnectionAttr> connectionAttrMap = new HashMap<>();


    public AnnotationConnectionSource() {
        this(true);
    }

    public AnnotationConnectionSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = Collections.singleton(new ConnectionAnnotationParser());
    }

    @Override
    public boolean isCandidateClass(Class<?> targetClass) {
        for (ConnectionAnnotationParser parser : this.annotationParsers) {
            if (parser.isCandidateClass(targetClass)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConnectionAttr getConnectionConfig(Method method, @Nullable Class<?> targetClass) {
        if (method.getDeclaringClass() == Object.class) {
            return null;
        }
        MethodClassKey cacheKey = new MethodClassKey(method, targetClass);
        ConnectionAttr cached = this.connectionAttrMap.get(cacheKey);
        if (cached != null) {
            return cached;
        }
        ConnectionAttr connAttr = computeConnectionAttr(method, targetClass);
        if (connAttr != null) {
            this.connectionAttrMap.put(cacheKey, connAttr);
        }
        return connAttr;
    }

    protected ConnectionAttr computeConnectionAttr(Method method, @Nullable Class<?> targetClass) {
        if (this.publicMethodsOnly && !Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
        ConnectionAttr connAttr = determineConnectionAttr(specificMethod);
        if (connAttr != null) {
            return connAttr;
        }
        connAttr = determineConnectionAttr(specificMethod.getDeclaringClass());
        if (connAttr != null && ClassUtils.isUserLevelMethod(method)) {
            return connAttr;
        }

        if (specificMethod != method) {
            connAttr = determineConnectionAttr(method);
            if (connAttr != null) {
                return connAttr;
            }
            connAttr = determineConnectionAttr(method.getDeclaringClass());
            if (connAttr != null && ClassUtils.isUserLevelMethod(method)) {
                return connAttr;
            }
        }
        return null;
    }

    protected ConnectionAttr determineConnectionAttr(AnnotatedElement element) {
        for (ConnectionAnnotationParser parser : this.annotationParsers) {
            ConnectionAttr attr = parser.parseConnectionAnnotation(element);
            if (attr != null) {
                return attr;
            }
        }
        return null;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof AnnotationConnectionSource)) {
            return false;
        }
        AnnotationConnectionSource otherTas = (AnnotationConnectionSource) other;
        return (this.annotationParsers.equals(otherTas.annotationParsers) &&
                this.publicMethodsOnly == otherTas.publicMethodsOnly);
    }

    @Override
    public int hashCode() {
        return this.annotationParsers.hashCode();
    }

}
