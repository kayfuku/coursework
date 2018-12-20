package cs601.project1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeSet;


/**
 * Inverted Index data structure to support searching of text. 
 * This maps terms or ASIN to the documents where those terms appear or have the ASIN. 
 * This also has a master table that maps document id to the document object. 
 * 
 * @author Kei Fukutani
 */
public class InvertedIndex {

	// Master table. Key: document id, Value: document object
	private HashMap<Integer, Doc> masterTable = new HashMap<>();
	// Inverted index For ASIN. Key: ASIN, Value: a list of document id
	private HashMap<String, ArrayList<Integer>> invertedIndexForAsin = new HashMap<>();
	// Inverted index For exact match. Key: term, Value: a list of DocId object sorted by count
	private HashMap<String, TreeSet<DocId>> invertedIndexForExactMatch= new HashMap<>();


	/**
	 * Stores data in master table and inverted index for ASIN and exact match while 
	 * AmazonSearchFileParser.readFile method reading a file. 
	 * @param doc  the document object to be loaded
	 * @param textAnalyzer  the text analyzer to be used to preprocess text 
	 */
	public void storeData(Doc doc, TextAnalyzer textAnalyzer) {
		addDataToMasterTable(doc);
		addDataToInvertedIndexForAsin(doc);		
		addDataToInvertedIndexForExactMatch(doc, textAnalyzer);
	}


	/**
	 * Populates master table.
	 * @param doc  the document object to be loaded
	 */
	private void addDataToMasterTable(Doc doc) {
		masterTable.put(doc.getId(), doc);
	}


	/**
	 * Stores data in inverted index for ASIN. 
	 * @param doc the document object to be stored
	 */
	private void addDataToInvertedIndexForAsin(Doc doc) {
		String asin = doc.getAsin().toLowerCase();
		int id = doc.getId();

		if (!invertedIndexForAsin.containsKey(asin)) {
			ArrayList<Integer> idList = new ArrayList<>();
			idList.add(id);
			invertedIndexForAsin.put(asin, idList);
		} else {
			ArrayList<Integer> idList = invertedIndexForAsin.get(asin);
			idList.add(id);
		}
	}


	/**
	 * Displays the content of the inverted index for ASIN for testing. 
	 */
	public void displayAllInvertedIndexForAsin() {
		for (Map.Entry<String, ArrayList<Integer>> e : invertedIndexForAsin.entrySet()) {
			System.out.print("asin: " + e.getKey() + " ");
			for (Integer id : e.getValue()) {
				System.out.print(id + " ");
			}
			System.out.print("\n");
		}
	}


	/**
	 * Stores data in inverted index for exact match.
	 * This creates a map for each document that maps each word in a document to DocId object that 
	 * has an id of the document and the frequency of the word in the document. 
	 * Using this map, this populates the inverted index where it maps a word to a list of DocId 
	 * objects sorted by the frequency. 
	 * @param doc  the document object to be stored 
	 * @param textAnalyzer  the text analyzer to be used to preprocess text 
	 */
	private void addDataToInvertedIndexForExactMatch(Doc doc, TextAnalyzer textAnalyzer) {
		// Creates a map that maps words to frequency for each document. 
		HashMap<String, DocId> wordToDocId = new HashMap<>();
		wordToDocId = textAnalyzer.countWordsForExactMatch(doc);

		String word = "";
		DocId docId = null;

		// Populates the inverted index for exact match. 
		for (Map.Entry<String, DocId> e : wordToDocId.entrySet()) {
			word = e.getKey();
			docId = e.getValue();

			if (!invertedIndexForExactMatch.containsKey(word)) {
				TreeSet<DocId> docIdsSortedByCount = new TreeSet<>();
				docIdsSortedByCount.add(docId);
				invertedIndexForExactMatch.put(word, docIdsSortedByCount);
			} else {
				TreeSet<DocId> docIdsSortedByCount = invertedIndexForExactMatch.get(word);
				docIdsSortedByCount.add(docId);				
			}		
		}

	}


	/**
	 * Displays the content of the inverted index for exact match for testing. 
	 */
	public void displayAllInvertedIndexForExactMatch() {
		for (Map.Entry<String, TreeSet<DocId>> e : invertedIndexForExactMatch.entrySet()) {
			System.out.print("word: " + e.getKey() + " ");
			for (DocId docId : e.getValue()) {
				System.out.print(docId + " ");
			}
			System.out.print("\n");
		}
	}


	/**
	 * Provides find functionality.
	 * This gets a list of documents that have a given ASIN. 
	 * @param asin  the ASIN to be used as a key
	 * @return a list of the documents if the documents exist, null otherwise
	 */
	public ArrayList<Doc> find(String asin) {
		ArrayList<Doc> docList = new ArrayList<>();
		ArrayList<Integer> idList = invertedIndexForAsin.get(asin);

		if (idList != null) {
			for (Integer id : idList) {
				docList.add(masterTable.get(id));
			}
		} else {
			return null;
		}

		return docList;
	}


	/**
	 * Provides reviewsearch and qasearch functionality. 
	 * This gets a list of documents that have a given term.
	 * @param term  the term to be used as a key
	 * @return a list of the documents if the documents exist, null otherwise
	 */
	public ArrayList<Doc> searchForExactMatch(String term) {
		ArrayList<Doc> docList = new ArrayList<>();
		TreeSet<DocId> docIds = invertedIndexForExactMatch.get(term);

		if (docIds != null) {
			for (DocId docId : docIds) {
				docList.add(masterTable.get(docId.getId()));
			}
		} else {
			return null;
		}

		return docList;
	}


	/**
	 * Provides reviewpartialsearch and qapartialsearch functionality. 
	 * This gets a list of documents that contain a given term.
	 * @param term  the term to be used as a key
	 * @return a list of the documents if the documents exist, null otherwise
	 */
	public ArrayList<Doc> searchForPartialMatch(String term) {
		ArrayList<Doc> docList = new ArrayList<>();
		HashSet<Integer> idSet = new HashSet<>();
		String word = "";

		for (Map.Entry<String, TreeSet<DocId>> entry : invertedIndexForExactMatch.entrySet()) {
			word = entry.getKey();
			if (word.contains(term)) {
				for (DocId docId : entry.getValue()) {
					idSet.add(docId.getId());
				}
			}
		}

		if (!idSet.isEmpty()) {
			for (int id : idSet) {
				docList.add(masterTable.get(id));
			}
		} else {
			return null;
		}

		return docList;
	}



	/**
	 * Provides reviewpartialsearch and qapartialsearch functionality. 
	 * This gets a list of documents that contain a given term.
	 * @param term  the term to be used as a key
	 * @return a list of the documents if the documents exist, null otherwise
	 */
	//	public ArrayList<Doc> searchForPartialMatch(String term) {
	//		ArrayList<Doc> docList = new ArrayList<>();
	//		String text = "";
	//		
	//		for (Map.Entry<Integer, Doc> entry : masterTable.entrySet()) {
	//			Doc doc = entry.getValue();
	//			text = doc.getText();
	//			if (text.contains(term)) {
	//				docList.add(doc);
	//			}
	//		}
	//		
	//		if (docList.isEmpty()) {
	//			return null;
	//		}
	//		
	//		return docList;
	//	}































}
