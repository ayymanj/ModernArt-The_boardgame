package paintings;

import players.Player;

public class DoublePaintingAuctionPainting extends Painting {
    private Painting secondPainting;
    public DoublePaintingAuctionPainting(int artistId) {
        super(artistId);
    }

    @Override
    public String getType() {
        return "Double Painting Auction";
    }

    @Override
    public void auction(Player[] players) {
        this.setOwner(this.getOwner());
        Player auctioneer = getOwner();

        int currentbid = 0;
        int aucInd = 0;

        boolean secondFound = false;

        System.out.println(auctioneer.getName() + " is starting a Double Auction!");

        for (int i = 0; i < players.length - 1; i++) {
            if(players[i] == auctioneer){
                aucInd = i;
                break;
            }

        }
        System.out.println("Searching for a second painting in " + auctioneer.getName() + "'s hand...");
        for (Painting paint : auctioneer.handPaintings) {
            if (paint.getArtistId() == this.getArtistId() && !(paint instanceof DoublePaintingAuctionPainting)) {
                secondPainting = paint;
                System.out.println("Found a second painting: ");
                auctionWithSecondPainting(players, paint);
                secondFound = true;
                break;
            }
        }
        if (!secondFound) {
            System.out.println("No matching painting found in " + auctioneer.getName() + "'s hand. Checking other players...");
            for (int i = 1; i < players.length; i++) {
                Player nextPlayer = players[(aucInd + i) % players.length];
                for (Painting paint : nextPlayer.handPaintings) {
                    if (paint.getArtistId() == this.getArtistId() && !(paint instanceof DoublePaintingAuctionPainting)) {
                        secondPainting = paint;
                        System.out.println(nextPlayer.getName() + " offers a second painting: " + paint.getType());
                        auctioneer = nextPlayer;
                        secondPainting.setOwner(this.getOwner());
                        auctionWithSecondPainting(players, secondPainting);
                        secondFound = true;
                        break;
                    }
                }
                if (secondFound) {
                    break;  // If a second painting is found, exit the loop
                }
                else{
                    currentBidder = auctioneer;
                    this.sold();
                }
            }
        }
    }

    private void auctionWithSecondPainting(Player[] players, Painting secondPainting) {

        int currentBid = 0;
        int highestBidder = -1;
        int highestBid = 0;

        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            int bid = player.bid(currentBid, secondPainting);
            System.out.println(player.getName() + " bids " + bid + " for the both painting.");
            if (bid > currentBid) {
                highestBid = bid;
                highestBidder = i;
            }
        }
        if (highestBidder > 0) {
            currentBidder = players[highestBidder];
            currentBid = highestBid;
            this.sold();
            secondPainting.sold();
            System.out.println("The Double Auction is complete! Both paintings sold to " + currentBidder.getName() + " for " + highestBid + ".");
        } else
            System.out.println("No valid bids received. The Double Auction failed.");
    }
}
