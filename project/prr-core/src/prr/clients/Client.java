package prr.clients;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import java.io.Serializable;

import prr.terminals.Terminal;
import prr.notifications.Notifications;
import prr.exceptions.NotificationsAlreadyOnException;
import prr.exceptions.NotificationsAlreadyOffException;

public class Client implements Serializable {
    private String Key;
    private String name;
    private int taxID;
    private long balance;
    private long payed;
    private long debts;
    private NotificationState notificationState;
    private ArrayList<Notifications> receivedNotifications;
    private Map<String, Terminal> terminals;
    private ClientState state;
    private TariffPlan tarriffPlan;
    private int consecutiveTextCommunications = 0;
    private int consecutiveVideoCommunications = 0;
    private NotificationReceivalMethod notificationReceivalMethod = new DefaultReceivalMethod();

    public Client(String key, String name, int taxID) {
        this.Key = key;
        this.name = name;
        this.taxID = taxID;
        this.balance = 0;
        this.payed = 0;
        this.debts = 0;
        this.notificationState = new YesNotifications();
        this.terminals = new TreeMap<String, Terminal>();
        this.receivedNotifications = new ArrayList<Notifications>();
        this.state = new NormalClient(this);
        this.tarriffPlan = new DefaultTariffPlan();
    }

    public String getKey() {
        return this.Key;
    }

    public ClientState setState(ClientState state) {
        return this.state = state;
    }

    public void setTarifPlan(TariffPlan plan) {
        this.tarriffPlan = plan;
    }

    public ClientState getState() {
        return this.state;
    }

    public String getName() {
        return this.name;
    }

    public int getTaxId() {
        return this.taxID;
    }

    public long getPayments() {
        return this.payed;
    }

    public long getDebts() {
        return this.debts;
    }

    public void updateDept(float depts) {
        this.debts += depts;
    }

    public void updatePayments(float payed) {
        this.payed += payed;
    }

    public TariffPlan getTariffPlan() {
        return this.tarriffPlan;
    }

    public Map<String, Terminal> getTerminals() {
        return this.terminals;
    }

    public NotificationState getNotificationState() {
        return this.notificationState;
    }

    public ArrayList<Notifications> getReceivedNotifications() {
        return this.receivedNotifications;
    }

    public int getConsecutiveTextCommunications() {
        return this.consecutiveTextCommunications;
    }

    public int getConsecutiveVideoCommunications() {
        return this.consecutiveVideoCommunications;
    }

    public void resetConsecutiveTextCommunications() {
        this.consecutiveTextCommunications = 0;
    }

    public void resetConsecutiveVideoCommunications() {
        this.consecutiveVideoCommunications = 0;
    }

    public void addToConsecutiveTextCommunications() {
        this.consecutiveTextCommunications += 1;
    }

    public void addToConsecutiveVideoCommunications() {
        this.consecutiveVideoCommunications += 1;
    }

    public NotificationReceivalMethod getNotificationReceivalMethod() {
        return this.notificationReceivalMethod;
    }

    public void setNotificationReceivalMethod(NotificationReceivalMethod method) {
        this.notificationReceivalMethod = method;
    }

    public int averageTerminal(){
        int numTerms = 0;
        int totalPayed = 0;
        for (Terminal term : this.terminals.values()){
            numTerms += 1;
            totalPayed += term.getPayed();
        }
        if (numTerms == 0){return 0;}
        return totalPayed / numTerms;
    }

    @Override
    public String toString() {
        return "CLIENT|" + this.getKey()
                + "|" + this.getName() + "|" + this.getTaxId()
                + "|" + this.state.getName() + "|" + this.getNotificationState().toString()
                + "|" + this.terminals.size()
                + "|" + (int) this.getPayments() + "|" + (int) this.getDebts()
                + "|" + this.averageTerminal();
    }

    public void addTerminal(Terminal terminal) {
        this.terminals.put(terminal.getKey(), terminal);
    }

    public void turnOnNotis() throws NotificationsAlreadyOnException {
        this.notificationState = this.notificationState.goOn();
    }

    public void turnOffNotis() throws NotificationsAlreadyOffException {
        this.notificationState = this.notificationState.goOff();
    }

    public float textCommCost(int len) {
        return this.state.textCommCost(len);
    }

    public float voiceCommCost(int duration, float discount) {
        return this.state.voiceCommCost(duration, discount);
    }

    public float videoCommCost(int duration, float discount) {
        return this.state.videoCommCost(duration, discount);
    }

    public void receiveNotification(Notifications notification) {
        this.notificationState.receiveNotification(notification, this);
    }

}