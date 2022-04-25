/**
 * Class models the medium difficulty of the game
 * @author Luca Khatri
 * @version 1.0.0
 */
public class MediumDifficulty extends DifficultyClass {

    /**
     * constructor where all variables are defined
     */
    MediumDifficulty() {
        super();
        this.setRatSpeed(8);
        this.setPopulationCap(25);
        this.setBombCap(3);
        this.setGasCap(1);
        this.setPoisonCap(3);
        this.setMaleSwapperCap(3);
        this.setFemaleSwapperCap(3);
        this.setStopSignCap(3);
        this.setDeathRatCap(3);
        this.setLavaCap(1);
        this.setTime(90);
        this.setFrameTime(100);
        int[] dropRates = {5000,10000,10000,5000,5000,5000,5000,5000};
        this.setDropRate(dropRates);
        int[] counters = {this.getBombCap(),this.getGasCap(),this.getLavaCap(),
                this.getPoisonCap(),getMaleSwapperCap(),getFemaleSwapperCap(),getStopSignCap(),getDeathRatCap()};
        this.setCounters(counters);

    }


    /**
     * returns the rat speed
     * @return rat speed
     */
    @Override
    public int getRatSpeed() {
        return super.getRatSpeed();
    }

    /**
     * returns the rat population cap
     * @return rat population cap
     */
    @Override
    public int getPopulationCap() {
        return super.getPopulationCap();
    }

    /**
     * returns the bomb cap
     * @return bomb cap
     */
    @Override
    public int getBombCap() {
        return super.getBombCap();
    }

    /**
     * returns the gas cap
     * @return gas cap
     */
    @Override
    public int getGasCap() {
        return super.getGasCap();
    }

    /**
     * returns the poison cap
     * @return poison cap
     */
    @Override
    public int getPoisonCap() {
        return super.getPoisonCap();
    }

    /**
     * returns the male swapper cap
     * @return male swapper cap
     */
    @Override
    public int getMaleSwapperCap() {
        return super.getMaleSwapperCap();
    }

    /**
     * returns the female swapper cap
     * @return female swapper cap
     */
    @Override
    public int getFemaleSwapperCap() {
        return super.getFemaleSwapperCap();
    }

    /**
     * sets the rat speed
     * @param ratSpeed speed of the rat
     */
    @Override
    public void setRatSpeed(int ratSpeed) {
        super.setRatSpeed(ratSpeed);
    }

    /**
     * sets the rat population cap
     * @param populationCap max rat population
     */
    @Override
    public void setPopulationCap(int populationCap) {
        super.setPopulationCap(populationCap);
    }

    /**
     * sets the bomb cap
     * @param bombCap max amount of bombs
     */
    @Override
    public void setBombCap(int bombCap) {
        super.setBombCap(bombCap);
    }

    /**
     * sets the gas cap
     * @param gasCap max amount of gas
     */
    @Override
    public void setGasCap(int gasCap) {
        super.setGasCap(gasCap);
    }

    /**
     * sets the poison cap
     * @param poisonCap max amount of poison
     */
    @Override
    public void setPoisonCap(int poisonCap) {
        super.setPoisonCap(poisonCap);
    }

    /**
     * sets the male swapper cap
     * @param maleSwapperCap max amount of male swapper
     */
    @Override
    public void setMaleSwapperCap(int maleSwapperCap) {
        super.setMaleSwapperCap(maleSwapperCap);
    }

    /**
     * sets the female swapper cap
     * @param femaleSwapperCap max amount of female swapper
     */
    @Override
    public void setFemaleSwapperCap(int femaleSwapperCap) {
        super.setFemaleSwapperCap(femaleSwapperCap);
    }


}


