package players;
import paintings.Painting;
import java.util.concurrent.ThreadLocalRandom;

public class ComputerPlayer extends Player{
    private final int[][] scoreboard;

    public ComputerPlayer(int money, int[][] scoreboard) {
        super(money);
        this.scoreboard = scoreboard;
    }

    @Override
    public Painting playPainting() {
        if (handPaintings.size() == 0) {
            return null;
        }
        int index = ThreadLocalRandom.current().nextInt(0, handPaintings.size());
        Painting InAnyOrder = handPaintings.get(index);
        handPaintings.remove(index);
        return InAnyOrder;
    }

    @Override
    public int bid(int currentBid, Painting p) {
        int potentialBid = potentialValue(p);
        int maxBid;

        if(p.getOwner() == this)
            maxBid = potentialBid / 2;
        else
            maxBid = potentialBid;

        if(maxBid > getMoney())
            maxBid = getMoney();

        if (maxBid < currentBid && p.getType() == "Fixed Price Auction") {
            System.out.println(getName() + " pass.");
            return 0;
        }
        else if(p.getType() == "Open Auction" ){
            int bid = ThreadLocalRandom.current().nextInt(0, maxBid + 1);
            if(bid < currentBid){
                return bid;
            }
            else{
                System.out.println(getName() + " bids " + bid);
                return bid;
            }
        }
        else if( p.getType() == "One Offer Auction"){
            int bid = ThreadLocalRandom.current().nextInt(0, maxBid + 1);
            System.out.println(getName() + " bids " + bid);
            return bid;
        }
        else if(p.getType() == "Hidden Auction"){
            int bid = ThreadLocalRandom.current().nextInt(0, maxBid + 1);
            System.out.println(getName() + " bids " + bid);
            return bid;
        }
        return ThreadLocalRandom.current().nextInt(0, maxBid + 1);

    }

    private int potentialValue(Painting p){
        int artistIndex = p.getArtistId();
        int artistScore;
        int maxValue = 30;

        for(int i = 0; i < 4; i++){
            artistScore = scoreboard[i][artistIndex];
            if(artistScore > 0)
                maxValue += artistScore;
            else{
                maxValue = 30;
            }
        }
        return maxValue;
    }
}
