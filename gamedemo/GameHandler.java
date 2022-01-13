package gamedemo;

public class GameHandler {

	public static void main(String[] args) {
		int length = 32; //min: 12
		
		GridHandler gh = new GridHandler(length);
		
		for (int i = 0; i < 10; i++) {
			gh.printGrid();
			gh.scrollGrid();
		}
		
		
		//TODO 
		//*kb input + anim
		//*collision
	}

}
