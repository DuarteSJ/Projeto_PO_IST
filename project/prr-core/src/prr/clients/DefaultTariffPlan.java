package prr.clients;

import java.io.Serializable;

public class DefaultTariffPlan implements TariffPlan, Serializable {

    public int getNormalTextCost(int len) {
        if (len < 50) {
            return 10;
        } else if (len < 100) {
            return 16;
        }
        return 2 * len;
    }

    public int getGoldTextCost(int len) {
        if (len < 100) {
            return 10;
        }
        return 2 * len;
    }

    public int getPlatTextCost(int len) {
        if (len < 50) {
            return 0;
        }
        return 4;
    }

    public float getNormalVoiceCost(int duration, float discount) {
        float normalCost = 20 * duration;
        return normalCost - normalCost * discount;
    }

    public float getGoldVoiceCost(int duration, float discount) {
        float normalCost = 10 * duration;
        return normalCost - normalCost * discount;
    }

    public float getPlatVoiceCost(int duration, float discount) {
        float normalCost = 10 * duration;
        return normalCost - normalCost * discount;
    }

    public float getNormalVideoCost(int duration, float discount) {
        float normalCost = 30 * duration;
        return normalCost - normalCost * discount;
    }

    public float getGoldVideoCost(int duration, float discount) {
        float normalCost = 20 * duration;
        return normalCost - normalCost * discount;
    }

    public float getPlatVideoCost(int duration, float discount) {
        float normalCost = 10 * duration;
        return normalCost - normalCost * discount;
    }
}
