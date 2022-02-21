package snake;

import java.lang.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener{

	static final int SCREEN_WIDTH = 500;
	static final int SCREEN_HEIGHT = 500;
	static final int UNIT_SIZE = 50;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int DELAY = 50;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 2;
	int applesEaten;
	int appleX;
	int appleY;
	char prevDirection = 'R';
	char direction = 'R';
	char moves[] = new char[GAME_UNITS + 1];
	int movesLength = 0;
	int movesIndex = 0;
	boolean running = false;
	Timer timer;
	Random random;
	boolean leftOrRight = false;
	boolean gameWin = false;
	
	int applex[] = {15, 11, 10, 3};
	int appley[] = {11, 4, 10, 3};
	int appleIndex = 0;
	
	String map = 	"__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________"
				  + "__________";
	
	GamePanel(){
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
		for (int i = 0; i < GAME_UNITS; i++) {
			x[i] = -1;
			y[i] = -1;
		}
		for (int i = 0; i < bodyParts; i++) {
			x[i] = 0;
			y[i] = SCREEN_HEIGHT - UNIT_SIZE - UNIT_SIZE;
		}
	}
	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY,this);
		timer.start();
	}
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	public void draw(Graphics g) {
		
		if(running) {
			
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			float greenComponent = (float)255;
			float redComponent = (float)0;
			float greenStep = (float)255 / (float)bodyParts;
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					redComponent = redComponent + greenStep;
					greenComponent = greenComponent - greenStep;
					g.setColor(new Color((int)redComponent,(int)greenComponent,0));
					//g.setColor(new Color(0,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			
			for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE); i++) {
				int xCoordinate = i % (SCREEN_WIDTH / UNIT_SIZE);
				int yCoordinate = i / (SCREEN_WIDTH / UNIT_SIZE);
				if (map.charAt(i) == '0') {
					g.setColor(Color.gray);
					g.fillRect(xCoordinate * UNIT_SIZE, yCoordinate * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			/*
			g.setColor(Color.red);
			g.setFont( new Font("Ink Free",Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
			*/
		}
		else {
			
			for(int i=0;i<SCREEN_HEIGHT/UNIT_SIZE;i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
		
			float greenComponent = (float)255;
			float redComponent = (float)0;
			float greenStep = (float)255 / (float)bodyParts;
			for(int i = 0; i< bodyParts;i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
				else {
					redComponent = redComponent + greenStep;
					greenComponent = greenComponent - greenStep;
					g.setColor(new Color((int)redComponent,(int)greenComponent,0));
					//g.setColor(new Color(0,180,0));
					//g.setColor(new Color(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}			
			}
			
			for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) * (SCREEN_HEIGHT / UNIT_SIZE); i++) {
				int xCoordinate = i % (SCREEN_WIDTH / UNIT_SIZE);
				int yCoordinate = i / (SCREEN_WIDTH / UNIT_SIZE);
				if (map.charAt(i) == '0') {
					g.setColor(Color.gray);
					g.fillRect(xCoordinate * UNIT_SIZE, yCoordinate * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
				}
			}
			
			if (gameWin == true) {
				gameWin(g);
			}
			else {
				gameOver(g);
			}

		}
		
	}
	public void newApple(){
		boolean thereIsAMatch = true;
		while (thereIsAMatch == true) {
			
			//appleX = applex[appleIndex] * UNIT_SIZE;
			//appleY = appley[appleIndex] * UNIT_SIZE;
			//appleIndex++;
			
			
			appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
			appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
			thereIsAMatch = false;
			
			for (int i = 0; i < bodyParts; i++) {
				if ((appleX == x[i]) && (appleY == y[i])) {
					thereIsAMatch = true;
				}
			}
		}
	}
	
	public void createmoves() {
		// otherwise, re-create the moves list
					movesIndex = 0;
					movesLength = 0;
					
					int lowestNonSnakeRow = 0;
					//find the first row with no snake parts
					for (int i = SCREEN_HEIGHT / UNIT_SIZE; i > 0; i--) {
						boolean noSnakesAbove = true;
						for (int j = 0; j < bodyParts; j++) {
							if ((y[j] / UNIT_SIZE) <= i) {
								noSnakesAbove = false;
							}
						}
						if (noSnakesAbove == true) {
							lowestNonSnakeRow = i;
							i = 0;
						}
					}
					boolean shouldSnake = true;
					int targetRow = lesser(lowestNonSnakeRow, appleY / UNIT_SIZE);
					if (targetRow == lowestNonSnakeRow) {
						shouldSnake = false;
					}
					boolean shouldSnake1 = false;
					if (targetRow % 2 == 1) {
						shouldSnake1 = true;
						targetRow--;
					}
					
					int numberOfUps = ((y[0] / UNIT_SIZE) - targetRow);
					for (int i = 0; i < numberOfUps; i++) {
						//turn to face upward
						moves[movesIndex] = 'U';
						movesIndex++;
						movesLength++;
					}
					
					//move the snake to the top-right
					for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 1; i++) {
						//add 'R' to the moves list
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					
					
					//skip the total number of downs allowable
					//if the length of the snake is less than the length of the bottom coil, skip to start of bottom
					int bottomCoil = 0;
					//if height is odd, bottomCoil is length * 2
					if ((SCREEN_HEIGHT / UNIT_SIZE) % 2 == 1) {
						bottomCoil = (SCREEN_WIDTH / UNIT_SIZE) * 2;
					}
					else {
						//otherwise (height is even), bottom coil is length + 1
						bottomCoil = (SCREEN_WIDTH / UNIT_SIZE) + 1;
					}
					
					if (((y[0] / UNIT_SIZE) > (targetRow + 2)) && shouldSnake && shouldSnake1) {
						//do one snake
						//turn to face downward
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					
						//turn to face leftward
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
					
						//move the snake to the left
						for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 3; i++) {
							//add 'L' to the moves list
							moves[movesIndex] = 'L';
							movesIndex++;
							movesLength++;
						}
					
						//turn to face downward
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					
						//turn to face rightward
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					
						//move the snake to the right
						for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 3; i++) {
							//add 'L' to the moves list
							moves[movesIndex] = 'R';
							movesIndex++;
							movesLength++;
						}
						targetRow++;
						targetRow++;
					}
					
					
					int numberOfSkips = 0;
					int numberOfRowSnakes = 0;
					int possibleRowSnakes = (((SCREEN_HEIGHT / UNIT_SIZE) - 2) - (targetRow)) / 2;
					numberOfSkips = 0;
					numberOfRowSnakes = possibleRowSnakes - numberOfSkips;
					
					for (int j = 0; j < numberOfSkips; j++) {
						//move downwards twice
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						targetRow++;
						targetRow++;
					}
					
					for (int j = 0; j < numberOfRowSnakes; j++) {
						//turn to face downward
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					
						//turn to face leftward
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
					
						//move the snake to the left
						for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 3; i++) {
							//add 'L' to the moves list
							moves[movesIndex] = 'L';
							movesIndex++;
							movesLength++;
						}
					
						//turn to face downward
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					
						//turn to face rightward
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					
						//move the snake to the right
						for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 3; i++) {
							//add 'L' to the moves list
							moves[movesIndex] = 'R';
							movesIndex++;
							movesLength++;
						}
					}
					
					// if the height is even
					if (((SCREEN_HEIGHT / UNIT_SIZE) % 2) == 0) {
						//turn to face downward
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
				
						//turn to face leftward
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
				
						//move the snake to the left
						for (int i = 0; i < (SCREEN_WIDTH / UNIT_SIZE) - 2; i++) {
							//add 'L' to the moves list
							moves[movesIndex] = 'L';
							movesIndex++;
							movesLength++;
						}
					
						//turn to face upward
						moves[movesIndex] = 'U';
						movesIndex++;
						movesLength++;
						
						/*
						//move the snake upward
						for (int i = 0; i < (SCREEN_HEIGHT / UNIT_SIZE) - 2; i++) {
							//turn to face upward
							moves[movesIndex] = 'U';
							movesIndex++;
							movesLength++;
						}
						*/
					}
					else {
						//do the first one manually
						//move downwards
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						//move downwards
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						//move left
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
						for (int i = 0; i < (((SCREEN_WIDTH / UNIT_SIZE) - 3) / 2); i++) {
							//move up
							moves[movesIndex] = 'U';
							movesIndex++;
							movesLength++;
							//move left
							moves[movesIndex] = 'L';
							movesIndex++;
							movesLength++;
							//move down
							moves[movesIndex] = 'D';
							movesIndex++;
							movesLength++;
							//move left
							moves[movesIndex] = 'L';
							movesIndex++;
							movesLength++;
						}
						if ((SCREEN_WIDTH / UNIT_SIZE) % 2 == 0) {
							/*
							//continue upward
							for (int i = 0; i < ((SCREEN_WIDTH / UNIT_SIZE) - 1); i++) {
								moves[movesIndex] = 'U';
								movesIndex++;
								movesLength++;
							}
							*/
							moves[movesIndex] = 'U';
							movesIndex++;
							movesLength++;
						}
						else {
							//left or right
							if (leftOrRight == true) {
								//left
								moves[movesIndex] = 'L';
								movesIndex++;
								movesLength++;
								//upward
								moves[movesIndex] = 'U';
								movesIndex++;
								movesLength++;
								leftOrRight = false;
							}
							else {
								//upward
								moves[movesIndex] = 'U';
								movesIndex++;
								movesLength++;
								//left
								moves[movesIndex] = 'L';
								movesIndex++;
								movesLength++;
								leftOrRight = true;
							}
							/*
							//continue upward
							for (int i = 0; i < ((SCREEN_WIDTH / UNIT_SIZE) - 2); i++) {
								moves[movesIndex] = 'U';
								movesIndex++;
								movesLength++;
							}
							*/
						}
					}
					
					movesIndex = 0;
					
					//make the first move
					// make the snake take that move
					switch(moves[movesIndex]) {
					case 'U':
						y[0] = y[0] - UNIT_SIZE;
						break;
					case 'D':
						y[0] = y[0] + UNIT_SIZE;
						break;
					case 'L':
						x[0] = x[0] - UNIT_SIZE;
						break;
					case 'R':
						x[0] = x[0] + UNIT_SIZE;
						break;
					}
					prevDirection = direction;
					movesIndex++;
	}

	public void updateArray(int xCopy[], int yCopy[], char direction, int bodyPartsCopy) {
		if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
			//System.out.println(bodyPartsCopy);
		}
		for(int i = bodyParts;i>0;i--) {
			xCopy[i] = xCopy[i-1];
			yCopy[i] = yCopy[i-1];
		}
			switch(direction) {
			case 'U':
				yCopy[0] = yCopy[0] - UNIT_SIZE;
				break;
			case 'D':
				yCopy[0] = yCopy[0] + UNIT_SIZE;
				break;
			case 'L':
				xCopy[0] = xCopy[0] - UNIT_SIZE;
				break;
			case 'R':
				xCopy[0] = xCopy[0] + UNIT_SIZE;
				break;
			}
	}
	
	public boolean noSnakeOnOrAbove(int yCopy[], int bodyPartsCopy) {
		boolean anySnake = false;
		for (int i = 0; i < bodyPartsCopy; i++) {
			if ( (yCopy[i] / UNIT_SIZE) < (yCopy[0] / UNIT_SIZE) ) {
				anySnake = true;
			}
		}
		//in case the row is odd the snake should still not use this row
		//despite there being no snake parts above the row
		if ( anySnake = false && (yCopy[0] / UNIT_SIZE) % 2 == 1) {
			anySnake = true;
		}
		return !(anySnake);
	}
	
	public boolean noSnakeOnOrAbove1(int yCopy[], int bodyPartsCopy) {
		
		boolean anySnake1 = false;
		for (int i = 0; i < bodyPartsCopy; i++) {
			if ( (yCopy[i] / UNIT_SIZE) <= (yCopy[0] / UNIT_SIZE) ) {
				anySnake1 = true;
			}
		}
		//in case the row is odd the snake should still not use this row
		//despite there being no snake parts above the row
		if ( anySnake1 = false && (yCopy[0] / UNIT_SIZE) % 2 == 1) {
			anySnake1 = true;
		}
		return !(anySnake1);
	}
	
	public boolean appleMatch(int xCopy[], int yCopy[]) {
		if (xCopy[0] == appleX && yCopy[0] == appleY) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public int coilOnceStopIfHitApple(int xCopy[], int yCopy[], int movesIndex, int movesLength, int bodyPartsCopy) {
		int numberofmovesIndexIncreases = 0;	
			//move right until you hit the rightmost column or the apple
			while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
				//move the snake right
				updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
				moves[movesIndex] = 'R';
				movesIndex++;
				movesLength++;
				numberofmovesIndexIncreases++;
				if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
					System.out.println("moving right");
				}
			}
			//move down if you have not hit the apple
			if (!(appleMatch(xCopy, yCopy))) {
				//move the snake down
				updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
				moves[movesIndex] = 'D';
				movesIndex++;
				movesLength++;
				numberofmovesIndexIncreases++;
				if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
					System.out.println("moving down");
				}
			}
			//move left until you hit the leftmost column or the apple
			while  ( ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
				//move the snake left
				updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
				moves[movesIndex] = 'L';
				movesIndex++;
				movesLength++;
				numberofmovesIndexIncreases++;
				if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
					System.out.println("moving left");
				}
			}
			//move down if you have not hit the apple
			if (!(appleMatch(xCopy, yCopy))) {
				//move the snake down
				updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
				moves[movesIndex] = 'D';
				movesIndex++;
				movesLength++;
				numberofmovesIndexIncreases++;
				if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
					System.out.println("moving down");
				}
			}
			return numberofmovesIndexIncreases;
	}
	
	public boolean canSkipToApple(int xCopy[], int yCopy[]) {
		// you can skip to the level of the apple if there is no snake parts
		// on the level of the apple, or any level above it up to and including
		// the level with the snake head, not including the snake head
		
		// check every level between the apple's level and the snake head level
		boolean anySnakePartsExceptHead = false;
		for (int i = 1; i < bodyParts; i++) {
			//check if the snake part's y level is between the apple's level 
			//and the snake head level
			if ( (yCopy[i] / UNIT_SIZE) <= (appleY / UNIT_SIZE) && (yCopy[i] / UNIT_SIZE) >= (yCopy[0] / UNIT_SIZE) ){
				if ( !(xCopy[i] == 0) ) {
					anySnakePartsExceptHead = true;
				}
			}
		}
		if (anySnakePartsExceptHead == false) {
		}
		return !(anySnakePartsExceptHead);
	}
	
	public boolean canMoveUp(int xCopy[], int yCopy[]) {
		// you can move to the left to go up if there are no snake parts
		// below the head
		boolean snakePartsBelowHead = false;
		for (int i = 0; i < bodyParts; i++) {
			if ( (yCopy[i]) > (yCopy[0]) ) {
				snakePartsBelowHead = true;
			}
		}
		return !(snakePartsBelowHead);
	}
	
	public boolean pathToLeftIsBlocked(int xCopy[], int yCopy[]) {
		boolean snakePartsInTheWay = false;
		for (int i = 0; i < bodyParts; i++) {
			if (yCopy[i] == yCopy[0]) {
				if (xCopy[i] < xCopy[0]) {
					snakePartsInTheWay = true;
				}
			}
		}
		
		if (snakePartsInTheWay) {
			System.out.println("need to coil once more");
		}
		else {
			System.out.println("the path is safe to go left");
		}
		
		return snakePartsInTheWay;
	}
	
	public void createSkipMoves() {
		movesIndex = 0;
		movesLength = 0;
		
		int xCopy[] = x.clone();
		int yCopy[] = y.clone();
		int bodyPartsCopy = bodyParts;
		
		/*
		//move example
		updateArray(xCopy, yCopy, 'D');
		bodyPartsCopy++;
		moves[movesIndex] = 'D';
		movesIndex++;
		movesLength++;
		*/
		
		// if you are in the left column
		if ((xCopy[0] / UNIT_SIZE) == 0) {
			// move upward until you are at the height of the apple 
			// or the lowest empty row
			/*
			while ( (!(noSnakeOnOrAbove(yCopy, bodyPartsCopy))) || ((appleY / UNIT_SIZE) < (yCopy[0] / UNIT_SIZE)) ) {
				//move the snake up
				updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'U';
				movesIndex++;
				movesLength++;
			}
			*/
			while ( (yCopy[0] / UNIT_SIZE) > 0 ) {
				//move the snake up
				updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'U';
				movesIndex++;
				movesLength++;
			}
			if ((yCopy[0] / UNIT_SIZE) % 2 == 1) {
				//move the snake up
				updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'U';
				movesIndex++;
				movesLength++;
			}
			//move the snake right
			updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
			bodyPartsCopy++;
			moves[movesIndex] = 'R';
			movesIndex++;
			movesLength++;
			/*
			//if the apple is below you
			if ( (appleY / UNIT_SIZE) > (yCopy[0] / UNIT_SIZE) ) {
				//coil until you hit the apple or can skip to it
				while ( !(canSkipToApple(xCopy, yCopy)) && !(appleMatch(xCopy, yCopy)) ) {
					int movesIncrease = coilOnceStopIfHitApple(xCopy, yCopy, movesIndex, movesLength, bodyPartsCopy);
					for (int i = 0; i < movesIncrease; i++) {
						movesIndex++;
						movesLength++;
						bodyPartsCopy++;
					}
					
				}
				//if you have not hit the apple, skip to the level of the apple
				// (or the one above it if the apple's level is even)
				// then coil to get the apple
				if (! (appleMatch(xCopy, yCopy))) {
					int intendedLevel = appleY / UNIT_SIZE;
					if (intendedLevel % 2 == 1 && !(appleY == yCopy[0])) {
						intendedLevel--;
					}
					//move downward until you are on the level of the apple
					while ((yCopy[0] / UNIT_SIZE) < intendedLevel) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
						
					//coil to get the apple
					//move right until you hit the rightmost column or the apple
					while ( ((xCopy[0]) < SCREEN_WIDTH) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					//move down if you have not hit the apple
					if (!(appleMatch(xCopy, yCopy))) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
					//move left until you hit the leftmost column or the apple
					while (  ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake left
						updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
					}
				}
			}
			else {
				// move right until you reach the apple
				while ( (xCopy[0] / UNIT_SIZE) < (appleX / UNIT_SIZE) ) {
					//move the snake right
					updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'R';
					movesIndex++;
					movesLength++;
				}
			}
			*/
		}
		else {
			System.out.println("row");
			// otherwise (snake is in a row-coil)
			
			// if the apple is in the left column, coil until you can go up
			// the side at it's level or lower
			if ( (appleX / UNIT_SIZE) == 0) {
				// if you are on an even row, move right until you reach the 
				// rightmost column, then move down once
				if ( (yCopy[0] / UNIT_SIZE) % 2 == 0) {
					//move right until you hit the rightmost column or the apple
					while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					if ( !appleMatch(xCopy, yCopy) ) {
						//move the snake down once
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
				}
				
				// move left until you reach the second to leftmost column
				while ( ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
					//move the snake left
					updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'L';
					movesIndex++;
					movesLength++;
					System.out.println("moving left to " + (xCopy[0] / UNIT_SIZE));
				}
				
				// if the apple is still below you or the path to the left 
				// side is blocked, coil
				while ( ((appleY / UNIT_SIZE) > (yCopy[0] / UNIT_SIZE)) || (pathToLeftIsBlocked(xCopy, yCopy)) ) {
					//move down
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
					//move all the way right
					while ( ((SCREEN_WIDTH / UNIT_SIZE) - 1) > (xCopy[0] / UNIT_SIZE) ) {
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					//move down
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
					//move to second to leftmost column
					while ( (xCopy[0] / UNIT_SIZE) > 1) {
						updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
					}
				}
				
				//move left once
				updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'L';
				movesIndex++;
				movesLength++;
			} // if the apple is below you, coil until you can skip to it
			else if ( (appleY / UNIT_SIZE) >= (yCopy[0] / UNIT_SIZE) ) {
				// if you are on an even row, move right until you reach the 
				// rightmost column, then move down once
				if ( (yCopy[0] / UNIT_SIZE) % 2 == 0) {
					//move right until you hit the rightmost column or the apple
					while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					if ( !appleMatch(xCopy, yCopy) ) {
						//move the snake down once
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
				}
				
				// move left until you reach the second to leftmost column
				while (  ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
					//move the snake left
					updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'L';
					movesIndex++;
					movesLength++;
				}
				
				if ( !appleMatch(xCopy, yCopy) ) {
					//move the snake down once
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
				}
				
				//coil until you hit the apple or can skip to it
				while ( !(canSkipToApple(xCopy, yCopy)) && !(appleMatch(xCopy, yCopy)) ) {
					int movesIncrease = coilOnceStopIfHitApple(xCopy, yCopy, movesIndex, movesLength, bodyPartsCopy);
					for (int i = 0; i < movesIncrease; i++) {
						movesIndex++;
						movesLength++;
						bodyPartsCopy++;
					}
				}
				//if you have not hit the apple, skip to the level of the apple
				// (or the one above it if the apple's level is even)
				// then coil to get the apple
				if (! (appleMatch(xCopy, yCopy))) {
					int intendedLevel = appleY / UNIT_SIZE;
					if (intendedLevel % 2 == 1 && !(appleY == yCopy[0])) {
						intendedLevel--;
					}
					//move downward until you are on the level of the apple
					while ((yCopy[0] / UNIT_SIZE) < intendedLevel) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
						
					//coil to get the apple
					//move right until you hit the rightmost column or the apple
					while (  ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
					}
					//move down if you have not hit the apple
					if (!(appleMatch(xCopy, yCopy))) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
					}
					//move left until you hit the leftmost column or the apple
					while ( ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake left
						updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
					}
				}
			}
			else {
				// Otherwise (the apple is above you)
				// coil until you can go up the left

				// if you are on an even row, move right until you reach the 
				// rightmost column, then move down once
				if ( (yCopy[0] / UNIT_SIZE) % 2 == 0) {
					//move right until you hit the rightmost column or the apple
					while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving right");
						}
					}
					//move the snake down
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
					if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
						System.out.println("moving down");
					}
				}
				
				// move left until you reach the second to leftmost column
				while (  ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
					//move the snake left
					updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'L';
					movesIndex++;
					movesLength++;
					if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
						System.out.println("moving left");
					}
				}
				
				while ( pathToLeftIsBlocked(xCopy, yCopy) && !(appleMatch(xCopy, yCopy)) ) {
					//move down once
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
					if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
						System.out.println("moving down");
					}
					//move all the way right
					while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1) ) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving right");
						}
					}
					//move down once
					updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'D';
					movesIndex++;
					movesLength++;
					if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
						System.out.println("moving down");
					}
					// move left until you reach the second to leftmost column
					while ( ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake left
						updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving left");
						}
					}
				}
				
				//move the snake left once
				updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'L';
				movesIndex++;
				movesLength++;
				if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
					System.out.println("moving left");
				}
				
				// go up until you are at the height of the apple or
				// the lowest empty row
				/*
				while ( !(noSnakeOnOrAbove1(yCopy, bodyPartsCopy)) || ((appleY / UNIT_SIZE) < (yCopy[0] / UNIT_SIZE)) ) {					
					//move the snake up
					updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'U';
					movesIndex++;
					movesLength++;
				}
				*/
				while ((yCopy[0] / UNIT_SIZE) > 0) {
					//move the snake up
					updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'U';
					movesIndex++;
					movesLength++;
				}
				if ((yCopy[0] / UNIT_SIZE) % 2 == 1) {
					//move the snake up
					updateArray(xCopy, yCopy, 'U', bodyPartsCopy);
					bodyPartsCopy++;
					moves[movesIndex] = 'U';
					movesIndex++;
					movesLength++;
					if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
						System.out.println("moving up");
					}
				}
				
				//move the snake right
				updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
				bodyPartsCopy++;
				moves[movesIndex] = 'R';
				movesIndex++;
				movesLength++;
				
				//tag here
				// then coil until you can skip to the apple
				while ( !(canSkipToApple(xCopy, yCopy)) && !(appleMatch(xCopy, yCopy)) ) {
					int movesIncrease = coilOnceStopIfHitApple(xCopy, yCopy, movesIndex, movesLength, bodyPartsCopy);
					for (int i = 0; i < movesIncrease; i++) {
						movesIndex++;
						movesLength++;
						bodyPartsCopy++;
					}
				}
			
				//if you have not hit the apple, skip to the level of the apple
				// (or the one above it if the apple's level is even)
				// then coil to get the apple
				if (! (appleMatch(xCopy, yCopy))) {
					int intendedLevel = appleY / UNIT_SIZE;
					if (intendedLevel % 2 == 1 && !(appleY == yCopy[0])) {
						intendedLevel--;
					}
					//move downward until you are on the level of the apple
					while ((yCopy[0] / UNIT_SIZE) < intendedLevel) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving down");
						}
					}
						
					//coil to get the apple
					//move right until you hit the rightmost column or the apple
					while ( ((xCopy[0] / UNIT_SIZE) < ((SCREEN_WIDTH / UNIT_SIZE) - 1)) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake right
						updateArray(xCopy, yCopy, 'R', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'R';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving right");
						}
					}
					//move down if you have not hit the apple
					if (!(appleMatch(xCopy, yCopy))) {
						//move the snake down
						updateArray(xCopy, yCopy, 'D', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'D';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving down");
						}
					}
					//move left until you hit the leftmost column or the apple
					while (  ((xCopy[0] / UNIT_SIZE) > 1) && !(appleMatch(xCopy, yCopy)) ) {
						//move the snake left
						updateArray(xCopy, yCopy, 'L', bodyPartsCopy);
						bodyPartsCopy++;
						moves[movesIndex] = 'L';
						movesIndex++;
						movesLength++;
						if ((appleX / UNIT_SIZE) == 5 && (appleY / UNIT_SIZE) == 9) {
							System.out.println("moving left");
						}
					}
				}
			}

		}
		
		movesIndex = 0;
		//make the first move
		// make the snake take that move
		switch(moves[movesIndex]) {
		case 'U':
			y[0] = y[0] - UNIT_SIZE;
			break;
		case 'D':
			y[0] = y[0] + UNIT_SIZE;
			break;
		case 'L':
			x[0] = x[0] - UNIT_SIZE;
			break;
		case 'R':
			x[0] = x[0] + UNIT_SIZE;
			break;
		}
		prevDirection = direction;
		movesIndex++;
	}
	
	public void move(){
		for(int i = bodyParts;i>0;i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		// if there are more moves to do
		if (movesIndex < movesLength) {
			// make the snake take that move
			switch(moves[movesIndex]) {
			case 'U':
				y[0] = y[0] - UNIT_SIZE;
				break;
			case 'D':
				y[0] = y[0] + UNIT_SIZE;
				break;
			case 'L':
				x[0] = x[0] - UNIT_SIZE;
				break;
			case 'R':
				x[0] = x[0] + UNIT_SIZE;
				break;
			}
			prevDirection = direction;
			movesIndex++;
		}
		else {
			createSkipMoves();
		}
	}
	
	public void checkApple() {
		if((x[0] == appleX) && (y[0] == appleY)) {
			bodyParts++;
			if (bodyParts == GAME_UNITS) {
				running = false;
				gameWin = true;
			}
			else {
			applesEaten++;
			newApple();
			}
		}
	}
	public void checkCollisions() {
		//checks if head collides with body
		for(int i = bodyParts - 1;i>0;i--) {
			if((x[0] == x[i])&& (y[0] == y[i])) {
				System.out.println("hit self");
				running = false;
			}
		}
		
		int mapIndex = (x[0] / UNIT_SIZE) + (y[0] / UNIT_SIZE) * (SCREEN_WIDTH / UNIT_SIZE);
		if (map.charAt(mapIndex) == '0') {
			running = false;
		}
		
		//check if head touches left border
		if(x[0] < 0) {
			running = false;
		}
		//check if head touches right border
		if(x[0] > SCREEN_WIDTH - UNIT_SIZE) {
			System.out.println("hit right border");
			running = false;
		}
		//check if head touches top border
		if(y[0] < 0) {
			running = false;
		}
		//check if head touches bottom border
		if(y[0] > SCREEN_HEIGHT - UNIT_SIZE) {
			running = false;
		}
		
		if(!running) {
			timer.stop();
		}
	}
	public void gameOver(Graphics g) {

		//Score
		/*
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Score: "+applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Score: "+applesEaten))/2, g.getFont().getSize());
		*/
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	public void gameWin(Graphics g) {
		//Game Win text
		g.setColor(Color.green);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Win", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(running) {
			move();
			checkApple();
			if (gameWin != true) {
				checkCollisions();
			}
		}
		repaint();
	}
	
	public int larger(int x, int y) {
		if (x > y) {
			return x;
		}
		else {
			return y;
		}
	}
	
	public int lesser(int x, int y) {
		if (x < y) {
			return x;
		}
		else {
			return y;
		}
	}
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(prevDirection != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(prevDirection != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(prevDirection != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(prevDirection != 'U') {
					direction = 'D';
				}
				break;
			}
		}
	}
}
