package services;

import controllers.SlideshowTabController;
import data.SongDAO;
import data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.SlideMaster;
import org.apache.poi.hslf.model.TextBox;
import org.apache.poi.hslf.model.TextShape;
import org.apache.poi.hslf.usermodel.RichTextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

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
//    private static final File DEFAULT_OUTPUT_DIR = new File(Preferences.getPPTOutURL());

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
     * Builds a powerpoint file out each song specified by a {@link List} of titles.
     * Powerpoint file will have a title equal to today's date.
     * @param titles list of song titles to identify which songs to create a powerpoint out of.
     * @param dir specifies where the created .ppt file will be upon completion.
     * if the given directory could not be written to (in which case, it will be in the default output directory).
     * @throws IOException if there was an exception writing the file to its target directory.
     */
    public void buildPPT(List<String> titles, File dir) throws IOException {

        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);
        System.out.println(dir);
        outputPPT(songs, dir, DateFormat.getDateInstance().format(new Date()));
    }

    /**
     * Builds the powerpoint with the given title. If the title lacks a .ppt at the end, it
     * will be added automatically. {@link PPTBuilder#buildPPT(java.util.List, java.io.File)}
     *
     * @param titles titles of all songs to be used
     * @param dir directory to be output to
     * @param fileName name that the powerpoint will have
     * @throws IOException
     */
    public void buildPPT(List<String> titles, File dir, String fileName) throws IOException {
        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, dir, fileName);
    }
/*

     * Builds a powerpoint file out each song specified by a {@link List}, saved at the directory
     * specified by {@link PPTBuilder#DEFAULT_OUTPUT_DIR}.
     * @param titles list of song titles to identify which songs to create a powerpoint out of.
     * @throws IOException if there was an exception writing the file to its target directory.
     * @see PPTBuilder#buildPPT(java.util.List, java.io.File)
     *//*
    public void buildPPT(List<String> titles) throws IOException {
        DEFAULT_OUTPUT_DIR.mkdirs();

        List<Song> songs = new ArrayList<Song>();
        populateSongs(titles, songs);

        outputPPT(songs, DEFAULT_OUTPUT_DIR, DateFormat.getDateInstance().format(new Date()));
    }*/

    private SlideMaster[] template;

    private void outputPPT(List<Song> songs, File outputDir, String fileName) throws IOException {
        SlideShow show = new SlideShow();
        SlideShow templateSlideShow = readSlideShow(new File("template.ppt"));
        template = templateSlideShow.getSlidesMasters();
        
        for (Song song: songs) {
            if (song.getTitle().equalsIgnoreCase(SlideshowTabController.BLANK_SLIDE_TITLE)) {
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

    private void addSongToPPT (SlideShow show, Song song) throws IOException {
        for (String lyrics: song.getLyrics().split("\n\n")) {
            
            Slide slide = show.createSlide();
            slide.setMasterSheet(template[0]);
            
            slide.addTitle().setText(song.getTitle());
            slide.getTextRuns()[0].getRichTextRunAt(0).setFontSize(36);

            insertTextbox(slide, lyrics, 28, TextShape.AlignLeft,
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
            if (title.equalsIgnoreCase(SlideshowTabController.BLANK_SLIDE_TITLE)) {
                songs.add(new Song(SlideshowTabController.BLANK_SLIDE_TITLE));
            } else {
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
        try {
            Song song = SongDAO.getTestSong();
            SongDAO.getInstance().addSong(song);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }

        String name = "testout.ppt";
        File someDir = new File("somedir/inside it/");
        someDir.mkdirs();

        Song song = SongDAO.getTestSong();

        try {
            PPTBuilder.getInstance().buildPPT(Arrays.asList(song.getTitle()), someDir);
            PPTBuilder.getInstance().buildPPT(Arrays.asList(song.getTitle()), someDir, name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        SongDAO.deleteStuff();
    }
    private SlideShow readSlideShow(File ppt) throws IOException {
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
