package me.natejones.fc;

import java.util.Objects;

class FilePair {
	private final IFileNode node1;
	private final IFileNode node2;

	public FilePair(IFileNode node1, IFileNode node2) {
		this.node1 = Objects.requireNonNull(node1);
		this.node2 = Objects.requireNonNull(node2);
	}

	public IFileNode getNode1() {
		return node1;
	}

	public IFileNode getNode2() {
		return node2;
	}

	@Override
	public int hashCode() {
		return node1.hashCode() + node2.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FilePair other = (FilePair) obj;
		return (node1.equals(other.node1) && node2.equals(other.node2))
				|| (node1.equals(other.node2) && node2.equals(other.node1));
	}

}