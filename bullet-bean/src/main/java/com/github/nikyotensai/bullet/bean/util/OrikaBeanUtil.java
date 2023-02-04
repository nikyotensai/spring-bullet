package com.github.nikyotensai.bullet.bean.util;

import ma.glasnost.orika.MapperFacade;
import ma.glasnost.orika.metadata.Type;
import ma.glasnost.orika.metadata.TypeFactory;

import java.util.List;


@SuppressWarnings("unused")
public class OrikaBeanUtil {

    private static MapperFacade mapper;

    private static MapperFacade getMapper() {
        if (mapper == null) {
            mapper = SpringUtil.getBean(MapperFacade.class);
        }
        return mapper;
    }


    public static <S, D> D map(S sourceObject, Class<D> destinationClass) {
        return getMapper().map(sourceObject, destinationClass);
    }

    public static <S, D> void map(final S sourceObject, final D destinationObject) {
        getMapper().map(sourceObject, destinationObject);
    }

    public static <S, D> void map(final S sourceObject, final D destinationObject, Type<S> sourceType, Type<D> destinationType) {
        getMapper().map(sourceObject, destinationObject, sourceType, destinationType);
    }

    /**
     * If you need higher performance, {@link OrikaBeanUtil#mapAsList(Iterable, Class, Class)} is recommended,
     * as {@link List} has a generic type,getting its type costs much.
     */
    public static <S, D> List<D> mapAsList(Iterable<S> sourceList, Class<D> destinationClass) {
        return getMapper().mapAsList(sourceList, destinationClass);
    }

    public static <S, D> List<D> mapAsList(Iterable<S> sourceList, Class<S> sourceClass, Class<D> destinationClass) {
        return getMapper().mapAsList(sourceList, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    public static <S, D> List<D> mapAsList(Iterable<S> sourceList, Type<S> sourceType, Type<D> destinationType) {
        return getMapper().mapAsList(sourceList, sourceType, destinationType);
    }

    public static <S, D> List<D> mapAsList(S[] source, Class<D> destinationClass) {
        return getMapper().mapAsList(source, destinationClass);
    }

    public static <S, D> List<D> mapAsList(S[] source, Class<S> sourceClass, Class<D> destinationClass) {
        return getMapper().mapAsList(source, TypeFactory.valueOf(sourceClass), TypeFactory.valueOf(destinationClass));
    }

    public static <S, D> D[] mapAsArray(final D[] destination, final S[] source, final Class<D> destinationClass) {
        return getMapper().mapAsArray(destination, source, destinationClass);
    }

    public static <S, D> D[] mapAsArray(D[] destination, S[] source, Type<S> sourceType, Type<D> destinationType) {
        return getMapper().mapAsArray(destination, source, sourceType, destinationType);
    }

    public static <E> Type<E> getType(final Class<E> rawType) {
        return TypeFactory.valueOf(rawType);
    }
}