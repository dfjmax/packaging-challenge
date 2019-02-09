package com.mobiquityinc;

import com.mobiquityinc.packer.Packer;

/**
 * Main application class.
 * 
 * @author dfjmax
 *
 */
public class Application {

	public static void main(String[] args) {

		// Validate the user added a path to a file
		if (args.length == 0) {
			System.out.println("No input file!");
		} else {
			System.out.println("Processing items...");
			System.out.println(Packer.pack(args[0]));
		}
	}

}
