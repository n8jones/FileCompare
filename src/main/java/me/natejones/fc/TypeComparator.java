package me.natejones.fc;

import java.io.IOException;

public class TypeComparator extends MemoizedComparator<String> {
	@Override
	public String computeValue(IFileNode node) throws IOException {
		return node.getType();
	}

	@Override
	public double compare(String value1, String value2) {
		return value1.equals(value2) ? 1.0 : 0.0;
	}
}
