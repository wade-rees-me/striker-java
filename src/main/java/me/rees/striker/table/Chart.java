package me.rees.striker.table;

import me.rees.striker.cards.Shoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chart {
	private static final int TABLE_SIZE = 21;

	// Inner class representing a single row in the chart
	private static class ChartRow {
		String key;
		String[] values;

		ChartRow() {
			this.key = "--";
			this.values = new String[Shoe.MAXIMUM_CARD_VALUE + 1];
			Arrays.fill(this.values, "---");
		}
	}

	private String name;
	private List<ChartRow> rows;
	private int nextRow;

	// Constructor
	public Chart(String name) {
		this.name = name;
		this.rows = new ArrayList<>();
		for (int i = 0; i < TABLE_SIZE; i++) {
			rows.add(new ChartRow());
		}
		this.nextRow = 0;
	}

	// Insert a key-value pair into the chart
	public void insert(String key, int up, String value) {
		int index = getRow(key);
		if (index < 0) {
			if (nextRow >= TABLE_SIZE) {
				throw new IllegalStateException("No more space in the chart.");
			}
			index = nextRow++;
			rows.get(index).key = key.toUpperCase();
		}
		rows.get(index).values[Shoe.MINIMUM_CARD_VALUE + up] = value.toUpperCase();
	}

	// Retrieve a value from the chart
	public String getValue(String key, int up) {
		int index = getRow(key);
		if (index < 0) {
			throw new IllegalArgumentException(
				String.format("Cannot find value in %s for %s vs %d", name, key, up));
		}
		return rows.get(index).values[up];
	}

	// Print the entire chart
	public void print() {
		System.out.println(name);
		System.out.println("--------------------2-----3-----4-----5-----6-----7-----8-----9-----X-----A---");
		for (int i = 0; i < nextRow; i++) {
			ChartRow row = rows.get(i);
			System.out.printf("%2s : ", row.key);
			for (String value : row.values) {
				System.out.printf("%4s, ", value);
			}
			System.out.println();
		}
		System.out.println("------------------------------------------------------------------------------");
	}

	// Get the index of a row with the given key
	private int getRow(String key) {
		String keyUpper = key.toUpperCase();
		for (int i = 0; i < nextRow; i++) {
			if (rows.get(i).key.equals(keyUpper)) {
				return i;
			}
		}
		return -1;
	}
}

