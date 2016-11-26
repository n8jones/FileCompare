package me.natejones.fc;

import java.io.IOException;

public class SizeComparator extends MemoizedComparator<Long> {
	@Override
	public Long computeValue(IFileNode node) throws IOException {
		return node.getSize();
	}

	@Override
	public double compare(Long value1, Long value2) {
		return value1.equals(value2) ? 1.0 : 0.0;
	}
}
