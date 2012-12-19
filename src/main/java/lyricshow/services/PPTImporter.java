package lyricshow.services;

import lyricshow.data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * User: jpipe
 * Date: 12/7/12
 */
public class PPTImporter {

    private static PPTImporter instance;
    private static final String NEWLINE_REPLACEMENT = " ";

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
        if (slides.length < 0)
            return;

        Song currentSong = startNewSong(slides[0]);
        for (Slide slide: slides) {
            String nextTitle = removeNewLines(slide.getTitle());
            if (nextTitle == null || slide.getTextRuns().length < 2) {
                continue;
            }
            if (nextTitle.equals(currentSong.getTitle())) {
                currentSong.setLyrics(currentSong.getLyrics()
                        + slide.getTextRuns()[1].getText().trim() + "\n\n");
            } else {
                songs.add(currentSong.copy());
                currentSong = startNewSong(slide);
            }
        }
        songs.add(currentSong);
    }

    private Song startNewSong(Slide slide) {
        Song song = new Song();

        song.setTitle(removeNewLines(slide.getTitle()));
        song.setLyrics("");

        TextRun[] textRuns = slide.getTextRuns();
        if (textRuns.length >= 3) {
            song.setAuthor(removeNewLines(textRuns[2].getText()));
        } else {
            song.setAuthor("");
        }

        if (textRuns.length >= 4) {
            if (textRuns[3].getText().toLowerCase().contains("copyright")) {
                song.setCopyright(removeNewLines(textRuns[3].getText()));
                song.setLyricist("");
            } else {
                song.setLyricist(removeNewLines(textRuns[3].getText()));
                song.setCopyright("");
            }
        } else {
            song.setLyricist("");
            song.setCopyright("");
        }

        if (textRuns.length >= 5) {
            song.setCopyright(removeNewLines(textRuns[4].getText()));
        }

        return song;
    }

    private String removeNewLines(String title) {
        if (title == null)
            return null;
        return title.replaceAll("\n", NEWLINE_REPLACEMENT);
    }

}
