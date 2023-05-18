package prr.clients;

public class GoldClient extends ClientState {

    public GoldClient(Client client) {
        super(client);
    }

    public void update() {
        if (this.client.getPayments() - this.client.getDebts() < 0) {
            this.client.setState(new NormalClient(this.client));
        } else if (this.client.getPayments() - this.client.getDebts() > 0
                && this.client.getConsecutiveVideoCommunications() > 4) {
            this.client.setState(new PlatClient(this.client));
        }
    }

    public String getName() {
        return "GOLD";
    }

    public float textCommCost(int len) {
        return this.client.getTariffPlan().getGoldTextCost(len);
    }

    public float voiceCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getGoldVoiceCost(duration, discount);
    }

    public float videoCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getGoldVideoCost(duration, discount);
    }
}