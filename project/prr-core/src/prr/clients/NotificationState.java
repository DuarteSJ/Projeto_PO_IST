package prr.clients;

import prr.exceptions.NotificationsAlreadyOnException;
import prr.exceptions.NotificationsAlreadyOffException;
import prr.notifications.Notifications;


public interface NotificationState{
    @Override
    public String toString();
    public YesNotifications goOn() throws NotificationsAlreadyOnException;
    public NoNotifications goOff() throws NotificationsAlreadyOffException;
    public void receiveNotification(Notifications notification, Client client);
}