package PseudoRFSearch;

import java.util.*;

import Classes.Document;
import Classes.Query;
import IndexingLucene.MyIndexReader;
import SearchLucene.QueryRetrievalModel;

public class PseudoRFRetrievalModel {

	MyIndexReader ixreader;
	HashSet<Integer> docId;
	List<Document> feedBackDoc;
	final double colLen = 142062976;
	final double numDoc = 503473;
	double u;


	
	public PseudoRFRetrievalModel(MyIndexReader ixreader)
	{
		this.ixreader=ixreader;


		u = colLen / numDoc;
	}
	
	/**
	 * Search for the topic with pseudo relevance feedback in 2017 spring assignment 4. 
	 * The returned results (retrieved documents) should be ranked by the score (from the most relevant to the least).
	 * 
	 * @param aQuery The query to be searched for.
	 * @param TopN The maximum number of returned document
	 * @param TopK The count of feedback documents
	 * @param alpha parameter of relevance feedback model
	 * @return TopN most relevant document, in List structure
	 */
	public List<Document> RetrieveQuery( Query aQuery, int TopN, int TopK, double alpha) throws Exception {	
		// this method will return the retrieval result of the given Query, and this result is enhanced with pseudo relevance feedback
		// (1) you should first use the original retrieval model to get TopK documents, which will be regarded as feedback documents
		// (2) implement GetTokenRFScore to get each query token's P(token|feedback model) in feedback documents
		// (3) implement the relevance feedback model for each token: combine the each query token's original retrieval score P(token|document) with its score in feedback documents P(token|feedback model)
		// (4) for each document, use the query likelihood language model to get the whole query's new score, P(Q|document)=P(token_1|document')*P(token_2|document')*...*P(token_n|document')
		
		QueryRetrievalModel model = new QueryRetrievalModel(ixreader);  //get model
		feedBackDoc = model.retrieveQuery(aQuery, TopK);      //get list of topk relevant docs
		docId = new HashSet<>();


		//get P(token|feedback documents)
		HashMap<String,Double> TokenRFScore=GetTokenRFScore(aQuery,TopK);
		HashMap<Integer, HashMap<String, Integer>> docInfo = new HashMap<>(); //<docId, <term, freq>>


		//for each term in queries, store its docFreq and collectionFreq
		String[] queries = aQuery.GetQueryContent().split(" ");
		for (String query: queries){
			if (!TokenRFScore.containsKey(query)) continue;
			int[][] postingLists = ixreader.getPostingList(query);  //[docId][docFreq]
//			if(postingLists == null || postingLists.length == 0) continue;
//			double queryColFreq = ixreader.CollectionFreq(query);
//			colFreq.put(query, queryColFreq);

			for(int[] postintList: postingLists){
				int docId = postintList[0];
				int docFreq = postintList[1];
				if(!docInfo.containsKey(docId)){
					HashMap<String, Integer> map = new HashMap<>();
					map.put(query, docFreq);
					docInfo.put(docId, map);
				}else{
					HashMap<String, Integer> map = docInfo.get(docId);
					map.put(query, docFreq);
					docInfo.put(docId, map);
				}
			}

		}

		//compare scores in heap
		Comparator<Document> comparator = new Comparator<Document>() {
			@Override
			public int compare(Document o1, Document o2) {
				if(o1.score() > o2.score()) return 1;
				if(o1.score() < o2.score()) return -1;
				return 0;
			}
		};

		//Store topN relevant documents
		PriorityQueue<Document> releDoc = new PriorityQueue<>(TopN, comparator);

		//compute score and put topN relevant doc into heap
		for (int Id: docId){
			double score = 1.0;
			for (String query: queries){
				if (!TokenRFScore.containsKey(query)) continue;
				double h = 0.0;
				h = (double) docInfo.get(Id).getOrDefault(query, 0) / (double) ixreader.docLength(Id);
//				if (ixreader.docLength(Id) != 0 && docInfo.containsKey(Id)){
//					h = (double) docInfo.get(Id).getOrDefault(query, 0) / (double) ixreader.docLength(Id);
//				}
//				else h = 0.0;
				score = score*(alpha*h) + (1.0-alpha)*TokenRFScore.get(query);

			}
			//new a document with its docId, doc content, and its score for the current query
			Document doc = new Document(String.valueOf(Id), ixreader.getDocno(Id), score);

			if(releDoc.size() < TopN) releDoc.add(doc);
			else{
				if(score > releDoc.peek().score()){
					releDoc.poll();
					releDoc.add(doc);
				}
			}

		}

		// sort all retrieved documents from most relevant to least, and return TopN
		List<Document> results = new ArrayList<Document>();
		while(!releDoc.isEmpty()){
			Document doc = releDoc.poll();
			results.add(0, doc);
		}
		
		return results;
	}
	
	public HashMap<String,Double> GetTokenRFScore(Query aQuery,  int TopK) throws Exception
	{
		// for each token in the query, you should calculate token's score in feedback documents: P(token|feedback documents)
		// use Dirichlet smoothing
		// save <token, score> in HashMap TokenRFScore, and return it
		HashMap<String,Double> TokenRFScore=new HashMap<String,Double>();

		int len = 0;
		//get the length of document and store docId
		for(Document doc: feedBackDoc){
			int id = Integer.parseInt(doc.docid());
			docId.add(id);
			int docLen = ixreader.docLength(id);
			len += docLen;

		}
//		System.out.println("doc length is " + len);

		String[] queries = aQuery.GetQueryContent().split(" ");
		for (String query: queries){
			int freq = 0;
			int[][] postingLists = ixreader.getPostingList(query);
			if(ixreader.CollectionFreq(query) == 0) continue;
			if (postingLists == null) continue;
			for (int[] postingList: postingLists){
				if(docId.contains(postingList[0])) freq += postingList[1];
			}

			double colFreq = (double) ixreader.CollectionFreq(query);
			if (colFreq == 0 && freq == 0) continue;
			double mole = (double) freq+u*colFreq / (double) colLen;
//			System.out.println("freq:" + freq);
//			System.out.println("colFreq:" + colFreq);
//			System.out.println("token: " + query + "score: " + mole);

			double deno = (double) len + u;
//			System.out.println(mole);
//			System.out.println(deno);
			TokenRFScore.put(query, mole/deno);
//			System.out.println("token: " + query + "score: " + mole/deno);
		}





		return TokenRFScore;
	}
	
	
}