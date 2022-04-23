import java.util.ArrayList;

public class Rock extends Power {

    /**
     * Power constructor
     *
     * @param xPos       x coordinate
     * @param yPos       y coordinate
     */
    public Rock(int xPos, int yPos) {
        super(false, xPos, yPos);

    }

    @Override
    void activate(ArrayList<Rat> rats, Tile currentTile) {

    }

    @Override
    void onTick(ArrayList<Rat> rats, Tile currentTile) {

    }
}
