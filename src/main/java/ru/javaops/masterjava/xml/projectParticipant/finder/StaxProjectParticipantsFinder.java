package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

public class StaxProjectParticipantsFinder extends AbstractProjectParticipantsFinder {
    public StaxProjectParticipantsFinder(String filePath) {
        super(filePath);
    }

    @Override
    public List<User> find(String projectName) {
        return null;
    }
}
