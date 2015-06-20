package com.github.easypack.mock;

import java.io.File;
import java.util.Collection;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.metadata.ArtifactMetadata;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.filter.ArtifactFilter;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.OverConstrainedVersionException;
import org.apache.maven.artifact.versioning.VersionRange;

/**
 * An {@link Artifact} mock for unit testing packagings.
 * 
 * @author agusmunioz
 *
 */
public class ArtifactMock implements Artifact {

	private File file;

	@Override
	public int compareTo(Artifact o) {

		return 0;
	}

	@Override
	public String getGroupId() {

		return null;
	}

	@Override
	public String getArtifactId() {

		return null;
	}

	@Override
	public String getVersion() {

		return null;
	}

	@Override
	public void setVersion(String version) {

	}

	@Override
	public String getScope() {

		return null;
	}

	@Override
	public String getType() {

		return null;
	}

	@Override
	public String getClassifier() {

		return null;
	}

	@Override
	public boolean hasClassifier() {

		return false;
	}

	@Override
	public File getFile() {

		return this.file;
	}

	@Override
	public void setFile(File destination) {
		this.file = destination;
	}

	@Override
	public String getBaseVersion() {

		return null;
	}

	@Override
	public void setBaseVersion(String baseVersion) {

	}

	@Override
	public String getId() {

		return null;
	}

	@Override
	public String getDependencyConflictId() {

		return null;
	}

	@Override
	public void addMetadata(ArtifactMetadata metadata) {

	}

	@Override
	public Collection<ArtifactMetadata> getMetadataList() {

		return null;
	}

	@Override
	public void setRepository(ArtifactRepository remoteRepository) {

	}

	@Override
	public ArtifactRepository getRepository() {

		return null;
	}

	@Override
	public void updateVersion(String version, ArtifactRepository localRepository) {

	}

	@Override
	public String getDownloadUrl() {

		return null;
	}

	@Override
	public void setDownloadUrl(String downloadUrl) {

	}

	@Override
	public ArtifactFilter getDependencyFilter() {

		return null;
	}

	@Override
	public void setDependencyFilter(ArtifactFilter artifactFilter) {

	}

	@Override
	public ArtifactHandler getArtifactHandler() {

		return null;
	}

	@Override
	public List<String> getDependencyTrail() {

		return null;
	}

	@Override
	public void setDependencyTrail(List<String> dependencyTrail) {

	}

	@Override
	public void setScope(String scope) {

	}

	@Override
	public VersionRange getVersionRange() {

		return null;
	}

	@Override
	public void setVersionRange(VersionRange newRange) {

	}

	@Override
	public void selectVersion(String version) {

	}

	@Override
	public void setGroupId(String groupId) {

	}

	@Override
	public void setArtifactId(String artifactId) {

	}

	@Override
	public boolean isSnapshot() {

		return false;
	}

	@Override
	public void setResolved(boolean resolved) {

	}

	@Override
	public boolean isResolved() {

		return false;
	}

	@Override
	public void setResolvedVersion(String version) {

	}

	@Override
	public void setArtifactHandler(ArtifactHandler handler) {

	}

	@Override
	public boolean isRelease() {

		return false;
	}

	@Override
	public void setRelease(boolean release) {

	}

	@Override
	public List<ArtifactVersion> getAvailableVersions() {

		return null;
	}

	@Override
	public void setAvailableVersions(List<ArtifactVersion> versions) {

	}

	@Override
	public boolean isOptional() {

		return false;
	}

	@Override
	public void setOptional(boolean optional) {

	}

	@Override
	public ArtifactVersion getSelectedVersion()
			throws OverConstrainedVersionException {

		return null;
	}

	@Override
	public boolean isSelectedVersionKnown()
			throws OverConstrainedVersionException {

		return false;
	}

}
