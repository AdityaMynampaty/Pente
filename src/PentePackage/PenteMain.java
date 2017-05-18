package PentePackage;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class PenteMain{

	public static final int EMPTY = 0;
	public static final int BLACKSTONE = -1;
	public static final int WHITESTONE = 1;
	
	public static void main(String[] args) {

		int boardWidth = 1750;
		int boardWidthInSquares = 19;
		
		
		JFrame f = new JFrame ("Play Pente - ALL THE TIME...");
		f.setDefaultCloseOperation(f.EXIT_ON_CLOSE);
		
		f.setSize(boardWidth, boardWidth);
		PenteGameBoard p = new PenteGameBoard(boardWidth-20, boardWidthInSquares);
		
		f.add(p);
		
		f.setVisible(true);
		
		
		
	}
	

}
