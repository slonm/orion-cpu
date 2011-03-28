/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.mihailslobodyanuk.utils.reflect.generics;

import ua.mihailslobodyanuk.utils.Defense;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

/**
 * Методы для извлечения информации о generic типах
 * @author user
 */
//TODO Протестировать
public class GenericReflectUtils {
    private GenericReflectUtils() {
    }

    /**
     * Возвращает актуальный класс, которым наследник параметризовал generic<br>
     * Пример 1:<br>
     * {@code class SuperClass<T extends List>{}}<br>
     * {@code class SubClass extends <Long>}<br>
     * {@code ...}<br>
     * {@code actualParameterClass(SubClass.class, SuperClass.class, 0) вернет Long.class}<br>
     * Пример 2:<br>
     * {@code class SuperClass<T extends List>}<br>
     * {@code { public Class getActualA(){return actualParameterClass(this.getClass(), A.class, 0);}}}<br>
     * {@code class SubClass extends <Long>}<br>
     * {@code A a;}<br>
     * {@code ...}<br>
     * {@code a.getActualA() вернет Long.class}<br>
     * @param <T>
     * @param subClass наследник SuperClass
     * @param superClass generic, актуальный класс параметра которого нужно выяснить
     * @param number номер в порядке объявления
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static <T> Class actualParameterClass(final Class<T> subClass,
            final Class<? super T> superClass, final int number) {
        return finalClassOfTypeParameter(subClass, superClass, number, null, null);
    }

    /**
     * Возвращает актуальный класс, которым наследник параметризовал generic<br>
     * @param <T>
     * @param subClass наследник SuperClass
     * @param argument параметр generic, актуальный класс которого нужно выяснить
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static <T> Class actualParameterClass(final Class<T> subClass, final Type argument) {
        return finalClassOfActualTypeArgument(subClass, argument, null, null);
    }

    /**
     * Возвращает актуальный класс, которым наследник параметризовал generic<br>
     * Пример 1:<br>
     * {@code class SuperClass<T>{}}<br>
     * {@code class SubClass<V> extends SuperClass<V>}<br>
     * {@code ...}<br>
     * {@code actualParameterClass(SubClass.class, SuperClass.class, 0, null) вернет Object.class}<br>
     * Пример 2:<br>
     * {@code class SuperClass<T extends List>{}}<br>
     * {@code class SubClass<V extends List> extends SuperClass<V>}<br>
     * {@code ...}<br>
     * {@code actualParameterClass(SubClass.class, SuperClass.class, 0, List.class) вернет List.class}<br>
     * {@code actualParameterClass(SubClass.class, SuperClass.class, 0, Long.class) вернет List.class так как Long не удовлетворяет диапазону}<br>
     * {@code actualParameterClass(SubClass.class, SuperClass.class, 0, ArrayList.class) вернет ArrayList.class т.к. ArrayList удовлетворяет диапазону}<br>
     * @param <T>
     * @param subClass наследник SuperClass
     * @param superClass generic, актуальный класс параметра которого нужно выяснить
     * @param number номер в порядке объявления
     * @param defaultValue возвращаемое значение, которое будет подставлено, если
     *      удовлетворяет диапазонам параметров, которые указаны при объявлении классов
     *      В случае, если значение defaultValue null метод ведет себя точно так же как
     *      actualParameterClass(Class, Class, int)
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static <T> Class actualParameterClass(final Class<T> subClass,
            final Class<? super T> superClass, final int number, final Class defaultValue) {
        return finalClassOfTypeParameter(subClass, superClass, number, defaultValue, null);
    }

    /**
     * Возвращает актуальный класс, которым наследник параметризовал generic<br>
     * @param <T>
     * @param subClass наследник SuperClass
     * @param argument параметр generic, актуальный класс которого нужно выяснить
     * @param defaultValue возвращаемое значение, которое будет подставлено, если
     *      удовлетворяет диапазонам параметров, которые указаны при объявлении классов
     *      В случае, если значение defaultValue null метод ведет себя точно так же как
     *      actualParameterClass(Class, Type)
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static <T> Class actualParameterClass(final Class<T> subClass,
            final Type argument, final Class defaultValue) {
        return finalClassOfActualTypeArgument(subClass, argument, defaultValue, null);
    }

    /**
     * Возвращает актуальный класс, которым generic наследник параметризовал generic предка<br>
     * @param <T>
     * @param subClass наследник SuperClass
     * @param superClass generic, актуальный класс параметра которого нужно выяснить
     * @param number номер в порядке объявления
     * @param subClassParameters вектор параметров которыми предполагается инициализировать subClass
     *      в случае если невозможно однозначно выяснить актуальный класс параметра предка будет взято
     *      значение нужного параметра из subClassParameters и, если оно
     *      удовлетворяет диапазонам параметров, которые указаны при объявлении классов,
     *      будет подставлено в возвращаемое значение.
     *      В случае, если значение subClassParameters null метод ведет себя точно так же как
     *      actualParameterClass(Class, Type)
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон,
     *      если размерность subClassParameters не соответствует вектору subClass параметров
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static <T> Class actualParameterClassWithSubDefaults(final Class<T> subClass,
            final Class<? super T> superClass, final int number, final Class[] subClassParameters) {
        return finalClassOfTypeParameter(subClass, superClass, number, null, subClassParameters);
    }

    /**
     * Возвращает актуальный класс, которым generic наследник параметризовал generic предка<br>
     * @param subClass наследник SuperClass
     * @param argument параметр generic, актуальный класс которого нужно выяснить
     * @param subClassParameters вектор параметров которыми предполагается инициализировать subClass
     *      в случае если невозможно однозначно выяснить актуальный класс параметра предка будет взято
     *      значение нужного параметра из subClassParameters и, если оно
     *      удовлетворяет диапазонам параметров, которые указаны при объявлении классов,
     *      будет подставлено в возвращаемое значение.
     *      В случае, если значение subClassParameters null метод ведет себя точно так же как
     *      actualParameterClass(Class, Type)
     * @throws IllegalArgumentException если subClass не наследует superClass, если number вышел за диапазон,
     *      если размерность subClassParameters не соответствует вектору subClass параметров
     * @return актуальный класс, которым наследник параметризовал generic
     * @author user
     * @since   JDK1.5
     */
    public static Class actualParameterClassWithSubDefaults(final Class subClass,
            final Type argument, final Class[] subClassParameters) {
        return finalClassOfActualTypeArgument(subClass, argument, null, subClassParameters);
    }
    //cache by key <genericClass, name>
    private static final Map<String, Long> typeParameterByNumber = new HashMap<String, Long>();
    private static final String typeParameterByNumberKeyFormat = "%s.%s";

    /**
     * Возвращает позицию параметра класса genericClass с именем name при объявлении<br>
     * @param genericClass generic класс
     * @param name имя параметра типа, позицию которого нужно выяснить
     * @throws IllegalArgumentException если genericClass или name null
     * @return позиция
     * @author user
     * @since   JDK1.5
     */
    public static int parameterPosition(final Class genericClass, final String name) {
        Defense.notNull(genericClass,"genericClass");
        Defense.notNull(name,"name");
        Long ret = typeParameterByNumber.get(String.format(typeParameterByNumberKeyFormat, genericClass.getName(), name));
        if (ret == null) {
            TypeVariable[] tvs = genericClass.getTypeParameters();
            for (int i = 0; i < genericClass.getTypeParameters().length; i++) {
                if (tvs[i].getName().equals(name)) {
                    ret = Long.valueOf(i);
                }
            }
            if (ret != null) {
                typeParameterByNumber.put(String.format(typeParameterByNumberKeyFormat, genericClass.getName(), name), ret);
                return ret.intValue();
            }
            throw new IllegalArgumentException("parameter with name \"" + name + "\" not found");
        }
        return ret.intValue();
    }

    /**
     *
     * @param method
     * @param subClass
     * @param subClassParameters
     * @return
     */
    public static Class finalMethodGenericReturnType(final Method method, final Class subClass,
            final Class[] subClassParameters) {
        Defense.notNull(method,"method");
        return finalClassOfActualTypeArgument(subClass, method.getGenericReturnType(), method.getReturnType(), subClassParameters);
    }

    /**
     * 
     * @param method
     * @param subClass
     * @param subClassParameters
     * @return
     */
    public static Class[] finalMethodGenericParameterTypes(final Method method,
            final Class subClass, final Class[] subClassParameters) {
        Defense.notNull(method,"method");
        int len = method.getParameterTypes().length;
        Class[] ret = new Class[len];
        for (int i = 0; i <
                len; i++) {
            ret[i] = finalClassOfActualTypeArgument(subClass, method.getGenericParameterTypes()[i], method.getParameterTypes()[i], subClassParameters);
        }

        return ret;
    }

    /**
     *
     * @param typeVariable
     * @param domainClass
     * @return
     */
    public static boolean isAssignableArgumentType(TypeVariable typeVariable, Class domainClass) {
        Defense.notNull(typeVariable,"typeVariable");
        Defense.notNull(domainClass,"domainClass");
        Class bound0 = boundClassOfTypeParameter(typeVariable, null);
        return bound0.isAssignableFrom(domainClass);
    }

    //cache <subClass, superClass, number>
    private static final Map<String, Class> typeParameterClassBySubClassSuperClassNumber = new HashMap<String, Class>();
    private static final String typeParameterClassBySubClassSuperClassNumberKeyFormat = "sub %s, super %s.%d";

    private static <T> Class finalClassOfTypeParameter(final Class<T> subClass,
            final Class<? super T> superClass, final int number, final Class defaultValue, final Class[] subClassParameters) {
        Defense.notNull(superClass,"superClass");
        Defense.assignable(subClass,superClass, "subClass");
        Defense.inBounds(number,0,superClass.getTypeParameters().length, "number");
        Class ret = null;
        if (defaultValue == null && (subClassParameters == null || subClassParameters.length == 0)) {
            typeParameterClassBySubClassSuperClassNumber.get(
                    String.format(typeParameterClassBySubClassSuperClassNumberKeyFormat,
                    subClass.getName(), superClass.getName(), number));
        }
        if (ret == null) {
            if (subClassParameters != null && subClassParameters.length > 0) {
                if (subClassParameters.length != subClass.getTypeParameters().length) {
                    throw new IllegalArgumentException("length subClassParameters not equals length superClass.getTypeParameters()");
                }
                for (int i = 0; i < subClassParameters.length; i++) {
                    if (!boundClassOfTypeParameter(superClass.getTypeParameters()[i], null).isAssignableFrom(subClassParameters[i])) {
                        throw new IllegalArgumentException("classes from subClassParameters out of bounds");
                    }
                }
            }
            ret = finalClassOfTypeParameter0(subClass, superClass, number, defaultValue, subClassParameters);
        }
        if (defaultValue == null && (subClassParameters == null || subClassParameters.length == 0)) {
            typeParameterClassBySubClassSuperClassNumber.put(
                    String.format(typeParameterClassBySubClassSuperClassNumberKeyFormat,
                    subClass.getName(), superClass.getName(), number), ret);
        }
        return ret;
    }

    private static Class finalClassOfTypeParameter0(final Class subClass,
            final Class superClass, final int number, final Class defaultValue, final Class[] subClassParameters) {
        if (subClass != superClass) {
            Type newSubType = subClass.getGenericSuperclass();
            if (newSubType == null) {
                for (int i = 0; i < subClass.getInterfaces().length; i++) {
                    if (superClass.isAssignableFrom(subClass.getInterfaces()[i])) {
                        newSubType = subClass.getGenericInterfaces()[i];
                        break;
                    }
                }
            }
            Class[] newSubClassParameters = null;
            Class newSubClass;
            if (newSubType instanceof ParameterizedType) {
                ParameterizedType newSubPType = (ParameterizedType) newSubType;
                newSubClass = (Class) newSubPType.getRawType();
                int newLen = newSubPType.getActualTypeArguments().length;
                if (newLen > 0) {
                    newSubClassParameters = new Class[newLen];
                    for (int i = 0; i < newLen; i++) {
                        Type argType = newSubPType.getActualTypeArguments()[i];
                        if (subClassParameters != null) {
                            newSubClassParameters[i] = finalClassOfActualTypeArgument(subClass, argType, null, subClassParameters);
                        } else {
                            if (argType instanceof TypeVariable) {
                                newSubClassParameters[i] = boundClassOfTypeParameter((TypeVariable) argType, subClassParameters);
                            } else {
                                newSubClassParameters[i] = finalClassOfActualTypeArgumentExcludeTypeVariable(argType);
                            }
                        }
                    }
                }
            } else {
                newSubClass = (Class) newSubType;
            }
            return finalClassOfTypeParameter0(newSubClass, superClass, number, defaultValue, newSubClassParameters);
        } else {
            TypeVariable paramTypeVar = subClass.getTypeParameters()[number];
            Class bound0 = boundClassOfTypeParameter(paramTypeVar, subClassParameters);
            if (defaultValue != null) {
                return bound0.isAssignableFrom(defaultValue) || bound0 == GenericArrayType.class ? defaultValue : bound0;
            } else {
                return bound0;
            }
        }
    }

    private static Class boundClassOfTypeParameter(
            TypeVariable parameter, final Class[] ownerParameters) {
        Type bound0 = parameter.getBounds()[0];
        Class owner = (Class) parameter.getGenericDeclaration();
        Class ret;

        if (bound0 instanceof TypeVariable) {
            int number = parameterPosition(owner, ((TypeVariable) bound0).getName());
            ret =
                    finalClassOfTypeParameter0(owner, owner, number, null, ownerParameters);
        } else {
            ret = finalClassOfActualTypeArgumentExcludeTypeVariable(bound0);
        }

        int number = parameterPosition(owner, parameter.getName());
        if (ownerParameters != null && ownerParameters.length > 0) {
            return ret.isAssignableFrom(ownerParameters[number]) ? ownerParameters[number] : ret;
        } else {
            return ret;
        }

    }

    //cache <subClass, argument>
    private static Class finalClassOfActualTypeArgument(final Class subClass,
            final Type argument,
            final Class defaultValue,
            final Class[] subClassParameters) {
        Defense.notNull(argument,"argument");
        if (argument instanceof TypeVariable) {
            TypeVariable typeVariable = (TypeVariable) argument;
            return finalClassOfTypeParameter(subClass, (Class) typeVariable.getGenericDeclaration(),
                    parameterPosition((Class)typeVariable.getGenericDeclaration(), typeVariable.getName()), defaultValue, subClassParameters);
        } else {
            Class ret = finalClassOfActualTypeArgumentExcludeTypeVariable(argument);
            if (defaultValue != null) {
                return ret.isAssignableFrom(defaultValue) || ret == GenericArrayType.class ? defaultValue : ret;
            } else {
                return ret;
            }

        }
    }

    private static Class finalClassOfActualTypeArgumentExcludeTypeVariable(final Type argument) {
        Defense.notNull(argument,"argument");

        if (argument instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) argument;
            return (Class) pt.getRawType();
        } else if (argument instanceof GenericArrayType) {
            return GenericArrayType.class;
        } else if (argument instanceof Class) {
            return (Class) argument;
        }

        return null;
    }
}
