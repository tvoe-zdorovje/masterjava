package ru.javaops.masterjava.xml.projectParticipant.finder;

public abstract class AbstractProjectParticipantsFinder implements ProjectParticipantsFinder {
    protected final String fileName;

    public AbstractProjectParticipantsFinder(String fileName) {
        this.fileName = fileName;
    }
}
