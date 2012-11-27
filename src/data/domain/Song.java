package data.domain;

import java.util.*;

/**
 * User: jpipe
 * Date: 11/19/12
 *
 * Represents a Song. Contains the title, lyrics, date last used, and all other keywords
 * of a song stored in the library. Attributes besides title and lyrics are stored in a
 * {@link List} of Strings.
 */
public class Song {

    String title;
    String lyrics;
    Date lastUsed;

    ArrayList<String> keywords = new ArrayList<String>();

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Long lastUsed) {
        this.lastUsed = new Date(lastUsed);
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void addKeyword(String... key) {
        Collections.addAll(keywords, key);
    }

    public void addAllKeywords(List<String> keys) {
        keywords.addAll(keys);
    }
    /**
     * Sets the date this song was last used to the current time;
     * that is, sets {@link #lastUsed} equal to a new {@link Date}.
     */
    public void setLastDateToNow() {
        lastUsed = new Date();
    }

    public boolean hasKeyword(String keyword) {
        return keywords.contains(keyword);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Song other = (Song) o;

        return title != null ? title.equalsIgnoreCase(other.title) : other.title == null;

    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
