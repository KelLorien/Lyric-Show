package lyricshow.util;

import lyricshow.data.domain.Song;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static lyricshow.util.SongStorageParser.Tag.*;

/**
 * User: jpipe
 * Date: 11/20/12
 */
public class SongStorageParser {

    private static final String LINE = System.getProperty("line.separator");

    /**
     * Returns the String representation of a song without lyrics. Intended for
     * use in creating/adding to the index.
     * @param song the Song to be converted to a String.
     * @return String representation of the song
     */
    public static String toIndexString(Song song) {
        return song.getTitle() + LINE + getStorageString(song);
    }

    /**
     * Returns the String representation of a song with lyrics. Intended for
     * use in creating/adding to the library.
     * @param song the Song to be converted to a String.
     * @return String representation of the song
     */
    public static String toFullStorageString(Song song) {
        return getStorageString(song) + song.getLyrics() + LINE;
    }

    /**
     * Returns the String that appears between the start and end tags of the
     * given {@link lyricshow.util.SongStorageParser.Tag}.
     * @param line the String to be parsed.
     * @param tag the {@link lyricshow.util.SongStorageParser.Tag} corresponding to the desired lyricshow.data.
     * @return String containing all characters between the starting and ending tag
     * of the given {@link lyricshow.util.SongStorageParser.Tag}. Returns null if there is no starting tag (that is, the
     * tag does not exist).
     * @throws java.text.ParseException if there is no ending tag in the String for the given {@link lyricshow.util.SongStorageParser.Tag}.
     */
    public static String extractTagDataFromString(String line, Tag tag) throws ParseException {
        String title;
        int index = line.indexOf(tag.tagString);
        if (index >= 0) {
            try {
                title = line.substring(index + tag.length(), line.indexOf(tag.end()));
            } catch (IndexOutOfBoundsException e) {
                throw new ParseException("Unable to parse " + tag.tagString +" from line:\n" + line +
                        "\nNo closing tag.\nFile may be corrupt!", line.length()-1);
            }
        } else {
            throw new ParseException("Unable to parse " + tag.tagString +" from line:\n" + line +
                    "\nNo opening tag.\nFile may be corrupt!", line.length()-1);
        }
        return title;
    }

    /**
     * Gets a list of keywords from a String.
     * In particular, it first calls
     * <code>
     *     extractTagDataFromString(line, Tag.KEY_LIST);
     * </code>
     * which returns a string containing zero or more {@link lyricshow.util.SongStorageParser.Tag#KEY} tags. It then splits
     * the result into substrings on {@link lyricshow.util.SongStorageParser.Tag#KEY} and calls
     * <code>
     *     extractTagDataFromString(line, Tag.KEY);
     * </code>
     * for each substring.
     * @param line The String containing the start and end tags for {@link lyricshow.util.SongStorageParser.Tag#KEY_LIST}
     *             and 0 or more {@link lyricshow.util.SongStorageParser.Tag#KEY} elements within it.
     * @return A {@link java.util.List} of Strings containing all Strings which are contained within the
     * {@link lyricshow.util.SongStorageParser.Tag#KEY} tags. Strings with length < 1 are ignored (therefore, empty {@link lyricshow.util.SongStorageParser.Tag#KEY}
     * tags are ignored).
     * @throws java.text.ParseException If there are missing end tags for either {@link lyricshow.util.SongStorageParser.Tag#KEY_LIST} or
     * {@link lyricshow.util.SongStorageParser.Tag#KEY}.
     * @see lyricshow.util.SongStorageParser#extractTagDataFromString(String, lyricshow.util.SongStorageParser.Tag)
     */
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
        String ss = valWithTag(song.getAuthor(), AUTHOR)
                + valWithTag(song.getLyricist(), LYRICIST)
                + valWithTag(song.getMusicalKey(), MUSICAL_KEY)
                + valWithTag(String.valueOf(getDateFromSong(song)), DATE)
                + valWithTag(song.getCopyright(), COPYRIGHT);

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
        KEY ( "<k>", "</k>"),
        KEY_LIST( "<keys>", "</keys>"),
        DATE ( "<d>", "</d>"),
        AUTHOR ( "<a>", "</a>"),
        LYRICIST( "<l>", "</l>"),
        COPYRIGHT( "<c>", "</c>"),
        MUSICAL_KEY( "<m>", "</m>");

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