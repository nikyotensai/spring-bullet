/*
 * Copyright 2002-2019 the original author or authors.
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
 * @since 2022-11-20
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
