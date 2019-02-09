package com.mobiquityinc.packer;

import static java.lang.Math.max;

import java.util.LinkedList;
import java.util.List;

import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.ItemPackage;
import com.mobiquityinc.reader.InputFileReader;

import junit.framework.TestCase;

/**
 * Class responsible of finding the best items combination to build a package
 * with a max weight.
 * 
 * Its main method {@link #pack(String)} receives an input file absolute path
 * with a list of test cases, and returns a String with the result for each
 * {@link TestCase}
 * 
 * I did a research and read about the knapsack algorithm using dynamic
 * programming and implemented this class using that solution.
 * 
 * @author dfjmax
 *
 */
public class Packer {

	/**
	 * This method receives an absolute path to a file containing the test cases,
	 * and calls the corresponding methods to build the end packages with items
	 * having the best value/weight ratio.
	 * 
	 * @param inputFile the absolute path to the input file containing the test
	 *                  cases
	 * @return a String containing which items were selected for each package
	 * 
	 * @throws {@link APIException} when the specified file input path does not
	 *         exist or contains invalid data
	 */
	public static String pack(String inputFile) {
		StringBuilder sb = new StringBuilder();
		for (ItemPackage itemPackage : new InputFileReader().read(inputFile)) {
			sb.append("\n" + getItemsForPackage(itemPackage));
		}
		return sb.toString();
	}

	/**
	 * This method gets the best items for a given {@link ItemPackage}.
	 * 
	 * Package private for easy testing.
	 * 
	 * @param itemPackage the {@link ItemPackage}
	 * @return a comma separated string containing the best cost/weight ratio items
	 *         indexes
	 */
	static String getItemsForPackage(ItemPackage itemPackage) {
		/*
		 * Items that does not fit in the package were removed previously in the
		 * InputFileReader. If the item package does not contain any items return "-"
		 */
		if (!itemPackage.hasItems()) {
			return "-";
		}
		/*
		 * If all the items fit inside the package, return the complete list of items
		 */
		if (itemPackage.getTotalItemsWeight() < itemPackage.getMaxWeight()) {
			return itemListToCommaSeparatedIndexString(itemPackage.getItems());
		}

		/*
		 * Otherwise call the knapsack algorithm and calculate the best possible
		 * solution
		 */
		List<Item> selectedItems = knapsack(itemPackage);
		return itemListToCommaSeparatedIndexString(selectedItems);
	}

	/**
	 * 
	 * Dynamic programming algorithm to solve the knapsack problem.
	 * 
	 * @param itemPackage the item package with the list of possible items
	 * 
	 * @return the selected items list
	 */
	private static List<Item> knapsack(ItemPackage itemPackage) {
		// Store the item size
		int itemSize = itemPackage.getItems().size();

		// Matrix to store the possible solutions
		double[][] possibleSolutions = new double[itemSize + 1][itemPackage.getMaxWeight() + 1];

		// Initialize first line to zero
		for (int i = 0; i <= itemPackage.getMaxWeight(); i++) {
			possibleSolutions[0][i] = 0;
		}

		// Iterate items
		for (int index = 1; index <= itemSize; index++) {

			// Iterate weights
			for (int weight = 1; weight <= itemPackage.getMaxWeight(); weight++) {

				// The current item
				Item item = itemPackage.getItems().get(index - 1);

				// If the item does not fits within the actual weight take the previous one
				if (item.getWeight() > weight) {
					possibleSolutions[index][weight] = possibleSolutions[index - 1][weight];
				} else {
					// Cost when taking the item
					double tookItemCost = item.getCost() + possibleSolutions[index - 1][weight - item.getWeight()];
					// Compare the costs of the actual item and the previous one, and take the
					// better one
					possibleSolutions[index][weight] = max(tookItemCost, possibleSolutions[index - 1][weight]);
				}

			}
		}
		return determineSolution(itemPackage, possibleSolutions);
	}

	/**
	 * 
	 * Determines the best solution among the previously calculated possibilities
	 * 
	 * @param itemPackage       the package being processed
	 * @param possibleSolutions a matrix with possible combinations of packages
	 * 
	 * @return a list of items with the best cost/weight ratio for the package
	 */
	private static List<Item> determineSolution(ItemPackage itemPackage, double[][] possibleSolutions) {

		// Store the item size
		int itemSize = itemPackage.getItems().size();

		// Store the capacity
		int capacity = itemPackage.getMaxWeight();

		// Items with the best value
		List<Item> selectedItems = new LinkedList<>();

		for (int index = itemSize - 1; index > 0; index--) {

			if (possibleSolutions[index][capacity] != possibleSolutions[index - 1][capacity]) {

				Item item = itemPackage.getItems().get(index - 1);
				selectedItems.add(item);
				capacity -= item.getWeight();
			}
		}
		return selectedItems;
	}

	/**
	 * Gets the indexes of the selected items as a comma separated string.
	 * 
	 * @return the items indexes as string, comma separated
	 */
	private static String itemListToCommaSeparatedIndexString(List<Item> items) {
		// Return the indexes sorted in ascending order
		String indexes[] = items.stream().map(Item::getIndex).sorted().map(i -> i.toString()).toArray(String[]::new);
		return String.join(",", indexes);
	}

}
