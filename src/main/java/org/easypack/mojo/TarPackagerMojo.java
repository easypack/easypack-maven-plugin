package org.easypack.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.maven.plugins.annotations.Mojo;
import org.easypack.script.BashUtils;

/**
 * Packages the project as a TAR file.
 * 
 * @author agusmunioz
 * 
 */
@Mojo(name = "tar")
public class TarPackagerMojo extends AbstractPackagerMojo {

	private static final String EXTENSION = "tar";

	@Override
	protected String getExtension() {
		return EXTENSION;
	}

	@Override
	protected ArchiveOutputStream getStream(File artifact) throws IOException {

		FileOutputStream output = new FileOutputStream(artifact);

		try {

			return new ArchiveStreamFactory().createArchiveOutputStream(
					ArchiveStreamFactory.TAR, output);

		} catch (ArchiveException e) {
			throw new IOException(e);
		}

	}

	@Override
	protected ArchiveEntry entry(File file, String name) {

		TarArchiveEntry entry = new TarArchiveEntry(file, name);

		if (BashUtils.isBash(file)) {
			entry.setMode(0777);
		}

		return entry;
	}

}
