package me.natejones.fc;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import javax.activation.MimetypesFileTypeMap;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

public class ArchiveFileNode implements IFileNode {
	private final Path archive;
	private final String name;
	private final long size;
	
	public ArchiveFileNode(Path archive, String name, long size){
		this.archive = archive;
		this.name = name;
		this.size = size;
	}

	@Override
	public String getName() {
		return archive + "!" + name;
	}

	@Override
	public long getSize() throws IOException {
		return size;
	}

	@Override
	public String getType() throws IOException {
		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
		return map.getContentType(name);
	}

	@Override
	public InputStream open() throws IOException {
		ArchiveStreamFactory asf = new ArchiveStreamFactory();
		byte[] bytes = new byte[(int) size];
		try (InputStream in = new BufferedInputStream(
				Files.newInputStream(archive, StandardOpenOption.READ));
				ArchiveInputStream ain =
						asf.createArchiveInputStream(in)) {
			ArchiveEntry entry;
			while((entry = ain.getNextEntry())!=null){
				if(name.equals(entry.getName()))
					break;
			}
			if(entry == null)
				throw new IOException("entry not found");
			ain.read(bytes);
		}
		catch (ArchiveException e) {
			throw new IOException("Problem reading archive", e);
		}
		return new ByteArrayInputStream(bytes);
	}
}
