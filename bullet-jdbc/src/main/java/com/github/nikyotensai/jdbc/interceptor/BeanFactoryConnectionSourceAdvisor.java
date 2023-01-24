/*
 * Copyright 2002-2017 the original author or authors.
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

import com.github.nikyotensai.jdbc.annotation.ConnectionSource;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;
import org.springframework.lang.Nullable;

/**
 * @author nikyotensai
 * @since 2022-11-20
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
