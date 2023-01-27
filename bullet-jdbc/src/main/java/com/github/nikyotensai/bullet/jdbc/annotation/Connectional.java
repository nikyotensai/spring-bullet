package com.github.nikyotensai.bullet.jdbc.annotation;

import java.lang.annotation.*;

/**
 * @author nikyotensai
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Connectional {

    boolean readOnly() default false;

}
