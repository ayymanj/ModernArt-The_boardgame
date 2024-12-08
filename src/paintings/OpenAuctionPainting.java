package paintings;
import players.Player;

public class OpenAuctionPainting extends Painting{

    public OpenAuctionPainting(int artist_id) {
        super(artist_id);
    }

    @Override
    public String getType() {
        return "Open Auction";
    }

    @Override
    public void auction(Player[] players) {
        do {
            for (Player p : players) {
                if (p == currentBidder) {
                    //he won the auction;
                    sold();
                    return;
                }
                int offer = p.bid(currentBid, this);
                if (offer > currentBid) {
                    bid(offer, p);
                }
            }
        } while (currentBidder != null);
        sold();
    }
    private boolean bid(int value, Player p) {
        if (currentBidder == null || value > currentBid && p != currentBidder && p.getMoney() >= value) {
            this.currentBid = value;
            this.currentBidder = p;
            return true;
        }
        return false;
    }
}
