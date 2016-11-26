package me.natejones.fc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;
import java.util.Collection;

import org.junit.Test;

public class FindDuplicatesTest {
	@Test
	public void test_find_duplicates() throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		FindDuplicates fd = new FindDuplicates("./src/test/resources",
				"./src/test/resources", md);
		Collection<FilePair> pairs = fd.findDuplicates();
		assertEquals("pairs size", 1, pairs.size());
		FilePair pair = pairs.iterator().next();
		assertTrue("pair node1", pair.getNode1().getName().endsWith("A.txt"));
	}

}
