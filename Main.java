package bb;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import java.util.ArrayList;

public class Main {
	
	//for difficulty later: easy ammo count = tiles.size x 0.8. medium = x 0.65. hard x 0.5
	public static final ArrayList<Side> tiles = new ArrayList<Side>();
	public static int boardSize;
	public static int shipSizeOne;
	public static int shipSizeTwo;
	public static double difficulty;
	
	public static boolean gameRunning;

	public static void main(String[] args) {
		
		Scanner input = new Scanner(System.in);
		System.out.println("Welcome to my game!");
		System.out.println("Please enter the size of board you wish to play on: ");
		System.out.println("Small: 4x4 board");
		System.out.println("Medium: 5x5 board");
		System.out.println("Large: 7x7 board");
		
		//gets user input to select board size
		String select = input.nextLine().toLowerCase();
		
		switch(select){
		case "small", "sm", "1", "4", "4x", "4x4", "s":
			boardSize = 4;
			shipSizeOne = 2;
			shipSizeTwo = 2;
			break;
		case "medium", "md", "med", "mid", "2", "5", "5x", "5x5", "m":
			boardSize = 5;
			shipSizeOne = 2;
			shipSizeTwo = 3;
			break;
		case "large", "lg", "3", "7", "7x", "7x7", "l":
			boardSize = 7;
			shipSizeOne = 4;
			shipSizeTwo = 5;
			break;
		default:
			System.out.println("Unexpected input.");
			System.out.println("Defaulting to \"Medium\" board size.");
			boardSize = 5;
			shipSizeOne = 2;
			shipSizeTwo = 3;
			try {
				TimeUnit.SECONDS.sleep(2);
			}
			catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			break;
		}
		
		//gets user input to select difficulty.
		System.out.println("Choose Difficulty! (How many shots you have!)");
		System.out.println("Easy");
		System.out.println("Medium");
		System.out.println("Hard");
		String diffSelect = input.nextLine().toLowerCase();
		
		switch(diffSelect) {
		case "easy", "e", "1":
			difficulty = 0.8;
			break;
		case "medium", "m", "2":
			difficulty = 0.65;
			break;
		case "hard", "h", "3":
			difficulty = 0.5;
			break;
		default:
			System.out.println("Unexpected input.");
			System.out.println("Defaulting to \"Medium\" difficulty.");
			difficulty = 0.65;
			try {
				TimeUnit.SECONDS.sleep(2);
			}
			catch(InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
			break;
		}
		
		//populating arrayList
		char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		int[] pair = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26};
		
		for (int i = 0; i < boardSize;) {								
			for (int j = 0; j < boardSize; j++) {						
				String name = (alphabet[i]+String.valueOf(pair[j]));
				tiles.add(new Side(0, false, "   |", name));
			}
			i++;
		}
		
		
		//ship creation
		Side.popMatrix(boardSize);
		int[] ship1 = Side.buildShip(shipSizeOne);
		Side.setShip(ship1);
		int[] ship2 = Side.buildShip(shipSizeOne);
		Side.setShip(ship2);
		int[] ship3 = Side.buildShip(shipSizeTwo);
		Side.setShip(ship3);
		
		//next: print board and ships(done), set win/lose conditions, main game loop.
		
		//game loop
		String message = "";
		gameRunning = true;
		int ammo = (int)(tiles.size()*difficulty);
		while (gameRunning){
			
			for (int i = 0; i < 30; ++i) System.out.println("\n"); //bootleg console clear
			
			//win condition check
			if(sinkCheck(ship1) && sinkCheck(ship2) && sinkCheck(ship3)) {
				gameRunning = false;
				System.out.println("You sank all the ship! Congratulations!");
				input.close();
				break;
			}
			
			//lose condition check
			if(loseCheck(ammo)) {
				gameRunning = false;
				System.out.println("You ran out of ammo! Better luck next time!");
				input.close();
				break;
			}
			
			
			//ship printing
			printShip(ship1);
			printShip(ship2);
			printShip(ship3);
			
			//ammo printing
			System.out.println("Shots Left: " + ammo);
			
			//new ascii board printer
			int l = 0;
			int m = 0;
			int o = 0;
			System.out.print("    "); //space in top left.
			while (o < boardSize) {
				System.out.print(" " + pair[o] + "  "); //prints top layer of numbers
				o++;
			}
			System.out.print("\n"); //line break;
			int indexer = 0;
			while (m < tiles.size()) {
				System.out.print(" " + alphabet[indexer] + " |"); //printing row markers.
				for (int n = 1; n <= boardSize; n++) {
					l = n + m;
					System.out.print(tiles.get(l - 1).getAscii()); //printing a row of object ascii each time loop iterates.
				}
				System.out.print("\n"); //line break
				indexer++;
				m = l;
			} 
			
			//user choosing where to fire loop.
			boolean turnEnd = false;											//resets turnEnd to false at start of each turn.
			boolean matchFound = false;											//resets matchFound to false at start of each turn
			while(turnEnd == false) {
				String tileName;
				System.out.println(message);
				System.out.println("Choose where to fire!");
				tileName = input.nextLine().toUpperCase();
				//loop iterates through each item in the ArrayList checking each object's "name" variable against the user input.
				int store = 0;
				for(int i = 0; i < 25; i++) {
					if(tileName.equals(tiles.get(i).getName())) {						//if user input successfully matches object in ArrayList
						matchFound = true;												//code stores the match as true boolean
						store = i;														//and stores the (i) value of the matched ArrayList object as "store"
					}
					if(matchFound && tiles.get(store).isFiredAt() == false) {			//if tile(store) has not been fired at.
						tiles.get(store).setters();										//sets variables to appropriate values with a method.
						if(tiles.get(store).getStatus() == 2) {							//if-else to set appropriate message based on what's been set.
							message = "that's a hit!";
							ammo--;
							turnEnd = true;
							break;
						}
						else {
							message = "that's a miss!";		
							ammo--;
							turnEnd = true;
							break;
						}
						
					}
					else if(matchFound && tiles.get(store).isFiredAt() == true){		//if tile(store) HAS been fired at.
						message = "You Cannot fire at the same place twice!";
						turnEnd = false;
					}
					else {																//if there was no match found in the for loop.
						message = "That's not a valid place to fire, pal!";
						turnEnd = false;
					}
				}

			}
			
		}
		
		//for testing
//		for(int i = 0; i < (boardSize*boardSize); i++) {
//			tiles.get(i).getValues();
//		}
//		System.out.println(Arrays.toString(ship1));
//		System.out.println(Arrays.toString(ship2));
//		System.out.println(Arrays.toString(ship3));
//	
//		System.out.println(Arrays.deepToString(Side.board));

	}

	private static boolean sinkCheck(int[] ship) {
		boolean sunk = true;
		for(int i = 0; i < ship.length; i++) {
			if(tiles.get(ship[i]).getStatus() != 2) {
				sunk = false;
				break;
			}
		}
		return sunk;
	}

	private static void printShip(int[] ship) {
		System.out.print("Ship    |");
		for(int i = 0; i < ship.length; i++) {
			System.out.print(tiles.get(ship[i]).getAscii());
		}
		System.out.print("\n");
	}
	
	private static boolean loseCheck(int ammo) {
		boolean end = false;
		if(ammo == 0) {
			end = true;
		}
		return end;
	}

}
