package PreProcessData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This is for INFSCI 2140 in 2018
 * 
 * TextTokenizer can split a sequence of text into individual word tokens.
 */
public class WordTokenizer {
	// Essential private methods or variables can be added.

	private char[] words;
	private int i;	//remember the location of index



	// YOU MUST IMPLEMENT THIS METHOD.
	public WordTokenizer( char[] texts ) {
		// Tokenize the input texts.
		i = 0;
		words = texts;

	}
	
	// YOU MUST IMPLEMENT THIS METHOD.
	public char[] nextWord() {
		// Return the next word in the document.
		// Return null, if it is the end of the document.
		int count = 0;	//count the number of chars which are digital or alphabetic
		ArrayList<Character> word = new ArrayList<>();
		while(i < words.length){
			//only keep numeric and alphabetic elements
			if(!Character.isDigit(words[i]) && !Character.isAlphabetic(words[i]) && count != 0){
				i ++;
				break;
			}
			if(Character.isDigit(words[i]) || Character.isAlphabetic(words[i])){
				word.add(words[i]);
				count ++;
			}
			i ++;
		}
		char[] result = new char[count];
		for(int j = 0; j < count; j ++){
			result[j] = word.get(j);
		}
		if(result.length > 0){
			return result;
		}




		return null;
	}

//	public static void main(String[] args) throws Exception{
//		String str = "supporter -- of the\n" +
//				"anti-Iraq coalition during the Gulf crisis";
//		char[] strToChar = str.toCharArray();
//		WordTokenizer test = new WordTokenizer(strToChar);
//		char[] we = test.nextWord();
//		for(int j = 0; j < we.length; j ++){
//			System.out.print(we[j]);
//		}
//		System.out.print(" ");
//		char[] can = test.nextWord();
//		for(int j = 0; j < can.length; j ++){
//			System.out.print(can[j]);
//		}
//		System.out.print(" ");
//		char[] c3 = test.nextWord();
//		for(int j = 0; j < c3.length; j ++){
//			System.out.print(c3[j]);
//		}
//		System.out.print(" ");
//		char[] c4 = test.nextWord();
//		for(int j = 0; j < c4.length; j ++){
//			System.out.print(c4[j]);
//		}
//		System.out.print(" ");
//		char[] c5 = test.nextWord();
//		for(int j = 0; j < c5.length; j ++){
//			System.out.print(c5[j]);
//		}
//		System.out.print(" ");
//		char[] c6 = test.nextWord();
//		for(int j = 0; j < c6.length; j ++){
//			System.out.print(c6[j]);
//		}
//		System.out.print(" ");
//		char[] c7 = test.nextWord();
//		for(int j = 0; j < c7.length; j ++){
//			System.out.print(c7[j]);
//		}
//		System.out.print(" ");
//		char[] c8 = test.nextWord();
//		for(int j = 0; j < c8.length; j ++){
//			System.out.print(c8[j]);
//		}
//		System.out.print(" ");
//
//	}
	
}
