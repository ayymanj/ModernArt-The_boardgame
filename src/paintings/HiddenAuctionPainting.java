package paintings;

import players.Player;

public class HiddenAuctionPainting extends Painting {

    public HiddenAuctionPainting(int artist_id) {
        super(artist_id);
    }

    @Override
    public String getType() {
        return "Hidden Auction";
    }

    @Override
    public void auction(Player[] players) {
        Player MrAuctioneer = this.getOwner(); //Let's set the one who auctions as the painting owner
        int[] offers = new int[players.length]; //array to store all the bids being made and later compare
        int indexOfAuctioneer = -1;

        for(int i = 0; i < players.length; i++){
            Player player = players[i];
            int bid = player.bid(0,this);
            offers[i] = bid;

            if(players[i] == MrAuctioneer)
                indexOfAuctioneer = i;
        }

        int topBid = 0;
        Player mostBidder = null;
        int mostBidderIndex = -1;

        for(int i = 0; i < offers.length; i++){
            if(offers[i] > topBid){
                topBid = offers[i];
                mostBidder = players[i];
                mostBidderIndex = i;
            }
        }

        for(int i = 0; i < offers.length; i++){
            if(offers[i] == topBid && i != mostBidderIndex){
                if(players[i] == MrAuctioneer) {
                    mostBidder = players[i];
                    mostBidderIndex = i;
                }
                else{
                    int currentDistance = (i - indexOfAuctioneer + offers.length) % offers.length;
                    int mostBidderDistance = (mostBidderIndex - indexOfAuctioneer + offers.length) % offers.length;
                    if (currentDistance < mostBidderDistance) {
                        mostBidder = players[i];
                        mostBidderIndex = i;
                    }
                }
            }
        }
        if(topBid > 0){
            this.currentBid = topBid;
            this.currentBidder = mostBidder;
            sold();
        }
        else{
            this.currentBid = 0;
            this.currentBidder = MrAuctioneer;
            sold();
        }
    }
}
