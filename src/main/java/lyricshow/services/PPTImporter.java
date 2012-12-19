package lyricshow.services;

import lyricshow.data.domain.Song;
import org.apache.poi.hslf.model.Slide;
import org.apache.poi.hslf.model.TextRun;
import org.apache.poi.hslf.usermodel.SlideShow;

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

    /**
     * Imports the songs stored in a PowerPoint file.
     * @param ppt {@link File} representing the powerpoint file to be read.
     * @return A list of {@link Song} objects, representing the data stored in the given powerpoint file.
     * @throws IOException
     */
    public List<Song> importSong(File ppt) throws IOException {
        List<Song> songs = new ArrayList<Song>();

        SlideShow show = readSlideShow(ppt);

        parseAllSongs(songs, show);
        
        return songs;
    }

    /**
     * Reads in the powerpoint.
     * @param ppt {@link File} pointing to the .ppt file to be read.
     * @return an instantiated {@link SlideShow} representing the given file. The file is not changed in
     * any way by this method
     * @throws IOException If there was an exception reading the file.
     */
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

    /**
     * Parses all the songs in the show and populates the given list with them.
     * Determines the end/beginning of songs bases on
     * {@link org.apache.poi.hslf.model.Slide#getTitle()}. Whenever the title changes, a new
     * slide is added.
     *
     * @param songs The list to be populated with songs.
     * @param show The {@link SlideShow} to be parsed.
     */
    private void parseAllSongs(List<Song> songs, SlideShow show) {
        Slide[] slides = show.getSlides();
        if (slides.length < 0)
            return;

        Song currentSong = startNewSong(slides[0], true);
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
                currentSong = startNewSong(slide, false);
            }
        }
        songs.add(currentSong);
    }

    /**
     * Creates a new song based on the information given by the {@link Slide} object passed in.
     * @param slide containing the relevant information for this song.
     * @param addLyrics designates whether the lyrics from this slide should be added to the returned
     *                  song. True means the lyrics contained in this slide will be added to the returned song
     *                  (in the second {@link TextRun}, according to
     *                  {@link org.apache.poi.hslf.model.Slide#getTextRuns()})
     * @return A song instantiated with all the information given on the page.
     */
    private Song startNewSong(Slide slide, boolean addLyrics) {
        Song song = new Song();

        song.setTitle(removeNewLines(slide.getTitle()));

        TextRun[] textRuns = slide.getTextRuns();

        if (addLyrics)
            song.setLyrics(textRuns[1].getText().trim() + "\n\n");

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
