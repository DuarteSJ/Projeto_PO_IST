package prr.communications;

import prr.terminals.Terminal;

public class VideoCommunication extends InteractiveCommunication {

    private float discount;

    public VideoCommunication(int key, Terminal receiver, Terminal sender, CommState state) {
        super(key, receiver, sender, state);
        this.discount = 0f;
    }

    public String getTypeName() {
        return "VIDEO";
    }

    @Override
    public boolean isVideo() {
        return true;
    }

    @Override
    public float calculateCost() {
        if (sender.isFriend(receiver)) {
            this.discount = 0.5f;
        }
        return this.sender.getOwner().videoCommCost(this.duration, this.discount);
    }
}
