package org.excercise;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main app class.
 */
public class GameOfLife extends JFrame {
	/**
	 * This is default serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Field size.
	 */
	private static final int FIELD_SIZE = 20;
	/**
	 * Main panel of login window.
	 */
	private JPanel mainPanel;
	/**
	 * Text area to display field.
	 */
	private JTextArea textArea;
	/**
	 * Current cells array.
	 */
	private int[][] currGen = new int[FIELD_SIZE][FIELD_SIZE];
	/**
	 * Cells array for next generation.
	 */
	private int[][] nextGen = new int[FIELD_SIZE][FIELD_SIZE];
	/**
	 * Potential birth places array.
	 */
	private int[][] candidates = new int[FIELD_SIZE][FIELD_SIZE];

	/**
	 * Main method of the app.
	 *
	 * @param args Main method args
	 */
	public static void main(final String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	/**
	 * Create GUI and show login window.
	 */
	private static void createAndShowGUI() {
		final JFrame frmGame = new GameOfLife();
		frmGame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmGame.setResizable(false);
		frmGame.pack();
		frmGame.setLocationRelativeTo(null);
		frmGame.setVisible(true);
	}

	/**
	 * Constructing game window.
	 */
	public GameOfLife() {
		// Calling parent (JFrame) constructor.
		super();

		// Set size and title of the window
		this.setTitle("Game of Life");
		this.setSize(new Dimension(400, 400));
		// Constructing GUI panel
		mainPanel = new JPanel(new BorderLayout());
		// The panel will take up all available space
		this.setContentPane(mainPanel);
		// Constructing buttons panel
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		mainPanel.add(panel, BorderLayout.NORTH);
		// Load data button
		JButton btnLoad = new JButton("Load");
		panel.add(btnLoad);
		btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadFile();
			}
		});
		// Next step button
		JButton btnNextGeneration = new JButton("Next generation");
		panel.add(btnNextGeneration);
		btnNextGeneration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();
			}
		});
		// Text area to display results
		textArea = new JTextArea();
		textArea.setColumns(FIELD_SIZE);
		textArea.setRows(FIELD_SIZE);
		mainPanel.add(textArea, BorderLayout.CENTER);
	}

	/**
	 * Loading input file
	 */
	private void loadFile() {
		String content = "";
		// Create Reader to load data from a text file
		try(BufferedReader br = new BufferedReader(new FileReader("input.txt"))) {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			content = sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Regular expression will match on 2 numbers (each a sequence of 1 or more digits) separated by comma
		// Groups of numbers are separated by semicolon or a new line
		String patternString = "((\\d+),(\\d+));?";
		Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
		// Regular expression matcher will use the pattern and look it in the loaded input file
		Matcher matcher = pattern.matcher(content);
		int x, y;
		// Emptying current generation array
		for (x = 0; x < FIELD_SIZE; x++) {
			for (y = 0; y < FIELD_SIZE; y++) {
				currGen[x][y] = 0;
			}
		}
		// in the group of 2 numbers first is X coordinate, second is Y
		while (matcher.find()) {
			x = Integer.valueOf(matcher.group(2));
			y = Integer.valueOf(matcher.group(3));
			// We set array item's value to 1 to denote that there is a living cell there
			currGen[x][y] = 1;
		}
		showField();
	}

	/**
	 * Display current generation.
	 * Each line in text area is a row of current generation array.
	 */
	private void showField() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < FIELD_SIZE; i++) {
			for (int j = 0; j < FIELD_SIZE; j++) {
				// "X" for live cell, "_" for empty space
				sb.append(currGen[i][j] == 1 ? "X" : "_");
			}
			sb.append("\n");
		}
		textArea.setText(sb.toString());
	}

	/**
	 * Decide which cells live or die, where new ones are born.
	 */
	private void process() {
		int x1, y1, count;
		// Empty arrays of next generation and candidates.
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				candidates[x][y] = 0;
				nextGen[x][y] = 0;
			}
		}
		// Check living cells
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				// Skipping empty spaces
				if (currGen[x][y] == 0) {
					continue;
				}
				count = 0;
				// Checking neighbors
				for (int i = -1; i < 2; i++) {
					x1 = x + i;
					// Check for out of bounds
					if (x1 < 0 || x1 == FIELD_SIZE) {
						continue;
					}
					for (int j = -1; j < 2; j++) {
						y1 = y + j;
						// Check for out of bounds
						if (y1 < 0 || y1 == FIELD_SIZE) {
							continue;
						}
						// Center is the cell whose neighbors we are counting
						if (i == 0 && j == 0) {
							continue;
						}
						// If a space near a living cell is empty, it's a candidate for birth place.
						if (currGen[x1][y1] == 0) {
							candidates[x1][y1] = 1;
						} else {
							count++;
						}
					}
				}
				// If a cell has 2 or 3 neighbors, it stays alive
				if (count == 2 || count == 3) {
					nextGen[x][y] = 1;
				}
			}
		}
		// Process candidates
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				// Skipping places where cells can't be born
				if (candidates[x][y] == 0) {
					continue;
				}
				count = 0;
				for (int i = -1; i < 2; i++) {
					x1 = x + i;
					// Check for out of bounds
					if (x1 < 0 || x1 == FIELD_SIZE) {
						continue;
					}
					for (int j = -1; j < 2; j++) {
						y1 = y + j;
						// Check for out of bounds
						if (y1 < 0 || y1 == FIELD_SIZE) {
							continue;
						}
						// Center is the cell whose neighbors we are counting
						if (i == 0 && j == 0) {
							continue;
						}
						// This candidate has a living cell near it
						if (currGen[x1][y1] == 1) {
							count++;
						}
					}
				}
				// If a candidate has exactly 3 neighbors, a cell will be born there
				if (count == 3) {
					nextGen[x][y] = 1;
				}
			}
		}
		// We have all we need for new generation: survivors and new cells.
		// Time to copy next generation to current...
		for (int x = 0; x < FIELD_SIZE; x++) {
			for (int y = 0; y < FIELD_SIZE; y++) {
				currGen[x][y] = nextGen[x][y];
			}
		}
		// ...and display it
		showField();
	}
}
