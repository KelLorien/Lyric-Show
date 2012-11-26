package util;

import data.domain.Song;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static util.SongStorageParser.Tag.*;

/**
 * User: jpipe
 * Date: 11/20/12
 */
public class SongStorageParser {

    private static final String LINE = "\n";

    public static String toIndexString(Song song) {
        System.out.println(valWithTag(song.getTitle(), TITLE) + LINE + getStorageString(song));
        return valWithTag(song.getTitle(), TITLE) + LINE + getStorageString(song);
    }

    public static String toStorageString(Song song) {
        System.out.println(getStorageString(song) + valWithTag(song.getLyrics(), LYRIC) + LINE);
        return getStorageString(song) + valWithTag(song.getLyrics(), LYRIC) + LINE;
    }


    public static String extractTagDataFromString(String line, Tag tag) throws ParseException {
        String title = null;
        int index = line.indexOf(tag.tagString);
        if (index >= 0) {
            try {
                title = line.substring(index + tag.length(), line.indexOf(tag.end()));
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Unable to parse " + tag.tagString +" from line:\n" + line +
                        "\nNo closing tag.", line.length()-1);
            }
        }
        return title;
    }

    //TODO: implement
    public static List<String> extractAllKeywords(String line) {
        throw new RuntimeException("THIS ISN'T FINISHED!!!!!");
    }

    private static String getStorageString(Song song) {
        String ss = valWithTag(String.valueOf(song.getLastUsed().getTime()), DATE);

        ss += ATT_LIST.start();
        for (String key: song.getKeywords()) {
            ss += valWithTag(key, KEY);
        }
        return ss + ATT_LIST.end() + LINE;
    }

    private static String valWithTag(String val, Tag tag) {
        return tag.start() + val + tag.end();
    }

    private static long getDateFromSong(Song song) {
        Date date = song.getLastUsed();
        if (date == null)
            return 0;
        return date.getTime();
    }

    public static enum Tag {
        TITLE ( "<t>", "<\\t>"),
        LYRIC ( "<l>", "<\\l>"),
        KEY ( "<a>", "<\\a>"),
        ATT_LIST ( "<ats>", "<\\ats>"),
        DATE ( "<d>", "\\d>");

        private String tagString;
        private String endTagString;

        Tag(String s, String e) {
            tagString = s;
            endTagString = e;
        }

        public String start() {
            return tagString;
        }

        public String end() {
            return endTagString;
        }

        public int length() {
            return tagString.length();
        }
    }
}
