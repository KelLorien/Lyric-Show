package examples;
import data.LibraryWriteException;
import data.SongDAO;
import data.domain.Song;

public class tester {

	public static void main(String args[])
	{
		SongDAO test = SongDAO.getInstance();
		Song song = new Song();
		song.setTitle("Hello world");
		try {
			test.addSong(song);
		} catch (LibraryWriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
