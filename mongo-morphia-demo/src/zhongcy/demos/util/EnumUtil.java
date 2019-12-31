package zhongcy.demos.util;

import org.apache.calcite.linq4j.Linq4j;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class EnumUtil {

    private static EnumUtil instance = new EnumUtil();

    Impl impl;

    EnumUtil() {
        impl = new Impl();
    }

    /**
     * * 获取value返回枚举对象
     * * @param value name 或 index
     * * @param clazz 枚举类型
     * *
     */
    public static <T extends EnumOriginalProvider> T getEnumObject(long idx, Class<T> clazz) {
        return instance.impl.getEnumObject(idx, clazz);
    }



    public static <T extends EnumOriginalProvider> T getEnumObject(String name, Class<T> clazz) {
        return instance.impl.getEnumObject(name, clazz);
    }

    private class Impl {

        private Map<Class, EnumFeature[]> enumMap;

        public Impl() {
            enumMap = new HashMap<>();
        }

        public <T extends EnumOriginalProvider> T getEnumObject(long value, Class<T> clazz) {
            if (!enumMap.containsKey(clazz)) {
                enumMap.put(clazz, createEnumFeatures(clazz));
            }

            try {
                EnumFeature first = Linq4j.asEnumerable(enumMap.get(clazz))
                        .firstOrDefault(f -> value == f.getIndex());
                if (first != null) {
                    return (T) first.getEnumValue();
                }
            } catch (Exception e) {
            }
            return null;
        }



        public <T extends EnumOriginalProvider> T getEnumObject(String value, Class<T> clazz) {
            if (!enumMap.containsKey(clazz)) {
                enumMap.put(clazz, createEnumFeatures(clazz));
            }

            try {
                EnumFeature first = Linq4j.asEnumerable(enumMap.get(clazz))
                        .firstOrDefault(f -> value.equals(f.getName()) || f.getEnumValue().toString().equals(value));
                if (first != null) {
                    return (T) first.getEnumValue();
                }
            } catch (Exception e) {
            }
            return null;
        }

        @SuppressWarnings("JavaReflectionInvocation")
        private <T extends EnumOriginalProvider> EnumFeature[] createEnumFeatures(Class<T> cls) {
            Method method = null;
            try {
                method = cls.getMethod("values");
                return Linq4j.asEnumerable((EnumOriginalProvider[]) method.invoke(null, (Object[]) null))
                        .select(s -> new EnumFeature(s, s.getName(), s.getIdx())).toList().toArray(new EnumFeature[0]);
            } catch (Exception e) {
                e.printStackTrace();
                return new EnumFeature[0];
            }
        }
    }

    private class EnumFeature {

        Object enumValue;

        String name;

        long index;

        public EnumFeature(Object enumValue, String name, long index) {
            this.enumValue = enumValue;
            this.name = name;
            this.index = index;
        }

        public Object getEnumValue() {
            return enumValue;
        }

        public String getName() {
            return name;
        }

        public long getIndex() {
            return index;
        }
    }
}

