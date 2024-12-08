package paintings;

import players.AFKPlayer;
import players.ComputerPlayer;
import players.Player;
import java.util.concurrent.ThreadLocalRandom;

public class FixedPriceAuctionPainting extends Painting{

    public FixedPriceAuctionPainting(int artist_id) {
        super(artist_id);
    }

    @Override
    public String getType() {
        return "Fixed Price Auction";
    }

    @Override
    public void auction(Player[] players) {
        Player MrAuctioneer = this.getOwner();
        if (MrAuctioneer == null) {
            System.out.println("Error: The auctioneer is not set.");
            return; // Exit the auction method
        }

        int fixedPrice = 0;

        if(!(MrAuctioneer instanceof ComputerPlayer && MrAuctioneer instanceof AFKPlayer)){
            while(true) {
                System.out.println(MrAuctioneer.getName() + ", please fix a price for the auction: ");
                    fixedPrice = MrAuctioneer.bid(0, this);
                if (fixedPrice >= 0 && fixedPrice <= MrAuctioneer.getMoney()) {
                    break;
                }
            }
        }
        else {
            System.out.println(MrAuctioneer.getName() + ", please fix a price for the auction: ");
            if(MrAuctioneer instanceof ComputerPlayer){
                fixedPrice = ThreadLocalRandom.current().nextInt(0, MrAuctioneer.getMoney() + 1);
            }
        }
        System.out.println("The fixed price is " + fixedPrice);

        boolean checkIfSold = false;
        int auctioneerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == MrAuctioneer) {
                auctioneerIndex = i;
                break;
            }
        }
        int startIndex = auctioneerIndex + 1;
        boolean round = true;
        while(round){
            int count = 0;
            int value = 0;
            for (int i = 0; i < players.length - 1; i++) {

                if (startIndex >= players.length) {
                    startIndex = 0;
                }
                Player p = players[startIndex];
                int bid = p.bid(fixedPrice, this);
                value+=bid;
                count++;

                if (bid >= fixedPrice) {
                    currentBidder = p;
                    currentBid = fixedPrice;
                    sold();
                    checkIfSold = true;
                    round = false;
                    break;
                }
                if((count == players.length - 1)&& value == 0){
                    round = false;
                    break;
                }
                startIndex++;
            }
        }
        if (!checkIfSold) {
            currentBidder = MrAuctioneer;
            currentBid = fixedPrice;
            sold();
        }
//        for(Player p : players){
//            if (p == MrAuctioneer) {
//                continue;
//            }
//            int bid = p.bid(fixedPrice, this);
//
//            if (bid >= fixedPrice) {
//                currentBidder = p;
//                currentBid = fixedPrice;
//                sold();
//                checkIfSold = true;
//                break;
//            }
//        }

    }
}
