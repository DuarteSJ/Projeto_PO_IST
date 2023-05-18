package prr.clients;

import java.io.Serializable;

public class PlatClient extends ClientState {

    public PlatClient(Client client) {
        super(client);
    }

    public void update() {
        if (this.client.getPayments() - this.client.getDebts() > 0
                && this.client.getConsecutiveTextCommunications() > 1) {
            this.client.setState(new GoldClient(this.client));
        } else if (this.client.getPayments() - this.client.getDebts() < 0) {
            this.client.setState(new NormalClient(this.client));
        }
    }

    public String getName() {
        return "PLATINUM";
    }

    public float textCommCost(int len) {
        return this.client.getTariffPlan().getPlatTextCost(len);
    }

    public float voiceCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getPlatVoiceCost(duration, discount);
    }

    public float videoCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getPlatVideoCost(duration, discount);
    }
}