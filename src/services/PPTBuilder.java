package services;

import data.SongDAO;
import data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;
import util.Preferences;

import java.awt.*;
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
    private static final File DEFAULT_OUTPUT_DIR = new File(Preferences.getPPTOutURL());

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
        DEFAULT_OUTPUT_DIR.mkdirs();

        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, DEFAULT_OUTPUT_DIR);
    }

    private void outputPPT(List<Song> songs, File outputFile) throws IOException {
        SlideShow show = new SlideShow();

        for (Song song: songs) {
            addSongToPPT(show, song);
        }

        writePPT(show, outputFile);
    }

    private static final int AUTH_WIDTH = 90;
    private static final int AUTH_HEIGHT = 50;
    private static final int TITLE_HEIGHT = 150;
    private static final int COPYRIGHT_HEIGHT = 100;

    private void addSongToPPT (SlideShow show, Song song) {
        for (String line: song.getLyrics().split("\n\n")) {
            Slide slide = show.createSlide();
            slide.addTitle().setText(song.getTitle());
            slide.getTextRuns()[0].getRichTextRunAt(0).setFontSize(48);

            insertTextbox(slide, line, 32, TextShape.AlignCenter,
                    new Rectangle(AUTH_WIDTH, TITLE_HEIGHT,
                    (int) show.getPageSize().getWidth() - (2*AUTH_WIDTH),
                    (int) show.getPageSize().getHeight() - COPYRIGHT_HEIGHT));

            insertTextbox(slide, song.getAuthor(), 12, TextShape.AlignLeft,
                    new java.awt.Rectangle(0, TITLE_HEIGHT, AUTH_WIDTH, AUTH_HEIGHT));

            insertTextbox(slide, song.getLyricist(), 12, TextShape.AlignRight,
                    new Rectangle((int) show.getPageSize().getWidth() - AUTH_WIDTH,
                            TITLE_HEIGHT, AUTH_WIDTH, AUTH_HEIGHT));

            insertTextbox(slide, song.getCopyright(), 10, TextShape.AlignCenter,
                    new Rectangle(0,(int) show.getPageSize().getHeight() - COPYRIGHT_HEIGHT,
                            (int) show.getPageSize().getWidth(), COPYRIGHT_HEIGHT));
        }
    }

    private void insertTextbox(Slide slide, String line, int font, int align, Rectangle rect) {
        TextBox box = new TextBox();
        box.setText(line);
        box.setHorizontalAlignment(align);
        RichTextRun textRun = box.getTextRun().getRichTextRunAt(0);
        textRun.setFontSize(font);
        box.setAnchor(rect);
        slide.addShape(box);
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

    private void populateSongs(List<String> titles, List<Song> songs) throws FileNotFoundException {
        for (String title: titles) {
            try {
                songs.add(dao.getSong(title));
            } catch (FileNotFoundException e) {
                System.err.println("Storage file not found for " + title);
                throw e;
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

    //TODO: remove for prod
    public static void main(String[] args) {
        new File(DEFAULT_OUTPUT_DIR, OUTPUT_NAME).delete();
        DEFAULT_OUTPUT_DIR.mkdir();

        Song song = SongDAO.getTestSong();

        try {
            PPTBuilder.getInstance().outputPPT(Arrays.asList(song), DEFAULT_OUTPUT_DIR);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SongDAO.deleteStuff(PPTBuilder.getInstance().dao);
    }
}
