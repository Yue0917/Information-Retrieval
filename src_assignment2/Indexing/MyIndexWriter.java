package Indexing;

import Classes.Path;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.io.File;

public class MyIndexWriter {
	// I suggest you to write very efficient code here, otherwise, your memory cannot hold our corpus...

	private FileWriter fw;  //output index files
	private String type; //remember the type of files
	// private Map<String, ArrayList<Integer>> map;  //<token, doc1 fre1 doc2 fre2 doc3 fre3>
	private Map<String, HashMap<Integer, Integer>> map;
	private int docCount; //count the number of docs
	private ArrayList<String> docNo;  //docno


	public MyIndexWriter(String type) throws IOException {
		// This constructor should initiate the FileWriter to output your index files
		// remember to close files if you finish writing the index
		this.type = type;
		this.docCount = 0;
		this.map = new HashMap<>();
		this.docNo = new ArrayList<>();
		File indexFile;
		if(this.type.equals("trecweb")) {
			indexFile = new File(Path.IndexWebDir + "index.txt");
		}else {
			indexFile = new File(Path.IndexTextDir + "index.txt");
		}
		fw = new FileWriter(indexFile);

	}

	public void IndexADocument(String docno, String content) throws IOException {
		// you are strongly suggested to build the index by installments
		// you need to assign the new non-negative integer docId to each document, which will be used in MyIndexReader
		int docId = docNo.size();  //keep track of the Id of doc
		docNo.add(docno);
		String[] words = content.split(" ");
		HashMap<Integer, Integer> docFreq = new HashMap<>();   //record fre of each doc
//		Set<String> tokens = new HashSet<>();
		for(String token : words){
			//if token is appeared before

//   if (tokens.contains(token)){
//    ArrayList<Integer> list = map.get(token);
//    int len = list.size() - 1;
//    list.set(len, list.get(len) + 1);
//   }else if(map.containsKey(token)){
//    ArrayList<Integer> list = map.get(token);
//    list.add(docId);
//    list.add(1);
//    tokens.add(token);
//   }else{
//    ArrayList<Integer> list = new ArrayList<>();
//    list.add(docId);
//    list.add(1);
//    map.put(token, list);
//    tokens.add(token);
//   }

			Map<Integer, Integer> defaultDocFre = new HashMap<>();
			defaultDocFre.put(docId, 0);
			Map<Integer, Integer> docFre = map.getOrDefault(token,
					(HashMap<Integer, Integer>) defaultDocFre);
			docFre.put(docId, docFre.getOrDefault(docId, 0)+1);
			map.put(token, (HashMap<Integer, Integer>) docFre);
//   System.out.println(token + " " + docId + " " + docFre.get(docId) );


		}
		this.docCount ++;
		//write into files every 10000 docs
		if(docCount%2 == 0){
		//   Set<Map.Entry<String, ArrayList<Integer>>> entries = map.entrySet();
		//   for (Map.Entry<String, ArrayList<Integer>> entry : entries){
		//    String token = entry.getKey();
		//    ArrayList<Integer> list = new ArrayList<>();
		//    for (int i = 0; i < list.size(); i = i + 2){
		//     fw.write(token + " ");
		//     fw.write(list.get(i) + " ");
		//     fw.write(list.get(i + 1) + "\r\n");
		//     System.out.println(token + " " + list.get(i) + " " + list.get(i + 1) + "\r\n");
	//    }
	//   }
//   		map = new HashMap<>();
			Set<Map.Entry<String, HashMap<Integer, Integer>>> entries = map.entrySet();
			for (Map.Entry<String, HashMap<Integer, Integer>> entry : entries) {
				String token = entry.getKey();
				fw.write(token + " ");
//    			System.out.println(token);
				for(int index : map.get(token).keySet()){
					fw.write(index + " ");
					fw.write(map.get(token).get(index) + " ");		//token doc1 fre1 doc2 fre2 ... docn fren
//     				System.out.println(index + map.get(token).get(index));
				}
				fw.write("\r\n");

			}
			map = new HashMap<>();
		}


	}

	public void Close() throws IOException {
		// close the index writer, and you should output all the buffered content (if any).
		// if you write your index into several files, you need to fuse them here.

	//  Set<Map.Entry<String, ArrayList<Integer>>> entries = map.entrySet();
	//  for (Map.Entry<String, ArrayList<Integer>> entry : entries){
		//   String token = entry.getKey();
		//   ArrayList<Integer> list = new ArrayList<>();
			//   for (int i = 0; i < list.size(); i = i + 2){
			//    fw.write(token + " ");
			//    fw.write(list.get(i) + " ");
			//    fw.write(list.get(i + 1) + "\r\n");
			//    System.out.println(token + " " + list.get(i) + " " + list.get(i + 1) + "\r\n");
			//   }
	// 	 }


		Set<Map.Entry<String, HashMap<Integer, Integer>>> entries = map.entrySet();		//write the left map into files
		for (Map.Entry<String, HashMap<Integer, Integer>> entry : entries) {
			String token = entry.getKey();
			fw.write(token + " ");
//			System.out.println(token);
			for(int index : map.get(token).keySet()){
				fw.write(index + " ");
				fw.write(map.get(token).get(index) + " ");
//    System.out.println(index + map.get(token).get(index));
			}
			fw.write("\r\n");

	//   Set<Map.Entry<Integer, Integer>> entriesDoc = map.get(token).entrySet();  //<doc1 fre1>
	//   fw.write(token + " ");
	//
	//   for(Map.Entry<Integer, Integer> entryDoc: entriesDoc){
	//    for (int i = 0; i < entriesDoc.size(); i ++){
	//
	//     fw.write(entryDoc.getKey() + " ");
	//     fw.write(entryDoc.getValue() + "\r\n");
	//     System.out.println(token + " " + entryDoc.getKey() + " " + entryDoc.getValue());
	//    }
	//   }
		}
//  map = new HashMap<String, HashMap<Integer, Integer>>();
		map = new HashMap<>();
		fw.close();

		//record docno into files
		File nameFile;
		if (this.type == "trecweb"){
			nameFile = new File(Path.DataWebDir);
		}else{
			nameFile = new File(Path.DataTextDir);
		}
		fw = new FileWriter(nameFile);
		for(String ss: docNo){
			fw.write(ss + "\r\n");
		}
		fw.close();

		docNo = null;


	}

//	 public static void main(String[] args) throws IOException {
//		  MyIndexWriter miw = new MyIndexWriter("trecweb");
//		  miw.IndexADocument("docno0001", "olivia judth olivia jerry judth larry jerry jerry");
//		  miw.IndexADocument("docno0002", "judth larry larry");
//		  miw.IndexADocument("docno0003", "olivia judth jerry");
//		  miw.IndexADocument("docno0004", "olivia larry kate");
//		  miw.Close();
//	 }




}