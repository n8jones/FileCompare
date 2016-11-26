package me.natejones.fc;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractComparator<T> implements IFileComparator {
	private final Map<IFileNode, T> values = new HashMap<>();

	public abstract T computeValue(IFileNode node) throws IOException;

	public double compare(T value1, T value2) {
		return Objects.deepEquals(value1, value2) ? 1.0 : 0.0;
	}

	@Override
	public double compare(IFileNode node1, IFileNode node2) throws IOException {
		T value1 = getValue(node1);
		T value2 = getValue(node2);
		return compare(value1, value2);
	}

	public T getValue(IFileNode node) throws IOException {
		T ret = values.get(node);
		if (ret == null) {
			ret = computeValue(node);
			values.put(node, ret);
		}
		return ret;
	}
}
