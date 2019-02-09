package com.mobiquityinc.reader;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.ItemPackage;

/**
 * Test suite for the {@link InputFileReader} class.
 * 
 * @author dfjmax
 *
 */
public class InputFileReaderUnitTest {

	// The test cases file
	private File testCasesFile;
	private InputFileReader reader;

	/**
	 * Setup the test file class to be used in all the tests.
	 */
	@Before
	public void setUp() {
		this.testCasesFile = new File("src/test/resources/integration-test-cases");
		this.reader = new InputFileReader();
	}

	@Test
	public void isLineValid_withNoItems_shouldReturnFalse() {
		String line = "81 : ";
		assertFalse(reader.isLineValid(line));
	}

	@Test
	public void isLineValid_withInvalidItems_shouldReturnFalse() {
		String line = "81 (1,53.38) (88.62,€98) (6,46.34,€48)";
		assertFalse(reader.isLineValid(line));
	}

	@Test
	public void isLineValid_withValidMaxWeightAndItems_shouldReturnTrue() {
		String line = "81 : (1,53.38,€45) (2,88.62,€98) (3,78.48,€3) (4,72.30,€76) (5,30.18,€9) (6,46.34,€48)";
		assertTrue(reader.isLineValid(line));
	}

	@Test
	public void read_validFile_shouldNotThrowException() throws APIException {
		this.reader.read(this.testCasesFile.getAbsolutePath());
	}

	@Test(expected = APIException.class)
	public void read_invalidFile_shouldThrowException() throws APIException {
		this.reader.read("c://");
	}

	@Test(expected = APIException.class)
	public void parseLine_invalidInputLine_shouldThrowException() throws APIException {
		this.reader.parseLine("81 (1,53.38) (88.62,€98) (6,46.34,€48)");
	}

	@Test
	public void parseLine_validInputLine_shouldBuildItemPackage() throws APIException {
		ItemPackage itemPackage = this.reader.parseLine("81 : (1,53.38,€45) (5,30.18,€9) (6,46.34,€48)");
		assertNotNull("Test case should not be null", itemPackage);
	}

	@Test
	public void parseLine_withItemsThatDoesNotFitInMaxWeight_shouldReturnEmptyItemPackage() throws APIException {
		ItemPackage itemPackage = this.reader.parseLine("8 : (1,15.3,€34)");
		assertNotNull("Item package should not be null", itemPackage);
		assertFalse("Item package should be empty", itemPackage.hasItems());
	}

}
