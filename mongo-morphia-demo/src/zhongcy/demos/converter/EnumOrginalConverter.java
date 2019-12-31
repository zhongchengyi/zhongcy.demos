package zhongcy.demos.converter;

import dev.morphia.converters.SimpleValueConverter;
import dev.morphia.converters.TypeConverter;
import zhongcy.demos.util.EnumOriginalProvider;
import zhongcy.demos.util.EnumUtil;


public class EnumOrginalConverter extends TypeConverter implements SimpleValueConverter {

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public Object decode(final Class targetClass, final Object fromDBObject, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        if (fromDBObject == null) {
            return null;
        }

        if (hasEnumOriginalProvider(targetClass)) {
            return EnumUtil.getEnumObject(Long.parseLong(fromDBObject.toString()), targetClass);
        }

        return Enum.valueOf(targetClass, fromDBObject.toString());
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public Object encode(final Object value, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        if (hasEnumOriginalProvider(value.getClass())) {
            return ((EnumOriginalProvider) value).getIdx();
        }

        return getName(((Enum) value));
    }

    private boolean hasEnumOriginalProvider(Class clzz) {
        Class<?>[] interfaces = clzz.getInterfaces();
        if (interfaces.length < 1) {
            return false;
        }
        if (interfaces.length == 1) {
            return interfaces[0] == EnumOriginalProvider.class;
        } else {
            for (Class<?> it : interfaces) {
                if (it == EnumOriginalProvider.class) {
                    return true;
                }
            }
            return false;
        }
    }


    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    protected boolean isSupported(final Class c, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        return c.isEnum();
    }

    private <T extends Enum> String getName(final T value) {
        return value.name();
    }
}
