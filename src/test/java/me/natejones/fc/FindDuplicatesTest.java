package me.natejones.fc;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.nio.file.Paths;
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

	@Test
	public void test_checksum() throws Exception {
		MessageDigest md = MessageDigest.getInstance("MD5");
		IFileNode a =
				new PathFileNode(Paths.get("./src/test/resources/A.txt"), md);
		IFileNode a2 =
				new PathFileNode(Paths.get("./src/test/resources/A2.txt"), md);
		assertThat("checksum equals", a.getChecksum(), equalTo(a2.getChecksum()));
		IFileNode b =
				new PathFileNode(Paths.get("./src/test/resources/B.txt"), md);
		assertThat("checksum not equals", a.getChecksum(),
				not(equalTo(b.getChecksum())));
	}
}
