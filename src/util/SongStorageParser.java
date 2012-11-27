package util;

import data.domain.Song;

import java.text.ParseException;
import java.util.ArrayList;
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
        return valWithTag(song.getTitle(), TITLE) + LINE + getStorageString(song);
    }

    public static String toFullStorageString(Song song) {
        return getStorageString(song) + song.getLyrics() + LINE;
    }


    public static String extractTagDataFromString(String line, Tag tag) throws ParseException {
        String title = null;
        int index = line.indexOf(tag.tagString);
        if (index >= 0) {
            try {
                title = line.substring(index + tag.length(), line.indexOf(tag.end()));
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Unable to parse " + tag.tagString +" from line:\n" + line +
                        "\nNo closing tag.\nFile may be corrupt!", line.length()-1);
            }
        }
        return title;
    }

    public static List<String> extractAllKeywords(String line) throws ParseException {
        ArrayList<String> keywords = new ArrayList<String>();

        String unparsedKeywords = extractTagDataFromString(line, KEY_LIST);

        for (String keyword: unparsedKeywords.split("(?=" + KEY.start() + ")")) {
            if (keyword.length() > 0)
                keywords.add(extractTagDataFromString(keyword, KEY));
        }

        return keywords;
    }

    private static String getStorageString(Song song) {
        String ss = valWithTag(String.valueOf(getDateFromSong(song)), DATE);

        ss += KEY_LIST.start();
        for (String key: song.getKeywords()) {
            ss += valWithTag(key, KEY);
        }
        return ss + KEY_LIST.end() + LINE;
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
        KEY ( "<a>", "<\\a>"),
        KEY_LIST( "<ats>", "<\\ats>"),
        DATE ( "<d>", "\\d>");

        private final String tagString;
        private final String endTagString;

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
