package me.natejones.fc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

public class FindDuplicatesTest {
	@Test
	public void test_find_duplicates() throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		List<IFileComparator> comparators = new ArrayList<>();
		comparators.add(new SizeComparator());
		comparators.add(new TypeComparator());
		comparators.add(new ChecksumComparator(md));
		FindDuplicates fd = new FindDuplicates("./src/test/resources",
				"./src/test/resources", comparators);
		Collection<FilePair> pairs = fd.findDuplicates();
		assertEquals("pairs size", 1, pairs.size());
		FilePair pair = pairs.iterator().next();
		assertTrue("pair node1", pair.getNode1().getName().endsWith("A.txt"));
	}

	@Test
	public void test_find_duplicates_search_archives() throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		List<IFileComparator> comparators = new ArrayList<>();
		comparators.add(new SizeComparator());
		comparators.add(new TypeComparator());
		comparators.add(new ChecksumComparator(md));
		FindDuplicates fd = new FindDuplicates("./src/test/resources/test.zip",
				"./src/test/resources/test.zip", comparators);
		fd.setSearchArchives(true);
		Collection<FilePair> pairs = fd.findDuplicates();
		assertEquals("pairs size", 1, pairs.size());
		FilePair pair = pairs.iterator().next();
		assertTrue("pair node1", pair.getNode1().getName().endsWith("A.txt"));
	}
}
