public class EasyDifficulty extends DifficultyClass {

    EasyDifficulty() {
        super();
        this.setRatSpeed(4);
        this.setPopulationCap(30);
        this.setBombCap(3);
        this.setGasCap(1);
        this.setPoisonCap(3);
        this.setMaleSwapperCap(3);
        this.setFemaleSwapperCap(3);
        this.setStopSignCap(3);
        this.setDeathRatCap(3);
        this.setLavaCap(1);
        this.setTime(120);
        this.setFrameTime(250);
        int[] dropRates = {5000,10000,10000,5000,5000,5000,5000,5000};
        this.setDropRate(dropRates);
        int[] counters = {this.getBombCap(),this.getGasCap(),this.getLavaCap(),
                this.getPoisonCap(),getMaleSwapperCap(),getFemaleSwapperCap(),getStopSignCap(),getDeathRatCap()};
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
