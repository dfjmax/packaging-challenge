package com.mobiquityinc.model;

import static java.util.Collections.unmodifiableList;
import static java.util.Comparator.comparing;

import java.util.List;

import org.apache.commons.validator.routines.IntegerValidator;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.reader.InputFileReader;

/**
 * This class represents a package of items.
 * 
 * It's assumed and validated in the {@link InputFileReader} class that only
 * items that could fit in the package will be added to the list, other ones
 * will be excluded directly.
 * 
 * @author dfjmax
 *
 */
public class ItemPackage {

	private int maxWeight;
	private List<Item> items;

	/**
	 * Creates an {@link ItemPackage}.
	 * 
	 * @param maxWeight the package max weight
	 * @param items     the package items
	 * 
	 * @throws {@link APIException} when invalid parameters are used to construct
	 *         the {@link ItemPackage}
	 */
	public ItemPackage(int maxWeight, List<Item> items) {
		super();
		if (!IntegerValidator.getInstance().isInRange(maxWeight, 0, 100)) {
			throw new APIException("The package can not be built as invalid parameters are being used.");
		}
		/*
		 * Item weights were multiplied by 100 to avoid having decimals, so in order to
		 * maintain the weight relationship it should be done here as well
		 */
		this.maxWeight = maxWeight * 100;
		this.items = items;
		/*
		 * Sort the items by weight and cost
		 */
		this.items.sort(comparing(Item::getWeight).thenComparing(Item::getCost));
	}

	/**
	 * Gets the max weight for this {@link ItemPackage}.
	 * 
	 * @return the max weight for this {@link ItemPackage}
	 */
	public int getMaxWeight() {
		return this.maxWeight;
	}

	/**
	 * Gets the {@link Item} list.
	 * 
	 * @return the {@link Item} list
	 */
	public List<Item> getItems() {
		// Return the item list while keeping it unmodifiable from the outside.
		return unmodifiableList(this.items);
	}

	/**
	 * Returns the sum of all the items in this {@link ItemPackage}.
	 * 
	 * @return the sum of the items
	 */
	public Double getTotalItemsWeight() {
		return this.items.stream().mapToDouble(Item::getWeight).sum();
	}

	/**
	 * Check if the {@link ItemPackage} has items to be evaluated.
	 * 
	 * @return true if the {@link ItemPackage} has items, false otherwise
	 */
	public boolean hasItems() {
		return !this.items.isEmpty();
	}

}
