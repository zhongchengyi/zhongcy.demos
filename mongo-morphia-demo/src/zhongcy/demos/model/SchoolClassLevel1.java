package zhongcy.demos.model;

/**
 * 班级等级：小班，中班，大班
 */
public enum SchoolClassLevel1 {
    /**
     * 托班
     */
    KINDERGARTEN_NURSERY(1, "托班"),
    /**
     * 小班
     */
    KINDERGARTEN_SMALL(2, "小班"),
    /**
     * 中班
     */
    KINDERGARTEN_MIDDLE(3, "中班"),
    /**
     * 大班
     */
    KINDERGARTEN_LARGE(4, "大班"),;

    long idx;

    String name;

    SchoolClassLevel1(long idx, String name) {
        this.idx = idx;
        this.name = name;
    }

    public long getIdx() {
        return idx;
    }

    public String getName() {
        return name;
    }

}
