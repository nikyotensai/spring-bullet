package com.github.nikyotensai.bean;


import com.github.nikyotensai.bean.util.SpringUtil;

/**
 * @author nikyotensai
 */
public interface BeanContext {

    @SuppressWarnings("unchecked")
    default <T> T getThis() {
        return (T) SpringUtil.getBean(this.getClass());
    }


    default <T> T getBean(Class<T> clazz) {
        return SpringUtil.getBean(clazz);
    }

}
