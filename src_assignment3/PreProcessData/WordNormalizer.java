package PreProcessData;

import Classes.*;

/**
 * This is for INFSCI 2140 in 2018
 * 
 */
public class WordNormalizer {
	// Essential private methods or variables can be added.

	// YOU MUST IMPLEMENT THIS METHOD.
	public char[] lowercase(char[] chars) {
		// Transform the word uppercase characters into lowercase.

		int len = chars.length;
		for(int i = 0; i < len; i ++){
			chars[i] = Character.toLowerCase(chars[i]);
		}



		return chars;
	}

	// YOU MUST IMPLEMENT THIS METHOD.
	public String stem(char[] chars) {
		// Return the stemmed word with Stemmer in Classes package.


		Stemmer stemmer = new Stemmer();
		stemmer.add(chars,chars.length);
		stemmer.stem();
		String str = "";
		str = stemmer.toString();
		return str;
	}


}
