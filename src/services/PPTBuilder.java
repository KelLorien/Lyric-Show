package services;

import data.SongDAO;
import data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import util.Preferences;

import java.io.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jpipe
 * Date: 11/26/12
 */
public class PPTBuilder {
    private static final String OUTPUT_NAME = "songs.ppt";
    private static final File DEFAUT_OUTPUT_DIR = new File(Preferences.getPPTOutURL());

    private static PPTBuilder instance = null;

    public static PPTBuilder getInstance() {
        if (instance == null)
            instance = new PPTBuilder();
        return instance;
    }

    private SongDAO dao = SongDAO.getInstance();

    //Private constructor. Use getInstance above to use this class
    private PPTBuilder() {
    }

    public void buildPPT (List<String> titles, File dir) throws IOException {
        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, dir);
    }

    public void buildPPT (List<String> titles) throws IOException {
        DEFAUT_OUTPUT_DIR.mkdirs();

        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, DEFAUT_OUTPUT_DIR);
    }

    private void outputPPT(List<Song> songs, File outputFile) throws IOException {
        SlideShow show = new SlideShow();

        for (Song song: songs) {
            addSongToPPT(show, song);
        }

        writePPT(show, outputFile);
    }

    private void addSongToPPT (SlideShow show, Song song) {
        for (String line: song.getLyrics().split("\n\n")) {
            Slide slide = show.createSlide();
            slide.addTitle().setText(song.getTitle());
            slide.getTextRuns()[0].getRichTextRunAt(0).setFontSize(48);

            TextBox lyrics = new TextBox();
            lyrics.setText(line);
            lyrics.setHorizontalAlignment(TextShape.AlignCenter);
            RichTextRun rtr = lyrics.getTextRun().getRichTextRunAt(0);
            rtr.setFontSize(32);

            lyrics.setAnchor(new java.awt.Rectangle(0, 150,
                    (int) show.getPageSize().getWidth(), (int) show.getPageSize().getHeight() - 150));

            slide.addShape(lyrics);
        }
    }

    private void writePPT(SlideShow show, File outputFile) throws IOException {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(outputFile, OUTPUT_NAME));
            show.write(outputStream);
        } finally {
            closeQuietly(outputStream);
        }
    }

    private void populateSongs(List<String> titles, List<Song> songs) {
        for (String title: titles) {
            try {
                songs.add(dao.getSong(title));
            } catch (FileNotFoundException e) {
                System.err.println("Storage file not found for " + title);
                e.printStackTrace();
            } catch (ParseException e) {
                System.err.println("Error reading storage file for song " + title);
                e.printStackTrace();
            }
        }
    }


    private void closeQuietly(Closeable io) {
        try {
            if (io != null)
                io.close();
        } catch (IOException e) {
            System.err.println("Error closing writer");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
//        new File(DEFAUT_OUTPUT_DIR, OUTPUT_NAME).delete();
        DEFAUT_OUTPUT_DIR.mkdir();

        Song song = new Song();
        song.setTitle("testing");
        song.setLastUsed(1L);
        song.addKeyword("keywuuuuuuuuuuuurd", "another won");
        song.setLyrics("A thousand times I've failed\n" +
                "Still your mercy remains\n" +
                "And should I stumble again\n" +
                "Still I'm caught in your grace\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "\n" +
                "My heart and my soul, I give You control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise, become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Your will above all else, my purpose remains\n" +
                "The art of losing myself in bringing you praise\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "\n" +
                "My heart, my soul, Lord I give you control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "\n" +
                "My Soul cries out to You\n" +
                "My Soul cries out to You\n" +
                "to You, to You\n" +
                "\n" +
                "My heart, my soul, Lord I give you control\n" +
                "Consume me from the inside out Lord\n" +
                "Let justice and praise become my embrace\n" +
                "To love You from the inside out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "\n" +
                "Everlasting, Your light will shine when all else fades\n" +
                "Never ending, Your glory goes beyond all fame\n" +
                "And the cry of my heart is to bring You praise\n" +
                "From the inside out, O my soul cries out\n" +
                "From the inside out, O my soul cries out\n" +
                "From the inside out, O my soul cries out.");
        try {
            PPTBuilder.getInstance().outputPPT(Arrays.asList(song), DEFAUT_OUTPUT_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
