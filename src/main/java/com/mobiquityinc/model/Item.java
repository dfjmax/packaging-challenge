package com.mobiquityinc.model;

import org.apache.commons.validator.routines.FloatValidator;

import com.mobiquityinc.exception.APIException;

/**
 * This class represents an item with its corresponding attributes.
 * 
 * @author dfjmax
 *
 */
public class Item {

	private int index;
	private int weight;
	private float cost;

	/**
	 * Item constructor.
	 * 
	 * @param index  the item index
	 * @param weight the item weight
	 * @param cost   the item cost
	 * 
	 * @throws {@link APIException} when invalid parameters are used to construct
	 *         the {@link Item}
	 */
	public Item(int index, float weight, float cost) {
		super();
		if (!FloatValidator.getInstance().isInRange(weight, 0, 100)
				|| !FloatValidator.getInstance().isInRange(cost, 0, 100)) {
			throw new APIException("The item can not be built as invalid parameters are being used.");
		}
		this.index = index;
		/*
		 * Multiply the weight * 100 so it does not have decimals(Knapsack algorithm
		 * works only with integers)
		 */
		this.weight = (int) weight * 100;
		this.cost = cost;
	}

	/**
	 * Get item index.
	 * 
	 * @return index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Get item weight.
	 * 
	 * @return weight
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Get item cost.
	 * 
	 * @return cost
	 */
	public float getCost() {
		return cost;
	}

}
