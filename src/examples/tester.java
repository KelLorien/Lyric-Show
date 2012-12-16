package examples;

import java.io.File;

public class tester {

	public static void main(String args[])
	{
		PowerPointReader ppr = new PowerPointReader();
		ppr.readPowerpoint(new File("/users/kim/Lyric-Show/PM081212-1.ppt"));
	}
}
