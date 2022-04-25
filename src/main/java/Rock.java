import java.util.ArrayList;

/**
 * Class models the behaviour of the rock weapon
 * @author Luca Khatri
 * @version 1.0
 */
public class Rock extends Power {

    /**
     * Power constructor
     *
     * @param xPos x coordinate
     * @param yPos y coordinate
     */
    public Rock(int xPos, int yPos) {
        super(false, xPos, yPos);

    }

    /**
     * abstract method that tells every power to do their thing
     * for rock - it does nothing
     * @param rats used to interact with all rats that stepped on the power.
     * @param currentTile used for removing power from its Tile.
     */
    @Override
    void activate(ArrayList<Rat> rats, Tile currentTile) {

    }

    /**
     * Abstract method for certain powers that need to activate after a
     * certain amount of time.
     *
     * for rock - it does nothing
     * @param rats used for updating the rat arraylist every game tick.
     * @param currentTile used for calling removeActivePower(this).
     */
    @Override
    void onTick(ArrayList<Rat> rats, Tile currentTile) {

    }
}
