package com.mobiquityinc.model;

import org.junit.Assert;
import org.junit.Test;

import com.mobiquityinc.exception.APIException;

/**
 * Test suite for the {@link Item} class.
 * 
 * @author dfjmax
 *
 */
public class ItemUnitTest {

	@Test(expected = APIException.class)
	public void newItem_withInvalidWeight_shouldThrowException() {
		new Item(1, 1000, 50);
	}

	@Test(expected = APIException.class)
	public void newItem_withInvalidCost_shouldThrowException() {
		new Item(1, 50, 1000);
	}

	@Test
	public void newItem_withValidParameters_shouldBeBuilt() {
		Item item = new Item(1, 20, 50);
		Assert.assertNotNull("Item should not be null", item);
	}

}
