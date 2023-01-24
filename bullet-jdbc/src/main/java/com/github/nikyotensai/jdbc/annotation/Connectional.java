package com.github.nikyotensai.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author nikyotensai
 * @since 2022-11-20
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Connectional {

    boolean readOnly() default false;

}
