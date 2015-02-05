package com.storeapp.util;

public class ParseNumber {

	private boolean success;
	private double value;

	public boolean tryParseDouble(String input) {

		try {
			value = Double.parseDouble(input);
			success = true;
		} catch (Exception e) {

		}
		return success;
	}

	public double getValue() {
		return value;
	}

}
