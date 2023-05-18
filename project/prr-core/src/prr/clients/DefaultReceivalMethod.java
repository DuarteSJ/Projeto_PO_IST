package prr.clients;

import prr.notifications.Notifications;

import java.io.Serializable;

public class DefaultReceivalMethod implements NotificationReceivalMethod, Serializable {
    public void receiveNotification(Notifications notification, Client client) {
        client.getReceivedNotifications().add(notification);
    }
}