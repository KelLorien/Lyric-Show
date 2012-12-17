package examples;

import javax.swing.*;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;

import org.apache.poi.hslf.usermodel.SlideShow;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;


public class tester {

	public static void main(String args[])
	{
		try {
			SlideShow check = readSlideShow(new File("template.ppt"));
			SlideShow newShow = new SlideShow();
			SlideMaster checkMaster = check.getSlidesMasters()[0];
			Slide newSlide = newShow.createSlide();
			newSlide.setMasterSheet(checkMaster);
			writePPT(newShow,new File(""),"test");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
    private static void writePPT(SlideShow show, File outputDir, String outputName) throws IOException {
        if (!outputName.endsWith(".ppt")){
            outputName += ".ppt";
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(outputDir, outputName));
            show.write(outputStream);
        } finally {
     
        }
    }
    private static SlideShow readSlideShow(File ppt) throws IOException {
        FileInputStream inputStream = null;
        SlideShow show = null;
        try {
            inputStream = new FileInputStream(ppt);
            show = new SlideShow(inputStream);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        return show;
    }
}
