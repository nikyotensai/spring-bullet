package com.github.nikyotensai.jdbc.annotation;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;

import java.lang.reflect.AnnotatedElement;

public class ConnectionAnnotationParser {

    public boolean isCandidateClass(Class<?> targetClass) {
        return AnnotationUtils.isCandidateClass(targetClass, Connectional.class);
    }

    public ConnectionAttr parseConnectionAnnotation(AnnotatedElement element) {
        AnnotationAttributes attributes = AnnotatedElementUtils.findMergedAnnotationAttributes(
                element, Connectional.class, false, false);
        if (attributes != null) {
            return parseConnectionAnnotation(attributes);
        } else {
            return null;
        }
    }

    public ConnectionAttr parseConnectionAnnotation(Connectional ann) {
        return parseConnectionAnnotation(AnnotationUtils.getAnnotationAttributes(ann, false, false));
    }

    protected ConnectionAttr parseConnectionAnnotation(AnnotationAttributes attributes) {
        ConnectionAttr connAttr = new ConnectionAttr();
        boolean readOnly = attributes.getBoolean("readOnly");
        connAttr.setReadOnly(readOnly);
        return connAttr;
    }


    @Override
    public boolean equals(@Nullable Object other) {
        return (other instanceof ConnectionAnnotationParser);
    }

    @Override
    public int hashCode() {
        return ConnectionAnnotationParser.class.hashCode();
    }

}
