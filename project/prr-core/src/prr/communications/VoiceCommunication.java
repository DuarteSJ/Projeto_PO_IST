package prr.communications;

import prr.terminals.Terminal;

public class VoiceCommunication extends InteractiveCommunication {

    private float discount;

    public VoiceCommunication(int key, Terminal receiver, Terminal sender, CommState state) {
        super(key, receiver, sender, state);
        this.discount = 0f;
    }

    public String getTypeName() {
        return "VOICE";
    }

    @Override
    public boolean isVideo() {
        return false;
    }

    @Override
    public float calculateCost() {
        if (sender.isFriend(receiver)) {
            this.discount = 0.5f;
        }
        return this.sender.getOwner().voiceCommCost(this.duration, this.discount);
    }
}
