import java.util.Scanner;


public class Board{

static String[] board;	    
         
static void printBoard(){
	
        //System.out.println("|---|---|---|---|---|");
        System.out.println("| " + board[0] + " | "+ board[1] + " | "+ board[2] + " | "
                + board[3] + " | " + board[ 4]
                + " |");
        System.out.println("|-------------------|");
        System.out.println("| " + board[5] + " | "+ board[6] + " | "+ board[7] + " | "
                + board[8] + " | " + board[9]
                + " |");
        System.out.println("|-------------------|");
        System.out.println("| " + board[10] + " | "+ board[11] + " | "+ board[12] + " | "
                           + board[13] + " | " + board[14]
                           + " |");
        System.out.println("|-------------------|");
        System.out.println("| " + board[15] + " | "+ board[16] + " | "+ board[17] + " | "
                           + board[18] + " | " + board[19]
                           + " |");
        System.out.println("|-------------------|");
        System.out.println("| "+ board[20] + " | "+ board[21] + " | " + board[22] + " | "
                           + board[23] + " | " + board[24]
                           + " |");
        //System.out.println("|---|---|---|---|---|");
    }

/*public static void main(String[] args){
    Scanner in = new Scanner(System.in);
    board = new String[25];
    for (int a = 0; a < 25; a++){
        board[a] = String.valueOf(a + 1);
        in.close();
    }
    
    printBoard();
	}*/ 

public static void main(String[] args){
    Scanner in = new Scanner(System.in);
    board = new String[25];
    board[0] = "#"; board[1] = " "; board[2] = " "; board[3] = " "; board[4] = " ";
    board[5] = "#"; board[6] = " "; board[7] = " "; board[8] = " "; board[9] = " ";
    board[10] = "#"; board[11] = " "; board[12] = " "; board[13] = " "; board[14] = " ";
    board[15] = "#"; board[16] = " "; board[17] = " "; board[18] = " "; board[19] = " ";
    board[20] = "#"; board[21] = "#"; board[22] = "#"; board[23] = "#"; board[24] = "#";
    in.close();
    
    
    printBoard();
	}
}