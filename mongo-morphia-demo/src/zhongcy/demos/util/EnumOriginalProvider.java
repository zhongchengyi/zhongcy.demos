package zhongcy.demos.util;

/**
 * enum 的原始数据提供
 */
public interface EnumOriginalProvider {

    default String getName() {
        return null;
    }

    long getIdx();
}



