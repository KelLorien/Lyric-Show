package lyricshow.services;

/**
 * User: Ben Okun
 * Date: 12/13/12
 */

import lyricshow.data.SongDAO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BackUp {

    //Back up as .txt files only
    public static void BackUpTxt(File dest) throws IOException {
        FileWriter output=null;
        Scanner libScanner = null;
        File songStore = new File(SongDAO.STORAGE_URL, SongDAO.LIBRARY_DIR_NAME);
        File backupDir = new File(dest, SongDAO.LIBRARY_DIR_NAME);
        backupDir.mkdirs();
        try{
            for (String fileName: songStore.list()) {
                output = new FileWriter(new File(backupDir, fileName));
                libScanner = new Scanner(new File(songStore, fileName));
                while (libScanner.hasNext()) {
                    output.write(libScanner.nextLine() + "\n");
                }
                output.close();
                libScanner.close();
            }

            File backupInd = new File(dest, SongDAO.INDEX_FILE_NAME);
            backupInd.createNewFile();
            output = new FileWriter(backupInd);
            libScanner = new Scanner(new File(SongDAO.STORAGE_URL, SongDAO.INDEX_FILE_NAME));
            while (libScanner.hasNext()) {
                output.write(libScanner.nextLine() + "\n");
            }
            output.close();
            libScanner.close();
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (libScanner != null) {
                    libScanner.close();
                }
            } catch (Exception e) {
                //ignore
            }
        }

    }

    //Back up as powerpoint files only
    public static void BackUpPPT(File dest) throws IOException, ParseException {
        PPTBuilder pptb = PPTBuilder.getInstance();
        List<String> titles = SongDAO.getInstance().getAllTitles();
        File target = new File(dest, "powerpoints");
        target.mkdirs();
        for(String title: titles){
            pptb.buildPPT(Arrays.asList(title), target, title);
        }

    }

}
