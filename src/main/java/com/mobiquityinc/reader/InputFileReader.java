package com.mobiquityinc.reader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mobiquityinc.exception.APIException;
import com.mobiquityinc.model.Item;
import com.mobiquityinc.model.ItemPackage;

/**
 * This class is responsible for reading an input file from an absolute file
 * path and converting it to a {@link List} of {@link ItemPackage}.
 * 
 * If one of the constraints is not met, the program will throw an
 * {@APIException} with the details and will be terminated.
 * 
 * @author dfjmax
 *
 */
public class InputFileReader {

	/**
	 * Regular expression that represents a valid input line in the file. Example
	 * valid line: " 10 : (1,20,€20) (2,40,€15) "
	 */
	private static String inputLineRegex = "^\\d+\\s*?:\\s*\\(\\s*\\d+\\s*,\\s*\\d*\\.{0,1}\\d+\\s*,\\s*€\\d*\\.{0,1}\\d+\\s*\\).*$";

	/**
	 * Regular expression that represents a sanitized(without spaces/euro sign)
	 * item. Example valid item: "(1,20,20)"
	 */
	private static Pattern itemRegex = Pattern.compile("\\((\\d+),(\\d+\\.?\\d*?),(\\d+)\\)");

	/**
	 * Reads a {@link List} of {@link ItemPackage} from an absolute file path.
	 * 
	 * @param inputFilePath the input file absolute path
	 * @return a {@link List} of {@link ItemPackage}
	 * 
	 * @throws {@link APIException} when the input file path is invalid or when the
	 *         file contains invalid data
	 */
	public List<ItemPackage> read(String inputFilePath) {

		// Create the file pointer
		File inputFile = new File(inputFilePath);

		// Create empty list of packages
		List<ItemPackage> itemPackages = new ArrayList<>();

		// Scanner is automatically closed at the end of the try statement
		try (Scanner scanner = new Scanner(inputFile)) {

			// Iterate through the file
			while (scanner.hasNextLine()) {
				// Parse and add the test case to the list
				String inputLine = scanner.nextLine();
				itemPackages.add(parseLine(inputLine));
			}

		} catch (IOException e) {
			// Wrap exception
			throw new APIException("Unable to read the specified test cases file.", e);
		}

		return itemPackages;
	}

	/**
	 * Checks if an input line has the valid format specified in the regular
	 * expression.
	 * 
	 * Package visibility for easy testing.
	 * 
	 * @param line the line to be validated
	 * @return returns true if the line is valid or false if it is not
	 */
	boolean isLineValid(String line) {
		return line.matches(inputLineRegex);
	}

	/**
	 * Parses a {@link ItemPackage} from an input line read from the file.
	 * 
	 * Package visibility for easy unit testing.
	 * 
	 * @param inputLine the input line to be parsed
	 * @param index     the index of the line being processed
	 * @return a {@Link ItemPackage}
	 * 
	 * @throws APIException when the passed input line is contains invalid data
	 */
	ItemPackage parseLine(String inputLine) {

		// General validation of the input line
		if (!isLineValid(inputLine)) {
			throw new APIException("Error parsing test case: invalid format.");
		}

		// Sanitize inputLine and split parameters
		String parameters[] = inputLine.replaceAll("[ €]", "").split(":");

		// Parse and check max package weight constraint
		int maxPackageWeight = Integer.parseInt(parameters[0]);

		// Create new list to store the package items
		List<Item> items = new LinkedList<>();
		Matcher itemMatcher = itemRegex.matcher(parameters[1]);

		while (itemMatcher.find()) {
			Integer index = Integer.valueOf(itemMatcher.group(1));
			Float weight = Float.valueOf(itemMatcher.group(2));
			Float cost = Float.valueOf(itemMatcher.group(3));

			// Exclude items that does not fit in the package
			if (weight <= maxPackageWeight) {
				items.add(new Item(index, weight, cost));
			}
		}

		// Return the test case
		return new ItemPackage(maxPackageWeight, items);
	}

}
