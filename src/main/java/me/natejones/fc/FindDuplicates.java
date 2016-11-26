package me.natejones.fc;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.logging.Logger;

public class FindDuplicates {
	private static final Logger log =
			Logger.getLogger(FindDuplicates.class.getName());
	private final Map<Path, IFileNode> nodeCache = new HashMap<>();
	private final Comparator<IFileNode> SIZE_COMP =
			Comparator.comparingLong(n -> {
				try {
					return n.getSize();
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
			});
	private final String target1;
	private final String target2;
	private final MessageDigest digest;

	public FindDuplicates(String target1, String target2, MessageDigest digest) {
		this.target1 = Objects.requireNonNull(target1);
		this.target2 = Objects.requireNonNull(target2);
		this.digest = Objects.requireNonNull(digest);
	}

	public static void main(String[] args)
			throws IOException, NoSuchAlgorithmException {
		if (args.length != 2) {
			System.out.println("Usage: java " + FindDuplicates.class.getName()
					+ " [file/dir] [file/dir]");
			return;
		}
		FindDuplicates main = new FindDuplicates(args[0], args[1],
				MessageDigest.getInstance("MD5"));
		Collection<FilePair> duplicates = main.findDuplicates();
		final PrintStream out = System.out;
		int count = 0;
		out.println("[");
		for (FilePair dup : duplicates){
			if(count++ < 1)
				out.println("\t{");
			else 
				out.println("\t,{");
			out.print("\t\t\"node1\": \"");
			out.print(dup.getNode1().getName().replace("\\", "\\\\"));
			out.print("\",\n\t\t\"node2\": \"");
			out.print(dup.getNode2().getName().replace("\\", "\\\\"));
			out.println("\"\n\t}");
		}
		out.print("]");
	}

	public Collection<FilePair> findDuplicates() throws IOException {
		Set<FilePair> processed = new HashSet<>();
		List<IFileNode> nodes1 = findNodes(target1);
		nodes1.sort(SIZE_COMP);
		List<IFileNode> nodes2 = findNodes(target2);
		nodes2.sort(SIZE_COMP);
		List<FilePair> pairs = new ArrayList<>(nodes1.size());
		int start = 0;
		log.info("Matching...");
		for (IFileNode node1 : nodes1) {
			for (int i = start; i < nodes2.size(); i++) {
				IFileNode node2 = nodes2.get(i);
				if (node1.getSize() > node2.getSize()) {
					start = i;
					break;
				}
				FilePair pair = new FilePair(node1, node2);
				if (!processed.contains(pair)) {
					processed.add(pair);
					if (match(node1, node2))
						pairs.add(pair);
				}
			}
		}
		log.info("Done.");
		return pairs;
	}

	public boolean match(IFileNode node1, IFileNode node2) throws IOException {
		if (Objects.equals(node1.getName(), node2.getName()))
			return false;
		if (node1.getSize() != node2.getSize())
			return false;
		if (!Objects.equals(node1.getType(), node2.getType()))
			return false;
		if (!Arrays.equals(node1.getChecksum(), node2.getChecksum()))
			return false;
		try (InputStream is1 = node1.open(); InputStream is2 = node2.open()) {
			byte[] buffer1 = new byte[1024];
			byte[] buffer2 = new byte[1024];
			int size;
			while ((size = is1.read(buffer1)) > 0) {
				if (size != is2.read(buffer2))
					return false;
				for (int i = 0; i < size; i++)
					if (buffer1[i] != buffer2[i])
						return false;
			}
		}
		return true;
	}

	public List<IFileNode> findNodes(String target) throws IOException {
		List<IFileNode> nodes = new ArrayList<>();
		Queue<Path> paths = new LinkedList<>();
		Path path = Paths.get(target).toAbsolutePath().normalize();
		log.info("Accessing: " + path);
		do {
			if (Files.isReadable(path)) {
				if (Files.isDirectory(path))
					Files.list(path).forEach(paths::add);
				else if (Files.isRegularFile(path))
					nodes.add(nodeCache.computeIfAbsent(path,
							p -> new PathFileNode(p, digest)));
			}
		} while ((path = paths.poll()) != null);
		return nodes;
	}
}
