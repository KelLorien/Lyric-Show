package data.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * User: jpipe
 * WeekDate: 11/19/12
 *
 * Represents a Song. Contains the title, lyrics, date last used, and all other attributes
 * of a song stored in the library. Attributes besides title and lyrics are stored in a
 * {@link java.util.HashMap} of {@link String} to {@link String}.
 */
public class Song {

    String title;
    String lyrics;
    Date lastUsed;

    HashMap<String, String> attributes = new HashMap<String, String>();

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

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }

    public Map<String, String> getAttributeMap() {
        return attributes;
    }
    /**
     * Sets the date this song was last used to the current time;
     * that is, sets {@link #lastUsed} equal to a new {@link Date}.
     */
    public void setLastDateToNow() {
        lastUsed = new Date();
    }

    public String getAttribute(String attrName) {
        return attributes.get(attrName);
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
