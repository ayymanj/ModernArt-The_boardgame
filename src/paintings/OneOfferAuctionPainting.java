package paintings;

import players.Player;

public class OneOfferAuctionPainting extends Painting{

    public OneOfferAuctionPainting(int artist_id) {
        super(artist_id);
    }

    @Override
    public String getType() {
        return "One Offer Auction";
    }

    @Override
    public void auction(Player[] players) {
        Player highestBidder = null;
        int topBid = 0;
        Player auctioneer = this.getOwner();

        int auctioneerInd = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == auctioneer) {
                auctioneerInd = i;
                break;
            }
        }
        int startInd = auctioneerInd + 1;
        int count = 0;

        for (int i = 0; i < players.length; i++) {
            if (startInd >= players.length) {
                startInd = 0;
            }
            Player bidder = players[startInd];
            int offer = bidder.bid(topBid, this);
            count += 1;

            if (offer > topBid) {
                topBid = offer;
                highestBidder = bidder;
            }
            startInd++;
            if (startInd == auctioneerInd + 1) {
                break;
            }
            if (count == players.length - 1 &&  topBid == 0){
                break;
            }
        }

        if(topBid > 0 && highestBidder != null){
            this.currentBid = topBid;
            this.currentBidder = highestBidder;
            this.sold();
        }
        else if (topBid == 0) {
            this.currentBid = 0;
            this.currentBidder = this.getOwner();
            this.sold();
        }

//        for(Player bidder : players){
//            int offer = bidder.bid(0,this);
//
//            if(offer > topBid){
//                topBid = offer;
//                highestBidder = bidder;
//            }
//        }

    }
}
