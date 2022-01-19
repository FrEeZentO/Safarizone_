package gamedemo;

//partly derived from https://github.com/kwhat/jnativehook/blob/2.2/doc/Keyboard.md
//using library "JNativeHook 2.21" from https://github.com/kwhat/jnativehook
//jnativehook is generally used to capture global keystrokes and process them
//as java natively is not able to capture "live keystrokes" from a terminal window
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

//this class takes care of implementing the "nativeKeyPressed" method and JNativeHook Library
//and takes care of the logic behind jumping
public class JumpHandler implements NativeKeyListener {

	private final int JUMPDURATION = 3; //how many steps the player remains in the air
	private boolean jump = false; //state of the jump
	private int remainingSteps = 0; //counter keeping track of remaining steps until player "lands"

	public JumpHandler() {
		
		//register global hook to capture keystrokes 
		try {
			GlobalScreen.registerNativeHook();
		}
		catch (NativeHookException ex) { //catch and process exception if one occurs
			System.out.println("There was an error with reading the keyboard input!");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
	}
	public void nativeKeyPressed(NativeKeyEvent e) {
		//if space is pressed and player is allowed to jump, then jump
		if (!jump && e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			jump = true; //player jumped
			remainingSteps = JUMPDURATION; //reset counter
		}

		//press escape to quit game with exit code 0
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			closeJumpHandler();
			System.out.println("Exit!");
			System.exit(0);
		}
	}

	//checks how long player is supposed to remain airborne 
	public void jumpCounter() {
		if (jump && remainingSteps > 0) {
			remainingSteps -= 1;
		} else if (remainingSteps == 0) {
			jump = false; //put player back on ground
		}
	}
	
	//get the state of the player (airborne or not)
	public boolean isInAir() { 
		return jump;
	}
	
	//stop reading global keystrokes by unregistering the global hook
	public void closeJumpHandler() {
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException nativeHookException) { //catch exception if one occurs
			nativeHookException.printStackTrace();
		}
	}
}   