package PentePackage;

import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Ralph {
	
	//Ralph is the computer player
	private PenteGameBoard myBoard;
	private int myStoneColor, opponentStoneColor;
	private int boardWidthSquares;
	private Square[][]theGameBoard;
	
	boolean timeToMakeAMove = false;
	boolean moveToMake = false;
	int moveToDealWithRow;
	int moveToDealWithCol;
	int moveToDealWithDiag;
	
	//Big addition for array groups
	private ArrayList<OpponentGroup> groups4 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups3 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups2 = new ArrayList<OpponentGroup>();
	private ArrayList<OpponentGroup> groups1 = new ArrayList<OpponentGroup>();
	
	RalphHelper vanellope;
	
	public Ralph(PenteGameBoard b, int stoneColor){
		myBoard = b;
		myStoneColor = stoneColor;
		this.setOpponentStoneColor();
		boardWidthSquares = b.getBoardWidthInSquares();
		theGameBoard = b.getActualGameBoard();	
		JOptionPane.showMessageDialog(null,  "Hi, this is Ralph");
		
		vanellope = new RalphHelper(myBoard, opponentStoneColor);
	}
	
	public void setOpponentStoneColor(){
		if(myStoneColor == PenteMain.BLACKSTONE){
			opponentStoneColor = PenteMain.WHITESTONE;
		}else{
			opponentStoneColor = PenteMain.BLACKSTONE;
		}
	}
	
	public Square doComputerMove(int lastMoveRow, int lastMoveCol){
		/*logic - making Ralph smarter
		 * 1. Stop any win, four in a row with one block
		 * 2. Unguarded three, stop it
		 * 3. If there is a possible capture, go for it
		 * 4. Set up for capture, put one block next to two consecutive
		 * 5. If all else, random move										*/
		
		this.assessBoard(lastMoveRow, lastMoveCol);
		vanellope.assessBoard(lastMoveRow, lastMoveCol);
		
		Square nextMove = null;
		
		//nextMove = this.blockAOne();
		//nextMove = this.blockATwo();
		//nextMove = this.blockAThree();
		//nextMove = this.blockAFour();
		
		//nextMove = this.chooseWhatToBlock(lastMoveRow, lastMoveCol);
		//nextMove = this.blockEverything1(lastMoveRow, lastMoveCol);
		
		nextMove = vanellope.blockItEverybody(vanellope.getVanellopeGroups4(), 4);
			if(nextMove == null){
				nextMove = this.blockItEverybody(groups4, 4);
				if(nextMove == null){
					nextMove = vanellope.blockItEverybody(vanellope.getVanellopeGroups3(), 3);
					if(nextMove == null){
					nextMove = this.blockItEverybody(groups3, 3);
						if(nextMove == null){
							nextMove = this.capture2(groups2, 2);
							if(nextMove == null){
								nextMove = this.blockItEverybody(groups2, 2);
								if(nextMove == null){
									nextMove = vanellope.blockItEverybody(vanellope.getVanellopeGroups2(), 2);
									if(nextMove == null){
										//nextMove = this.blockItEverybody(groups1, 1);
										if(nextMove == null){
											System.out.println("Randomly Moving");
											nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
										}
									}
								}else{
									System.out.println(" Just found a block on a two at " + nextMove.getRow() + ", " + 
											nextMove.getCol());
								}
							}
						} else {
							System.out.println(" Just found a block on a three at " + nextMove.getRow() + ", " + 
									nextMove.getCol());
						}
					}
				}
			}
		
		if(nextMove == null){
			 nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
		}
				
		return nextMove;
	}
	//MAKING RALPH SMARTER
	
	//ASSESS THE BOARD FOR OPPONENT GROUPS
	public void assessBoard(int lastMoveRow, int lastMoveCol){
		groups4.clear();
		groups3.clear();
		groups2.clear();
		groups1.clear();
		System.out.println("Starting a new sweep");
		this.lookForGroupsHorizontally(lastMoveRow, lastMoveCol);
		this.lookForGroupsVertically(lastMoveRow, lastMoveCol);
		this.lookForGroupsDiagonallyRight(lastMoveRow, lastMoveCol);
		this.lookForGroupsDiagonallyLeft(lastMoveRow, lastMoveCol);
		this.doInMiddleCheck(3);
		this.doInMiddleCheck(4);
	}
 
	//Look for groups horizontally ****************
	public void lookForGroupsHorizontally(int lastMoveRow, int lastMoveCol){
		int curCol;
		
		for(int row = 0; row < boardWidthSquares; ++row){
			curCol = 0;
			//Step 1 - Skip empty squares and your own color until you find opponent stone
			//Step 2 - When you find one, make an opponent group
			//Step 3 - Get all stones for the group (ones adjacent horizontally)
				//While col is less than bWSquare
			while(curCol < boardWidthSquares){
				Square newStart = findOpponentStartHorizontal(row, curCol);
				
				if(newStart != null){
					//System.out.println("On row " + row + " I found a start at col " + newStart.getCol());
					//Make an object group, add stone to array, check edges
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.HORIZONTAL_GROUP);
					newGroup.addSquareToGroup(newStart);
					int startRow = newStart.getRow();
					int startCol = newStart.getCol();
					if(startCol <= 0){
						newGroup.setEnd1Square(null);
					}else{
						newGroup.setEnd1Square(theGameBoard[startRow][startCol-1]);
					}
					
					if(startRow == lastMoveRow && startCol == lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
					}
					
					startCol++;
					
						while(startCol<boardWidthSquares&&theGameBoard[startRow][startCol].getState()==this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							//System.out.println("Adding stones to a group");
							if(startRow == lastMoveRow && startCol == lastMoveCol){
								newGroup.setCurrentMoveIsInThisGroup(true);
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
							}
						startCol++;
					}
					//Setting second edge draws upon first edge method
						if(startCol >= boardWidthSquares){
							newGroup.setEnd2Square(null);
						}else{
							newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
							//newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						}
					
						curCol = startCol;	
						this.addNewGroupToGroupLists(newGroup);
					
					}else{
					//If startSquare is null
						curCol = boardWidthSquares;
						//System.out.println("Hi, In check I didnt find any squares");
					}
				

			}
			
			
		}
		
		
		
	}
	
	public void lookForGroupsVertically(int lastMoveRow, int lastMoveCol){
		int curRow;
		
		for(int col = 0; col < boardWidthSquares; ++col){
			curRow = 0;
			//Step 1 - Skip empty squares and your own color until you find opponent stone
			//Step 2 - When you find one, make an opponent group
			//Step 3 - Get all stones for the group (ones adjacent horizontally)
				//While col is less than bWSquare
			while(curRow < boardWidthSquares){
				Square newStart = findOpponentStartVertical(curRow, col);
				
				if(newStart != null){
					//System.out.println("On row " + newStart.getRow() + " I found a start at col " +col);
					//Make an object group, add stone to array, check edges
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.VERTICAL_GROUP);
					newGroup.addSquareToGroup(newStart);
					int startRow = newStart.getRow();
					int startCol = newStart.getCol();
					if(startRow <= 0){
						newGroup.setEnd1Square(null);
					}else{
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol]);
					}
					
					if(startRow == lastMoveRow && startCol == lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
					}
					
					startRow++;
					
						while(startRow<boardWidthSquares&&theGameBoard[startRow][startCol].getState()==this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							if(startRow == lastMoveRow && startCol == lastMoveCol){
								newGroup.setCurrentMoveIsInThisGroup(true);
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength()-1);
							}
						startRow++;
					}
					//Setting second edge draws upon first edge method
						if(startRow >= boardWidthSquares){
							newGroup.setEnd2Square(null);
						}else{
							newGroup.setEnd2Square(theGameBoard[curRow][col]);
							//newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						}
					
						curRow = startRow;	
						this.addNewGroupToGroupLists(newGroup);
					
					}else{
					//If startSquare is null
						curRow = boardWidthSquares;
						//System.out.println("Hi, In check I didnt find any squares");
					}
				

			}
			
			
		}
	}
	
	//DIAGONALLY LEFT CHECK----------------------------------------------------------------------------------------
	

	public void lookForGroupsDiagonallyLeft( int lastMoveRow, int lastMoveCol ){
		//Do Part 1 of the Diagonal...
		for(int row = 0 ; row < boardWidthSquares; ++row ){
	
			int curCol = boardWidthSquares-1; 
			int curRow = row;
	
			
			while(curCol >= row  && curRow < boardWidthSquares) { 
				
				Square groupStart = findOpponentStartDiagLeft( curRow,  curCol);
				
				if( groupStart != null ) {	
					//You have a start so set up a new group!
					//System.out.println ("Hi I found a group start at " + groupStart.getRow() + ", " + 
					//groupStart.getCol() );
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_LEFT_GROUP);
					newGroup.addSquareToGroup(groupStart);
					int startRow = groupStart.getRow();
					int startCol = groupStart.getCol();
					
					// Check first edge
					if(startRow - 1 >= 0 && startCol + 1 < boardWidthSquares) {
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol+1]);
					} else {
						newGroup.setEnd1Square(null);
						
					}
					//see if current move is part of this group
					if( startRow == lastMoveRow && startCol == lastMoveCol ){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					
					//look for additional group members
					startCol--;  
					startRow++;
					boolean done = false;
					
					while( startCol >= row && startRow < boardWidthSquares && !done){
						if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor ) {
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);		
							if( startRow == lastMoveRow && startCol == lastMoveCol ){
								newGroup.setCurrentMoveIsInThisGroup(true);
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
							}
		
							startRow++;
							startCol--;
						} else {
							done = true;
						}	
					}	
					//check other edge
					if(startRow < boardWidthSquares && startCol >= 0) {
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					} else {
						newGroup.setEnd2Square(null);
						
					}
					
					//Important to stop infinite loop
					curCol = startCol;
					curRow = startRow;
					//add group to list
					this.addNewGroupToGroupLists(newGroup);
					
				} else {
					//get out of loop!!
					curRow = boardWidthSquares;
					curCol = row-1; 
				}	
			}	
		}
		
		//System.out.println("Start of second part of diagonal");
		//Do Part 2 of the Diagonal
		for(int col = boardWidthSquares-2 ; col >= 0; --col ){
			
			int curCol = col;
			int curRow = 0;
			
			//System.out.println("At start of searching loop cur row is " + curRow + " and curCol is " 
			//+ curCol);
			
			
			while(curRow <= col  && curCol >= 0) {	
				
				//System.out.println("Right before findOpponentStartDiagLeft curRow and col are "
				// + curRow + ",  " + curCol);
				Square groupStart = findOpponentStartDiagLeft( curRow,  curCol);
				
				if(groupStart != null){
					
					//System.out.println ("Hi I found a group start at " + groupStart.getRow() + ", "
					// + groupStart.getCol() );
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_LEFT_GROUP);
					newGroup.addSquareToGroup(groupStart);
					int startRow = groupStart.getRow();
					int startCol = groupStart.getCol();
					
					// Check first edge  same problem so same code from above should work...
					if(startRow - 1 >= 0 && startCol + 1 < boardWidthSquares) {
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol+1]);
					} else {
						newGroup.setEnd1Square(null);
						
					}
					//see if current move is part of this group
					if( startRow == lastMoveRow && startCol == lastMoveCol ){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					
					//look for additional group members
					startCol--;  // startCol[__];
					startRow++;
					boolean done = false;
					
					while( !done && startCol >=0  && startRow < boardWidthSquares ){	
						if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor ) {
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);		
							if( startRow == lastMoveRow && startCol == lastMoveCol ){
								newGroup.setCurrentMoveIsInThisGroup(true);
								newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
							}
		
							startRow++;
							startCol--;
						} else {
							done = true;
						}		
					}
					
					//check other edge
					if(startRow < boardWidthSquares && startCol >= 0) {
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
						
					} else {
						newGroup.setEnd2Square(null);
					}
					
					//Important to stop infinite loop
					curCol = startCol;
					curRow = startRow;
					//add group to list
					this.addNewGroupToGroupLists(newGroup);
					
					
				} else {
					
					//get out of loop
					curCol = -1;
					//System.out.println();
					
				}
			}
		}		
	}


	//DIAGONALLY RIGHT CHECK--------------------------------------------------------------------------------
	
	public void lookForGroupsDiagonallyRight(int lastMoveRow, int lastMoveCol){
		for(int row = 0; row < boardWidthSquares; ++row){
			int curCol = 0;
			int curRow = row;
			
			while(curCol < boardWidthSquares - row && curRow < boardWidthSquares){
				Square groupStart = findOpponentDiagRight1(curRow, curCol, 0);
				if(groupStart != null){
					System.out.println("I found a group start at " +groupStart.getRow()+ ", "
							+groupStart.getCol());
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_RIGHT_GROUP);
					newGroup.addSquareToGroup(groupStart);
					int startRow = groupStart.getRow();
					int startCol = groupStart.getCol();
				//Check 1st edge
					if(startRow - 1 >= 0 && startCol - 1 >= 0){
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol-1]);
					}else{
						newGroup.setEnd1Square(null);
					}
				//See if current move is part of this group
					if(startRow == lastMoveRow && startCol == lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
	//Start here when relooking at code
				//Look for additional group members
					startRow++;
					startCol++;
					boolean done = false;
					//Loop that collects the length of opponent stones
					while(startCol < boardWidthSquares - row && startRow < boardWidthSquares && !done){
						if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
						if(startRow == lastMoveRow && startCol == lastMoveCol){	
							newGroup.setCurrentMoveIsInThisGroup(true);
							newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
						} //done = true;
							
							startRow++;
							startCol++;
			//
						}else{
							done = true;
						}
						
					}
				//Check 2nd edge
					if(startRow < boardWidthSquares && startCol < boardWidthSquares){
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}else{
						newGroup.setEnd2Square(null);
						
					}
					//Stops Infinite Loop
					curRow = startRow;
					curCol = startCol;
				//Add group to list
					this.addNewGroupToGroupLists(newGroup);
				
				
				}else{
					curRow = boardWidthSquares;
					//curCol = boardWidthSquares;
				}
			}
			
		}
		//Second half of the board
		for(int col = 1; col < boardWidthSquares; ++col){
			
			int curCol = col;
			int curRow = 0;
			while(curRow < boardWidthSquares - col && curCol < boardWidthSquares){
				Square groupStart = findOpponentDiagRight1(curRow, curCol, 0);
				
				if(groupStart != null){
					//System.out.println("Hi I found a group start at " + groupStart.getRow() + 
					//		", " +groupStart.getCol());
					OpponentGroup newGroup = new OpponentGroup(OpponentGroup.DIAG_RIGHT_GROUP);
					newGroup.addSquareToGroup(groupStart);
					int startRow = groupStart.getRow();
					int startCol = groupStart.getCol();
					
					//Check 1st edge, same problems as above
					if(startRow - 1 >= 0 && startCol - 1 >= 0){
						newGroup.setEnd1Square(theGameBoard[startRow-1][startCol-1]);
					}else{
						newGroup.setEnd1Square(null);
					}
				//See if current move is part of this group
					if(startRow == lastMoveRow && startCol == lastMoveCol){
						newGroup.setCurrentMoveIsInThisGroup(true);
						newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
					}
					
				//Look for additional group members
					startRow++;
					startCol++;
					boolean done = false;
					while(startRow < boardWidthSquares - col && startCol < boardWidthSquares && !done){
						if(theGameBoard[startRow][startCol].getState() == this.opponentStoneColor){
							newGroup.addSquareToGroup(theGameBoard[startRow][startCol]);
							//if(startRow == lastMoveRow && startCol == lastMoveCol){
							newGroup.setCurrentMoveIsInThisGroup(true);
							newGroup.setCurrentMoveArrayListLocation(newGroup.getGroupLength());
						//}//done = true;
							
							startRow++;
							startCol++;
							
						}else{
							done = true;
						}
					}
					
					//Check 2nd edge
					if(startRow >= 0 && startCol >= 0){
						newGroup.setEnd2Square(theGameBoard[startRow][startCol]);
					}else{
						newGroup.setEnd2Square(null);
					}
					
					//Stops Infinite Loop
					curRow = startRow;
					curCol = startCol;
					//Add group to list
					this.addNewGroupToGroupLists(newGroup);
					
				}else{
					//Get out of loop
					curCol = boardWidthSquares;
					
				}
			}
			
		}
	}

	
	//Find a horizontal start group
	//Start on a horizontal row, finds first square and can grab up the rest
	public Square findOpponentStartHorizontal(int whatRow, int whatCol){
		//System.out.println("Hi from op.start.horz");
		Square opponentStart = null;
		boolean done = false;
		int currentCol = whatCol;
		
		while(!done && currentCol < boardWidthSquares){
			if(theGameBoard[whatRow][currentCol].getState() == this.opponentStoneColor){
				opponentStart = theGameBoard[whatRow][currentCol];
				done = true;
			}
			currentCol++;
		}
		//System.out.println("About to return horizontal start");
		return opponentStart;
	}
	
	public Square findOpponentStartVertical(int whatRow, int whatCol){
		//System.out.println("Hello from op.start.vert");
		Square opponentStart = null;
		boolean done = false;
		int currentRow = whatRow;
		
		while(!done && currentRow < boardWidthSquares){
			if(theGameBoard[currentRow][whatCol].getState() == this.opponentStoneColor){
				opponentStart = theGameBoard[currentRow][whatCol];
				done = true;
			}
			currentRow++;
		}
		//System.out.println("About to return a square");
		return opponentStart;		
	}
	
	public Square findOpponentDiagRight1(int whatRow, int whatCol, int r){
		
		Square opponentStart = null;
		boolean done = false;
		int currentCol = whatCol;
		int currentRow = whatRow;
		
		while(!done && currentCol < boardWidthSquares-r && currentRow < boardWidthSquares){
			if(theGameBoard[currentRow][currentCol].getState() == this.opponentStoneColor){
				opponentStart = theGameBoard[currentRow][currentCol];
				done = true;
			}
			currentRow++;
			currentCol++;
		}
		
		return opponentStart;
		
	}
	
	public Square findOpponentStartDiagLeft( int whatRow, int whatCol ){
		
		//System.out.println();
		//System.out.println("At top of findOpponentStartDiagonalLEFT whatRow is " +
		//		whatRow + " and whatCol is " + whatCol);
		Square opponentStart = null;
		boolean done = false; 
		int currentCol = whatCol;
		int currentRow = whatRow;
		
		while( !done && currentCol >= 0 && currentRow < boardWidthSquares){
			
		//	System.out.println("In findOpponentDiagLEFT loop, checking currentRow " + currentRow + " and currentCol  " + currentCol );
			
			if(theGameBoard[currentRow][currentCol].getState() == this.opponentStoneColor){
				opponentStart = theGameBoard[currentRow][currentCol];
				done = true;	
			}
			currentCol--;
			currentRow++;
		}
		
		//System.out.println(" Hello from bottom of findOpponentDiagRightTop just about to return a start square");
		return opponentStart;
	}
	
	//Add the list to one of the four lists
	public void addNewGroupToGroupLists(OpponentGroup ng){
		//Takes a new OpponentGroup object and adds it to one
		//of the four OpponentGroup ArrayLists
		//System.out.println("In addNewGroupToGroupList, group length is " +ng.getGroupLength());
		switch(ng.getGroupLength()){
			case 1:
				groups1.add(ng);
				//System.out.println(" I have a " +ng.getGroupTypeText()+ " group with one opponent stone.");
				break;
			case 2:
				groups2.add(ng);
				//System.out.println(" I have a " +ng.getGroupTypeText()+ " group with two opponent stones.");
				break;
			case 3:
				groups3.add(ng);
				//System.out.println(" I have a " +ng.getGroupTypeText()+ " group with three opponent stones.");
				break;
			case 4:
				groups4.add(ng);
				//System.out.println(" I have a " +ng.getGroupTypeText()+ " group with four opponent stones.");
				break;
			default:
				//System.out.println("Something is messed up.");
				break;
		}
		
	}
	
	public Square makeARandomMove(int lastMoveRow, int lastMoveCol){
		int newMoveRow, newMoveCol;
		do{
		newMoveRow = (int) (Math.random()*boardWidthSquares);
		newMoveCol = (int) (Math.random()*boardWidthSquares);
		this.assessBoard(newMoveRow, newMoveCol);
		}while(theGameBoard[newMoveRow][newMoveCol].getState() != PenteMain.EMPTY);
		return theGameBoard[newMoveRow][newMoveCol];
	}
	
	//Middle Check Stuff----------------------------------------------------------------------

	//New added April 30 for in Middle Checks
	public void doInMiddleCheck( int groupSize ){
			
			for(int row = 0; row < boardWidthSquares; ++row){
				for(int col = 0; col < boardWidthSquares; ++col){
					if(theGameBoard[row][col].getState() == PenteMain.EMPTY){
						checkForBlockInMiddle(row, col, groupSize);
					}
				}
			}
	}
	
	
	public void checkForBlockInMiddle(int row, int col, int groupSize){
		
		boolean done = false;
		int[] myDys = {-1, 0, 1};
        int whichDy = 0;
  
		while(!done && whichDy < 3){
			checkForBlockInMiddleAllAround(row, col, groupSize, myDys[whichDy], 1 );
			whichDy++;
		}
		checkForBlockInMiddleAllAround(row, col, groupSize, 1, 0 );	
	}
	
	
	public void checkForBlockInMiddleAllAround(int row, int col, int groupSize, int dy, int dx)
	{
	
		int sRow = row;
		int sCol = col;
		//System.out.println("In checkForBlockInMiddleAllAround sRow and sCol is [" + 
		//sRow + ", " + sCol + "]");
		
		//for a right-check and left...
		int howManyRight = 0;
		int howManyLeft = 0;
		
		//loop to check right side of where stone s is
		int step = 1;
		//System.out.println("In checkForWinAllInOne sRow and sCol are [" + sRow + ", " + sCol + "]");
		//System.out.println("In checkForWinAllInOne dy and dx are [" + dy + ", " + dx + "]");
		while((sCol + (step * dx) < boardWidthSquares) && (sRow + (step * dy) < boardWidthSquares) &&
				(sCol + (step * dx) >= 0) && (sRow + (step * dy) >= 0) &&
				(theGameBoard[sRow + (step * dy)][sCol + (step * dx)].getState() == this.opponentStoneColor)){
			howManyRight++;
			step++;
		}
		//Moving Left....
		step = 1;
		while((sCol - (step * dx) >= 0) &&  (sRow - (step * dy) >= 0) &&
				(sCol - (step * dx) < boardWidthSquares) && (sRow - (step * dy) < boardWidthSquares) &&
				(theGameBoard[sRow - (step * dy)][sCol - (step * dx)].getState() == this.opponentStoneColor)){
			howManyLeft++;
			step++;
		}
		
		
		if((howManyRight + howManyLeft) >= groupSize){
			//If you have this then you want to set Up an Opponent group for this
			System.out.println("For square at " + row + ", " + col + " we have middle group of size of " 
			+ (howManyRight + howManyLeft));
			OpponentGroup newGroup;
			if( groupSize == 4 ) {
				String middleGroupText = getMiddleGroupText(dx, dy, 4);
				newGroup = new OpponentGroup(OpponentGroup.MIDDLE_4_GROUP);
				newGroup.setGroupRanking(4);
				newGroup.setGroupLength(4);
				System.out.println("Hi in checkformiddleallaround, I just created a group of 4 and length is " + newGroup.getGroupLength());
				newGroup.setGroupTypeText(middleGroupText);
			} else {
				String middleGroupText = getMiddleGroupText(dx, dy, 3);
				newGroup = new OpponentGroup(OpponentGroup.MIDDLE_3_GROUP);
				newGroup.setGroupRanking(3);
				newGroup.setGroupLength(3);
				newGroup.setGroupTypeText(middleGroupText);
			}
			
			newGroup.setInMiddleGroupStatus(true);
			newGroup.setInMiddleGroupSquare(theGameBoard[row][col]);
			this.addNewGroupToGroupLists(newGroup);		
		}	
	}
	
	public String getMiddleGroupText(int dx, int dy, int groupSize){
		String gs = "";
		if(groupSize == 4){
			gs = "4";
		} else {
			gs = "3";
		}
		String theType = "";
		if(dx == 1){
			if(dy == 1) theType = "Diag Right";
			if(dy == 0) theType = "Horizontal";
			if(dy == -1) theType = "Diag Left";
		} else {
			theType = "Vertical";
		}
		
		return "Middle " + gs + ": " + theType;
	}
	
	
//DO COMPUTER MOVE STUFF - BLOCKING HUMAN PLAYER ----------------------------------------------------------------
	public Square blockAThree(){
		Square nextMove = null;
		System.out.println("In blockAThree, groups3 size is " +groups3.size());
		if(groups3.size() > 0){
			boolean done = false;
			int groupIndex = 0;
			
			while(!done && groupIndex < groups3.size()){
				Square e1 = groups3.get(groupIndex).getEnd1Square();
				Square e2 = groups3.get(groupIndex).getEnd2Square();
				
				groupIndex++;
				
				System.out.println("*************This is group index:"+groupIndex);
				System.out.println("These are the end squares: [" +e1.getRow()+ ", " + e1.getCol()+ "] and ["
						+e2.getRow()+ ", " + e2.getCol()+ "].");
				
				if(e1.getState() == PenteMain.EMPTY && e2.getState() == PenteMain.EMPTY
						&& e1 != null && e2 != null){
					System.out.println("e1 and e2 are empty");

					int r = (int)(Math.random()*100);
					if(r < 50){
						nextMove = e1;
					}else{
						nextMove = e2;
					}
					done = true;
				}else{
					if(e1.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e1 is empty");
						nextMove = e1;
					}
					if(e2.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e2 is empty");
						nextMove = e2;
						done = true;
						}
					}
									
			}						
			
		}
		return nextMove;
	}
	
	public Square blockAFour(){
		Square nextMove = null;
		System.out.println("In blockAFour, groups4 size is " +groups4.size());
		if(groups4.size() > 0){
			boolean done = false;
			int groupIndex = 0;
			
			while(!done && groupIndex < groups4.size()){
				Square e1 = groups4.get(groupIndex).getEnd1Square();
				Square e2 = groups4.get(groupIndex).getEnd2Square();
				
				groupIndex++;
				
				System.out.println("*************This is group index:"+groupIndex);
				//System.out.println("These are the end squares: [" +e1.getRow()+ ", " + e1.getCol()+ "] and ["
					//	+e2.getRow()+ ", " + e2.getCol()+ "].");
				
				if(e1.getState() == PenteMain.EMPTY && e2.getState() == PenteMain.EMPTY
						&& e1 != null && e2 != null){
					System.out.println("e1 and e2 are empty");

					int r = (int)(Math.random()*100);
					if(r < 50){
						nextMove = e1;
					}else{
						nextMove = e2;
					}
					done = true;
				}else{
					if(e1.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e1 is empty");
						nextMove = e1;
					}
					if(e2.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e2 is empty");
						nextMove = e2;
						done = true;
						}
					}
									
			}
			
		}
		return nextMove;
	}
	
	public Square blockATwo(){
		Square nextMove = null;
		System.out.println("In blockATwo, groups2 size is " +groups2.size());
		if(groups2.size() > 0){
			boolean done = false;
			int groupIndex = 0;
			
			while(!done && groupIndex < groups2.size()){
				Square e1 = groups2.get(groupIndex).getEnd1Square();
				Square e2 = groups2.get(groupIndex).getEnd2Square();
				
				groupIndex++;
				
				System.out.println("*************This is group index:"+groupIndex);
				System.out.println("These are the end squares: [" +e1.getRow()+ ", " + e1.getCol()+ "] and ["
						+e2.getRow()+ ", " + e2.getCol()+ "].");
				
				if(e1.getState() == PenteMain.EMPTY && e2.getState() == PenteMain.EMPTY
						&& e1 != null && e2 != null){
					System.out.println("e1 and e2 are empty");

					int r = (int)(Math.random()*100);
					if(r < 50){
						nextMove = e1;
					}else{
						nextMove = e2;
					}
					done = true;
				}else{
					if(e1.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e1 is empty");
						nextMove = e1;
					}
					if(e2.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e2 is empty");
						nextMove = e2;
						done = true;
						}
					}
									
			}
			
		}
		return nextMove;
	}
	
	public Square blockAOne(){
		Square nextMove = null;
		System.out.println("In blockATwo, groups1 size is " +groups1.size());
		if(groups1.size() > 0){
			boolean done = false;
			int groupIndex = 0;
			
			while(!done && groupIndex < groups1.size()){
				Square e1 = groups1.get(groupIndex).getEnd1Square();
				Square e2 = groups1.get(groupIndex).getEnd2Square();
				
				groupIndex++;
				
				System.out.println("*************This is group index:"+groupIndex);
				System.out.println("These are the end squares: [" +e1.getRow()+ ", " + e1.getCol()+ "] and ["
						+e2.getRow()+ ", " + e2.getCol()+ "].");
				
				if(e1.getState() == PenteMain.EMPTY && e2.getState() == PenteMain.EMPTY
						&& e1 != null && e2 != null){
					System.out.println("e1 and e2 are empty");

					int r = (int)(Math.random()*100);
					if(r < 50){
						nextMove = e1;
					}else{
						nextMove = e2;
					}
					done = true;
				}else{
					if(e1.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e1 is empty");
						nextMove = e1;
					}
					if(e2.getState() == PenteMain.EMPTY && e1 != null && e2 != null){
						System.out.println("e2 is empty");
						nextMove = e2;
						done = true;
						}
					}
									
			}
			
		}
		return nextMove;
	}
	
	public Square chooseWhatToBlock(int lastMoveRow, int lastMoveCol){
		Square nextMove = null;
		int whichGroup = (int)(Math.random()*100);
		
		if(whichGroup <= 33){
			nextMove = this.blockATwo();
			if(this.blockATwo() == null){
				System.out.println("Here in chooseWhatBlock(), about to move randomly");
				nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
			}
		}
		
		if(whichGroup > 33 && whichGroup <= 66){
			nextMove = this.blockAThree();
			if(this.blockAThree() == null){
				nextMove = this.blockATwo();
				if(this.blockATwo() == null){
					System.out.println("Here in chooseWhatBlock(), about to move randomly");
					nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
				}
			}
			
		}
		
		if(whichGroup >= 67){
			nextMove = this.blockAFour();
			if(this.blockAFour() == null){
				nextMove = this.blockAThree();
				if(this.blockAThree() == null){
					if(this.blockATwo() == null){
						System.out.println("Here in chooseWhatBlock(), about to move randomly");
						nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
					}
				}
			}
		}
		
		
		
		return nextMove;
	}
	
	public Square blockEverything1(int lastMoveRow, int lastMoveCol){
		Square nextMove = null;
		nextMove = this.blockAFour();
		if(this.blockAFour() == null){
			nextMove = this.blockAThree();
			if(this.blockAThree() == null){
				nextMove = this.blockATwo();
				if(this.blockATwo() == null){
					System.out.println("Here in blockEveryThing(), about to move randomly");
					nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
				}
			}
		}
		
		return nextMove;
	}
	
	public Square blockEverything2(int lastMoveRow, int lastMoveCol){
		Square nextMove = null;
		nextMove = this.blockAFour();
		if(this.blockAFour() == null){
			nextMove = this.blockAThree();
			if(this.blockAThree() == null){
				nextMove = this.blockATwo();
				if(this.blockATwo() == null){
					nextMove = this.blockAOne();
					if(this.blockAOne() ==null){
						System.out.println("Here in blockEveryThing(), about to move randomly");
						nextMove = this.makeARandomMove(lastMoveRow, lastMoveCol);
					}
				}
			}
		}
		
		return nextMove;
	}
	
	public Square blockItEverybody(ArrayList<OpponentGroup> whatGroup, int whatGroupSize){
			
			Square nextMove = null;
			//Your code here
			System.out.println("In BlockIt for this group the size is " + whatGroup.size() );
			
			if(whatGroup.size() > 0){
				
				boolean done = false;
				int groupIndex = 0;
				
				while(!done && groupIndex < whatGroup.size()){
					 OpponentGroup currentGroup = whatGroup.get(groupIndex);
					 Square e1 = whatGroup.get(groupIndex).getEnd1Square();
					 Square e2 = whatGroup.get(groupIndex).getEnd2Square();
				
					 System.out.println("At top of loop in BlockIt and groupIndex is " + groupIndex);
					 
					 groupIndex++;
			
					 if(currentGroup.getInMiddleGroupStatus() == true){
						 nextMove = currentGroup.getInMiddleGroupSquare();
						 System.out.println("In blockItEverybody, returning a middle square");
					 } else {
						  
						 // So here we now know that e1 is empty...
						 if((e1 != null && e1.getState() == PenteMain.EMPTY ) && (e2 != null && e2.getState() == PenteMain.EMPTY )){
							 System.out.println("We have found for this opponent group both e1 and e2 are empty");
							 int r = (int)(Math.random() * 100);
							 if(r > 50){
								 nextMove = e1;
							 } else {
								 nextMove = e2;
							 }
							 done=true;
						 }else {

							 if(whatGroupSize == 4){
							 
							 if (e1 != null && e1.getState() == PenteMain.EMPTY) {
								 System.out.println("e1 is empty in blockIt");
								 nextMove = e1;
								 done = true;
								 
							 } else {
								 if(e2 != null && e2.getState() == PenteMain.EMPTY){
									 System.out.println("e2 is empty in blockIt");
									 nextMove = e2;
									 done = true;
								 }
							 }
							 
						 }
					 } 
				}
			}
			}	
			
			return nextMove;
			
		}
	
		public Square capture2(ArrayList<OpponentGroup> whatGroup, int whatGroupSize){
			Square nextMove = null;
			System.out.println("In capture2, groupSize is " +whatGroup.size());
			if(whatGroup.size() > 0){

				boolean done = false;
				int groupIndex = 0;
				
				while(!done && groupIndex < whatGroup.size()){
					 OpponentGroup currentGroup = whatGroup.get(groupIndex);
					 Square e1 = whatGroup.get(groupIndex).getEnd1Square();
					 Square e2 = whatGroup.get(groupIndex).getEnd2Square();
				
					 System.out.println("At top of loop in BlockIt and groupIndex is " + groupIndex);
					 
					 groupIndex++;
			
					 if(currentGroup.getInMiddleGroupStatus() == true){
						 nextMove = currentGroup.getInMiddleGroupSquare();
						 System.out.println("In blockItEverybody, returning a middle square");
					 } else {
						  
						 // So here we now know that e1 is empty...
						 if((e1 != null && e1.getState() == PenteMain.EMPTY ) && (e2 != null && e2.getState() == 
								 PenteMain.BLACKSTONE )){
							 System.out.println("We have found for this opponent group both e1 and e2 are empty");
							 nextMove = e1;
							 done = true;
						 }else {
							 if((e1 != null && e1.getState() == PenteMain.BLACKSTONE ) && (e2 != null && e2.getState() == 
									 PenteMain.EMPTY )){
								 System.out.println("We have found for this opponent group both e1 and e2 are empty");
								 nextMove = e2;
								 done = true;
							 
							 } 
						 }
					 }
				}
			}
		
			return nextMove;

		}
		public Square specialProcessingForThree() {
			Square nextMove = null;
			return nextMove;
		}
	
//END OF BLOCKING/DEFENDING, BEGGINING OF ATTACKING/OFFENSE----------------------------------------------------
	public Square addOnToMyGroup(){
		Square nextMove = null;
		//Do we need to create arrays for Ralph's stone?
		//New class named ComputerGroup?
		//I think so
		return nextMove;
	}
	
	
}