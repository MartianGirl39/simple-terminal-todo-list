package com.techelevator.lists;

import java.util.ArrayList;
import java.util.List;

public class ToDo extends ListItem {

    private List<String> participants;

    public ToDo(String description, String[] people) {
        super(description, false);

        this.participants = new ArrayList<>();
        for(String participant : people) {
            this.participants.add(participant);
        }
    }

    public List<String> getParticipants() {
        return this.participants;
    }

    public void addParticipant(String person) {
        this.participants.add(person);
    }

    public String toString() {
        return super.getDescription() + "\n" + this.participants + "\n" + "Completed? " + super.isCompleted() + "\n";
    }

}
