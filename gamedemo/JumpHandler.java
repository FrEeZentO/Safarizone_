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
public class JumpHandler implements NativeKeyListener 
{
	private final int JUMPDURATION = 3; //how many steps the player remains in the air
	private int jump = 0; //state of the jump
	private int remainingSteps = 0; //counter keeping track of remaining steps until player "lands"
	
	private boolean doubleJump = false;

	public JumpHandler(boolean doubleJumpActivated) 
	{
		// activate doubleJump
		if (doubleJumpActivated) {
			this.doubleJump = true;
		}
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
	
	public void nativeKeyPressed(NativeKeyEvent e) 
	{
		//if space is pressed and player is allowed to jump, then jump
		if ((jump == 1 && doubleJump) && e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			jump = 2; //player jumped to layer 2
	   	} else if (jump == 0 && e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			jump = 1; //player jumped to layer 1
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
	public void jumpCounter() 
	{
		if ((jump == 1 || jump == 2) && remainingSteps > 0) {
			remainingSteps -= 1;
		} else if (remainingSteps == 0) {
			jump = 0; //put player back on ground
		}
	}
	
	public int getRemainingSteps() {
		return remainingSteps;
	}
	
	//get the state of the player (airborne (2, 1) or not (0))
	public int isInAir() 
	{ 
		return jump;
	}
	
	//stop reading global keystrokes by unregistering the global hook
	public void closeJumpHandler() 
	{
		try {
			GlobalScreen.unregisterNativeHook();
		} catch (NativeHookException nativeHookException) { //catch exception if one occurs
			nativeHookException.printStackTrace();
		}
	}
}   