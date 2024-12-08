package players;

import paintings.FixedPriceAuctionPainting;
import paintings.HiddenAuctionPainting;
import paintings.OneOfferAuctionPainting;
import paintings.Painting;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class represents a player in the ModernArt game
 */
public class Player {
    private final String name; // The name of the player
    private int money; // The money the player has
    protected static int totalPlayers = 0; // Total number of players in the game
    public List<Painting> handPaintings = new ArrayList<>(); // Paintings in hand
    protected List<Painting> boughtPaintings = new ArrayList<>(); // Paintings bought
    protected int level; // Player's level
    protected int experiencePoints; // Experience points for leveling

    public Player(int money, String name) {
        this.money = money;
        this.level = 1; // Starting level
        this.experiencePoints = 0; // Initial experience points
        if (this instanceof AFKPlayer)
            this.name = "AFK " + (totalPlayers - 1);
        else if (this instanceof ComputerPlayer)
            this.name = "Computer " + (totalPlayers - 1);
        else
            this.name = name;
    }

    public Player(int money) {
        this(money, "Player " + totalPlayers++);
    }

    public void dealPaintings(Painting painting) {
        handPaintings.add(painting);
        painting.setOwner(this);
    }

    public final String getName() {
        return name;
    }

    private Painting removePaintingFromHand(int index) {
        if (index < 0 || index >= handPaintings.size()) {
            return null;
        }
        Painting painting = handPaintings.get(index);
        handPaintings.remove(index);
        return painting;
    }

    public Painting playPainting() {
        int index = -1;
        if (handPaintings.size() == 0) {
            return null;
        }
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println(this);
                int i = 0;
                for (Painting p : handPaintings) {
                    System.out.println(i++ + ": " + p);
                }
                System.out.print("Please enter the index of the Painting you want to play: ");
                index = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        } while (index < 0 || index >= handPaintings.size());
        return removePaintingFromHand(index);
    }

    public final int getMoney() {
        return money;
    }

    public int bid(int currentBid, Painting p) {
        int offer = bid(currentBid);
        if (offer <= 0 && p instanceof FixedPriceAuctionPainting) {
            System.out.println(name + " has passed.");
            return 0;
        } else if (offer <= 0 && (p instanceof HiddenAuctionPainting || p instanceof OneOfferAuctionPainting)) {
            System.out.println(name + " bids 0");
            return 0;
        }
        return offer;
    }

    @Deprecated
    protected int bid(int currentBid) {
        Scanner scanner = new Scanner(System.in);
        do {
            try {
                System.out.println(this);
                System.out.print("Enter your bid (enter 0 = forfeit): ");
                int bid = scanner.nextInt();
                if (bid > money)
                    continue;
                return bid;
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine();
            }
        } while (true);
    }

    public void pay(int amount) {
        money -= amount;
    }

    public void earn(int amount) {
        money += amount;
    }

    public String toString() {
        return name + " has $" + money + " (Level: " + level + ")";
    }

    public void buyPainting(Painting painting) {
        boughtPaintings.add(painting);
    }

    public void sellPainting(int[] scores) {
        for (Painting painting : boughtPaintings) {
            money += scores[painting.getArtistId()];
            earnXP(scores[painting.getArtistId()]); // Earn XP based on sold paintings
        }
        boughtPaintings.clear();
    }

    // Leveling methods
    public void earnXP(int amount) {
        experiencePoints += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        int requiredXP = level * 100; // Example: 100 XP for level 1, 200 for level 2, etc.
        if (experiencePoints >= requiredXP) {
            level++;
            onLevelUp();
        }
    }

    protected void onLevelUp() {
        System.out.println(name + " leveled up to level " + level + "!");
        money += 20; // Reward for leveling up
        // Additional rewards or abilities can be added here
    }
    public int getXP() {
        return experiencePoints;
    }

}