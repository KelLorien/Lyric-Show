package examples;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * User: jpipe
 * WeekDate: 10/17/12
 * Time: 4:37 PM
 */
public class PowerPointReader {

    public static void readPowerpoint(File ppt) {
        FileInputStream inputStream = null;
        SlideShow show = null;
        try {
            inputStream = new FileInputStream(ppt);
            show = new SlideShow(inputStream);
        } catch (FileNotFoundException e) {
            System.out.println("could not create file stream!");
            return;
        } catch (IOException e) {
            System.out.println("could not instantiate Slideshow!");
            return;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }

        for (Slide slide : show.getSlides()) {
            System.out.println("==================================");
            TextRun[] textRuns = slide.getTextRuns();
            if (textRuns.length == 0 || textRuns[0].getText().isEmpty()) {
                System.out.println("\nEMPTY\n");
                continue;
            }
            System.out.println("Title: " + textRuns[0].getText().trim() + "\n");
            System.out.println("Lyrics: " + textRuns[1].getText().trim() + "\n");
        }

    }

}
