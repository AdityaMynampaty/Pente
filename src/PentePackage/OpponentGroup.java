package PentePackage;

import java.util.ArrayList;

public class OpponentGroup {
	
	//Literal Codes for each type of group
	public static final int HORIZONTAL_GROUP = 1;
	public static final int VERTICAL_GROUP = 2;
	public static final int DIAG_RIGHT_GROUP = 3;
	public static final int DIAG_LEFT_GROUP = 4;
	public static final int MIDDLE_3_GROUP = -3;
	public static final int MIDDLE_4_GROUP = -4;
	
	//Class Data
	ArrayList<Square> groupList;	//Expandable list
	int groupLength = 0;
	int groupRanking = 0;
	Square end1Square = null;
	Square end2Square = null;
	
	private boolean inMiddleGroupStatus = false;
	private Square inMiddleSquare = null;
	
	//New fields to have this class know what type of group it is
	private int groupType;
	private String groupTypeText;
	
	private boolean currentMoveIsInGroup = false;
	private int currentMoveArrayListLocation = -1;
	
	//CONSTRUCTOR
	public OpponentGroup(int gt){
		groupList = new ArrayList<Square>();
		//System.out.println("Made a new group Op. (Group)");
		groupType = gt;
		this.setGroupTypeToString();
	}
	

	public void addSquareToGroup(Square whichSquare){
		groupList.add(whichSquare);
		groupLength++;
		groupRanking++;
	}
	
	public void setEnd1Square(Square whatSquare){
		end1Square = whatSquare;
	}
	
	public void setEnd2Square(Square whatSquare){
		end2Square = whatSquare;
	}
	
	public ArrayList<Square> getGroupList(){
		return groupList;
	}
	
	public Square getEnd1Square(){
		return end1Square;
	}
	
	public Square getEnd2Square(){
		return end2Square;
	}
	
	public int getGroupLength(){
		return groupLength;
	}
	
	public int getGroupRanking(){
		return groupRanking;
	}
	
	public void setGroupRanking(int newRanking){
		groupRanking = newRanking;
	}
	
	public int getOpponentTypeGroup(){
		return groupType;
	}
	//Added April 15 to keep info on current move
	public void setCurrentMoveIsInThisGroup(boolean setting){
		currentMoveIsInGroup = true;
	}
	
	public boolean getCurrentMoveIsInGroup(){
		return currentMoveIsInGroup;
	}
	
	public void setCurrentMoveArrayListLocation(int arrayListIndex){
		currentMoveArrayListLocation = arrayListIndex;
	}
	
	public int getArrayListSizeFromArray(){
		return groupList.size();
	}
	
	private void setGroupTypeToString(){
		switch(groupType){
		case MIDDLE_3_GROUP:	//-3
			groupTypeText = "Middle-3";
			break;
		case MIDDLE_4_GROUP:
			groupTypeText = "Middle-4";
		case HORIZONTAL_GROUP:	//1
			groupTypeText = "Horizontal";
			break;
		case VERTICAL_GROUP: 	//2
			groupTypeText = "Vertical";
			break; 
		case DIAG_RIGHT_GROUP: 	//2
			groupTypeText = "Diagonal Right";
			break;
		case DIAG_LEFT_GROUP: 	//2
			groupTypeText = "Diagonal Left";
			break;
		default:
			groupTypeText = "Something is messed up";
			break;
		}
				
	}
	
	public String getGroupTypeText(){
		return groupTypeText;
	}
	
	public void setGroupTypeText(String groupType){
		groupTypeText = groupType;
	}
	
	public void setGroupLength(int number){
		groupLength = number;
	}	
	
	public void setInMiddleGroupStatus(boolean value){
		inMiddleGroupStatus = value;
	}
	
	public boolean getInMiddleGroupStatus(){
		return inMiddleGroupStatus;
	}
	
	public void setInMiddleGroupSquare(Square whatSquare){
		inMiddleSquare = whatSquare;
	}
	
	public Square getInMiddleGroupSquare(){
		return inMiddleSquare;
	}
	
}
