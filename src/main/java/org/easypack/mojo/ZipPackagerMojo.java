package org.easypack.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.maven.plugins.annotations.Mojo;
import org.easypack.script.BashUtils;

/**
 * Packages as a ZIP file.
 * 
 * @author agusmunioz
 * 
 */
@Mojo(name = "zip")
public class ZipPackagerMojo extends AbstractPackagerMojo {

	private static final String EXTENSION = "zip";

	@Override
	protected String getExtension() {
		return EXTENSION;
	}

	@Override
	protected ArchiveOutputStream getStream(File artifact) throws IOException {

		FileOutputStream output = new FileOutputStream(artifact);

		try {

			return new ArchiveStreamFactory().createArchiveOutputStream(
					ArchiveStreamFactory.ZIP, output);

		} catch (ArchiveException e) {
			throw new IOException(e);
		}

	}

	@Override
	protected ArchiveEntry entry(File file, String name) {

		ZipArchiveEntry entry = new ZipArchiveEntry(file, name);

		if (BashUtils.isBash(file)) {
			entry.setUnixMode(0777);
		}

		return entry;
	}

}
