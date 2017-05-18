package PentePackage;

import java.awt.Color;
import java.awt.Graphics;

public class Square {

	private int xLoc, yLoc, sWidth;
	private Color boardSquareColor = new Color(255, 234, 142);
	private Color crossHairColor = new Color(112, 71, 5);
	private Color wStoneColor = new Color(248, 249, 229);
	private int squareState = PenteMain.EMPTY;
	
	//This is added for captures
	private int myRow, myCol;
	
	public Square(int x, int y, int w, int r, int c){
		xLoc = x;
		yLoc = y;
		sWidth = w;
		myRow = r;
		myCol = c;
	}
	
	public void drawMe(Graphics g){
		//Square
		g.setColor(boardSquareColor);
		g.fillRect(xLoc, yLoc, sWidth, sWidth);
		//Crosshairs
			//Vertical Crosshair
		g.setColor(crossHairColor);
		g.drawLine(xLoc + (int)(sWidth/2), yLoc, xLoc + (int)(sWidth/2), yLoc + sWidth);
			//Horizontal Crosshair
		g.setColor(crossHairColor);
		g.drawLine(xLoc, yLoc + (int)(sWidth/2), xLoc + sWidth, yLoc + (int)(sWidth/2));
		
		if(squareState == PenteMain.BLACKSTONE){
			g.setColor(Color.BLACK);
			g.fillOval(xLoc+5, yLoc+5, sWidth-10, sWidth-10);
		}
		
		if(squareState == PenteMain.WHITESTONE){
			g.setColor(wStoneColor);
			g.fillOval(xLoc+5, yLoc+5, sWidth-10, sWidth-10);
		}
		
	}
	
	public void setState(int newState){
		squareState = newState;
	}
	
	public int getState(){
		return squareState;
	}
	
	public int getRow(){
		return myRow;
	}
	
	public int getCol(){
		return myCol;
	}
	//Checks for a mouse click on each square
	public boolean youClickedMe(int mouseX, int mouseY){
		boolean squareClicked = false;
		//Code to check coordinates
		//Pseudocode: If mouse xLoc is xLoc<mouseX<xLoc + sWidth and yLoc is yLoc<mouseY<yLoc + sWidth and mouse is clicked
			//youClickedMe = true
		if(xLoc < mouseX && mouseX < xLoc + sWidth && yLoc < mouseY && mouseY < yLoc + sWidth){
			squareClicked = true;
		}
		
		return squareClicked;
	}
	
	
}
