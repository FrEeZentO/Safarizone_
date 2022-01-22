 package gamedemo;

import java.util.Random;

//this class handles everything in regard to the game, from printing to animation

public class GameHandler 
{
	private final int Y_HEIGHT = 3; //y = 3 because of cheatCode doubleJump
	private final char PLAYER_CHAR = '>';
	private final char OBJECT_CHAR = '#';
	private final char EMPTY_FIELD = ' '; //spaces are used to represent empty fields

	//one can experiment with these values
	private final int MIN_DISTANCE = 3; //minimum distance between objects (preferred min 3)
	private final int MAX_DISTANCE = 7; //maximum distance between objects (preferred max ~5) 
	private final int SPAWN_CLEARANCE = 3; //minimum spawn clearance for fresh start
	
	private int distance = 0; //variable to hold distance to next object

	private Random randNum = new Random(); //used for random number generation

	private char[][] grid; //used as the playing field/grid
	
	private boolean didCollide = false; //"check for collision" flag
	
	//construct new grid using set attributes and given length, then init. objects on map
	public GameHandler(int x_length) 
	{
		grid = new char[Y_HEIGHT][x_length];
		generateInitialObjects();
		grid[1][0] = PLAYER_CHAR;
	}

	//print formatted grid  
	public void printGrid() 
	{
		//clear terminal first (windows specific!)
		//from https://stackoverflow.com/questions/25209808/clear-the-console-in-java
		//TODO: bad practice, find better way to clear terminal
		try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();} catch (Exception e) {}
		
		for (int i = 0; i < grid.length; i++ ) {
			for (int j = 0; j < grid[i].length; j++) {
				System.out.print(grid[i][j]);
			}
			System.out.println("");
		}
	}

	//generate initial fields containing objects
	private char[][] generateInitialObjects() 
	{
		int initialDistance;
		
		//fill grid with empty fields
		for (int i = 0; i < grid.length; i++ ) {
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j] = EMPTY_FIELD;
			}
		}

		//loop through fields and spawn objects in specific random distance to each other
		for (int field = SPAWN_CLEARANCE; field < grid[2].length; field++) {
			initialDistance = getRandomDistance();

			if (initialDistance < grid[1].length-field) {
				grid[2][field+initialDistance] = OBJECT_CHAR; //grid[1] because lower half of array is used as playing fields
				field += initialDistance;
			}

		}

		return grid;
	}

	//return random distance between objects by generating inclusive random number between two given ints
	//derived from //from https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
	private int getRandomDistance() 
	{
		return randNum.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1) + MIN_DISTANCE; //TODO Improve random num generation // the generator tends to output the same number over and over if it is requested multiple times in a short period
	}

	//move grid by one in direction of player and place objects
	public void scrollAndPlaceObjects() 
	{
		if (distance == 0) {
			distance = getRandomDistance();
		}

		if (countEmptyFields() == distance) {
			grid[2][grid[1].length-1] = OBJECT_CHAR;
		} else {
			shiftGridToLeft();
		}

	}

	//shift grid by one field to the left
	private void shiftGridToLeft() 
	{

		//shift all fields to the left, except the first field (player position)
		for (int field = 1; field < grid[2].length-1; field++) {
			grid[2][field] = grid[2][field+1];
		}
		//make sure last field is empty
		grid[2][grid[1].length-1] = EMPTY_FIELD;
	}

	//counts empty fields at the end of the grid
	private int countEmptyFields() 
	{
		int counter = 0;

		//start at the end of array and check for empty space
		//if space is empty increase counter and check next field.
		//we check the lower part at the end of the array using the length
		//and continue to decrease it, thus going backwards
		while(grid[2][grid[1].length-1-counter] == EMPTY_FIELD) {
			counter++;
		}
		return counter;
	}

	//checks if the player collides with an object while regarding state of jump
	public void checkCollision(int isInAir) 
	{
		//if object is in field next to player and player is not air, register collision
		if (grid[2][1] == OBJECT_CHAR & isInAir == 0) {
			didCollide = true;
		} else {
			didCollide = false;
		}
	}

	//return didCollide
	public boolean getDidCollide() 
	{
		return didCollide;
	}

	//put player in air if player 
	public void animateJump(int isInAir) 
	{
		if (isInAir == 2) {
			// set player to layer 0 and clear layer 1
			grid[2][0] = EMPTY_FIELD;
			grid[1][0] = EMPTY_FIELD;
			grid[0][0] = PLAYER_CHAR;
		} else if (isInAir == 1) {
			// set player to layer 1 and clear layer 2
			grid[2][0] = EMPTY_FIELD;
			grid[1][0] = PLAYER_CHAR;
		} else {
			// reset player jump
			grid[0][0] = EMPTY_FIELD;
			grid[1][0] = EMPTY_FIELD;
			grid[2][0] = PLAYER_CHAR;
		}
	}
}
