package org.excercise;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTextArea;

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
     * Constructing login window.
     */
    public GameOfLife() {
        super();

        this.setTitle("Game of Life");
        this.setSize(new Dimension(400, 400));

        mainPanel = new JPanel(new BorderLayout());
        
        this.setContentPane(mainPanel);
        
        JPanel panel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) panel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        mainPanel.add(panel, BorderLayout.NORTH);
        
        JButton btnLoad = new JButton("Load");
        panel.add(btnLoad);
        btnLoad.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				loadFile();
			}
		});
        
        JButton btnNextGeneration = new JButton("Next generation");
        panel.add(btnNextGeneration);
        btnNextGeneration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				process();				
			}
		});
        
        textArea = new JTextArea();
        textArea.setColumns(FIELD_SIZE);
        textArea.setRows(FIELD_SIZE);
        mainPanel.add(textArea, BorderLayout.CENTER);
    }
    
    private void loadFile() {
    	String content = "";
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
    	System.out.println(content);
    	
		String patternString = "((\\d+),(\\d+));?";
		Pattern pattern = Pattern.compile(patternString, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(content);
		int x, y;
    	for (x = 0; x < FIELD_SIZE; x++) {
    		for (y = 0; y < FIELD_SIZE; y++) {
    			currGen[x][y] = 0;
    		}
    	}
		while (matcher.find()) {
			x = Integer.valueOf(matcher.group(2));
			y = Integer.valueOf(matcher.group(3));
			currGen[x][y] = 1;
		}
		showField();
    }
    
    private void showField() {
    	StringBuilder sb = new StringBuilder();
    	for (int i = 0; i < FIELD_SIZE; i++) {
    		for (int j = 0; j < FIELD_SIZE; j++) {
    			sb.append(currGen[i][j] == 1 ? "X" : "_");
    		}
    		sb.append("\n");
    	}
    	textArea.setText(sb.toString());
    }
    
    private void process() {
    	int x1, y1, count;
    	for (int x = 0; x < FIELD_SIZE; x++) {
    		for (int y = 0; y < FIELD_SIZE; y++) {
    			candidates[x][y] = 0;
    			nextGen[x][y] = 0;
    		}
    	}
    	// Check living cells
    	for (int x = 0; x < FIELD_SIZE; x++) {
    		for (int y = 0; y < FIELD_SIZE; y++) {
    			if (currGen[x][y] == 0) {
    				continue;
    			}
    			count = 0;
    			for (int i = -1; i < 2; i++) {
    				x1 = x + i;
    				if (x1 < 0 || x1 == FIELD_SIZE) {
    					continue;
    				}
    				for (int j = -1; j < 2; j++) {
    					y1 = y + j;
    					if (y1 < 0 || y1 == FIELD_SIZE) {
        					continue;
        				}
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
				if (count == 2 || count == 3) {
					nextGen[x][y] = 1;
				}
    		}
    	}
    	// Process candidates
    	for (int x = 0; x < FIELD_SIZE; x++) {
    		for (int y = 0; y < FIELD_SIZE; y++) {
    			if (candidates[x][y] == 0) {
    				continue;
    			}
    			count = 0;
    			for (int i = -1; i < 2; i++) {
    				x1 = x + i;
    				if (x1 < 0 || x1 == FIELD_SIZE) {
    					continue;
    				}
    				for (int j = -1; j < 2; j++) {
    					y1 = y + j;
    					if (y1 < 0 || y1 == FIELD_SIZE) {
        					continue;
        				}
    					if (i == 0 && j == 0) {
    						continue;
    					}
    					if (currGen[x1][y1] == 1) {
    						count++;
    					}
    				}
    			}
				if (count == 3) {
					nextGen[x][y] = 1;
				}
    		}
    	}
    	for (int x = 0; x < FIELD_SIZE; x++) {
    		for (int y = 0; y < FIELD_SIZE; y++) {
    			currGen[x][y] = nextGen[x][y];
    		}
    	}
    	showField();
    }
}
