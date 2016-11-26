package me.natejones.fc;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class ChecksumComparator extends MemoizedComparator<byte[]> {
	private final MessageDigest digest;

	public ChecksumComparator(MessageDigest md) {
		this.digest = md;
	}

	@Override
	public byte[] computeValue(IFileNode node) throws IOException {
		try (InputStream is = node.open()) {
			byte[] buffer = new byte[1024];
			int size;
			digest.reset();
			while ((size = is.read(buffer)) > 0)
				digest.update(buffer, 0, size);
			return digest.digest();
		}
	}

	@Override
	public double compare(byte[] value1, byte[] value2) {
		return MessageDigest.isEqual(value1, value2) ? 1.0 : 0.0;
	}

}
