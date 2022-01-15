package gamedemo;

//partly derived from https://github.com/kwhat/jnativehook/blob/2.2/doc/Keyboard.md
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

//this class contains the main class, which in turn contains the core game logic.
//it also takes care of the global keyboard hook integration.
public class MainHandler implements NativeKeyListener{

	public static void main(String[] args) {          
		int length = 32; //length of the playing field (min 10)
		int jumpDuration = 2; //how many "steps" the player is supposed to be airborne
		int stepsPerSecond = 5; //how many fields move per second

		//register hook to capture key presses and catch exception if one occurs
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		}

		GameHandler gh = new GameHandler(length);
		JumpHandler jh = new JumpHandler(jumpDuration);
		GlobalScreen.addNativeKeyListener(jh); //used for capturing keyboard inputs

		//Main Game Loop
		while (!gh.getDidCollide()) {
			gh.checkCollision(jh.isInAir()); //check if player collides with object with regards to jump state
			if (gh.getDidCollide()) break;  //TODO Improve Logic (bad practice) / if player collides quit instantly by breaking loop
			gh.animateJump(jh.isInAir());  //if player is in air and did not crash, draw player in air
			gh.scrollAndPlaceObjects();   //shift all fields one to the left and place new object 
			gh.printGrid(); //print the grid formatted correctly

			try { //TODO: Improve timing method (bad practice) / using thread.sleep to halt the loop and adjust the game speed.
				Thread.sleep(1000/stepsPerSecond); // (1000 ms / steps per second) = scrolling speed
			} catch (InterruptedException e) {
				e.printStackTrace(); //print exception if one is caught...
			}

			jh.jumpCounter(); //check state of jump and allow player to jump again
		}
		System.out.println("Game over. Press Escape to exit.");

	}
}
