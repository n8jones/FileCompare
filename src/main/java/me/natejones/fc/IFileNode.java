package me.natejones.fc;

import java.io.IOException;
import java.io.InputStream;

interface IFileNode {
	String getName();

	long getSize() throws IOException;

	String getType() throws IOException;

	InputStream open() throws IOException;
}