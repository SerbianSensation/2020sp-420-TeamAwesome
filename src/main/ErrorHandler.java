// Package name
package main;

// System imports
import java.util.HashMap;

// Local imports

/**
 * Class to help with error handling. Contains a map of error codes to their string equivalents.
 *
 */
public class ErrorHandler {
	// Map with the relationship: error_code -> String representation of error
	public static final HashMap<Integer, String> ERROR_TABLE = createErrorTable();
	
	// Initializer for ERROR_TABLE
	private static final HashMap<Integer, String> createErrorTable() {
		// Create temporary map
		HashMap<Integer, String> tempMap = new HashMap<Integer, String>();
		
		// Add error codes to map
		tempMap.put(100, "Failure to close console scanner.");
		tempMap.put(101, "No commands we're passed to console.");
		tempMap.put(102, "Did not get expected number of arguments.");
		tempMap.put(103, "User did not specify a file.");
		tempMap.put(104, "Command was not recognized");
		tempMap.put(105, "File does not exist.");
		
		return tempMap;
	}
	
	/**
	 * Get the string representation of the given code
	 * @param code - error code from table
	 * @return String representation of 'code' if 'code' exists in ERROR_TABLE
	 */
	public static final String toString(int code) {
		if(ERROR_TABLE.containsKey(code))
			return "Error " + code + ": " + ERROR_TABLE.get(code);
		return "No such error found";
	}
}
