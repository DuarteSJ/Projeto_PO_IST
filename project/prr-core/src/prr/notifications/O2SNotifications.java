package prr.notifications;

import prr.clients.Client;

import java.io.Serializable;

public class O2SNotifications extends Notifications implements Serializable {
    private static final long serialVersionUID = 202208091753L;

    public O2SNotifications(String terminalkey, Client client) {
        super(terminalkey, client);
    }

    public String getNotificationType() {
        return "O2S";
    }
}
