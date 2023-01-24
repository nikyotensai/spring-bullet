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

package com.github.nikyotensai.jdbc.interceptor;

import com.github.nikyotensai.jdbc.ConnectionManager;
import com.github.nikyotensai.jdbc.annotation.ConnectionAttr;
import com.github.nikyotensai.jdbc.annotation.ConnectionSource;
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
