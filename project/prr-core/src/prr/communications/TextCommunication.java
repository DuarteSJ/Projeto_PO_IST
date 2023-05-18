package prr.communications;

import prr.terminals.Terminal;

public class TextCommunication extends Communication {

    protected String text;

    public TextCommunication(int key, Terminal receiver, Terminal sender, String text) {
        super(key, receiver, sender);
        this.text = text;
        this.state = new OverState();
        this.cost = calculateCost();

    }

    public String getTypeName() {
        return "TEXT";
    }

    public int getAccountingUnits() {
        return this.text.length();
    }

    public boolean isVideo() {
        return false;
    };

    @Override
    public float calculateCost() {
        return this.sender.getOwner().textCommCost(this.text.length());
    }
}
