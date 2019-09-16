package com.ritesh.innerhourtodo.fire_database;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class ReminderItems_Model {
    public String reminder_Title;
    public String reminder_Description;
    public String reminder_Date;
    public String reminder_Coments;
    public String reminder_ID;
    public int intColor;


    public ReminderItems_Model() {
    }

    public String getReminder_Title() {
        return reminder_Title;
    }

    public void setReminder_Title(String reminder_Title) {
        this.reminder_Title = reminder_Title;
    }

    public String getReminder_Description() {
        return reminder_Description;
    }

    public void setReminder_Description(String reminder_Description) {
        this.reminder_Description = reminder_Description;
    }

    public String getReminder_Date() {
        return reminder_Date;
    }

    public void setReminder_Date(String reminder_Date) {
        this.reminder_Date = reminder_Date;
    }

    public String getReminder_Coments() {
        return reminder_Coments;
    }

    public void setReminder_Coments(String reminder_Coments) {
        this.reminder_Coments = reminder_Coments;
    }

    public String getReminder_ID() {
        return reminder_ID;
    }

    public void setReminder_ID(String reminder_ID) {
        this.reminder_ID = reminder_ID;
    }

    public int getIntColor() {
        return intColor;
    }

    public void setIntColor(int intColor) {
        this.intColor = intColor;
    }

    @Override
    public String toString() {
        return this.reminder_Title + "\n" + this.reminder_Description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("reminder_Title", reminder_Title);
        result.put("reminder_Description", reminder_Description);
        result.put("reminder_Date", reminder_Date);
        result.put("reminder_Coments", reminder_Coments);
        result.put("reminder_ID", reminder_ID);
        result.put("intColor", intColor);
        return result;
    }
}
