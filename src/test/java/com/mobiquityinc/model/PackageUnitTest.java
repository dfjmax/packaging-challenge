package com.mobiquityinc.model;

import static java.lang.Double.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mobiquityinc.exception.APIException;

/**
 * Test suite for the {@link ItemPackage} class.
 * 
 * @author dfjmax
 *
 */
public class PackageUnitTest {

	private Item testItem1;
	private Item testItem2;

	@Before
	public void setUp() {
		this.testItem1 = new Item(1, 10, 10);
		this.testItem2 = new Item(2, 20, 20);
	}

	@Test
	public void newPackage_withValidParameters_shouldBeBuilt() {
		ItemPackage pack = new ItemPackage(20, Arrays.asList(testItem1));
		Assert.assertNotNull("Test case should not be null", pack);
	}

	@Test(expected = APIException.class)
	public void newItemPackage_withInvalidMaxWeight_shouldThrowException() {
		new ItemPackage(1000, Arrays.asList(testItem1));
	}

	@Test
	public void newItemPackage_withValidParameters_shouldReturnCorrectTotalItemsWeight() {
		ItemPackage itemPackage = new ItemPackage(50, Arrays.asList(testItem1, testItem2));
		assertNotNull("Item package should not be null", itemPackage);
		assertEquals("Sum should of the weights should be 3000", valueOf(3000), itemPackage.getTotalItemsWeight());
	}

}
