package gamedemo;

import java.util.Random;

//this class handles everything in regard to the game, from printing to animation

public class GameHandler {
	
	private final int Y_HEIGHT = 2; //y = 2 because of char jump
	private final char PLAYER_CHAR = '>';
	private final char OBJECT_CHAR = '#';
	private final char EMPTY_FIELD = 0; //empty char arrays are init with 0 as a char by default, so we use 0 as a char to illustrate empty fields here too.

	private final int MIN_DISTANCE = 3; //minimum distance between objects (preferred min 3)
	private final int MAX_DISTANCE = 5; //maximum distance between objects (preferred max ~5)
	private int distance = 0; //variable to hold distance to next object

	private Random randNum = new Random(); //used for random number generation

	private char[][] grid; //used as the playing field/grid
	
	private boolean didCollide = false; //"check for collision" flag
	
	//construct new grid using set attributes and given length, then init. objects on map
	public GameHandler(int x_length) {
		grid = new char[Y_HEIGHT][x_length];
		grid[1][0] = PLAYER_CHAR;
		generateInitialObjects();
	}

	//print formatted grid
	public void printGrid() {
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
	private char[][] generateInitialObjects() {
		int initialDistance;

		//loop through fields and spawn objects in specific random distance to each other
		for (int field = 0; field < grid[1].length; field++) {
			initialDistance = getRandomDistance();

			if (initialDistance < grid[0].length-field) {
				grid[1][field+initialDistance] = OBJECT_CHAR; //grid[1] because lower half of array is used as playing fields
				field += initialDistance;
			}

		}

		return grid;
	}

	//return random distance between objects by generating inclusive random number between two given ints
	private int getRandomDistance() {
		//from https://stackoverflow.com/questions/20389890/generating-a-random-number-between-1-and-10-java
		return randNum.nextInt(MAX_DISTANCE - MIN_DISTANCE + 1) + MIN_DISTANCE;
	}

	//move grid by one in direction of player and place objects
	public void scrollAndPlaceObjects() {
		if (distance == 0) {
			distance = getRandomDistance();
		}

		if (countEmptyFields() == distance) {
			grid[1][grid[1].length-1] = OBJECT_CHAR;
		} else {
			shiftGridToLeft();
		}

	}

	//shift grid by one field to the left
	private void shiftGridToLeft() {

		//shift all fields to the left, except the first field (player position)
		for (int field = 1; field < grid[1].length-1; field++) {
			grid[1][field] = grid[1][field+1];
		}
		//make sure last field is empty
		grid[1][grid[1].length-1] = EMPTY_FIELD;
	}

	//counts empty fields at the end of the grid
	private int countEmptyFields() {
		int counter = 0;

		//start at the end of array and check for empty space
		//if space is empty increase counter and check next field.
		//we check the lower part at the end of the array using the length
		//and continue to decrease it, thus going backwards
		while(grid[1][grid[1].length-1-counter] == EMPTY_FIELD) {
			counter++;
		}
		return counter;
	}

	//checks if the player collides with an object while regarding state of jump
	public void checkCollision(boolean isInAir) {
		//if object is in field next to player and player is not air, register collision
		if (grid[1][1] == OBJECT_CHAR & isInAir == false) {
			didCollide = true;
		} else {
			didCollide = false;
		}
	}

	public boolean getDidCollide() {
		return didCollide;
	}

	//put player in air if player 
	public void animateJump(boolean isInAir) {
		if (isInAir) {
			grid[1][0] = EMPTY_FIELD;
			grid[0][0] = PLAYER_CHAR;
		} else {
			grid[1][0] = PLAYER_CHAR;
			grid[0][0] = EMPTY_FIELD;
		}
	}
}