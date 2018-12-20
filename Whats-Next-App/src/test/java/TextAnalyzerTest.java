import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import cs601.sideproject.NextWordsHolder;
import cs601.sideproject.TextAnalyzer;

public class TextAnalyzerTest {
	
	
	private TextAnalyzer textAnalyzer;
	private Method methodPreprocessText;
	private Method methodCountNextWords;
	private Method makeNextWordsCountMap;
	private Field wordToCountMap;

	@Before
	public void setUp() throws Exception {
		
		NextWordsHolder nextWordsHolder = new NextWordsHolder();
		textAnalyzer = new TextAnalyzer(nextWordsHolder.getWordToCountMap());
		methodPreprocessText = TextAnalyzer.class.getDeclaredMethod("preprocessText", String.class);
		methodPreprocessText.setAccessible(true);
		methodCountNextWords = TextAnalyzer.class.getDeclaredMethod("countNextWords", String.class, String.class);
		methodCountNextWords.setAccessible(true);
		makeNextWordsCountMap = TextAnalyzer.class.getDeclaredMethod("makeNextWordsCountMap", String.class);
		makeNextWordsCountMap.setAccessible(true);
		
		wordToCountMap = TextAnalyzer.class.getDeclaredField("wordToCountMap");
	    wordToCountMap.setAccessible(true);

	}

	@Test
	public void testPreprocessText1() throws Exception {
		
		String testString1 = "a b c";
		
		String[] expectedStringArray = {"a", "b", "c"};
		
		assertArrayEquals(expectedStringArray, (String[]) methodPreprocessText.invoke(textAnalyzer, testString1));
	}
	
	@Test
	public void testPreprocessText2() throws Exception {
		
		String testString2 = "a  b   c";
		
		String[] expectedStringArray = {"a", "b", "c"};
		
		assertArrayEquals(expectedStringArray, (String[]) methodPreprocessText.invoke(textAnalyzer, testString2));
	}
	
	@Test
	public void testPreprocessText3() throws Exception {
		
		String testString3 = "a	b	c";
		
		String[] expectedStringArray = {"a", "b", "c"};
		
		assertArrayEquals(expectedStringArray, (String[]) methodPreprocessText.invoke(textAnalyzer, testString3));
	}
	
	@Test
	public void testCountNextWords() throws Exception {
		String word = "a";
		String nextWord = "b";
		
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
				
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("a");
		Set<String> actualKeySet2 = map2.keySet();

		String[] expectedKeySet = {"a"};
		String[] expectedKeySet2 = {"b"};

		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("b"), is(1));
	}
	
	
	@Test
	public void testCountNextWords2() throws Exception {
		String word = "a";
		String nextWord = "b";
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
		word = "a";
		nextWord = "c";
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
				
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("a");
		Set<String> actualKeySet2 = map2.keySet();

		String[] expectedKeySet = {"a"};
		String[] expectedKeySet2 = {"b", "c"};
		
//		System.out.println(actualKeySet2.toString());

		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("c"), is(1));		
	}
	
	@Test
	public void testCountNextWords3() throws Exception {
		String word = "a";
		String nextWord = "b";
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
		word = "a";
		nextWord = "c";
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
		word = "a";
		nextWord = "c";
		methodCountNextWords.invoke(textAnalyzer, word, nextWord);
				
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("a");
		Set<String> actualKeySet2 = map2.keySet();

		String[] expectedKeySet = {"a"};
		String[] expectedKeySet2 = {"b", "c"};
		
//		System.out.println(actualKeySet2.toString());

		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("c"), is(2));		
	}
	
	@Test
	public void testMakeNextWordsCountMap() throws Exception {
		
		String line = "a b a c";
		
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("a");
		Set<String> actualKeySet2 = map2.keySet();
		
		String[] expectedKeySet = {"a", "b"};
		String[] expectedKeySet2 = {"b", "c"};
		
		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
	}
	
	@Test
	public void testMakeNextWordsCountMap2() throws Exception {
		
		String line = "a b a c";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		line = "a b";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("a");
		Set<String> actualKeySet2 = map2.keySet();
		
		String[] expectedKeySet = {"a", "b", "c"};
		String[] expectedKeySet2 = {"b", "c"};
		
		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("b"), is(2));		
	}
	
	@Test
	public void testMakeNextWordsCountMap3() throws Exception {
		
		String line = "a b a c";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		line = "a";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("c");
		Set<String> actualKeySet2 = map2.keySet();
		
		String[] expectedKeySet = {"a", "b", "c"};
		String[] expectedKeySet2 = {"a"};
		
		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("a"), is(1));		
	}
	
	@Test
	public void testMakeNextWordsCountMap4() throws Exception {
		
		String line = "The dog runs around the dog.";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("the");
		Set<String> actualKeySet2 = map2.keySet();
		
		String[] expectedKeySet = {"the", "dog", "runs", "around"};
		String[] expectedKeySet2 = {"dog"};
		
		assertArrayEquals(expectedKeySet, actualKeySet.toArray());
		assertArrayEquals(expectedKeySet2, actualKeySet2.toArray());
		assertThat(map2.get("dog"), is(2));		
	}
	
	@Test
	public void testMakeNextWordsCountMap5() throws Exception {
		
		String line = "The dog runs around the dog.";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		line = "The cat runs around the dog.";
		makeNextWordsCountMap.invoke(textAnalyzer, line);
		
		HashMap<String, HashMap<String, Integer>> map = 
				(HashMap<String, HashMap<String, Integer>>) wordToCountMap.get(textAnalyzer);
		Set<String> actualKeySet = map.keySet();
		HashMap<String, Integer> map2 = map.get("dog");
		HashMap<String, Integer> map3 = map.get("the");
		Set<String> actualKeySet2 = map2.keySet();
		
		String[] expectedKeySet = {"the", "dog", "runs", "around", "cat"};
		HashSet<String> expectedSet1 = new HashSet<>(Arrays.asList(expectedKeySet));
		
		String[] expectedKeySet2 = {"runs", "the"};
		HashSet<String> expectedSet2 = new HashSet<>(Arrays.asList(expectedKeySet2));
		
		assertTrue(expectedSet1.containsAll(actualKeySet));
		assertTrue(expectedSet2.containsAll(actualKeySet2));
		assertThat(map3.get("dog"), is(3));		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
