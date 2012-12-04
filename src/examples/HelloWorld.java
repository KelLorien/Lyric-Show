package examples;

import java.io.*;
import java.util.Scanner;

import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.usermodel.SlideShow;
import org.apache.poi.util.IOUtils;

/**
 * User: jpipe
 * Date: 10/10/12
 * Time: 5:18 PM
 */
public class HelloWorld {

    public static void main(String[] args) {
//        String path;
//        if (args.length > 0) {
//            path = args[0];
//        } else {
//            System.out.println("Enter the fully qualified name of the file to parsed.");
//            path = new Scanner(System.in).nextLine().trim();
//        }
//
//        PowerPointReader.readPowerpoint(new File(path));

        createPowerPoint();
    }

    //this is just an example
    public static void createPowerPoint() {
        String fileName = "pwr.ppt";
        new File(fileName).delete();

        SlideShow show = new SlideShow();

        Slide s1 = show.createSlide();

//        s1.addTitle().setText("Hello There");


        TextBox txt = new TextBox();
        txt.setText("Hello, World!");
        txt.setAnchor(new java.awt.Rectangle(300, 400, 300, 50));

        s1.addShape(txt);

        FileOutputStream outputStream;
        try {
            outputStream = new FileOutputStream(fileName);
        } catch (FileNotFoundException e) {
            System.out.println("Could not create file!");
            return;
        }

        try {
            show.write(outputStream);
        } catch (IOException e) {
            System.out.println("Could not write the file!");
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                //ignore
            }
        }
    }

}
