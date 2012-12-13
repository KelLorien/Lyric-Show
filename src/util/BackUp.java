package util;		//Not entirely sure which package to put this in, feel free to move it.

/** 
 * User: Catloaf
 * Date: 12/13/12
 */

import data.SongDAO;
import data.domain.Song;
import services.PPTBuilder;

import java.text.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class BackUp {
	static String dest; //Destination folder
	
	//Back Up all Songs as both Powerpoint files and as .txt files
	public static void BackUpAll(){
		BackUpTxt();
		BackUpPPT();
	}
	
	//Back up as .txt files only
	public static void BackUpTxt(){
		PrintWriter out=null;
		File output = null;
		List<String> titles = SongDAO.getInstance().getAllTitles();
		try{
			for(String title: titles){
				Song song = SongDAO.getInstance().getSong(title);
				output = new File(dest + "/"+ song.getTitle()+".txt");
				output.createNewFile();
				out = new PrintWriter(output);
				
				//Write to file here.
				//Format yet to be determined
				out.println();//Currently just here to shut eclipse up.
			}
		}
		catch(IOException e){
			//Not sure what to do here
		}
		catch(ParseException pe){
			//Not sure what to do here
		}
		
	}
	
	//Back up as powerpoint files only
	public static void BackUpPPT(){
		PPTBuilder pptb = PPTBuilder.getInstance();
		File output = null;
		List<String> titles = SongDAO.getInstance().getAllTitles();
		try{
			for(String title: titles){
				Song song = SongDAO.getInstance().getSong(title);
				output = new File(dest + "/"+ song.getTitle()+".ppt");
				List<String> x = new ArrayList<String>();	//just a temporary list of length one
				x.add(title);								//holding the title of the current song in titles
				pptb.buildPPT(x, output);
			}
		}
		catch(IOException e){
			//Not sure what to do here
		}
		catch(ParseException pe){
			//Not sure what to do here
		}
	}
	
	public static void setDestination(String d){
		dest = d;
	}
	
}
