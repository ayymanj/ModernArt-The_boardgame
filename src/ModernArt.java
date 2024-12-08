
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

import paintings.*;
import players.*;


public class ModernArt {

    /**
     * PRE_DEAL contains the number of paintings that should be dealt to each
     * player before each round
     *
     * So for example, PRE_DEAL[3] means for 3 players, the number of painting
     * to be dealt to each player before round 1 is 10, round 2 is 6, and round
     * 3 is 6, and round 4 is 0
     */
    public static final int[][] PRE_DEAL = {null, null, null, //game can't be played for 0, 1, 2 players
        {10, 6, 6, 0}, {9, 4, 4, 0}, {8, 3, 3, 0}};
    /**
     * The game has 4 rounds in total
     */
    public static final int ROUND = 4;
    /**
     * The initial money each player has is 100
     */
    public static final int INITIAL_MONEY = 100;
    
    @Deprecated
    public static final int[] INITIAL_COUNT = {12, 13, 15, 15, 15};
    /**
     * Combination of paints for each artist
     *
     * Artist             | Open Auction | Hidden Auction | One Offer Auction | Fixed Price Auction 
     * 0. Manuel Carvalho | 3            | 3              | 3                 | 3 
     * 1. Sigrid Thaler   | 4            | 3              | 3                 | 3 
     * 2. Daniel Melim    | 4            | 3              | 4                 | 4 
     * 3. Ramon Martins   | 4            | 4              | 4                 | 3 
     * 4. Rafael Silveira | 4            | 4              | 3                 | 4
     */
    public static final int[][] PAINTS = {{3, 3, 3, 3, 3}, {4, 3, 3, 3,3}, {4, 3, 4, 4,4}, {4, 4, 4, 3,3}, {4, 4, 3, 4, 3}};

    /**
     * The price of the most sold paintings is 30, the second most sold is 20,
     * and the third most sold is 10
     *
     * Tie-breaker: if two artists have the same number of painting sold the one
     * with the lower id will be the winner, i.e.,
     *
     * If 0. Manuel Carvalho and 1. Sigrid Thaler have the same number of
     * paintings sold then 0. Manuel Carvalho will be considered have more
     * paintings sold than 1. Sigrid Thaler
     *
     */
    private static final int SCORES[] = {30, 20, 10};
    /**
     * Each round a painting can only be played for 5 times. The 5th time the
     * painting is played, it will not be placed in auction and that round ends
     * immediately
     */
    private static final int MAX_PAINTINGS = 5;
    /**
     * The number of players in the game, it should be between 3-5
     */
    private int noOfPlayers;
    /**
     * The array of players in the game
     */
    private Player[] players;
    /**
     * The deck of paintings
     */
    private List<Painting> deck = new ArrayList<>();
    /**
     * The score board of the game
     */
    private int[][] scoreboard = new int[ROUND][Painting.ARTIST_NAMES.length];

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                int noOfPlayers = Integer.parseInt(args[0]);
                if (noOfPlayers < 3 || noOfPlayers > 5) {
                    throw new Exception();
                }
                new ModernArt(noOfPlayers).startgame();
            } catch (Exception e) {
                System.out.println("Invalid argument. Please enter a valid integer between 3-5.");
            }
        } else {
            new ModernArt().startgame();
        }
    }


    /**
     * Default constructor, the game will be played with 3 players by default
     */
    public ModernArt() {
        //TODO
        this.noOfPlayers = 3;
        this.players = new Player[noOfPlayers];
        players[0] = new Player(INITIAL_MONEY);
        players[1] = new ComputerPlayer(INITIAL_MONEY, scoreboard);
        players[2] = new AFKPlayer(INITIAL_MONEY);
        prepareDeck();
    }

    
    public ModernArt(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
        this.players = new Player[noOfPlayers];
        players[0] = new Player(INITIAL_MONEY);
        players[1] = new ComputerPlayer(INITIAL_MONEY, scoreboard);

        for (int i = 2; i < noOfPlayers; i++) {
            players[i] = new AFKPlayer(INITIAL_MONEY);
        }
        prepareDeck();



    }

   
    public void prepareDeck() {
        //TODO
        for (int i = 0; i < PAINTS.length; i++)
            for (int j = 0; j < PAINTS[i].length; j++)
                for(int noOfTimes = 0; noOfTimes < PAINTS[i][j]; noOfTimes++){
                    if(j == 0)
                        deck.add(new OpenAuctionPainting(i));
                    else if (j == 1)
                        deck.add(new HiddenAuctionPainting(i));
                    else if (j == 2)
                        deck.add(new OneOfferAuctionPainting(i));
                    else if (j == 3)
                        deck.add(new FixedPriceAuctionPainting(i));
                    else
                        deck.add(new DoublePaintingAuctionPainting(i));

                }
        shuffle(deck);
    }
    
    public void dealPainting(int round) {
        //TODO
        for (int i = 0; i < noOfPlayers; i++) {
            for (int j = 0; j < PRE_DEAL[noOfPlayers][round]; j++) {
                players[i].dealPaintings(deck.remove(0));
            }
        }
    }

    
    public int[] updateScoreboard(int round, int[] paintingCount) {
        //TODO
        int maxIndex = 0;
        for (int i = 0; i < paintingCount.length; i++) {
            if (paintingCount[i] > paintingCount[maxIndex]) {
                maxIndex = i;
            }
        }
        int secondMaxIndex = -1;
        for (int i = 0; i < paintingCount.length; i++) {
            if (i == maxIndex) {
                continue;
            }
            if (secondMaxIndex == -1 || paintingCount[i] > paintingCount[secondMaxIndex]) {
                secondMaxIndex = i;
            }
        }
        int thirdMaxIndex = -1;
        if (secondMaxIndex != -1)
            for (int i = 0; i < paintingCount.length; i++) {
                if (i == maxIndex || i == secondMaxIndex) {
                    continue;
                }
                if (thirdMaxIndex == -1  || paintingCount[i] > paintingCount[thirdMaxIndex]) {
                    thirdMaxIndex = i;
                }
            }
        scoreboard[round][maxIndex] = SCORES[0];
        if (secondMaxIndex != -1 && paintingCount[secondMaxIndex] > 0) {
            scoreboard[round][secondMaxIndex] = SCORES[1];
        }
        if (thirdMaxIndex != -1 && paintingCount[thirdMaxIndex] > 0) {
            scoreboard[round][thirdMaxIndex] = SCORES[2];
        }
        for (int i = 0; i < paintingCount.length; i++) {
            if (i < players.length) {
                players[i].earnXP(paintingCount[i] * 10);
            }
        }
        int[] scoreForThisRound = new int[Painting.ARTIST_NAMES.length];
        for (int i = 0; i <= round; i++)
            for (int j = 0; j < Painting.ARTIST_NAMES.length; j++)
                if (scoreboard[round][j] != 0)
                    scoreForThisRound[j] += scoreboard[i][j];
        return scoreForThisRound;
    }

   
    public void startgame() {
        System.out.println("Welcome to ModernArt!");
        System.out.println("In this game, players earn experience points (XP) based on their performance.");
        System.out.println("You will earn 10 XP for each painting you sell.");
        System.out.println("As you accumulate XP, you will level up and unlock new rewards!");
        System.out.println("Let's begin the game!\n");

        // Initial XP for each player
        System.out.println("Initial XP for each player:");
        for (Player player : players) {
            System.out.println(player.getName() + ": " + player.getXP() + " XP");
        }
        System.out.println();
        int currentPlayer = 0;

        for (int round = 0; round < ROUND; round++) {
            //deal the paintings
            dealPainting(round);
            //start auction
            int[] paintingCount = new int[Painting.ARTIST_NAMES.length];
            while (true) {
                Player player = players[currentPlayer];
                Painting p = player.playPainting();

                if (p != null) {
                    System.out.println(p.getOwner().getName() + " plays " + p);
                    p.auction(players);
                    if (++paintingCount[p.getArtistId()] == MAX_PAINTINGS) {
                        break; //this round end immediately and the painting is not putting up for auction
                    }
                }

                currentPlayer = (currentPlayer + 1) % noOfPlayers;
                System.out.println("The number of painting sold: ");
                for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                    System.out.println(Painting.ARTIST_NAMES[i] + " " + paintingCount[i]);
                }
            }
            System.out.println("Complete auction - sell paintings");
            //update score board
            int[] scoreForThisRound = updateScoreboard(round, paintingCount);
            System.out.println("Print the score board after auction");
            System.out.print("\t\t");
            for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                System.out.print("\t" + j);
            }
            for (int i = 0; i < round + 1; i++) {
                System.out.print("\nRound " + i + ":\t\t");

                for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                    System.out.print(scoreboard[i][j] + "\t");
                }
            }

            System.out.println("\n\nPrint the price for each artist's painting");
            for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                System.out.println(Painting.ARTIST_NAMES[i] + " " + scoreForThisRound[i]);
            }

            //Sell the paintings
            for (Player p : players) {
                p.sellPainting(scoreForThisRound);
            }
            System.out.println("Updated XP after Round " + (round + 1) + ":");
            for (Player player : players) {
                System.out.println(player.getName() + ": " + player.getXP() + " XP");
            }
            System.out.println();
        }
        System.out.println("Game over, score of each player");
        for (int i = 0; i < noOfPlayers; i++) {
            System.out.println(players[i]);
        }
    }

    
    public void shuffle(List<Painting> deck) {
        for (int i = 0; i < deck.size(); i++) {
            int index = ThreadLocalRandom.current().nextInt(deck.size());
            Painting temp = deck.get(i);
            deck.set(i, deck.get(index));
            deck.set(index, temp);
        }
    }

    public int[][] getScoreboard() {
        return scoreboard;
    }

    public List<Painting> getDeck() {
        return deck;
    }
    public Player[] getPlayers() {return players;}
}
