package edu.ub.happyhound_app.model;

import java.util.ArrayList;
import java.util.List;

public class ReminderManager {
    private static List<Reminder> paseoReminderList = new ArrayList<>();
    private static List<Reminder> comidaReminderList = new ArrayList<>();
    private static List<Reminder> otherReminderList = new ArrayList<>();


    public static List<Reminder> getPaseoReminderList() {
        return paseoReminderList;
    }

    public static void setPaseoReminderList(List<Reminder> paseoReminderList) {
        ReminderManager.paseoReminderList = paseoReminderList;
    }

    public static List<Reminder> getComidaReminderList() {
        return comidaReminderList;
    }

    public static void setComidaReminderList(List<Reminder> comidaReminderList) {
        ReminderManager.comidaReminderList = comidaReminderList;
    }

    public static List<Reminder> getOtherReminderList() {
        return otherReminderList;
    }

    public static void setOtherReminderList(List<Reminder> reminders) {
        otherReminderList = reminders;
    }
}
