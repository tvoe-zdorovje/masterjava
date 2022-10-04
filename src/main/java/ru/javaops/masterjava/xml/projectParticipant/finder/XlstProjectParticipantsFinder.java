package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

/**
 * Does not have to implement
 * */
public class XlstProjectParticipantsFinder extends AbstractProjectParticipantsFinder {
    public XlstProjectParticipantsFinder(String fileName) {
        super(fileName);
    }

    @Override
    public List<User> find(String projectName) {
        return null;
    }
}
