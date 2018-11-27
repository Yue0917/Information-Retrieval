package PreProcessData;
import Classes.*;

import java.io.*;
import java.util.HashSet;

public class StopWordRemover {
	// Essential private methods or variables can be added.

	private HashSet<String> stopWords;


	// YOU SHOULD IMPLEMENT THIS METHOD.
	public StopWordRemover( ) {
		// Load and store the stop words from the fileinputstream with appropriate data structure.
		// NT: address of stopword.txt is Path.StopwordDir

		//
		Path filePath = new Path();
		try{
			// Load stop word
			String fileInPath = filePath.StopwordDir;
			File file = new File(fileInPath);
			FileInputStream fis = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String line;
			// Use set to store stop words
			stopWords = new HashSet<>();
			while((line = br.readLine()) != null){
				stopWords.add(line);
			}
		}catch(FileNotFoundException e){
			System.out.println("File Not Found!");
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	// YOU SHOULD IMPLEMENT THIS METHOD.
	public boolean isStopword( char[] word ){
		// Return true if the input word is a stopword, or false if not.

		if(stopWords.contains(new String(word))){
			return true;
		}

		return false;
	}


//	public static void main(String[] args) throws Exception{
//		StopWordRemover test = new StopWordRemover();
//		char[] c = "zero".toCharArray();
//		if(test.isStopword(c)){
//			System.out.print("This is a stop word.");
//		}else
//			System.out.print("Not!");
//	}
}
