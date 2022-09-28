package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.util.Schemas;

import javax.xml.validation.Schema;

public abstract class AbstractProjectParticipantsFinder implements ProjectParticipantsFinder {
    static final String PAYLOAD_SCHEMA_PATH = "payload.xsd";
    static final Schema PAYLOAD_SCHEMA = Schemas.ofClasspath(PAYLOAD_SCHEMA_PATH);

    protected final String fileName;

    public AbstractProjectParticipantsFinder(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be null or empty");
        }
        this.fileName = fileName;
    }
}
