/*	Ofek Gila
	February 10th, 2014
	MancalaKalah.java
	This game will try to play the game of Mancala (known as Kalah)
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Scanner;

public class MancalaKalah {

	JFrame frame;			// JFrame and JPanel must be global object instances
	MyPanel panel;
	
	
	public static void main(String[] args) {
		MancalaKalah sjf = new MancalaKalah();
		sjf.Run();
	}

	public void Run() {
		frame = new JFrame("Mancala Kalah");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create JPanel and add to frame
		
		panel = new MyPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);	// add panel to frame
		
		frame.setResizable(false);
		frame.setSize(1060, 500);		// explicitly set size in pixels
		frame.setVisible(true);		// set to false to make invisible
		
	}

}	// end class SimpleJFrame2

// Create a JPanel class
////////////////////////////////////////////////
// >>> MANDATORY: implement a MouseMotionListener
//
class MyPanel extends JPanel implements MouseListener, MouseMotionListener {

	public final int HoleSize = 120;
	public final int boarders = 20;
	public int[] MKT = new int[14];	// Mancala Kalah Table
	public int[][] SMKT = new int[19][100];
	public boolean moveAgain;
	public boolean done;
	public boolean captured;
	public boolean AI = false;
	public int resultD, mPFTBRD;	// move piece for the best result difference
	private int xloc, yloc;	
	private int width, height;
	public byte turn, winner;
	public int placeClicked;
	public int seedOn, lastSeed;
	public int captures;
	public int turns;
	public Graphics g;
	public Color co;
	
	public MyPanel() {
		addMouseListener(this);
		addMouseMotionListener(this);
		constructor();
	}
	public void constructor()	{
		turn = 1;
		turns = 0;
		moveAgain = done = captured = false;
		co = new Color(169, 106, 23);
		setBackground(co);
		lastSeed = seedOn = placeClicked = -1;
		for (int i = 0; i < MKT.length; i++)
			if (i != 6 && i != 13)
				MKT[i] = 4;
			else MKT[i] = 0;
	}
	public void paintComponent(Graphics g) {
		this.g = g;
		super.paintComponent(g);	// execute the superclass method first
		width = getWidth();			// width of JPanel
		height = getHeight();		// height of JPanel
		drawMancalaKalah();
	}
	public void drawMancalaKalah()	{
		g.setColor(Color.black);
		g.setFont(new Font("Arial", Font.BOLD, 40));
		String s = "";
		if (captured) s = "Captured: " + captures;
		//System.out.println(done + " " + winner);
		if (!(done))	g.drawString("Player: " + turn + "   " + s, boarders*2 + HoleSize, boarders + 20);
		else if (winner == 1)	g.drawString("Player 1 won!!!", boarders*2 + HoleSize, boarders+20);
		else if (winner == 2)	g.drawString("Player 2 won!!!", boarders*2 + HoleSize, boarders+20);
		else if (winner == 0)	g.drawString("Tie game!!!", boarders*2 + HoleSize, boarders+20);

		g.setColor(Color.lightGray);
		g.fillOval(boarders * 2 + HoleSize, boarders*7 + HoleSize*2, width - 2 * (boarders * 2 + HoleSize), 4 * boarders);
		g.setColor(Color.gray);
		g.drawOval(boarders * 2 + HoleSize, boarders*7 + HoleSize*2, width - 2 * (boarders * 2 + HoleSize), 4 * boarders);
		g.setColor(Color.magenta);
		g.drawString("UNDO", ((boarders * 2 + HoleSize + width - 2 * (boarders * 2 + HoleSize)) / 2) + 15, boarders*7 + HoleSize*2 + 3 * boarders - 5);
		g.setColor(Color.black);

		//g.setFont(new Font("Arial", Font.ITALIC, 20));
	//	if (13 
	//		co = new Color(247, 247, 131);
	//		g.setColor(co);)
		if (lastSeed < placeClicked)	{
			co = new Color(173, 118, 45);
			g.setColor(co);
			g.fillOval(boarders, boarders * 2, HoleSize, height - boarders * 4);
		}
		if ((6 > placeClicked && 6 < lastSeed) || (lastSeed < placeClicked && (6 > placeClicked || 6 < lastSeed)))	{
			co = new Color(173, 118, 45);
			g.setColor(co);
			g.fillOval(width - boarders - HoleSize, boarders * 2, HoleSize, height - boarders * 4);
		}
		if (lastSeed == 13) {
			g.setColor(Color.orange);
			g.fillOval(boarders, boarders * 2, HoleSize, height - boarders * 4);
		}
		if (lastSeed == 6) {
			g.setColor(Color.orange);
			g.fillOval(width - boarders - HoleSize, boarders * 2, HoleSize, height - boarders * 4);
		}
		g.setColor(Color.black);
		g.drawOval(boarders, boarders * 2, HoleSize, height - boarders * 4);
		g.drawString("" + MKT[13], boarders + 10, boarders * 2 + (height - boarders * 4)/2);
		g.drawOval(width - boarders - HoleSize, boarders * 2, HoleSize, height - boarders * 4);
		g.drawString("" + MKT[6], width - boarders - HoleSize + 10, boarders * 2 + (height - boarders * 4)/2);
		int i, a;
		for (i = 0; i < 14; i++)	{
			if (i != 6 && i != 13)	{
				if (i < 6)	{
					if ((i > placeClicked && i < lastSeed) || (lastSeed < placeClicked && (i > placeClicked || i < lastSeed)))	{
						co = new Color(173, 118, 45);
						g.setColor(co);
						g.fillOval(i * HoleSize + boarders * 2 + HoleSize, boarders * 3, HoleSize, HoleSize);
					}
					if (i == placeClicked)	{
						g.setColor(Color.green);
						g.fillOval(i * HoleSize + boarders * 2 + HoleSize, boarders * 3, HoleSize, HoleSize);
					}
					if (i == lastSeed)	{
						g.setColor(Color.orange);
						g.fillOval(i * HoleSize + boarders * 2 + HoleSize, boarders * 3, HoleSize, HoleSize);
					}
					if (captured && i == seedOn)	{
						g.setColor(Color.red);
						g.fillOval(i * HoleSize + boarders * 2 + HoleSize, boarders * 3, HoleSize, HoleSize);
					}
					g.setColor(Color.black);
					g.drawOval(i * HoleSize + boarders * 2 + HoleSize, boarders * 3, HoleSize, HoleSize);
					g.drawString("" + MKT[i], i * HoleSize + boarders * 2 + HoleSize + 10, boarders * 3 + HoleSize / 2);
				}
				else {
					if ((i > placeClicked && i < lastSeed) || (lastSeed < placeClicked && (i > placeClicked || i < lastSeed)))	{
						co = new Color(173, 118, 45);
						g.setColor(co);
						g.fillOval((12-i) * HoleSize + boarders * 2 + HoleSize, boarders * 6 + HoleSize, HoleSize, HoleSize);
					}
					if (i == placeClicked)	{
						g.setColor(Color.green);
						g.fillOval((12-i) * HoleSize + boarders * 2 + HoleSize, boarders * 6 + HoleSize, HoleSize, HoleSize);
					}
					if (i == lastSeed)	{
						g.setColor(Color.orange);
						g.fillOval((12-i) * HoleSize + boarders * 2 + HoleSize, boarders * 6 + HoleSize, HoleSize, HoleSize);
					}
					if (captured && i == seedOn)	{
						g.setColor(Color.red);
						g.fillOval((12-i) * HoleSize + boarders * 2 + HoleSize, boarders * 6 + HoleSize, HoleSize, HoleSize);
					}
					g.setColor(Color.black);
					g.drawOval((i - 7) * HoleSize + boarders * 2 + HoleSize, boarders * 6 + HoleSize, HoleSize, HoleSize);
					g.drawString("" + MKT[i], (5-(i - 7)) * HoleSize + boarders * 2 + HoleSize + 10, boarders * 6 + HoleSize / 2 + HoleSize);
				}
			}
		}
	}
	public void playMove(int x, int y)	{
	Scanner kn = new Scanner(System.in);	
		int i, a;
		if (AI && turn == 2)	{
			for (i = 0; i < MKT.length; i++)
				SMKT[i][turns] = MKT[i];
			SMKT[14][turns] = turn;
			SMKT[15][turns] = lastSeed;
			SMKT[16][turns] = seedOn;
			SMKT[17][turns] = placeClicked;
			if (captured) SMKT[18][turns] = 1;
			else SMKT[18][turns] = 2;
			int pC = 0, b, c = -10000;
			for (a = 7; a < 13; a++)	{
				if (SMKT[a][turns] != 0)	b = runAI(a, 20);
				else b = -123456;
				System.out.println(b);
				//String s = kn.nextLine();
				if (b > c && SMKT[a][turns] != 0)	{
					c = b;
					pC = a;
				}
			}
			placeClicked = pC;
			for (i = 0; i < MKT.length; i++)
					MKT[i] = SMKT[i][turns];
			turn = (byte)SMKT[14][turns];
			System.out.println("here " + placeClicked);
		}
		else placeClicked = getPlaceClicked(x, y);
		if (placeClicked == 15) return;
		if (MKT[placeClicked] == 0) return;
		if (turn == 1 && placeClicked > 6) return;
		if (turn == 2 && placeClicked < 6) return;
		for (i = 0; i < MKT.length; i++)
		SMKT[i][turns] = MKT[i];
		SMKT[14][turns] = turn;
		SMKT[15][turns] = lastSeed;
		SMKT[16][turns] = seedOn;
		SMKT[17][turns] = placeClicked;
		if (captured) SMKT[18][turns] = 1;
		else SMKT[18][turns] = 2;
		turns++;
		moveAgain = false;
		seed(placeClicked, MKT[placeClicked]);
		MKT[placeClicked] = 0;
		captured = false;
		if (MKT[seedOn] == 1 && !(seedOn == 6 || seedOn == 13) && MKT[12 - seedOn] != 0)	{	// Capturing
			captures = 1;
			MKT[seedOn] = 0;
			seedOn = 12 - seedOn;
			//if (seedOn > 13) seedOn -= 13; 
			captures += MKT[seedOn];
			MKT[seedOn] = 0;
			if (turn == 1) MKT[6] += captures;
			else MKT[13] += captures;
			captured = true;
		}
		done = true;
		for (i = 0; i < 6; i++)			// Checking if game is done
			if (MKT[i] != 0) done = false;
		SMKT[14][turns] = turn;
		if (done) collectSide(2);
		else {
			done = true;
			for (a = 7; a < 13; a++)
				if (MKT[a] != 0) done = false;
			if (done) collectSide(1);
		}
		if (done) {	if (MKT[6] > MKT[13]) winner = 1; else if (MKT[6] != MKT[13]) winner = 2;	else winner = 0;	}	// Sets winner when game is done
		if (!(moveAgain)) {	if (turn == 1) turn = 2; else turn = 1;	}	// Switches turn if you don't get to go again
		repaint();
	}
	public int runAI(int placeClicked, int searchDepth)	{
	//	if (placeClicked == 15) return 0;
	//	if (MKT[placeClicked] == 0) return 0;
		//if (turn == 1 && placeClicked > 6) return 0;
		//if (turn == 2 && placeClicked < 6) return 0;
		int i, a;
		if (searchDepth == 0) {		return MKT[13] - MKT[6];	}
		if (MKT[placeClicked] == 0) return MKT[13] - MKT[6];
		int turns  = this.turns + searchDepth;
		//for (a = 0; a < MKT.length; a++)
		//			MKT[a] = SMKT[a][turns];
		//		turn = (byte)SMKT[14][turns];
		//System.out.println("depth" + (MKT[13])+" " + (MKT[6]));
		
		//int turns  = this.turns + searchDepth;
		moveAgain = false;
		seed(placeClicked, MKT[placeClicked]);
		MKT[placeClicked] = 0;
		captured = false;
		if (MKT[seedOn] == 1 && !(seedOn == 6 || seedOn == 13) && MKT[12 - seedOn] != 0)	{	// Capturing
			captures = 1;
			MKT[seedOn] = 0;
			seedOn = 12 - seedOn;
			//if (seedOn > 13) seedOn -= 13; 
			captures += MKT[seedOn];
			MKT[seedOn] = 0;
			if (turn == 1) MKT[6] += captures;
			else MKT[13] += captures;
			captured = true;
		}
		done = true;
		for (i = 0; i < 6; i++)			// Checking if game is done
			if (MKT[i] != 0) done = false;
		if (done) collectSide(2);
		else {
			done = true;
			for (a = 7; a < 13; a++)
				if (MKT[a] != 0) done = false;
			if (done) collectSide(1);
		}
		for (i = 0; i < MKT.length; i++)
			SMKT[i][turns] = MKT[i];
		SMKT[14][turns] = turn;
		//System.out.println("done!" + (MKT[13]) + " " + MKT[6]);	
		if (done) {	return MKT[13] - MKT[6];	}
		if (!(moveAgain)) {	if (turn == 1) turn = 2; else turn = 1;	}	// Switches turn if you don't get to go again
		resultD = 1;
		//System.out.println(searchDepth);
		if (turn == 1)	{
			for (i =0; i < 6; i++)	{
				int rD = runAI(i, searchDepth - 1);
				if (rD < -20) return rD;
				if (rD > resultD) {	resultD = rD;	mPFTBRD = i;	}
			}
		}
		if (turn == 2)	{
			for (i =7; i < 13; i++)	{
				int rD = runAI(i, searchDepth - 1);
				if (rD < -20) return rD;
				if (rD < resultD) {	resultD = rD;	mPFTBRD = i;	}
			}
		}
		return resultD;
	}
	public void collectSide(int side)	{
		int i, a;
		if (side == 1)	{
			for (i = 0; i < 6; i++)	{
				MKT[6] += MKT[i];
				MKT[i] = 0;
			}
		}
		else	{
			for (a = 7; a < 13; a++)	{
				MKT[13] += MKT[a];
				MKT[a] = 0;
			}
		}
	}
	public void seed(int seed, int seedsLeft)	{
		if (seedsLeft > 0)	{
			if (seed == 5)	{
				if (turn == 1) seed = 6;
				else seed = 7;
			}
			else if (seed == 12)	{
				if (turn == 2) seed = 13;
				else seed = 0;
			}
			else if (seed == 6) seed = 7;
			else if (seed == 13) seed = 0;
			else seed++;
			MKT[seed]++;
			seed(seed, seedsLeft - 1);
		}
		if (seedsLeft == 0 && (seed == 13 || seed == 6)) moveAgain = true;
		if (seedsLeft == 0)	{	seedOn = seed;	lastSeed = seed;	}
	}
	public int getPlaceClicked(int x, int y)	{
		int i;
		for (i = 0; i < 14; i++)	{
			if (i != 6 && i != 13)	{
				if (i < 6)	{
					if (x >= i * HoleSize + boarders * 2 + HoleSize && x <= i * HoleSize + boarders * 2 + HoleSize * 2 && y >= boarders * 3 && y <= boarders * 3 + HoleSize)
						return i;
				}
				else {
					if (x >= (5-(i - 7)) * HoleSize + boarders * 2 + HoleSize && x <= (5-(i - 7)) * HoleSize + boarders * 2 + HoleSize * 2 && y >= boarders * 6 + HoleSize && y <= boarders * 6 + HoleSize * 2)
						return i;
				}
			}
		}
		return 15;
	}
	public void mousePressed (MouseEvent e) {	}
	public void mouseReleased (MouseEvent e) {	}
	public void mouseClicked (MouseEvent e) {
		xloc = e.getX();
		yloc = e.getY();
		if (xloc >= boarders * 2 + HoleSize && xloc <= 7 * HoleSize + boarders * 2 && yloc >= boarders * 3 && yloc <= boarders * 6 + HoleSize * 2)
			playMove(xloc, yloc);
		//g.drawOval(boarders * 2 + HoleSize, boarders*7 + HoleSize*2, width - 2 * (boarders * 2 + HoleSize), 4 * boarders);
		if (xloc >= boarders * 2 + HoleSize && xloc <= boarders * 2 + HoleSize + (width - 2 * (boarders * 2 + HoleSize)) && yloc >= boarders * 7 + HoleSize * 2 && yloc <= boarders * 13 + HoleSize * 2)	{
			if (!(e.isShiftDown()))	{
				turns--;
				for (int i = 0; i < MKT.length; i++)
					MKT[i] = SMKT[i][turns];
				turn = (byte)SMKT[14][turns];
				lastSeed = SMKT[15][turns];
				seedOn = SMKT[16][turns];
				placeClicked = SMKT[17][turns];
				if (SMKT[18][turns] == 1) captured = true;
				else captured = false;
				moveAgain = done = false;
			}
			else	{
				MKT = new int[14];	// Mancala Kalah Table
				SMKT = new int[19][50];
				constructor();
			}
			repaint();
		}
		else;
	}
	public void mouseEntered (MouseEvent e) {}
	public void mouseExited (MouseEvent e) {}
	public void mouseDragged (MouseEvent e)	{	}
	public void mouseMoved (MouseEvent e)	{	};

}