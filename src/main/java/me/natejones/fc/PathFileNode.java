package me.natejones.fc;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

class PathFileNode implements IFileNode {
	private final Path path;
	private final String name;

	public PathFileNode(Path path) {
		this.path = path;
		this.name = path.toString();
	}

	public String getName() {
		return name;
	}

	public long getSize() throws IOException {
		return Files.size(path);
	}

	@Override
	public String getType() throws IOException {
		return Files.probeContentType(path);
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