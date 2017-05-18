package PentePackage;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PenteGameBoard extends JPanel implements MouseListener{
	
		private int bWidthPixels;
		private int bWidthSquares;
		private int bSquareWidth;
		Color boardSquareColor = new Color(150, 111, 51);
		
		Square[][]theBoard;
		int currentTurn = PenteMain.BLACKSTONE;
		
		//This is for counting captures
		private int whiteStoneCaptures = 0;
		private int blackStoneCaptures = 0;
		
		Ralph computerMoveGenerator = null;
		boolean playingAgainstRalph;
		int ralphsStoneColor;
		
				

		public PenteGameBoard(int bWPixel, int bWSquares){
			bWidthPixels = bWPixel;
			bWidthSquares = bWSquares;
			bSquareWidth = (int)(bWidthPixels/bWidthSquares);
			
			this.setSize(bWidthPixels, bWidthPixels);
			this.setBackground(Color.CYAN);
			
			theBoard = new Square[bWSquares][bWSquares];
		
		//Make Squares
		for(int row = 0; row < bWidthSquares; ++row){
			for(int col = 0; col < bWSquares; ++col){
				theBoard [row][col] = new Square((col*bSquareWidth), (row*bSquareWidth), bSquareWidth, row, col);
			}
		}
			
		theBoard[9][9].setState(PenteMain.BLACKSTONE);
		String computerAnswer = JOptionPane.showInputDialog("Would you like to play against the computer player (y or n)?");		
		if(computerAnswer.equals("Y")||computerAnswer.equals("Yes")||computerAnswer.equals("yes")||computerAnswer.equals("y")){
			System.out.println("About to start the epic Pente duel");
			computerMoveGenerator = new Ralph(this, currentTurn);
			playingAgainstRalph = true;
			ralphsStoneColor = currentTurn;
		}
		this.changeTurn();
		
		this.addMouseListener(this);
		}
		
		//Overriding method
		public void paintComponent(Graphics g){
			g.setColor(Color.CYAN);
			g.fillRect(0, 0, bWidthPixels, bWidthPixels);
			
			for(int row = 0; row < bWidthSquares; ++row){
				for(int col = 0; col < bWidthSquares; ++col){
					theBoard[row][col].drawMe(g);
				}
			}	
			
		}
		
		public void changeTurn(){
			//If current turn is black, make it white, vice versa
			//Do I need an "and if mouse is clicked in my if statements" ?
			if(currentTurn == PenteMain.BLACKSTONE){
				currentTurn = PenteMain.WHITESTONE;
			}else{
				currentTurn = PenteMain.BLACKSTONE;
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			//System.out.println("You clicked at " + e.getX()+ ", " +e.getY());
			playGame(e);
			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		public void playGame(MouseEvent e){
			Square s = findSquare(e.getX(), e.getY());
			if(s != null){
				if(s.getState() == PenteMain.EMPTY){
					this.doPlay(s);
					if(currentTurn == ralphsStoneColor && playingAgainstRalph == true){
						Square cs = computerMoveGenerator.doComputerMove(s.getRow(), s.getCol());
						cs.setState(currentTurn);
						this.doPlay(cs);
					}
					
				}else{
					//JOptionPane.showMessageDialog(null, "You can't place a stone on this square");
				}
			}else{
				//JOptionPane.showMessageDialog(null, "You didn't click on a square");
			}
		}
		
		public void doPlay(Square s){
			s.setState(currentTurn);
			this.repaint();
			this.checkForCaptures(s);
			this.checkForWinOnCaptures();
			//this.checkForWin(s);
			this.checkForWin2(s);
			this.changeTurn();
		}
		
		public Square findSquare(int mouseX, int mouseY){
			Square clickedOnSquare = null;
			
			//run through all squares and call youClickedMe()
			for(int row = 0; row < bWidthSquares; ++row){
				for(int col = 0; col < bWidthSquares; ++col){
					if(theBoard[row][col].youClickedMe(mouseX, mouseY) == true){
						clickedOnSquare = theBoard[row][col];
					}
				}
			}
			return clickedOnSquare;
		}
							
		public void checkForCaptures(Square s){
			int sRow = s.getRow();
			int sCol = s.getCol();
			int theOpposite = this.getTheOppositeState(s);
			int squareColor = s.getState();
			
			//Horizontal Captures
			if(sCol < bWidthSquares - 3  && sRow >= 3 && sCol >=3){
				if(theBoard[sRow][sCol+1].getState() == theOpposite){
					//HW 4/4 in here					
					if(theBoard[sRow][sCol].getState() == theBoard[sRow][sCol+3].getState() &&
					theBoard[sRow][sCol+1].getState() == theBoard[sRow][sCol+2].getState()){
					
						
					takeStones(sRow, sCol+1, sRow, sCol+2, squareColor);	//Going to give black the capture for now, will be fixed
					repaint();
					}
	
				}
				
				if(theBoard[sRow][sCol-1].getState() == theOpposite){
					if(theBoard[sRow][sCol].getState() == theBoard[sRow][sCol-3].getState() &&
					theBoard[sRow][sCol-1].getState() == theBoard[sRow][sCol-2].getState()){
						
					takeStones(sRow, sCol-1, sRow, sCol-2, squareColor);
					repaint();
					}
				}
			}	
			//Vertical Captures
			if(sRow < bWidthSquares - 3 && sRow >= 3 && sCol >=3){
				if(theBoard[sRow+1][sCol].getState() == theOpposite){
					//HW 4/4 in here					
					if(theBoard[sRow][sCol].getState() == theBoard[sRow+3][sCol].getState() &&
					theBoard[sRow+1][sCol].getState() == theBoard[sRow+2][sCol].getState()){
					
						
					takeStones(sRow+1, sCol, sRow+2, sCol, squareColor);	//Going to give black the capture for now, will be fixed
					repaint();
					}
	
				}
				
				if(theBoard[sRow-1][sCol].getState() == theOpposite){
					if(theBoard[sRow][sCol].getState() == theBoard[sRow-3][sCol].getState() &&
					theBoard[sRow-1][sCol].getState() == theBoard[sRow-2][sCol].getState()){
						
					takeStones(sRow-1, sCol, sRow-2, sCol, squareColor);
					repaint();
					}
				}
			}
			//Diagonal Captures There are 2
			if(sRow < bWidthSquares - 3 && sCol < bWidthSquares - 3  && sRow >= 3 && sCol >=3){
				if(theBoard[sRow+1][sCol+1].getState() == theOpposite){
					//HW 4/4 in here					
					if(theBoard[sRow][sCol].getState() == theBoard[sRow+3][sCol+3].getState() &&
					theBoard[sRow+1][sCol+1].getState() == theBoard[sRow+2][sCol+2].getState()){
					
						
					takeStones(sRow+1, sCol+1, sRow+2, sCol+2, squareColor);	//Going to give black the capture for now, will be fixed
					repaint();
					}
	
				}
				
				if(theBoard[sRow-1][sCol-1].getState() == theOpposite){
					if(theBoard[sRow][sCol].getState() == theBoard[sRow-3][sCol-3].getState() &&
					theBoard[sRow-1][sCol-1].getState() == theBoard[sRow-2][sCol-2].getState()){
						
					takeStones(sRow-1, sCol-1, sRow-2, sCol-2, squareColor);
					repaint();
					}
				}
			}
			if(sRow < bWidthSquares - 3 && sCol < bWidthSquares - 3 && sRow >= 3 && sCol >=3){
				if(theBoard[sRow+1][sCol-1].getState() == theOpposite){
					//HW 4/4 in here					
					if(theBoard[sRow][sCol].getState() == theBoard[sRow+3][sCol-3].getState() &&
					theBoard[sRow+1][sCol-1].getState() == theBoard[sRow+2][sCol-2].getState()){
					
						
					takeStones(sRow+1, sCol-1, sRow+2, sCol-2, squareColor);	//Going to give black the capture for now, will be fixed
					repaint();
					}
	
				}
				
				if(sRow > bWidthSquares - 3 && sCol < bWidthSquares - 3){
					if(theBoard[sRow-1][sCol+1].getState() == theOpposite){
						if(theBoard[sRow][sCol].getState() == theBoard[sRow-3][sCol+3].getState() &&
						theBoard[sRow-1][sCol+1].getState() == theBoard[sRow-2][sCol+2].getState()){
							
						takeStones(sRow-1, sCol+1, sRow-2, sCol+2, squareColor);
						repaint();
						}
					}
				}
			}
		}
		
		
		public int getTheOppositeState(Square s){
			if(s.getState() == PenteMain.BLACKSTONE){
				return PenteMain.WHITESTONE;
			}else{
				return PenteMain.BLACKSTONE;
			}
		}
		
		public void takeStones(int r1, int c1, int r2, int c2, int taker){
			//This is to clear stones
			theBoard[r1][c1].setState(PenteMain.EMPTY);
			theBoard[r2][c2].setState(PenteMain.EMPTY);
			
			if(taker == PenteMain.BLACKSTONE){
				++blackStoneCaptures;
			}else{
				++whiteStoneCaptures;
			}
			
			this.checkForWinOnCaptures();
			repaint();
		}

		public void checkForWinOnCaptures() {
			if(blackStoneCaptures == 5){
				JOptionPane.showMessageDialog(null, "Black Wins By Captures!");
			}
			
			if(whiteStoneCaptures == 5){
				JOptionPane.showMessageDialog(null, "White Wins By Captures");
			}
			
		}
		
		public void checkForWin(Square s){
			int sRow = s.getRow();
			int sCol = s.getCol();
			this.repaint();
			
			for(int dy = -1; dy <=1; ++dy){
				if((dy>0 && sRow < bWidthSquares-4)|| (dy<0 && sRow >= 4) || dy ==0){
					for(int dx = -1; dx <=1; ++dx){
						if(!(dx==0 && dy==0)){
						if((dx>0 && sCol < bWidthSquares-4)||(dx<0 && sCol >=4)||dx==0){
							if(theBoard[sRow + (1*dy)][sCol + (1*dx)].getState()==currentTurn){
								if(theBoard[sRow + (2*dy)][sCol + (2*dx)].getState()==currentTurn){
									if(theBoard[sRow + (3*dy)][sCol + (3*dx)].getState()==currentTurn){
										if(theBoard[sRow + (4*dy)][sCol + (4*dx)].getState()==currentTurn){
											this.weHaveAWinner();
											repaint();
										}
									}
								}
							}
						}
						}
					}//end of dx loop
				}//end of sRow check
			}//end of dy loop	
		}
		
		public void checkForWin2(Square s){
			boolean done = false;
			int[] myDys = {-1, 0, 1};
			int whichDy = 0;
			
			while(!done && whichDy < 3){
				if(checkForWinAllInOne(s, myDys[whichDy], 1) == true){
					weHaveAWinner();
					done = true;
				}
				++whichDy;
			}
			if(!done){
				if(checkForWinAllInOne(s, 1, 0) == true){
					weHaveAWinner();
				}
			}
		}
		
	public void weHaveAWinner(){
		String theWinner = null;
		if(currentTurn == PenteMain.BLACKSTONE){
			theWinner = "Black";
		}else{
			theWinner = "White";
		}
		JOptionPane.showMessageDialog(null, "Game, Set, Match! "+theWinner+" Wins!");

	}
	
	public boolean checkForWinAllInOne(Square s, int dy, int dx){
		boolean isThereAWin = false;
		int sRow = s.getRow();
		int sCol = s.getCol();
		
		int howManyRight = 0;
		int howManyLeft = 0;
		int step = 1;
		while((sCol + (step * dx) < bWidthSquares) && ((sRow + (step * dy)) < bWidthSquares) && //dy for rows?
				(sCol + (step *dx) >=0) && (sRow + (step * dy) >= 0) &&
				(theBoard[sRow + (step * dy)][sCol + (step * dx)].getState() == currentTurn))
		{
			howManyRight++;
			step++;			
		}
		step = 1;
		while((sCol - (step * dx) >= 0) && (sRow - (step * dy) >= 0) &&
				(sCol - (step * dx) < bWidthSquares) && ((sRow - (step * dy)) < bWidthSquares) &&
				(theBoard[sRow - (step * dy)][sCol - (step * dx)].getState() == currentTurn))
		{
			howManyLeft++;
			step++;			
		}
		
		if((howManyRight + howManyLeft + 1 >= 5)){
			isThereAWin = true;
		}
		
		return isThereAWin;
	}
	
	public int getBoardWidthInSquares(){
		return bWidthSquares;
	}
	
	public Square[][]getActualGameBoard(){
		return theBoard;
	}
	
	public int getCurrentTurn(){
		return currentTurn;
	}
	
	public int getCurrentCapturesRalph(){
		return blackStoneCaptures;
	}
	
	public int getCurrentCapturesHuman(){
		return whiteStoneCaptures;
	}
		
}
		
		

