package util;

import data.domain.Song;

import java.text.ParseException;

import static util.SongStorageParser.Tag.*;

/**
 * User: jpipe
 * Date: 11/20/12
 */
public class SongStorageParser {

    private static final String LINE = "\n";

    public static String toIndexString(Song song) {
        String ss = valWithTag(song.getTitle(), TITLE) + LINE;

     //   ss += valWithTag(String.valueOf(song.getLastUsed().getTime()), DATE) + LINE;

        ss += ATT_LIST.start() + LINE;
        for (String key: song.getKeywords()) {
            ss += valWithTag(key, KEY);
        }
        ss += ATT_LIST.end() + LINE;

        return ss + LINE;
    }

    public static String toStorageString(Song song) {
        return valWithTag(song.getTitle(), TITLE) + LINE + valWithTag(song.getLyrics(), LYRIC) + LINE;
    }


    public static String extractTagDataFromString(String line, Tag tag) throws ParseException {
        String title = null;
        int index = line.indexOf(tag.tagString);
        if (index >= 0) {
            try {
                title = line.substring(index + tag.length(), line.indexOf(tag.end()));
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Unable to parse title attribute from line:\n" + line +
                        "\nNo closing tag.", line.length()-1);
            }
        }
        return title;
    }

    private static String valWithTag(String val, Tag tag) {
        return tag.start() + val + tag.end();
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
