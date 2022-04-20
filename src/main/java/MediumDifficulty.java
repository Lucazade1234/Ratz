public class MediumDifficulty extends DifficultyClass {

    MediumDifficulty() {
        super();
        this.setRatSpeed(8);
        this.setPopulationCap(25);
        this.setBombCap(2);
        this.setGasCap(2);
        this.setPoisonCap(2);
        this.setMaleSwapperCap(2);
        this.setFemaleSwapperCap(2);
        this.setTime(90);
        this.setFrameTime(150);
        int[] dropRates = {0,10000,0,0,0,0,0,0};
        this.setDropRate(dropRates);
        int[] counters = {2,2,2,2,2,2,2,2};
        this.setCounters(counters);

    }


    @Override
    public int getRatSpeed() {
        return super.getRatSpeed();
    }

    @Override
    public int getPopulationCap() {
        return super.getPopulationCap();
    }

    @Override
    public int getBombCap() {
        return super.getBombCap();
    }

    @Override
    public int getGasCap() {
        return super.getGasCap();
    }

    @Override
    public int getPoisonCap() {
        return super.getPoisonCap();
    }

    @Override
    public int getMaleSwapperCap() {
        return super.getMaleSwapperCap();
    }

    @Override
    public int getFemaleSwapperCap() {
        return super.getFemaleSwapperCap();
    }

    @Override
    public void setRatSpeed(int ratSpeed) {
        super.setRatSpeed(ratSpeed);
    }

    @Override
    public void setPopulationCap(int populationCap) {
        super.setPopulationCap(populationCap);
    }

    @Override
    public void setBombCap(int bombCap) {
        super.setBombCap(bombCap);
    }

    @Override
    public void setGasCap(int gasCap) {
        super.setGasCap(gasCap);
    }

    @Override
    public void setPoisonCap(int poisonCap) {
        super.setPoisonCap(poisonCap);
    }

    @Override
    public void setMaleSwapperCap(int maleSwapperCap) {
        super.setMaleSwapperCap(maleSwapperCap);
    }

    @Override
    public void setFemaleSwapperCap(int femaleSwapperCap) {
        super.setFemaleSwapperCap(femaleSwapperCap);
    }


}


