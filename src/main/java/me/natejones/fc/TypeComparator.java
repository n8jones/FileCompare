package me.natejones.fc;

import java.io.IOException;
import java.util.Objects;

public class TypeComparator extends AbstractComparator<String> {
	@Override
	public String computeValue(IFileNode node) throws IOException {
		return node.getType();
	}
}
