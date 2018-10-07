package Indexing;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import Classes.Path;
import java.io.File;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileNotFoundException;


public class PreProcessedCorpusReader {
	private BufferedReader br; //used to read files
	private int index = 0; //help record whether the line is docno or content
	
	
	public PreProcessedCorpusReader(String type) throws IOException {
		// This constructor opens the pre-processed corpus file, Path.ResultHM1 + type
		// You can use your own version, or download from http://crystal.exp.sis.pitt.edu:8080/iris/resource.jsp
		// Close the file when you do not use it any more
		Path file_path = new Path();
		try{
			String file_in_path = file_path.ResultHM1 + type;
			File file = new File(file_in_path);
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
		}catch(FileNotFoundException e){
			System.out.println("File Not Found!");
		}


	}
	

	public Map<String, String> NextDocument() throws IOException {
		// read a line for docNo, put into the map with <"DOCNO", docNo>
		// read another line for the content , put into the map with <"CONTENT", content>

		Map<String, String> map = new HashMap<>();	//read result files
		String line;
		if((line = br.readLine()) != null){
			String docno = line;   //get docno
			String content = br.readLine();   //read another line for content
			map.put("DOCNO", docno);
			map.put("CONTENT", content);
			return map;

		}

		br.close();

		return null;
	}

//	 public static void main(String[] args) throws IOException{
//		  PreProcessedCorpusReader p = new PreProcessedCorpusReader("trectext");
//		  Map<String, String> pp = p.NextDocument();
//		  System.out.println(pp.get("DOCNO"));
//		  System.out.println(pp.get("CONTENT"));
//	 }

}
