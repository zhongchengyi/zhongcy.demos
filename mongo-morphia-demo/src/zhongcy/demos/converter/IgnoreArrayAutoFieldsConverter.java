package zhongcy.demos.converter;

import dev.morphia.converters.SimpleValueConverter;
import dev.morphia.converters.TypeConverter;

import java.util.regex.Pattern;


/**
 * 忽略 array 自动生成的字段
 */
public class IgnoreArrayAutoFieldsConverter extends TypeConverter implements SimpleValueConverter {

    static final Pattern PATTERN_THIS$ = Pattern.compile("this\\$\\d+");

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public Object decode(final Class targetClass, final Object fromDBObject, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        return null;
    }

    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    public Object encode(final Object value, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        return null;
    }


    @Override
    @SuppressWarnings({"unchecked", "deprecation"})
    protected boolean isSupported(final Class c, final dev.morphia.mapping.MappedField optionalExtraInfo) {
        if (optionalExtraInfo != null) {
            return PATTERN_THIS$.matcher(optionalExtraInfo.getNameToStore()).matches();
        }
        return false;
    }

}
