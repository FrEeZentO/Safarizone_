package gamedemo;

import java.util.concurrent.*;

import com.github.kwhat.jnativehook.GlobalScreen; //required to start the global hook (see JumpHandler)

//this class contains the main class, which in turn contains the core game logic.
//it also takes care of the global keyboard hook integration.
public class MainHandler
{	
	//the objects generated have to be static, as we want to access those from within the main method
	private static GameMenuHandler gmh = new GameMenuHandler();

	public static void main(String[] args) 
	{
		boolean result = gmh.startMenu(); //show start screen
		
		if (result == true) {
			JumpHandler jh = new JumpHandler(gmh.checkActivatedCheatCode("doubleJump", gmh.getActivatedCheatCodes())); //when doubleJump is set pass it to JumpHandler to activate it
			GameHandler gh = new GameHandler(gmh.getGridLength()); //generate playing field with given length
			
			GlobalScreen.addNativeKeyListener(jh); //start capturing global keyboard input
			//Main Game Loop
			while (!gh.getDidCollide()) {
				gh.checkCollision(jh.isInAir()); //check if player collides with object with regards to jump state

				if (gh.getDidCollide()) { //TODO Improve Logic (bad practice) / if player collides quit instantly by breaking loop
					jh.closeJumpHandler(); //close jump handler to stop reading global keyboard inputs
					break;  
				}

				gh.animateJump(jh.isInAir());  //if player is in air and did not crash, draw player in air
				gh.scrollAndPlaceObjects();   //shift all fields one to the left and place new object 
				gh.printGrid(); //print the grid formatted correctly

				
				final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
			    executorService.scheduleAtFixedRate
			    (new Runnable() {
			        public void run() 
			        {
			            gmh.getSpeed();
			        }
			    }, 0, 1, TimeUnit.SECONDS);
				
			    
				try { //TODO: Improve timing method (bad practice) / using thread.sleep to halt the loop and adjust the game speed.
					Thread.sleep(1000/gmh.getSpeed()); // (1000 ms / steps per second) = scrolling speed
				} catch (InterruptedException e) {
					e.printStackTrace(); //print exception if one is caught...
				}

				jh.jumpCounter(); //check state of jump and allow player to jump again
				gmh.increaseScore(); //increase score after every loop
			}
			jh.closeJumpHandler();
			gmh.gameOver();
		}
	}
}
