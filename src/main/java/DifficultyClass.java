/**
 * Abstract class to model all difficulties
 * @author Luca Khatri
 * @version 1.0
 *
 */

public abstract class DifficultyClass {

    private int ratSpeed;
    private int populationCap;
    private int bombCap;
    private int gasCap;
    private int poisonCap;
    private int maleSwapperCap;
    private int femaleSwapperCap;
    private int stopSignCap;
    private int deathRatCap;
    private int lavaCap;
    private int[] dropRate;
    private int[] counters;
    private int time;
    private int frameTime;


    /**
     * difficulty constructor
     * @param ratSpeed speed of the rat
     * @param populationCap max population
     * @param bombCap max amount of bombs
     * @param gasCap max amount of gas
     * @param poisonCap max amount of poison
     * @param maleSwapperCap max amount of male swapper
     * @param femaleSwapperCap max amount of female swapper
     */
    DifficultyClass(int ratSpeed , int populationCap, int bombCap, int gasCap, int poisonCap, int maleSwapperCap, int femaleSwapperCap){
        this.ratSpeed = ratSpeed;
        this.populationCap = populationCap;
        this.bombCap = bombCap;
        this.gasCap = gasCap;
        this.poisonCap = poisonCap;
        this.maleSwapperCap = maleSwapperCap;
        this.femaleSwapperCap = femaleSwapperCap;
    }

    /**
     * difficulty constructor
     */
    public DifficultyClass() {

    }

    public int getRatSpeed() {
        return ratSpeed;
    }

    public int getPopulationCap() {
        return populationCap;
    }

    public int getBombCap() {
        return bombCap;
    }

    public int getGasCap() {
        return gasCap;
    }

    public int getPoisonCap() {
        return poisonCap;
    }

    public int getMaleSwapperCap() {
        return maleSwapperCap;
    }

    public int getFemaleSwapperCap() {
        return femaleSwapperCap;
    }

    public void setRatSpeed(int ratSpeed) {
        this.ratSpeed = ratSpeed;
    }

    public void setPopulationCap(int populationCap) {
        this.populationCap = populationCap;
    }

    public void setBombCap(int bombCap) {
        this.bombCap = bombCap;
    }

    public void setGasCap(int gasCap) {
        this.gasCap = gasCap;
    }

    public void setPoisonCap(int poisonCap) {
        this.poisonCap = poisonCap;
    }

    public void setMaleSwapperCap(int maleSwapperCap) {
        this.maleSwapperCap = maleSwapperCap;
    }

    public void setFemaleSwapperCap(int femaleSwapperCap) {
        this.femaleSwapperCap = femaleSwapperCap;
    }

    public int[] getDropRate() {
        return dropRate;
    }

    public void setDropRate(int[] dropRate) {
        this.dropRate = dropRate;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getFrameTime() {
        return frameTime;
    }

    public void setFrameTime(int frameTime) {
        this.frameTime = frameTime;
    }

    public int[] getCounters() {
        return counters;
    }

    public void setCounters(int[] counters) {
        this.counters = counters;
    }

    public int getStopSignCap() {
        return stopSignCap;
    }

    public void setStopSignCap(int stopSignCap) {
        this.stopSignCap = stopSignCap;
    }

    public int getDeathRatCap() {
        return deathRatCap;
    }

    public void setDeathRatCap(int deathRatCap) {
        this.deathRatCap = deathRatCap;
    }

    public int getLavaCap() {
        return lavaCap;
    }

    public void setLavaCap(int lavaCap) {
        this.lavaCap = lavaCap;
    }
}

