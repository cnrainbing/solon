/*
 * Copyright 2017-2024 noear.org and authors
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
package org.noear.solon.core.util;

import org.noear.solon.Utils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

/**
 * 泛型处理工具
 *
 * @author 颖
 * @since 1.5
 */
public class GenericUtil {
    /**
     * 分析类型参数
     *
     * <pre><code>
     * public class DemoEventListener extend EventListener<Demo>{ }
     * Class<?>[] tArgs = GenericUtil.resolveTypeArguments(DemoEventListener.class, EventListener.class);
     * assert tArgs[0] == Demo.class
     * </code></pre>
     * @param clazz     类型
     * @param genericIfc 泛型接口
     * */
    public static Class<?>[] resolveTypeArguments(Class<?> clazz, Class<?> genericIfc) {
        for (Type type0 : clazz.getGenericInterfaces()) {
            if (type0 instanceof ParameterizedType) {
                ParameterizedType type = (ParameterizedType) type0;
                Class<?> rawType = (Class<?>) type.getRawType();

                if (rawType == genericIfc || getGenericInterfaces(rawType).contains(genericIfc)) {
                    return Arrays.stream(type.getActualTypeArguments())
                            .map(item -> (Class<?>) item)
                            .toArray(Class[]::new);
                }
            } else if (type0 instanceof Class<?>) {
                Class<?>[] classes = resolveTypeArguments((Class<?>) type0, genericIfc);
                if (classes != null) {
                    return classes;
                }
            }
        }

        Type type1 = clazz.getGenericSuperclass();
        if (type1 instanceof ParameterizedType) {
            ParameterizedType type = (ParameterizedType) type1;
            return Arrays.stream(type.getActualTypeArguments())
                    .map(item -> (Class<?>) item)
                    .toArray(Class[]::new);
        }

        return null;
    }

    /**
     * 获取指定类的所有父类
     *
     * @param clazz 要获取的类
     * @return 所有父类
     */
    private static List<Class<?>> getGenericInterfaces(Class<?> clazz) {
        return getGenericInterfaces(clazz, new ArrayList<>());
    }

    /**
     * 获取指定类的所有父类
     *
     * @param clazz 要获取的类
     * @return 所有父类
     */
    private static List<Class<?>> getGenericInterfaces(Class<?> clazz, List<Class<?>> classes) {
        Type[] interfaces = clazz.getGenericInterfaces();
        for (Type type : interfaces) {
            if (type instanceof ParameterizedType) {
                Class<?> aClass = (Class<?>) ((ParameterizedType) type).getRawType();
                classes.add(aClass);
                for (Type type0 : aClass.getGenericInterfaces()) {
                    if (type0 instanceof ParameterizedType) {
                        Class<?> clazz0 = (Class<?>) ((ParameterizedType) type0).getRawType();
                        classes.add(clazz0);
                        getGenericInterfaces(clazz0, classes);
                    }
                }
            }
        }
        return classes;
    }

    /**
     * 转换为参数化类型
     * */
    public static ParameterizedType toParameterizedType(Type type) {
        ParameterizedType result = null;
        if (type instanceof ParameterizedType) {
            result = (ParameterizedType) type;
        } else if (type instanceof Class) {
            final Class<?> clazz = (Class<?>) type;
            Type genericSuper = clazz.getGenericSuperclass();
            if (null == genericSuper || Object.class.equals(genericSuper)) {
                // 如果类没有父类，而是实现一些定义好的泛型接口，则取接口的Type
                final Type[] genericInterfaces = clazz.getGenericInterfaces();
                if (genericInterfaces != null && genericInterfaces.length > 0) {
                    // 默认取第一个实现接口的泛型Type
                    genericSuper = genericInterfaces[0];
                }
            }
            result = toParameterizedType(genericSuper);
        }
        return result;
    }

    ///////////////////////////

    private static final Map<Type, Map<String, Type>> genericInfoCached = new HashMap<>();

    /**
     * 获取泛型变量和泛型实际类型的对应关系Map
     *
     * @param type 被解析的包含泛型参数的类
     * @return 泛型对应关系Map
     */
    public static Map<String, Type> getGenericInfo(Type type) {
        Map<String, Type> tmp = genericInfoCached.get(type);
        if (tmp == null) {
            Utils.locker().lock();

            try {
                tmp = genericInfoCached.get(type);

                if (tmp == null) {
                    tmp = createTypeGenericMap(type);
                    genericInfoCached.put(type, tmp);
                }
            } finally {
                Utils.locker().unlock();
            }
        }

        return tmp;
    }


    /**
     * 创建类中所有的泛型变量和泛型实际类型的对应关系Map
     *
     * @param type 被解析的包含泛型参数的类
     * @return 泛型对应关系Map
     */
    private static Map<String, Type> createTypeGenericMap(Type type) {
        final Map<String, Type> typeMap = new HashMap<>();

        // 按继承层级寻找泛型变量和实际类型的对应关系
        // 在类中，对应关系分为两类：
        // 1. 父类定义变量，子类标注实际类型
        // 2. 父类定义变量，子类继承这个变量，让子类的子类去标注，以此类推
        // 此方法中我们将每一层级的对应关系全部加入到Map中，查找实际类型的时候，根据传入的泛型变量，
        // 找到对应关系，如果对应的是继承的泛型变量，则递归继续找，直到找到实际或返回null为止。
        // 如果传入的非Class，例如TypeReference，获取到泛型参数中实际的泛型对象类，继续按照类处理
        while (null != type) {
            final ParameterizedType parameterizedType = toParameterizedType(type);
            if(null == parameterizedType){
                break;
            }
            final Type[] typeArguments = parameterizedType.getActualTypeArguments();
            final Class<?> rawType = (Class<?>) parameterizedType.getRawType();
            final TypeVariable[] typeParameters = rawType.getTypeParameters();

            Type value;
            for (int i = 0; i < typeParameters.length; i++) {
                value = typeArguments[i];
                // 跳过泛型变量对应泛型变量的情况
                if(false == value instanceof TypeVariable){
                    typeMap.put(typeParameters[i].getTypeName(), value);
                }
            }

            type = rawType;
        }

        return typeMap;
    }
}