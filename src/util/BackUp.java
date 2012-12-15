package util;		//Not entirely sure which package to put this in, feel free to move it.

/**
 * User: Catloaf
 * Date: 12/13/12
 */

import data.SongDAO;
import data.domain.Song;
import services.PPTBuilder;

import java.io.*;
import java.text.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class BackUp {

    //Back Up all Songs as both Powerpoint files and as .txt files
    public static void BackUpAll(String dest) throws IOException, ParseException {
        BackUpTxt(dest);
        BackUpPPT(dest);
    }

    //Back up as .txt files only
    public static void BackUpTxt(String dest) throws IOException {
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
                    output.write(libScanner.nextLine());
                }
                output.close();
                libScanner.close();
            }

            File backupInd = new File(dest, SongDAO.INDEX_FILE_NAME);
            backupInd.createNewFile();
            output = new FileWriter(backupInd);
            libScanner = new Scanner(new File(SongDAO.STORAGE_URL, SongDAO.INDEX_FILE_NAME));
            while (libScanner.hasNext()) {
                output.write(libScanner.nextLine());
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
    public static void BackUpPPT(String dest) throws IOException, ParseException {
        PPTBuilder pptb = PPTBuilder.getInstance();
        List<String> titles = SongDAO.getInstance().getAllTitles();
        File target = new File(dest, "powerpoints");
        target.mkdirs();
        for(String title: titles){
            Song song = SongDAO.getInstance().getSong(title);
            pptb.buildPPT(Arrays.asList(title), target, title);
        }

    }

}
