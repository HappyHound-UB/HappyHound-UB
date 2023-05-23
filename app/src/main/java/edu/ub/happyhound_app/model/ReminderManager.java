package edu.ub.happyhound_app.model;

import java.util.ArrayList;
import java.util.List;

public class ReminderManager {
    private static List<Reminder> reminderList = new ArrayList<>();

    public static List<Reminder> getReminderList() {
        return reminderList;
    }

    public static void setReminderList(List<Reminder> reminders) {
        reminderList = reminders;
    }
}
