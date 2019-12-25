package zhongcy.demos;

import org.apache.poi.sl.usermodel.SlideShow;
import org.apache.poi.sl.usermodel.SlideShowFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        // write your code here
        try {
            SlideShow slideShow = SlideShowFactory.create(new File(System.getProperty("user.dir") + "/res/1.pptx"));
            Dimension pageSize = slideShow.getPageSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
