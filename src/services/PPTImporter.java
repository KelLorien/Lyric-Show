package services;

import data.SongDAO;
import data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.usermodel.SlideShow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * User: jpipe
 * Date: 12/7/12
 */
public class PPTImporter {

    private static PPTImporter instance;

    public static PPTImporter getInstance() {
        if (instance == null)
            instance = new PPTImporter();
        return instance;
    }

    private PPTImporter() {
    }

    public List<Song> importSong(File ppt) throws IOException {
        List<Song> songs = new ArrayList<Song>();

        SlideShow show = readSlideShow(ppt);

        parseAllSongs(songs, show);

        return songs;
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
                    //ignore
                }
            }
        }

        return show;
    }

    private void parseAllSongs(List<Song> songs, SlideShow show) {
        Slide[] slides = show.getSlides();
        Song currentSong = startNewSong(slides[0]);
        for (Slide slide: slides) {
            String nextTitle = slide.getTitle();
            if (nextTitle.equals(currentSong.getTitle())) {
                currentSong.setLyrics(currentSong.getLyrics()
                        + slide.getTextRuns()[1].getText() + "\n\n");
            } else {
                songs.add(currentSong.copy());
                currentSong = startNewSong(slide);
            }
        }
        songs.add(currentSong);
    }

    private Song startNewSong(Slide slide) {
        Song song = new Song();

        song.setTitle(slide.getTitle());
        song.setLyrics("");
        song.setAuthor(slide.getTextRuns()[2].getText());
        song.setLyricist(slide.getTextRuns()[3].getText());
        song.setCopyright(slide.getTextRuns()[4].getText());

        return song;
    }

    public static void main(String[] args) {
        File target = new File("songs.ppt");

        try {
            Song newSong = getInstance().importSong(target).get(0);

            SongDAO.getInstance().addSong(newSong);

            PPTBuilder.getInstance().buildPPT(Arrays.asList(newSong.getTitle()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
