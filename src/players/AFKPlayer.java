package players;

import paintings.*;

public class AFKPlayer extends Player{

    public AFKPlayer(int money) {
        super(money);
    }

    @Override
    public int bid(int currentBid, Painting p) {
        if(p instanceof OneOfferAuctionPainting || p instanceof HiddenAuctionPainting){
            System.out.println(this.getName() + " bids 0");
        }
        else if(p instanceof FixedPriceAuctionPainting && (p.getOwner() != this)){
            System.out.println(this.getName() + " pass.");
        }
        return 0;
    }

    @Override
    public Painting playPainting() {
        if(handPaintings.isEmpty()){
            return null;
        }
        Painting firstInOrder = handPaintings.get(0);
        handPaintings.remove(0);
        return firstInOrder;
    }
}
