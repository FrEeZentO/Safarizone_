package gamedemo;

import java.io.File; //used for writing scores to file 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter; //used for writing score to file
import java.util.Scanner; //used to get input

//this class handles the game menus such as start screen, game over screen, etc. and also takes care of score keeping

public class GameMenuHandler {
	private Scanner scanner; //used to get input without catching all global keystrokes (less prone to errors)
	private String input; //store input as String
	private int score; //used to keep track of score

	private final String GAMENAME = "SAFARIZONE"; //game title
	private final String FILENAME = GAMENAME + "_Scores.txt"; //file name where scores are stored (in same dir as game jar)
	private final String CONFIRM = "Y"; //confirm button
	private final String DENY = "N"; //deny button
	private final String JUMP = "SPACE";
	private final String QUIT = "ESCAPE";
	private final String ENDMSG = "Goodbye!"; //exit message

	private final int MIN_LENGTH = 16; //16 - 50 are suggested, as they provide the best experience. Too long numbers take to long to process and slow down the game.
	private final int MAX_LENGTH = 50;
	private final int MIN_SPEED = 2; //2-8 are playable, as > 8 starts to lag
	private final int MAX_SPEED = 8;


	private int gridLength;
	private int stepsPerSecond;

	//welcome screen
	public void startScreen() {
		scanner = new Scanner(System.in);

		System.out.println("Welcome to " + GAMENAME + "!");
		System.out.println("Press " + JUMP + " to jump and " + QUIT + " to quit mid-game.");
		System.out.println("Please enter the length of the playing field (" + MIN_LENGTH + " - " + MAX_LENGTH + ")");

		//get length
		while (true) {
			input = scanner.next(); //read input
			gridLength = Integer.parseInt(input);
			if (gridLength >= MIN_LENGTH && gridLength <= MAX_LENGTH) {
				break;
			} else {
				System.out.println("Enter valid length!");
			}
		}

		//get stepsPerSecond
		System.out.println("Enter how fast the enemies should move (" + MIN_SPEED + " - " + MAX_SPEED + ")");
		while (true) {
			input = scanner.next(); //read input
			stepsPerSecond = Integer.parseInt(input);
			if (stepsPerSecond >= MIN_SPEED && stepsPerSecond <= MAX_SPEED) {
				break;
			} else {
				System.out.println("Enter valid speed!");
			}
		}

	}

	public int getGridLength() {
		return gridLength;
	}

	public int getSpeed() {
		return stepsPerSecond;
	}

	//game over screen
	public void gameOver(){
		scanner = new Scanner(System.in);

		System.out.println("Game over. Your score is " + score +".");
		System.out.println("Do you want to save the score?");
		System.out.println("Enter " + CONFIRM +" to do so and quit so or " + DENY + " to quit.");

		while (true) { //keep user in infinite loop until input is valid and loop breaks by exiting.
			input = scanner.next();
			if (input.equalsIgnoreCase(CONFIRM)) {
				System.out.println("Enter your name:");
				input = scanner.next();

				String output = generateOutput(input, stepsPerSecond, gridLength); //generate final score
				System.out.println(output); //show user final output
				printToFile(output); //write output to file
				System.out.println("Your score file is located in the current execution directory.");
				System.out.println(ENDMSG);
				System.exit(0);
			} else if (input.equalsIgnoreCase(DENY)) {
				System.out.println(ENDMSG);
				System.exit(0);
			} else {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}

	//increase Score by one
	public void increaseScore() {
		score += 1;
	}

	//generate output string
	private String generateOutput(String name, int stepsPerSecond, int gridLength) {
		return  "Name: " + name + " @Speed: " + stepsPerSecond + " @Length: " + gridLength;
	}
	
	
	//write to output file
	//partly derived from https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java
	//and https://stackoverflow.com/questions/8210616/printwriter-append-method-not-appending
	private void printToFile(String output) {
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(new File(FILENAME),true)); //file is saved to current directory out of which the game is executed
			out.append(output + System.lineSeparator()); //add new line to the score file if it already exists
			out.close(); //close file
		} catch (FileNotFoundException e) { //throw exception if file is not found
			System.out.println("There was an error outputting the score!");
			e.printStackTrace();
		}


	}
}
