package PreProcessData;

import java.io.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import Classes.Path;

/**
 * This is for INFSCI 2140 in 2018
 *
 */
public class TrectextCollection implements DocumentCollection {
	// Essential private methods or variables can be added.

	private String doc_start = "<DOC>";
//	private String doc_end = "</DOC>";
	private String uni_id_start = "<DOCNO>";
	private String uni_id_end = "</DOCNO>";
	private String text_start = "<TEXT>";
	private String text_end = "</TEXT>";

	//define a glogal variable to store data between 2 methods
	private BufferedReader br;


	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrectextCollection() throws IOException {
		// 1. Open the file in Path.DataTextDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!

		//open file and read file.
		Path file_path = new Path();
		try{
			String file_in_path = file_path.DataTextDir;

			File file = new File(file_in_path);
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
		}catch(FileNotFoundException e){
			System.out.println("File Not Found!");
		}


		
	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its doc number and content.
		// 2. When no document left, return null, and close the file.


		String uniID = new String();	//store unique-identifier
		String line = br.readLine();		//store each line in trectext
		StringBuffer buffer = new StringBuffer();	//store content
		Map<String, Object> doc = new HashMap<>();	//store Unique Identifier and relative content

		while(line != null){
			if(line.equals(doc_start)){	//check if reaching the <DOC> line
				line = br.readLine();	//reach the <DOCNO> line
				if(line.length() > 7) {	//make sure this line has Unique Identifier
					uniID = line.split(uni_id_start)[1]; //split <docno> and the following String
					uniID = uniID.split(uni_id_end)[0];	//split ID and </docno>
					uniID = uniID.trim();	//delete " "
				}
			}
			if(line.equals(text_start)){	//check if reaching the <Text> line
				line = br.readLine();
				while(!line.equals(text_end)){	//check if reaching the </TEXT> line

					buffer.append(line + " ");	//get the content before reaching the </TEXT> line
					line = br.readLine();
				}
				if(uniID != null && buffer != null){
					doc.put(uniID, buffer.toString().toCharArray());
					return doc;
				}else if(uniID == null || buffer == null){
					continue;
				}

			}
			line = br.readLine();
		}



		br.close();
		return null;
	}



//
	
}
