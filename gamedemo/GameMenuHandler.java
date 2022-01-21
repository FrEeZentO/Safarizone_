package gamedemo;

import java.io.File; //used for writing scores to file 
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter; //used for writing score to file
import java.util.Scanner; //used to get input
import java.util.Arrays; //used to convert Array to String
import java.util.Objects; //used with .util.Arrays to filter nonNull values

//this class handles the game menus such as start screen, game over screen, etc. and also takes care of score keeping

public class GameMenuHandler 
{
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
	private final String CREDIT = "This project was made by Team SAFARIZONE \nProduct Owner and Scrum Master: \nCaroline Schweizer \n\nDeveloper: \nPascal Reschke \nSemin Buljevic \nSamira Baecker";

	private final int MIN_LENGTH = 16; //16 - 50 are suggested, as they provide the best experience. Too long numbers take to long to process and slow down the game.
	private final int MAX_LENGTH = 50;
	private final int MIN_SPEED = 2; //2-8 are playable, as > 8 starts to lag
	private final int MAX_SPEED = 8;
	
	private String[] ACTIVATED_CHEAT_CODES = new String[5];
	private final String[] CHEAT_CODES = {"doubleJump"};

	private int gridLength;
	private int stepsPerSecond;

	/* show start menu
	 * @return boolean
	 */
	public boolean startMenu() {
		scanner = new Scanner(System.in);
		
		outerloop: // use labels to break out of multiple loops
		while (true) {
			try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();} catch (Exception e) {}
			
			System.out.println("|=================================================|");
			System.out.println("|             Welcome to " + GAMENAME + "!              |");
			System.out.println("|=================================================|");
			System.out.println("|   Please enter one of the following options:    |");
			System.out.println("| Play (1), Controls (2), Cheatcode (3), Quit (4) |");
			System.out.println("|=================================================|");
			
			input = scanner.next(); //read input to navigate through the menu
			switch (input) {
				case "1":
					playMenu();
					break outerloop;
				case "2":
					controlsMenu();
					break;
				case "3":
					cheatCodesMenu();
					break;
				case "4":
					System.out.println("|=================================================|");
					System.out.println("|               Thanks for playing!               |");
					System.out.println("|=================================================|");
					break outerloop;
				default:
					break;
			}
		}
		// do not break menu loop when you navigate through menu (Controls, Cheatcodes)
		if (gridLength != 0 && stepsPerSecond != 0) {
			return true;
		}
		return false;
	}
	
	/* show play menu
	 * @return void
	 */
	public void playMenu()
	{
		try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();} catch (Exception e) {} // TODO
		
		System.out.println("|==============================================|");
		System.out.println("|                   New Game                   |");
		System.out.println("|==============================================|");
		System.out.println("| Please enter the length of the playing field |");
		System.out.println("|                  (" + MIN_LENGTH + " - " + MAX_LENGTH + ")                   |");
		System.out.println("|==============================================|");
		
		//get gridLength
		while (true) {
			input = scanner.next();
			gridLength = Integer.parseInt(input);
			if (gridLength >= MIN_LENGTH && gridLength <= MAX_LENGTH) {
				break;
			} else {
				System.out.println("Enter valid length!");
			}
		}
		
		//get stepsPerSecond
		System.out.println("|==============================================|");
		System.out.println("|    Enter how fast the enemies should move    |");
		System.out.println("|                   (" + MIN_SPEED + " - " + MAX_SPEED + ")                    |");
		System.out.println("|==============================================|");
		while (true) {
			input = scanner.next();
			stepsPerSecond = Integer.parseInt(input);
			if (stepsPerSecond >= MIN_SPEED && stepsPerSecond <= MAX_SPEED) {
				break;
			} else {
				System.out.println("Enter valid speed!");
			}
		}
	}
	
	/* show controls menu
	 * @return void
	 */
	public void controlsMenu() 
	{
		try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();} catch (Exception e) {} // TODO
		
		System.out.println("|==============================================|");
		System.out.println("|                   Controls                   |");
		System.out.println("|==============================================|");
		System.out.println("|             Press " + JUMP + " to jump              |");
		System.out.println("|        Press " + QUIT + " to quit mid-game.        |");
		System.out.println("|----------------------------------------------|");
		System.out.println("|             Press B to go back.              |");
		System.out.println("|==============================================|");
		
		while (true) {
			input = scanner.next();
			if (new String(input).equals("b") || new String(input).equals("B")) {
				break;
			}
		}
	}
	
	/* show cheatCode menu
	 * @return void
	 */
	public void cheatCodesMenu() 
	{
		while (true) {
			try {new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();} catch (Exception e) {} // TODO
			
			String[] activatedCheatCodes = getActivatedCheatCodes();
			
			System.out.println("|==============================================|");
			System.out.println("|                  Cheatcodes                  |");
			System.out.println("|==============================================|");
			System.out.println("| Following cheatcodes are activated: " + Arrays.toString(activatedCheatCodes));
			System.out.println("|----------------------------------------------|");
			System.out.println("|             Enter cheatcode or               |");
			System.out.println("|             Press B to go back.              |");
			System.out.println("|==============================================|");
			
			input = scanner.next();
			if (new String(input).equals("b") || new String(input).equals("B")) {
				break;
			} else {
				// set cheatCode when available and not activated yet
				setCheatCode(input, activatedCheatCodes);
			}
		}
	}
	
	/* show gameOver screen
	 * @return void
	 */
	public void gameOver() 
	{
		scanner = new Scanner(System.in);

		System.out.println("Game over. Your score is " + score +".");
		System.out.println("Do you want to save the score?");
		System.out.println("Enter " + CONFIRM +" to save and quit or enter " + DENY + " to quit without saving.");

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
				System.out.println(CREDIT);
				System.exit(0);
			} else {
				System.out.println("Invalid input. Please try again.");
			}
		}
	}
	
	// =====================================================================================
	// HELPER-Functions
	// =====================================================================================
	
	/* check if cheatCode is already activated
	 * @return boolean
	 */
	public boolean checkActivatedCheatCode(String code, String[] activatedCheatCodes) 
	{
		if (activatedCheatCodes.length > 0) {
			for (int i = 0; i < activatedCheatCodes.length; i++) {
				if (new String(code).equals(activatedCheatCodes[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	/* increase score by one
	 * @return void
	 */
	public void increaseScore() 
	{
		score += 1;
	}
	
	/* generate output screen
	 * @return String
	 */
	private String generateOutput(String name, int stepsPerSecond, int gridLength) 
	{
		return  "Name: " + name + " @Speed: " + stepsPerSecond + " @Length: " + gridLength;
	}
	
	/* write to output file
	 * partly derived from https://stackoverflow.com/questions/1053467/how-do-i-save-a-string-to-a-text-file-using-java
	 * and https://stackoverflow.com/questions/8210616/printwriter-append-method-not-appending
	 * @return void
	 */
	private void printToFile(String output) 
	{
		try {
			PrintWriter out = new PrintWriter(new FileOutputStream(new File(FILENAME),true)); //file is saved to current directory out of which the game is executed
			out.append(output + System.lineSeparator()); //add new line to the score file if it already exists
			out.close(); //close file
		} catch (FileNotFoundException e) { //throw exception if file is not found
			System.out.println("There was an error outputting the score!");
			e.printStackTrace();
		}
	}
	
	// =====================================================================================
	// GETTER-Functions
	// =====================================================================================
	
	/* get activated cheatCodes as array
	 * @return <String> array 
	 */
	public String[] getActivatedCheatCodes() 
	{
		return Arrays.stream(ACTIVATED_CHEAT_CODES).filter(Objects::nonNull).toArray(String[]::new); //filter array for null values and get only activated string values
	}
	
	/* get length of grid
	 * @return Integer
	 */
	public int getGridLength() 
	{
		return gridLength;
	}
	
	/* get speed of game
	 * @return Integer
	 */
	public int getSpeed() 
	{
		return stepsPerSecond;
	}
	
	// =====================================================================================
	// SETTER-Functions
	// =====================================================================================
	
	/* set cheatCode when it is available and not activated yet
	 * @return void
	 */
	public void setCheatCode(String code, String[] activatedCheatCodes) 
	{
		for (int i = 0; i < CHEAT_CODES.length; i++) {
			if (new String(code).equals(CHEAT_CODES[i]) && checkActivatedCheatCode(code, activatedCheatCodes) == false) { //check if entered code equals hardcoded cheatCode in array
				ACTIVATED_CHEAT_CODES[activatedCheatCodes.length] = CHEAT_CODES[i]; //set cheatCode as activated
				// set grid to [][]
				// activate all functions to double jump
			}
		}
	}
}
