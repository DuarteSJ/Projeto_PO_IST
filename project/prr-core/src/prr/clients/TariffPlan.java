package prr.clients;


public interface TariffPlan {

    public int getNormalTextCost(int len);

    public int getGoldTextCost(int len);

    public int getPlatTextCost(int len);

    public float getNormalVoiceCost(int duration, float discount);

    public float getGoldVoiceCost(int duration, float discount);

    public float getPlatVoiceCost(int duration, float discount);

    public float getNormalVideoCost(int duration, float discount);

    public float getGoldVideoCost(int duration, float discount);

    public float getPlatVideoCost(int duration, float discount);
}
