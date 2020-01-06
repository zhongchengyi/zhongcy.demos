package zhongcy.demos;

import org.apache.poi.ooxml.POIXMLDocumentPart;
import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;
import org.apache.poi.xslf.usermodel.*;
import sun.text.normalizer.Replaceable;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class PPTDemo {

    @SuppressWarnings("unused")
    public void run() {
        try {
            SlideShow slideShow = SlideShowFactory.create(new File("./res/1.pptx"));

            // 获取所有的幻灯片
            List slides = slideShow.getSlides();

            // 获取第一页幻灯片
            XSLFSlide slider = (XSLFSlide) slides.get(0);

            // 获取所有的备注
            XSLFNotes notes = slider.getNotes();

            // 获取所有的批注
            List<XSLFComment> comments = slider.getComments();

            // 获取所有的形状
            List<XSLFShape> shapes = slider.getShapes();

            // 获取所有的关联部分，包括：备注，批注，图片，图表，母版等
            List<POIXMLDocumentPart.RelationPart> relationParts = slider.getRelationParts();

            for (POIXMLDocumentPart.RelationPart part : relationParts) {
                POIXMLDocumentPart documentPart = part.getDocumentPart();
                // 批注部分， 与 slider.getComments() 对应
                if (documentPart instanceof XSLFComments) {
                    XSLFComments comments1 = (XSLFComments) documentPart;
                }
                // 图表部分，有多个，每个图表一个 XSLFChart
                if (documentPart instanceof XSLFChart) {
                    XSLFChart chart = (XSLFChart) documentPart;
                }

                //  备注部分， 与 slider.getNotes() 相同
                if (documentPart instanceof XSLFNotes) {
                    XSLFNotes notes1 = (XSLFNotes) documentPart;
                    // 文本段落，一行为一段
                    List<List<XSLFTextParagraph>> textParagraphs = notes1.getTextParagraphs();
                    // 第一行的所有文本，包含文本样式
                    List<XSLFTextRun> textRuns = textParagraphs.get(0).get(0).getTextRuns();
                    // 第一行的文本内容
                    String text = textParagraphs.get(0).get(0).getText();
                }

                // 母版，只有一个
                if (documentPart instanceof XSLFSlideLayout) {
                    XSLFSlideLayout relation1 = (XSLFSlideLayout) documentPart;
                }
            }

            for (XSLFShape shape : shapes) {
                if (shape instanceof XSLFTextShape) {
                    // 有文本的 sharpe
                    XSLFTextShape textShape = (XSLFTextShape) shape;
                    // 文本段落，一行为一段
                    List<XSLFTextParagraph> textParagraphs = textShape.getTextParagraphs();
                    // 第一行的所有文本，包含文本样式
                    List<XSLFTextRun> textRuns = textParagraphs.get(0).getTextRuns();
                    // 第一行的文本内容
                    String text = textParagraphs.get(0).getText();
                } else if (shape instanceof XSLFGroupShape) {
                    // 图形组合
                    XSLFGroupShape groupShape = (XSLFGroupShape) shape;
                    // 图形组合下的图形，可以与 slider.getShapes() 获取的list一样操作
                    List<XSLFShape> groupShapeShapes = groupShape.getShapes();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
