package bb;

public class Side {
	
	//status 0=no ship, 1=has ship, 2=damagedship.
	private int status;
	private boolean firedAt;
	private String ascii;
	private String name;
	public static int[][] board = new int[Main.boardSize][Main.boardSize];
	
	public Side(int status, boolean firedAt, String ascii, String name) {
		super();
		this.status = status;
		this.firedAt = firedAt;
		this.ascii = ascii;
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isFiredAt() {
		return firedAt;
	}

	public void setFiredAt(boolean firedAt) {
		this.firedAt = firedAt;
	}

	public String getAscii() {
		return ascii;
	}

	public void setAscii(String ascii) {
		this.ascii = ascii;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static void popMatrix(int size) {
		int x = 0;
		int i = 0;
		while(i < size) {
			for(int j = 0; j < size; j++) {
				board[i][j] = x;
				x++;
			}
			i++;
		}
	}
	
	static public boolean randBool() {
		boolean rand = Math.random() < 0.5;
		return rand;
	}
	
	static public void setShip(int[] size) {
		for(int i = 0; i < size.length; i++) {
			Main.tiles.get(size[i]).setStatus(1);
		}
	}
	
	static public boolean statusCheck(int[] check) {
		boolean pass = true;
		for(int i = 0; i < check.length; i++) {
			if(Main.tiles.get(check[i]).getStatus() == 1) {
				pass = false;
				break;
			}
		}
		return pass;
	}
	
	static public int[] buildShip(int shipLength) {							//method returns an array of values corresponing to appropraite positions of the arraylist index values.
		boolean looping = false;
		int[] ship = new int[shipLength];
		do {
			int[] store = new int[shipLength];
			boolean plusMinus = randBool();
			boolean dimension = randBool();
			
			looping = false;
			try {
				int init = (int)(Math.random()*Main.boardSize);				//chooses a random value to be the first section of ship
				store[0] = init;											
				for(int i = 0; i < shipLength-1; i++) {						//for loop/if-else statements choose to increase or decrease one of the multidimensional array indexes[][] based on position of first random value (init). this makes sure ships are places logically (in a row, without wrapping around the board.)
					if (plusMinus == true) {
						store[i+1] = init + i + 1;
					}
					else {
						store[i+1] = init - i - 1;
					}
					if(dimension == true) {
						for(int j = 0; j < store.length; j++) {
							ship[j] = board[store[j]][init];
						}
					}
					else {
						for(int j = 0; j < store.length; j++) {
							ship[j] = board[init][store[j]];
						}
					}
				}
			}
			catch (Exception ArrayIndexOutOfBoundsException){				//try/catch catches if method generates an OOB index value, and sets the method to try again with a boolean.
				looping = true;
			}
			
		} while(looping == true || statusCheck(ship) == false);
		return ship;
	}

	
	//for setting appropriate values based on user input
		public void setters() {
			if(this.status == 0) {												//if selected tile hasn't been fired at, and has no ship.
				this.firedAt = true;
				this.ascii = " O |";											//O indicates a miss
			}
			else if(this.status == 1) {											//if selected tile hasn't been fired at, and has a ship.
				this.firedAt = true;
				this.status = 2;												//if ship has been hit, status changes to indicate
				this.ascii = " X |";
			}
			else {
				System.out.println("I think I can remove this and replace the elseif statement with just else, but I'll test that later.");
			}
		}
	
	//for testing
	public void getValues() {
		System.out.println(this.status + " " + this.firedAt + " " + this.ascii + " " + this.name);
	}
	
	

}
