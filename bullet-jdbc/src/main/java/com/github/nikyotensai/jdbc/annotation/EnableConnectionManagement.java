
package com.github.nikyotensai.jdbc.annotation;

import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import java.lang.annotation.*;

/**
 * @author nikyotensai
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ConnectionConfiguration.class)
public @interface EnableConnectionManagement {

    boolean proxyTargetClass() default false;

    int order() default Ordered.LOWEST_PRECEDENCE - 1;

}
