package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;

import java.util.List;

public class JaxbProjectParticipantsFinder extends AbstractProjectParticipantsFinder {
    private final JaxbParser parser;

    public JaxbProjectParticipantsFinder(String filePath) {
        super(filePath);
        this.parser = new JaxbParser(ObjectFactory.class);
    }

    @Override
    public List<User> find(String projectName) {
        return null;
    }
}
