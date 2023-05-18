package prr.notifications;

import prr.clients.Client;

import java.io.Serializable;

public class S2INotifications extends Notifications implements Serializable {
    private static final long serialVersionUID = 202208091753L;

    public S2INotifications(String terminalkey, Client client) {
        super(terminalkey, client);
    }

    public String getNotificationType() {
        return "S2I";
    }
}
