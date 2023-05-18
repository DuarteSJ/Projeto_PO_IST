package prr.notifications;

import prr.clients.Client;
import java.io.Serializable;

abstract public class Notifications implements Serializable {
    private static final long serialVersionUID = 202208091753L;
    private String type;
    private String terminalKey;
    private Client notifiedClient;

    public Notifications(String terminalKey, Client notifiedClient) {
        this.terminalKey = terminalKey;
        this.notifiedClient = notifiedClient;
    }

    abstract public String getNotificationType();

    @Override
    public String toString() {
        return this.getNotificationType() + "|" + this.terminalKey;
    }

}
