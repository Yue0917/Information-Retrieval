package Indexing;

import Classes.Path;

import java.io.*;
import java.util.*;


public class MyIndexReader {
	//you are suggested to write very efficient code here, otherwise, your memory cannot hold our corpus...

	private String type;
	private HashMap<String, ArrayList<String>> map;   //<docno, <tokenID, frequency>>
	private ArrayList<String> docNo; // docno
	private HashMap<String, Integer> docnoToId;  //help get id according to docno
	private HashMap<Integer, Integer> docFreq; //<docID, frequecy>
	private ArrayList<String> tokenFreq;	//record fre of token in each doc

	public MyIndexReader( String type ) throws IOException {
		//read the index files you generated in task 1
		//remember to close them when you finish using them
		//use appropriate structure to store your index
		this.docFreq = new HashMap<>();
		this.type = type;
		this.docnoToId = new HashMap<>();
		this.docNo = new ArrayList<>();
		this.map = new HashMap<>();
		File file;
		if(this.type.equals("trecweb")){
			file = new File(Path.DataWebDir);
		}else file = new File(Path.DataTextDir);
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		int index= 0;
		while((line = br.readLine()) != null){
			docNo.add(line);
			docnoToId.put(line, index);
			index ++;
		}

		br.close();;
		fr.close();
	}
	
	//get the non-negative integer dociId for the requested docNo
	//If the requested docno does not exist in the index, return -1
	public int GetDocid( String docno ) {

		if(docnoToId.containsKey(docno)){
			return docnoToId.get(docno);
		}
		return -1;

	}

	// Retrieve the docno for the integer docid
	public String GetDocno( int docid ) {
		if(docnoToId.containsValue(docid)){
			return docNo.get(docid);
		}


		return null;
	}
	
	/**
	 * Get the posting list for the requested token.
	 * 
	 * The posting list records the documents' docids the token appears and corresponding frequencies of the term, such as:
	 *  
	 *  [docid]		[freq]
	 *  1			3
	 *  5			7
	 *  9			1
	 *  13			9
	 * 
	 * ...
	 * 
	 * In the returned 2-dimension array, the first dimension is for each document, and the second dimension records the docid and frequency.
	 * 
	 * For example:
	 * array[0][0] records the docid of the first document the token appears.
	 * array[0][1] records the frequency of the token in the documents with docid = array[0][0]
	 * ...
	 * 
	 * NOTE that the returned posting list array should be ranked by docid from the smallest to the largest. 
	 * 
	 * @param token
	 * @return
	 */
	public int[][] GetPostingList( String token ) throws IOException {

		getFreqMap(token);  //get the frequency of token in each doc.
		int[][] result = new int[tokenFreq.size()/2][2];
		for(int i = 0, j = 0; i < tokenFreq.size()/2 && j < tokenFreq.size(); i ++, j = j + 2){
			result[i][0] = Integer.parseInt(tokenFreq.get(j)); //docId
			result[i][1] = Integer.parseInt(tokenFreq.get(j + 1));   //frequecy in corresponding doc
		}
		if(result != null){
			return result;
		}

//		if(!map.containsKey(token)){
//			getFreqMap(token);
//			HashMap<Integer, Integer> temp = map.get(token);
//			int[][] result = new int[temp.size()][2];
//
//			Set<Map.Entry<Integer, Integer>> entriesDoc = map.get(token).entrySet();  //<doc1 fre1>
//			int i = 0;
//			for(Map.Entry<Integer, Integer> entryDoc: entriesDoc){
//				if(i < temp.size()){
//					result[i][0] = entryDoc.getKey();
//					result[i][1] = entryDoc.getValue();
//					i ++;
//				}
//
//			}
//			return result;
//
//		}

		return null;
	}

	// Return the number of documents that contains the token.
	public int GetDocFreq( String token ) throws IOException {
		if(!map.containsKey(token)){
			getFreqMap(token);	//get fre of token in each doc
			return tokenFreq.size()/2;  //the list is constructed by docId and relative fre, so return half size of the list

		}

		return 0;
	}
	
	// Return the total number of times the token appears in the collection.
	public long GetCollectionFreq( String token ) throws IOException {

		if(!tokenFreq.contains(token)){
			getFreqMap(token);		//get list [doc1, fre1, doc2, fre2, doc3, fre3.....]
			long freq = 0;
			for(int i = 1; i < tokenFreq.size(); i = i + 2){
				freq += Integer.parseInt(tokenFreq.get(i)); 	//compute the total sum of fre1, fre2, fre3....
			}
			return freq;
		}
//		if(tokenFreq.contains(token)){
//			long freq = 0;
//			for(int i = 1; i < tokenFreq.size(); i = i + 2){
//				freq += Integer.parseInt(tokenFreq.get(i));
//				System.out.println(tokenFreq.get(i));
//			}
//			return freq;
//		}

//		if(!map.containsKey(token)){
//			getFreqMap(token);
//			int freq = 0;
//
//			int freq = 0;
//			if(map.containsKey(token)){
//				HashMap<Integer, Integer> temp = map.get(token);
//				Set<Map.Entry<Integer, Integer>> entriesDoc = map.get(token).entrySet();
//				for(Map.Entry<Integer, Integer> entryDoc: entriesDoc){
//					freq += entryDoc.getValue();
//				}
//			}
//			return freq;
//
//		}


		return 0;
	}

	public void getFreqMap(String token) throws IOException {
		File file;
		if(this.type.equals("trecweb")){
			file = new File(Path.IndexWebDir + "index.txt");
		}else file = new File(Path.IndexTextDir + "index.txt");
		FileReader fr = new FileReader(file);
		BufferedReader br = new BufferedReader(fr);
		String line;
		tokenFreq = new ArrayList<>();		//generate frequency of the token in each doc [doc1, fre1, doc2, fre2, doc3, fre3...docn, fren]
		while((line = br.readLine()) != null){
			String[] lineToWords = line.split(" ");
			if(!lineToWords[0].equals(token)){  //read next line until reaching the target token
				continue;
			}else{
				for(int i = 1; i < lineToWords.length; i ++){
					tokenFreq.add(lineToWords[i]);   //get fre of this token in each doc
					map.put(token, tokenFreq);		//generate the map <token, [doc1, fre1, doc2, fre2...]>
				}

			}
		}
		br.close();
		fr.close();

	}


//	public void getFreqMap1(String token) throws IOException {
//		File file;
//		if(this.type.equals("trecweb")){
//			file = new File(Path.IndexWebDir + "index.txt");
//		}else file = new File(Path.IndexTextDir + "index.txt");
//		FileReader fr = new FileReader(file);
//		BufferedReader br = new BufferedReader(fr);
//		String line;
//		HashMap<Integer, Integer> idFreq = new HashMap<>();
//		while((line = br.readLine()) != null){
//			String[] lineToWords = line.split(" ");
//			if(!lineToWords[0].equals(token)){
//				//traverse until reach the right token
//				continue;
//			}else if(map.containsKey(lineToWords[0])){
//				//if token, get id and frequecy
//				idFreq = map.get(lineToWords[0]);
//				for(int docid : idFreq.keySet()){
//					System.out.print(docid + " " + idFreq.get(docid));
//				}
//			}else{
//				idFreq = new HashMap<>();
//
//				map.put(lineToWords[0], idFreq);
//
//			}
//
//		}
//		br.close();
//		fr.close();
//	}

	
	public void Close() throws IOException {

		map = null;
		docNo = null;
	}
	
}