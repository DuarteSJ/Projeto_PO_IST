package prr.clients;

public class NormalClient extends ClientState {

    public NormalClient(Client client) {
        super(client);
    }

    // Can only go to gold
    public void update() {
        if ((this.client.getPayments() - this.client.getDebts()) > 500) {
            this.client.setState(new GoldClient(this.client));
        }
    }

    public String getName() {
        return "NORMAL";
    }

    public float textCommCost(int len) {
        return this.client.getTariffPlan().getNormalTextCost(len);
    }

    public float voiceCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getNormalVoiceCost(duration, discount);
    }

    public float videoCommCost(int duration, float discount) {
        return this.client.getTariffPlan().getNormalVideoCost(duration, discount);
    }

}