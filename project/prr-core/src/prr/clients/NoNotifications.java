package prr.clients;

import prr.exceptions.NotificationsAlreadyOffException;

import prr.notifications.Notifications;

import java.io.Serializable;

public class NoNotifications implements NotificationState, Serializable{

    @Override
    public String toString(){
        return "NO";
    }
    public YesNotifications goOn(){
        return new YesNotifications();
    }
    public NoNotifications goOff() throws NotificationsAlreadyOffException{
        throw new NotificationsAlreadyOffException();
    }
    public void receiveNotification(Notifications notification, Client client){
    }
}