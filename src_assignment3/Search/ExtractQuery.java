package Search;

import Classes.Query;

import Classes.Path;

import java.io.*;
import java.util.ArrayList;
import PreProcessData.*;

public class ExtractQuery {

	private ArrayList<Query> queries = new ArrayList<>();
	private BufferedReader br;
	private int count = 0;


	public ExtractQuery() {
		//you should extract the 4 queries from the Path.TopicDir
		//NT: the query content of each topic should be 1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
		//NT: you can simply pick up title only for query, or you can also use title + description + narrative for the query content.

		Path file_path = new Path();
		try {
			String file_in_path = file_path.TopicDir;
			File file = new File(file_in_path);
			FileInputStream fis = new FileInputStream(file);
			br = new BufferedReader(new InputStreamReader(fis));
			Query query = new Query();
			String line = br.readLine();
			while (line != null) {
				if (line.equals("<top>")) {
					line = br.readLine();
					String id = line.split(" ")[2]; //<num> line
					if (!id.equals("")) query.SetTopicId(id);
					line = br.readLine();
					String title = line.split(">")[1].trim(); //<title> line
					title = preProcess(title);
					if (!title.equals("")) query.SetQueryContent(title);

					if (query.GetTopicId() != null && query.GetQueryContent() != null) {
						queries.add(query);
						query = new Query();
					}
				}
				line = br.readLine();


			}
		}catch(Exception e){
				System.out.println("Error!");
		}

	}


	public boolean hasNext() {
		if(count < queries.size()) return true;  //make sure there exists next
		return false;
	}
	
	public Query next() {

		if(hasNext()){
			return queries.get(count++);
		}
		return null;
	}

	//1) tokenized, 2) to lowercase, 3) remove stop words, 4) stemming
	public static String preProcess(String content){

		// Loading stopword, and initiate StopWordRemover.
		StopWordRemover stopwordRemover = new StopWordRemover();

		// Initiate WordNormalizer.
		WordNormalizer normalizer = new WordNormalizer();
		WordTokenizer tokenizer = new WordTokenizer(content.toCharArray());

		char[] word = null;
		StringBuffer sb = new StringBuffer();

		while ((word = tokenizer.nextWord()) != null) {

			// Each word is transformed into lowercase.
			word = normalizer.lowercase(word);

			// Only non-stopword will appear in result file.
			if (!stopwordRemover.isStopword(word))
				// Words are stemmed.
				sb.append(normalizer.stem(word) + " ");
		}
		String processedContent = new String(sb);

		return processedContent.trim();

	}

//	public static void main(String[] args){
//		ExtractQuery test = new ExtractQuery();
//		Query q1 = test.next();
//		System.out.println(q1.GetTopicId());
//		System.out.println(q1.GetQueryContent());
//		Query q2 = test.next();
//		System.out.println(q2.GetTopicId());
//		System.out.println(q2.GetQueryContent());
//		Query q3 = test.next();
//		System.out.println(q3.GetTopicId());
//		System.out.println(q3.GetQueryContent());
//		Query q4 = test.next();
//		System.out.println(q4.GetTopicId());
//		System.out.println(q4.GetQueryContent());
//	}

}
