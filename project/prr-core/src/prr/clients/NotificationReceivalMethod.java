package prr.clients;

import prr.notifications.Notifications;

public interface NotificationReceivalMethod {

    public void receiveNotification(Notifications notification, Client client);
}