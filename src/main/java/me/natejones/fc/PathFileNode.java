package me.natejones.fc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

class PathFileNode implements IFileNode {
	private final Path path;
	private final MessageDigest digest;
	private final String name;
	private long size = -1;
	private String type;
	private byte[] checksum;

	public PathFileNode(Path path, MessageDigest digest) {
		this.path = path;
		this.digest = digest;
		this.name = path.toString();
	}

	public String getName() {
		return name;
	}

	public long getSize() throws IOException {
		if (size < 0)
			size = Files.size(path);
		return size;
	}

	@Override
	public String getType() throws IOException {
		if (type == null)
			type = Files.probeContentType(path);
		return type;
	}

	@Override
	public byte[] getChecksum() throws IOException {
		if (checksum == null) {
			try (InputStream is = open()) {
				byte[] buffer = new byte[1024];
				int size;
				digest.reset();
				while ((size = is.read(buffer)) > 0)
					digest.update(buffer, 0, size);
				checksum = digest.digest();
			}
		}
		return checksum;
	}

	@Override
	public InputStream open() throws IOException {
		return Files.newInputStream(path);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PathFileNode other = (PathFileNode) obj;
		if (path == null) {
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;
		return true;
	}
}