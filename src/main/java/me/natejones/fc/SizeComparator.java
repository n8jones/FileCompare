package me.natejones.fc;

import java.io.IOException;

public class SizeComparator extends AbstractComparator<Long> {
	@Override
	public Long computeValue(IFileNode node) throws IOException {
		return node.getSize();
	}
}
