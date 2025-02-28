/**
 * 
 *
 * @author 
 * @version 
 */
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.ArrayList;

import javax.swing.event.MouseInputAdapter;


public class Game
{
    private final Canvas canvas;
    private final List<Card> cards;
    private final List<Card> player0Deck;
    private final List<Card> player1Deck;
    private final List<Card> player0Ground;
    private final List<Card> player1Ground;
    private final List<Card> player0Wins;
    private final List<Card> player1Wins;
    private final Text player0Text;
    private final Text player1Text;
    private final Rect bg;
    private boolean gameOver = false;
    private final Rect redScreen;

    private final List<String> ranks;
     
            
    /**
     * Create a window that will display and allow the user to play the game
     */
    public Game() {
        ranks = new ArrayList<String>();
        ranks.add("2"); ranks.add("3"); ranks.add("4"); ranks.add("5");
        ranks.add("6"); ranks.add("7"); ranks.add("8"); ranks.add("9");
        ranks.add("10"); ranks.add("Jack"); ranks.add("Queen");
        ranks.add("King"); ranks.add("Ace");

        cards = Card.loadCards();

        // Prepare the canvas
        canvas = Canvas.getCanvas();
        canvas.clear();
        canvas.setTitle("MyGame");
        
        player0Deck = new ArrayList<Card>();
        player1Deck = new ArrayList<Card>();
        player0Ground = new ArrayList<Card>();
        player1Ground = new ArrayList<Card>();
        player0Wins = new ArrayList<Card>();
        player1Wins = new ArrayList<Card>();
        bg = new Rect(0, 0, canvas.getWidth(), canvas.getHeight(), "green", true);
        redScreen = new Rect(0, 0, canvas.getWidth(), canvas.getHeight(), "red", false);
        player0Text = new Text("You", 50, 280, 40, "black", true);
        player1Text = new Text("Computer", 600, 300, 40, "black", true);

        deal();
        buildDisplay();  
        // Add a mouse handler to deal with user input
        canvas.addMouseHandler(new MouseInputAdapter() {
            public void mouseClicked(MouseEvent e) {
                onClick(e.getButton(), e.getX(), e.getY());
            }
            
            public void mouseMoved(MouseEvent e) {
                onMove(-1, e.getX(), e.getY());
            }

            public void mouseDragged(MouseEvent e) {
                onMove(e.getButton(), e.getX(), e.getY());
            }
        });
        
    }
    
    /**
     * Reset the Game and display to the beginning state
     */
    public void reset() {
        canvas.clear();
        
        buildDisplay();
    }

    /**
     * Deal out the cards at the beginning of the game
     */
    private void deal() {
        boolean whichPlayer = true;
        while(cards.size() > 0){
            int cardNum = (int)(Math.random() * cards.size());
            if(whichPlayer)
                player0Deck.add(cards.remove(cardNum));
            else
                player1Deck.add(cards.remove(cardNum));
            whichPlayer = !whichPlayer;
        }
    }

    /**
     * Displays the cards
     */
    private void displayCards() {
        bg.makeInvisible();
        bg.makeVisible();
        player0Text.makeInvisible();
        player0Text.makeVisible();
        player1Text.makeInvisible();
        player1Text.makeVisible();
        int p0X = 50;
        int p0Y = 50;
        for (int i = player0Deck.size() - 1; i >= 0; i--) {
            Card card = player0Deck.get(i);
            card.turnFaceDown();
            card.setPosition(p0X, p0Y);
            card.makeVisible();
            p0X++;
            p0Y--;
        }

        
        int p1X = 600;
        int p1Y = 350;
        for (int i = player1Deck.size() - 1; i >= 0; i--) {
            Card card = player1Deck.get(i);
            card.turnFaceDown();
            card.setPosition(p1X, p1Y);
            card.makeVisible();
            p1X++;
            p1Y--;
        }

        p0X = 50;
        p0Y = 350;

        for (int i = 0; i < player0Wins.size(); i++) {
            Card card = player0Wins.get(i);
            card.setPosition(p0X, p0Y);
            card.turnFaceUp();
            card.makeVisible();
            p0X++;
            p0Y--;
        }

        p1X = 600;
        p1Y = 50;
        for (int i = 0; i < player1Wins.size(); i++) {
            Card card = player1Wins.get(i);
            card.setPosition(p1X, p1Y);
            card.turnFaceUp();
            card.makeVisible();
            p1X++;
            p1Y--;
        }

        int g0X;
        int g0Y;
        boolean faceUp = true;
        for(int i = 0; i < player0Ground.size(); i++){
            System.out.println("Drawing battle ground card");
            Card card = player0Ground.get(i);
            card.makeInvisible();
            g0X = canvas.getWidth()/2 - card.getWidth()/2 - i * card.getWidth() / 2;
            g0Y = canvas.getHeight()/2 - 20 - card.getHeight();
            card.setPosition(g0X, g0Y);
            if(faceUp){
                card.turnFaceUp();
            } else {
                card.turnFaceDown();
            }
            card.makeVisible();
            faceUp = !faceUp;
        }

        int g1X;
        int g1Y;
        faceUp = true;
        for(int i = 0; i < player1Ground.size(); i++){
            Card card = player1Ground.get(i);
            card.makeInvisible();
            g1X = canvas.getWidth()/2 - card.getWidth()/2 + i * card.getWidth() / 2;
            g1Y = canvas.getHeight()/2 + 20;
            card.setPosition(g1X, g1Y);
            if(faceUp) {
                card.turnFaceUp();
            } else {
                card.turnFaceDown();
            }
            card.makeVisible();
            faceUp = !faceUp;
        }
        canvas.redraw();
    }

    private void battle (){
        if(player0Ground.size() == 0 || player1Ground.size() == 0){
            return;
        }
        int val0 = ranks.indexOf(player0Ground.get(player0Ground.size() - 1).getRank());
        System.out.println("Value 0: " + val0);
        int val1 = ranks.indexOf(player1Ground.get(player1Ground.size() - 1).getRank());
        System.out.println("Value 1: " + val1);
        if(val0 < val1){
            
            System.out.println("Player 1 wins");
            if(Math.random() < 0.5){
                while(player0Ground.size() > 0){
                    player1Wins.add(player0Ground.remove(0));
                }
                while(player1Ground.size() > 0){
                    player1Wins.add(player1Ground.remove(0));
                }
            } else {
                while(player1Ground.size() > 0){
                    player1Wins.add(player1Ground.remove(0));
                }
                while(player0Ground.size() > 0){
                    player1Wins.add(player0Ground.remove(0));
                }
            }
        } else if(val1 < val0){
            System.out.println("Player 0 wins");
            if(Math.random() < 0.5){
                while(player0Ground.size() > 0){
                    player0Wins.add(player0Ground.remove(0));
                }
                while(player1Ground.size() > 0){
                    player0Wins.add(player1Ground.remove(0));
                }
            } else {
                while(player1Ground.size() > 0){
                    player0Wins.add(player1Ground.remove(0));
                }
                while(player0Ground.size() > 0){
                    player0Wins.add(player0Ground.remove(0));
                }
            }
        }
    }

    /**
     * Play one turn of the game 
    */
    private void play(){
        logData();
        player0Ground.add(player0Deck.remove(0));
        player1Ground.add(player1Deck.remove(0));
        logData();
        buildDisplay();
        wait(1000);
        battle();
        logData();
        buildDisplay();
        logData();
    }
    
    /**
     * Setup the display for the game
     */
    private void buildDisplay() {
        displayCards();
    }

    /**
     * Handle the user clicking in the window
     * @param button the button that was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onClick(int button, int x, int y) {
        if(player0Deck.get(0).contains(x, y) || player1Deck.get(0).contains(x, y)){
            play();
        } else {
            flashRed();
        }
        
    }

    /**
     * Handle the user moving the mouse in the window
     * @param button the button that was pressed, or -1 if no button was pressed
     * @param x the x coordinate of the mouse position
     * @param y the y coordinate of the mouse position
     */
    private void onMove(int button, int x, int y) {
        if (button == -1) {
            //System.out.println("Mouse moved to " + x + ", " + y);
        }
        else {
            //System.out.println("Mouse dragged to " + x + ", " + y + " with button " + button);
        }
    }
    
    /**
     * Quickly flash the window background red
     */
    public void flashRed() {
        // The user messed up, show them they made a mistake
        redScreen.makeVisible();
        wait(500);
        redScreen.makeInvisible();
    }
    
    /**
     * Wait for a specified number of milliseconds before finishing. This
     * provides an easy way to specify a small delay which can be used when
     * producing animations.
     *
     * @param milliseconds the number
     */
    public static void wait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            // ignoring exceptions at the moment
        }
    }

    private void logData(){
        System.out.println("Player 0 deck: " + player0Deck);
        System.out.println("Player 1 deck: " + player1Deck);
        System.out.println("Player 0 battleground: " + player0Ground);
        System.out.println("Player 1 battleground: " + player1Ground);
        System.out.println("Player 0 winnings: " + player0Wins);
        System.out.println("Player 1 winnings: " + player1Wins);
    }

    /**
     * Run the game
     */
    public static void main(String[] args) {
        // Start the game
        new Game();
    }
}
