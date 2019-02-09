package com.mobiquityinc.packer;

import static com.mobiquityinc.packer.Packer.getItemsForPackage;
import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.ItemPackage;

/**
 * Test suite for the {@link Packer} class.
 * 
 * @author dfjmax
 *
 */
public class PackerUnitTest {

	// The test cases file
	private File testCasesFile;

	private Item testItem1;
	private Item testItem2;

	@Before
	public void setUp() {
		this.testCasesFile = new File("src/test/resources/integration-test-cases");

		this.testItem1 = new Item(1, 10, 10);
		this.testItem2 = new Item(2, 20, 20);
	}

	@Test
	public void getItemsForPackage_withTotalItemsWeightLessThanMaxWeight_shouldIncludeAllItemsInPackage() {
		ItemPackage itemPackage = new ItemPackage(50, Arrays.asList(testItem1, testItem2));
		assertEquals("Result should contain 1 and 2", "1,2", getItemsForPackage(itemPackage));
	}

	@Test
	public void getItemsForPackage_withEmptyPackage_shouldReturnScoreCharacter() {
		ItemPackage itemPackage = new ItemPackage(50, emptyList());
		assertEquals("Result should be '-'", "-", getItemsForPackage(itemPackage));
	}

	@Test
	public void pack_withCompleteTestCasesFile_shouldReturnExpectedResults() {
		String result = Packer.pack(testCasesFile.getAbsolutePath());
		String expectedResult = "\n4\n-\n2,7\n8,9";
		assertEquals("Result is correct", expectedResult, result);
	}

}
