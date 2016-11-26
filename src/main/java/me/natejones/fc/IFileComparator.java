package me.natejones.fc;

import java.io.IOException;

public interface IFileComparator {
	/**
	 * Compares 2 files returning how similar they are based on the attributes
	 * being compared.
	 * 
	 * @param node1
	 * @param node2
	 * @return 0.0 not similar to 1.0 exactly similar.
	 * @throws IOException
	 */
	double compare(IFileNode node1, IFileNode node2) throws IOException;
}
