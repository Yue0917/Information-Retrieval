package PreProcessData;

import java.io.IOException;
import java.util.*;

import Classes.Path;
import java.io.*;

/**
 * This is for INFSCI 2140 in 2018
 *
 */
public class TrecwebCollection implements DocumentCollection {
	// Essential private methods or variables can be added.

	//define some varibales which will be used during the class
	private BufferedReader br;
	private String doc_start = "<DOC>";
	private String doc_end = "</DOC>";
	private String uni_id_start = "<DOCNO>";
	private String uni_id_end = "</DOCNO>";
//	private String content_start = "<DOCHDR>";
	private String content_start = "</DOCHDR>";



	// YOU SHOULD IMPLEMENT THIS METHOD.
	public TrecwebCollection() throws IOException {
		// 1. Open the file in Path.DataWebDir.
		// 2. Make preparation for function nextDocument().
		// NT: you cannot load the whole corpus into memory!!

		Path file_path = new Path();	//read file
		try{
			String file_in_path = file_path.DataWebDir;
			File file = new File(file_in_path);
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
//		StringBuffer buffer = new StringBuffer();
		}catch(FileNotFoundException e){
			System.out.println("File Not Found!");
		}


	}
	
	// YOU SHOULD IMPLEMENT THIS METHOD.
	public Map<String, Object> nextDocument() throws IOException {
		// 1. When called, this API processes one document from corpus, and returns its doc number and content.
		// 2. When no document left, return null, and close the file.
		// 3. the HTML tags should be removed in document content.

		String uniID = new String(); 	//store unique identifier
		String line = br.readLine();		//read each line in file
		StringBuffer buffer = new StringBuffer();	//store content
		Map<String, Object> doc = new HashMap<>();	//return map with ID and content
		while(line != null){	//make sure whether the file reaches the end

			if(line.equals(doc_start)){		//<DOC> line

				line = br.readLine();		//<DOCNO> line
				if(line.length() > 7) {		//double-check this line has ID
					uniID = line.split(uni_id_start)[1]; //split <docno> and the following String
					uniID = uniID.split(uni_id_end)[0];		//split ID and </DOCNO>
					uniID = uniID.trim();		//filter out space
				}
			}
			if(line.equals(content_start)){		//</DOCHDR> line

				line = br.readLine();	//the content starts from this line
				while(!line.equals(doc_end)){	//check if reaching the </DOC> line

					buffer.append(line + " ");

					line = br.readLine();
				}
				if(uniID != null && buffer != null){	//make sure ID and content is not null before they are added into map
					buffer = removeTags(buffer);	//remove tags<>
					doc.put(uniID, buffer.toString().toCharArray());
					
					return doc;
				}else if(uniID == null || buffer == null){		//if any of them is null, then do not add them into map.
					continue;
				}

			}
			line = br.readLine();

		}


		br.close();
		return null;
	}

	//remove <...>
	private StringBuffer removeTags(StringBuffer content){
		StringBuffer leftContent = new StringBuffer();
		leftContent = leftContent.append(content.toString().replaceAll("\\<.*?>", ""));
		return leftContent;
	}

	
}
