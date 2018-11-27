package Search;

import java.io.IOException;
import java.util.*;

import Classes.Query;
import Classes.Document;
import IndexingLucene.MyIndexReader;

public class QueryRetrievalModel {
	
	protected MyIndexReader indexReader;
	private double u;
	private long colLen;
	private int numOfDocs = 503473; //number of trectext docs
	
	public QueryRetrievalModel(MyIndexReader ixreader) {
		indexReader = ixreader;
		try {
			getCollectionLen();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// set u as the length of collection divided by the number of docs
		// to eliminate effects of words in one document.
		u = (double) colLen / numOfDocs;

	}

	// get the length of Collention
	public void getCollectionLen() throws IOException {
		colLen = 0;
		for(int i = 0; i < numOfDocs; i++){
			if(indexReader.docLength(i) != 0)
				colLen += indexReader.docLength(i);
		}
	}
	
	/**
	 * Search for the topic information. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * TopN specifies the maximum number of results to be returned.
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @return
	 */
	
	public List<Document> retrieveQuery( Query aQuery, int TopN ) throws IOException {
		// NT: you will find our IndexingLucene.Myindexreader provides method: docLength()
		// implement your retrieval model here, and for each input query, return the topN retrieved documents
		// sort the documents based on their relevance score, from high to low


		String[] terms = aQuery.GetQueryContent().split(" ");   //terms in a query
		HashMap<String, Long> termColFreq = new HashMap<>();  // term and its frequency in collection
		HashMap<Integer, Map<String, Integer>> termDocFreq = new HashMap<>();  //<docID, <term, termDocFreq>>

		// get two maps
		// termColFreq computes the term frequency in collection
		// termDocFreq computes the term frequency in each doc stored by docId
		for(String term: terms){
			if(termColFreq.containsKey(term)) continue; //if freq is already known
			int[][] postingLists = indexReader.getPostingList(term);
			if(postingLists == null || postingLists.length == 0) continue; //no such term existed in collection.
			long colFreq = indexReader.CollectionFreq(term);
			termColFreq.put(term, colFreq);

			for(int[] postingList: postingLists){
				int docId = postingList[0];
				int docFreq = postingList[1];
				if(!termDocFreq.containsKey(docId)){
					HashMap<String, Integer> map = new HashMap<>();
					map.put(term, docFreq);
					termDocFreq.put(docId, map);
				}else{
					Map<String, Integer> map = termDocFreq.get(docId);
					map.put(term, docFreq);
				}
			}

		}

		//comparator used to order elements in heap (rank document by its score)
		Comparator<Document> order = new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				if(o1.score() < o2.score()){
					return -1;
				}if(o1.score() > o2.score()){
					return 1;
				}
				return 0;
			}
		};

		PriorityQueue<Document> heap = new PriorityQueue<>(TopN, order);

		//compute score of each doc and put doc into heap
		for(int docId: termDocFreq.keySet()){
			double score = 1;
			for(String term: terms){
				if(!termColFreq.containsKey(term)){
					//if term does not appear in the collection, detect it and process such cases
					continue;
				}
				double demo = indexReader.docLength(docId) + u;
				double mole;
				if(termDocFreq.get(docId).containsKey(term)){
					mole = termDocFreq.get(docId).get(term) + u * termColFreq.get(term) / colLen;
				}else{
					mole = u * termColFreq.get(term) / colLen;
				}
				score *= mole/demo;


			}
			if (heap.size() < TopN) {
				Document doc = new Document(String.valueOf(docId), indexReader.getDocno(docId), score);
				heap.add(doc);
			} else if (score > heap.peek().score()) {
				heap.poll();
				Document doc = new Document(String.valueOf(docId), indexReader.getDocno(docId), score);
				heap.add(doc);
			}
		}

		ArrayList<Document> list = new ArrayList<>();
		for(int i = 0; i < TopN; i ++){
			list.add(0, heap.poll());

		}
		if(list.size() != 0 && list != null) return list;






		return null;
	}
	
}