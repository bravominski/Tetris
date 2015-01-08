package Tetris_ver2_storage;

/**
 * CIS 120 HW10
 * (c) University of Pennsylvania
 * @version 2.0, Mar 2013
 */

// imports necessary libraries for Java swing
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

/**
 * Game Main class that specifies the frame and widgets of the GUI
 */
public class Game implements Runnable {
	public void run() {
		
		final JFrame frame = new JFrame("TETRIS"); // Title
		
		// Status and score panel
		final JPanel panel = new JPanel();
		final JPanel score = new JPanel();
		final JPanel status = new JPanel();
		final JPanel warning = new JPanel();
		frame.add(panel, BorderLayout.SOUTH);
		final JLabel cur_score = new JLabel("   0"); // Initial score
		final JLabel cur_status = new JLabel("Playing..."); // Initial status 
		final JLabel cur_warning = new JLabel("Good Luck!");
		score.add(cur_score);
		status.add(cur_status);
		warning.add(cur_warning);
		panel.add(score, BorderLayout.WEST);
		panel.add(status, BorderLayout.EAST);
		panel.add(warning, BorderLayout.CENTER);
		// Main playing area
		final GameCourt court = new GameCourt(cur_score, cur_status, cur_warning);
		court.setSize(300, 600);
		frame.add(court, BorderLayout.CENTER);

		// Reset_Start button
		final JPanel reset_panel = new JPanel();
		frame.add(reset_panel, BorderLayout.NORTH);

		// Note here that when we add an action listener to the reset
		// button, we define it as an anonymous inner class that is
		// an instance of ActionListener with its actionPerformed()
		// method overridden. When the button is pressed,
		// actionPerformed() will be called.
		final JButton start_reset = new JButton("Start");
		start_reset.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent e) {
				court.start();
				if(start_reset.getLabel().equals("Start")) {
					start_reset.setLabel("Reset");
				}
			}
		});
		reset_panel.add(start_reset);
		// Put the frame on the screen
		frame.setSize(300, 700);
		frame.setLocationRelativeTo(null);
		//frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
	}

	/*
	 * Main method run to start and run the game Initializes the GUI elements
	 * specified in Game and runs it IMPORTANT: Do NOT delete! You MUST include
	 * this in the final submission of your game.
	 */
	public static void main(String[] args) {
		JOptionPane.showMessageDialog(null, "Hi! This is Minsu!\n"
				+ "Thank you so much for playing my game!\n"
				+ "I made a classic Tetris for CIS120 final project!\n"
				+ "It's pretty similiar to Tetris that people mostly know.\n\n"
				+ "Here are some basic key instruction\n"
				+ "Right Arrow => Move right\n"
				+ "Left Arrow => Move left\n"
				+ "Up Arrow => Rotate right\n"
				+ "Down Arrow => Rotate left\n"
				+ "R/r => Reset\n"
				+ "P/p => Pause\n"
				+ "V/v => Previw On and Off\n"
				+ "D/d => Down one unit\n"
				+ "Shfit => Storage In and out\n"
				+ "Spacebar => All the way down\n\n"
				+ "p.s. WATCH OUT! Game gets faster as you score higher *^^*",
				"Instruction", JOptionPane.INFORMATION_MESSAGE);
		SwingUtilities.invokeLater(new Game());
	}
}
