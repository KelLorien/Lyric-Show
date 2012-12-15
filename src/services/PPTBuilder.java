package services;

import controllers.ManageTabController;
import controllers.SlideshowTabController;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * User: jpipe
 * Date: 11/26/12
 */
public class PPTBuilder {
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

    /**
     * Builds a powerpoint file out each song specified by a {@link List} of titles. If the given
     * directory is not writable or is <code>null</code>, it will print to {@link PPTBuilder#DEFAULT_OUTPUT_DIR}.
     * Powerpoint file will have a title equal to today's date.
     * @param titles list of song titles to identify which songs to create a powerpoint out of.
     * @param dir specifies where the created .ppt file will be upon completion.
     * @return <code>true</code> if the .ppt was successful stored in the given directory. Returns <code>false</code>
     * if the given directory could not be written to (in which case, it will be in the default output directory).
     * @throws IOException if there was an exception writing the file to its target directory.
     * @see PPTBuilder#buildPPT(java.util.List)
     */
    public void buildPPT(List<String> titles, File dir) throws IOException {
        if (dir == null || !dir.canWrite()) {
            System.err.println("cannot write to given directory " + dir + ". Writing to default");
            buildPPT(titles);
            return;
        }
        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, dir, DateFormat.getDateInstance().format(new Date()));
    }

    /**
     * Builds the powerpoint with the given title. If the title lacks a .ppt at the end, it
     * will be added automatically. {@link PPTBuilder#buildPPT(java.util.List, java.io.File)}
     *
     * @param titles titles of all songs to be used
     * @param dir directory to be output to
     * @param fileName name that the powerpoint will have
     * @return true if succeeded
     * @throws IOException
     */
    public void buildPPT(List<String> titles, File dir, String fileName) throws IOException {
        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, dir, fileName);
    }

    /**
     * Builds a powerpoint file out each song specified by a {@link List}, saved at the directory
     * specified by {@link PPTBuilder#DEFAULT_OUTPUT_DIR}.
     * @param titles list of song titles to identify which songs to create a powerpoint out of.
     * @return <code>true</code> if the .ppt was successful stored in the default directory.
     * @throws IOException if there was an exception writing the file to its target directory.
     * @see PPTBuilder#buildPPT(java.util.List, java.io.File)
     */
    public void buildPPT(List<String> titles) throws IOException {
        DEFAULT_OUTPUT_DIR.mkdirs();

        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, DEFAULT_OUTPUT_DIR, DateFormat.getDateInstance().format(new Date()));
    }


    private void outputPPT(List<Song> songs, File outputDir, String fileName) throws IOException {
        SlideShow show = new SlideShow();

        for (Song song: songs) {
            if (song.getTitle().equals(SlideshowTabController.BLANK_SLIDE_TITLE)) {
                show.createSlide();
            } else {
                addSongToPPT(show, song);
            }
        }

        writePPT(show, outputDir, fileName);
    }

    private static final int LYRIC_MARGIN = 25;
    private static final int AUTH_WIDTH = 150;
    private static final int AUTH_HEIGHT = 50;
    private static final int TITLE_HEIGHT = 125;
    private static final int COPYRIGHT_HEIGHT = 75;

    private void addSongToPPT (SlideShow show, Song song) {
        for (String lyrics: song.getLyrics().split("\n\n")) {
            Slide slide = show.createSlide();
            slide.addTitle().setText(song.getTitle());
            slide.getTextRuns()[0].getRichTextRunAt(0).setFontSize(36);

            insertTextbox(slide, lyrics, 28, TextShape.AlignCenter,
                    new Rectangle(LYRIC_MARGIN, TITLE_HEIGHT + (int) (.5 * AUTH_HEIGHT),
                    (int) show.getPageSize().getWidth() - (LYRIC_MARGIN * 2),
                    (int) show.getPageSize().getHeight() - (TITLE_HEIGHT + AUTH_HEIGHT)));

            insertTextbox(slide, song.getAuthor(), 12, TextShape.AlignLeft,
                    new java.awt.Rectangle(0, TITLE_HEIGHT - (int) (.5 * AUTH_HEIGHT), AUTH_WIDTH, AUTH_HEIGHT));

            insertTextbox(slide, song.getLyricist(), 12, TextShape.AlignRight,
                    new Rectangle((int) show.getPageSize().getWidth() - AUTH_WIDTH,
                            TITLE_HEIGHT - (int) (.5 * AUTH_HEIGHT), AUTH_WIDTH, AUTH_HEIGHT));

            insertTextbox(slide, song.getCopyright(), 10, TextShape.AlignCenter,
                    new Rectangle(0,(int) show.getPageSize().getHeight() - COPYRIGHT_HEIGHT,
                            (int) show.getPageSize().getWidth(), COPYRIGHT_HEIGHT));
        }
    }

    private void insertTextbox(Slide slide, String line, int font, int align, Rectangle rect) {
        if (line == null || line.length() < 1)
            return;

        TextBox box = new TextBox();
        box.setText(line);
        box.setHorizontalAlignment(align);
        RichTextRun textRun = box.getTextRun().getRichTextRunAt(0);
        textRun.setFontSize(font);
        box.setAnchor(rect);
        slide.addShape(box);
    }

    private void writePPT(SlideShow show, File outputDir, String outputName) throws IOException {
        if (!outputName.endsWith(".ppt")){
            outputName += ".ppt";
        }

        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(new File(outputDir, outputName));
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
        String name = "songs.ppt";
        new File(DEFAULT_OUTPUT_DIR, name).delete();
        DEFAULT_OUTPUT_DIR.mkdir();

        Song song = SongDAO.getTestSong();

        try {
            PPTBuilder.getInstance().outputPPT(Arrays.asList(song), DEFAULT_OUTPUT_DIR, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SongDAO.deleteStuff();
    }
}
