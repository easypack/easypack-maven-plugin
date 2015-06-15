package org.easypack.mojo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.maven.plugins.annotations.Mojo;
import org.easypack.script.BashUtils;

/**
 * Packages the project as a TAR GZ file.
 * 
 * @author agusmunioz
 * 
 */
@Mojo(name = "targz")
public class TarGzPackagerMojo extends AbstractPackagerMojo {

	private static final String EXTENSION = "tar.gz";

	@Override
	protected String getExtension() {
		return EXTENSION;
	}

	@Override
	protected ArchiveOutputStream getStream(File artifact) throws IOException {

		FileOutputStream output = new FileOutputStream(artifact);

		return new TarArchiveOutputStream(
				new GzipCompressorOutputStream(output));

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
