package zhongcy.demos;

import javafx.scene.control.Slider;
import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.xslf.usermodel.XSLFChart;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PPTDemo {

    public void run() {
        try {
            SlideShow slideShow = SlideShowFactory.create(new File("./res/1.pptx"));

            for (Object o : slideShow.getSlides()) {
                XSLFSlide slider = (XSLFSlide) o;

                // 第一页
                if (slider.getSlideNumber() == 1) {
                    for (POIXMLDocumentPart.RelationPart part : slider.getRelationParts()) {
                        POIXMLDocumentPart documentPart = part.getDocumentPart();
                        // 是图表
                        if (documentPart instanceof XSLFChart) {
                            XSLFChart chart = (XSLFChart) documentPart;

                            // 查看里面的图表数据，才能知道是什么图表
                            CTPlotArea plot = chart.getCTChart().getPlotArea();

                            // 柱状图
                            if (!plot.getBarChartList().isEmpty()) {
                                CTBarChart barChart = plot.getBarChartArray(0);


                                int i = 0;
                                for (CTBarSer ser : barChart.getSerList()) {
                                    SeriesData data = new SeriesData("", new ArrayList<>());
                                    data.value.add(new NameDouble("行1", 2+i));
                                    data.value.add(new NameDouble("行2", 3+i));
                                    data.value.add(new NameDouble("行3", 4+i));
                                    data.value.add(new NameDouble("行4", 5+i));
                                    data.value.add(new NameDouble("行5", 6+i));
                                    updateChartCatAndNum(data, ser.getTx(), ser.getCat(), ser.getVal());
                                    ++i;
                                }
                            }

                            // 饼图
                            else if (!plot.getPieChartList().isEmpty()) {
                                CTPieChart pieChart = plot.getPieChartArray(0);
                                int i = 0;
                                for (CTPieSer ser : pieChart.getSerList()) {
                                    SeriesData data = new SeriesData("", new ArrayList<>());
                                    data.value.add(new NameDouble("行1", 2+i));
                                    data.value.add(new NameDouble("行2", 3+i));
                                    data.value.add(new NameDouble("行3", 4+i));
                                    data.value.add(new NameDouble("行4", 5+i));
                                    data.value.add(new NameDouble("行5", 6+i));
                                    updateChartCatAndNum(data, ser.getTx(), ser.getCat(), ser.getVal());
                                    ++i;
                                }
                            }
                        }
                    }
                }

            }

            try {
                try (FileOutputStream out = new FileOutputStream("./res/o1.pptx")) {
                    slideShow.write(out);
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 更新 chart 的缓存数据
     *
     * @param data          数据
     * @param serTitle      系列的标题缓存
     * @param catDataSource 条目的数据缓存
     * @param numDataSource 数据的缓存
     */
    protected void updateChartCatAndNum(SeriesData data, CTSerTx serTitle, CTAxDataSource catDataSource,
                                        CTNumDataSource numDataSource) {

        // 更新系列标题
        //        serTitle.getStrRef().setF(serTitle.getStrRef().getF()); //
        //        serTitle.getStrRef().getStrCache().getPtArray(0).setV(data.name);

        // TODO cat 也可能是 numRef
        long ptCatCnt = catDataSource.getStrRef().getStrCache().getPtCount().getVal();
        long ptNumCnt = numDataSource.getNumRef().getNumCache().getPtCount().getVal();
        int dataSize = data.value.size();
        for (int i = 0; i < dataSize; i++) {
            NameDouble cellValu = data.value.get(i);
            CTStrVal cat = ptCatCnt > i ? catDataSource.getStrRef().getStrCache().getPtArray(i)
                    : catDataSource.getStrRef().getStrCache().addNewPt();
            cat.setIdx(i);
            cat.setV(cellValu.name);

            CTNumVal val = ptNumCnt > i ? numDataSource.getNumRef().getNumCache().getPtArray(i)
                    : numDataSource.getNumRef().getNumCache().addNewPt();
            val.setIdx(i);
            val.setV(String.format("%.2f", cellValu.value));

        }

        // 更新对应 excel 的range
        catDataSource.getStrRef().setF(
                replaceRowEnd(catDataSource.getStrRef().getF(),
                        ptCatCnt,
                        dataSize));
        numDataSource.getNumRef().setF(
                replaceRowEnd(numDataSource.getNumRef().getF(),
                        ptNumCnt,
                        dataSize));

        // 删除多的
        if (ptNumCnt > dataSize) {
            for (int idx = dataSize; idx < ptNumCnt; idx++) {
                catDataSource.getStrRef().getStrCache().removePt(dataSize);
                numDataSource.getNumRef().getNumCache().removePt(dataSize);
            }
        }
        // 更新个数
        catDataSource.getStrRef().getStrCache().getPtCount().setVal(dataSize);
        numDataSource.getNumRef().getNumCache().getPtCount().setVal(dataSize);
    }

    /**
     * 替换 形如： Sheet1!$A$2:$A$4 的字符
     *
     * @param range
     * @return
     */
    public static String replaceRowEnd(String range, long oldSize, long newSize) {
        Pattern pattern = Pattern.compile("(:\\$[A-Z]+\\$)(\\d+)");
        Matcher matcher = pattern.matcher(range);
        if (matcher.find()) {
            long old = Long.parseLong(matcher.group(2));
            return range.replaceAll("(:\\$[A-Z]+\\$)(\\d+)", "$1" + Long.toString(old - oldSize + newSize));
        }
        return range;
    }

    /**
     * 一个系列的数据
     */
    public static class SeriesData {

        /**
         * value 系列的名字
         */
        public String name;

        public List<NameDouble> value;

        public SeriesData(java.util.List<NameDouble> value) {
            this.value = value;
        }

        public SeriesData(String name, List<NameDouble> value) {
            this.name = name;
            this.value = value;
        }

        public SeriesData() {
        }
    }


    /**
     *
     */
    public class NameDouble {

        public String name;

        /**
         */
        public double value;

        public NameDouble(String name, double value) {
            this.name = name;
            this.value = value;
        }

        @SuppressWarnings("unused")
        public NameDouble() {
        }

    }
}
