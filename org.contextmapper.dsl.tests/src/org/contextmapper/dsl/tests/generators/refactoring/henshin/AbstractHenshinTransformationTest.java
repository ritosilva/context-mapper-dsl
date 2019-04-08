package org.contextmapper.dsl.tests.generators.refactoring.henshin;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.contextmapper.dsl.ContextMappingDSLStandaloneSetup;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class AbstractHenshinTransformationTest {

	protected File testDir;

	@BeforeEach
	void prepare() {
		String dirName = UUID.randomUUID().toString();
		this.testDir = new File(new File(System.getProperty("java.io.tmpdir")), dirName);
		this.testDir.mkdir();
	}

	protected Resource getResourceCopyOfTestCML(String testCMLName) throws IOException {
		File testFile = new File(testDir, testCMLName);
		FileUtils.copyFile(getTestFile(testCMLName), testFile);
		new ContextMappingDSLStandaloneSetup().createInjectorAndDoEMFRegistration();
		ResourceSet rs = new ResourceSetImpl();
		return rs.getResource(URI.createFileURI(testFile.getAbsolutePath()), true);
	}

	protected File getTestFile(String testCMLName) {
		return new File(Paths.get("").toAbsolutePath().toString(), "/integ-test-files/refactorings/" + testCMLName);
	}

	@AfterEach
	void cleanup() throws IOException {
		Path directory = Paths.get(this.testDir.getAbsolutePath());
		Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				Files.delete(file);
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				Files.delete(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

}