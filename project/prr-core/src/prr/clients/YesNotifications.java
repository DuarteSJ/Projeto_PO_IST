package prr.clients;

import prr.exceptions.NotificationsAlreadyOnException;

import prr.notifications.Notifications;

import java.io.Serializable;

public class YesNotifications implements NotificationState, Serializable {

    @Override
    public String toString() {
        return "YES";
    }

    public YesNotifications goOn() throws NotificationsAlreadyOnException {
        throw new NotificationsAlreadyOnException();
    }

    public NoNotifications goOff() {
        return new NoNotifications();
    }

    public void receiveNotification(Notifications notification, Client client) {
        client.getNotificationReceivalMethod().receiveNotification(notification, client);
    }
}