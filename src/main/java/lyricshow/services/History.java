package lyricshow.services;

import lyricshow.util.Preferences;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * User: jpipe
 * Date: 12/12/12
 * Time: 3:39 PM
 */
public class History {

    private static final String HISTORY_FILENAME = "hist.dat";
    private static final String DATE_DELIM = "<--->";
    private static final int HISTORY_LENGTH = 4;

    private static lyricshow.services.History instance = null;

    public static lyricshow.services.History getInstance() {
        if (instance == null)
            instance = new lyricshow.services.History();
        return instance;
    }

    private File History = new File(Preferences.getLibraryURL() + HISTORY_FILENAME);

    private History() {}

    /**
     * Updates the history. If the history file cannot be found, a new one is created.
     * @param titles the new titles to be added to the history.
     * @throws java.io.IOException if there is a problem writing to the history file.
     *
     */
    public void updateHistory(List<String> titles) throws IOException {
        String newHistory = DATE_DELIM + DateFormat.getDateInstance(DateFormat.MEDIUM).format(new Date()) + "\n";

        for (String title: titles) {
            newHistory += title + "\n";
        }

        String historyText = "";
        try {
            historyText = getHistory();
        } catch (FileNotFoundException e) {
            System.out.println("History not found. Creating new file");
            History.createNewFile();
            writeToHistory(newHistory);
            return;
        }

        int count = 1;

        String[] dateSplit = historyText.split(DATE_DELIM);

        for (; count < HISTORY_LENGTH; count++) {
            if (count >= dateSplit.length)
                break;
            newHistory += DATE_DELIM + dateSplit[count];
        }

        writeToHistory(newHistory);
    }

    /**
     * Gets the (unformatted) history. Stored as far back as specified by {@link lyricshow.services.History#HISTORY_LENGTH}
     * @return String, which shows the titles used the last 4 times a powerpoint presentation was created
     * by this application.
     * @throws java.io.FileNotFoundException if there is no history file.
     */
    public String getHistory() throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(History);

            String fullHistory = "";
            while (scanner.hasNext())
                   fullHistory += scanner.nextLine() + "\n";

            return fullHistory.trim();
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    private void writeToHistory(String newHistory) throws IOException {
        if (!History.delete()) {
            throw new IOException("Could not delete old history file");
        }

        History.createNewFile();

        FileWriter writer = null;

        try {
            writer = new FileWriter(History);
            writer.write(newHistory);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    //ignore
                }
            }
        }
    }
}
