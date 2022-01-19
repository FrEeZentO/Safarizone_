package gamedemo;

//partly derived from https://github.com/kwhat/jnativehook/blob/2.2/doc/Keyboard.md
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

//this class takes care of implementing the "nativeKeyPressed" method
//and takes care of the logic behind jumping
public class JumpHandler implements NativeKeyListener {

	private boolean jump = false; //state of the jump
	private int jumpDuration; //how many steps the player remains in the air
	private int remainingSteps = 0; //counter keeping track of remaining steps until player "lands"

	public JumpHandler(int jumpDuration) {
		this.jumpDuration = jumpDuration;
	}
	public void nativeKeyPressed(NativeKeyEvent e) {
		//if space is pressed and player is allowed to jump, then jump
		if (!jump && e.getKeyCode() == NativeKeyEvent.VC_SPACE) {
			jump = true; //player jumped
			remainingSteps = jumpDuration; //reset counter
		}

		//press escape to quit game with exit code 0
		if (e.getKeyCode() == NativeKeyEvent.VC_ESCAPE) {
			try {
				GlobalScreen.unregisterNativeHook();
				System.out.println("QUIT!");
				System.exit(0); 
			} catch (NativeHookException nativeHookException) { //catch exception if one occurs
				nativeHookException.printStackTrace();
			}
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
}   