package hu.dushu.developers.popularmovies;

import junit.framework.TestCase;

/**
 * Created by renfeng on 9/14/15.
 */
public class DetailsActivityFragmentTest extends TestCase {

	public void testStringReplacement() {

		assertEquals("456", DetailsActivityFragment.remove("123,456", "123"));
		assertEquals("9123,456", DetailsActivityFragment.remove("9123,456", "123"));
		assertEquals("9,456", DetailsActivityFragment.remove("9,123,456", "123"));

		assertEquals("123", DetailsActivityFragment.add(null, "123"));
		assertEquals("123,456", DetailsActivityFragment.add("456", "123"));
		assertEquals("123,456,789", DetailsActivityFragment.add("456,789", "123"));

		assertTrue(DetailsActivityFragment.contains("123,456,789", "123"));
		assertTrue(DetailsActivityFragment.contains("123,456,789", "456"));
		assertTrue(DetailsActivityFragment.contains("123,456,789", "789"));
	}
}
