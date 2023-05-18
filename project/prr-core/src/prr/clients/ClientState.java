package prr.clients;

import java.io.Serializable;

public abstract class ClientState implements Serializable {
    public Client client;

    public ClientState(Client client) {
        this.client = client;
    }

    public abstract void update();

    public abstract String getName();

    public abstract float textCommCost(int len);

    public abstract float voiceCommCost(int duration, float discount);

    public abstract float videoCommCost(int duration, float discount);

}
