package ru.javaops.masterjava.xml.projectParticipant.finder;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.schema.Payload;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class JaxbProjectParticipantsFinder extends AbstractProjectParticipantsFinder {
    private final Payload payload;

    public JaxbProjectParticipantsFinder(String filePath) {
        super(filePath);
        try {
            JaxbParser parser = new JaxbParser(ObjectFactory.class);
            parser.setSchema(PAYLOAD_SCHEMA);
            this.payload = parser.unmarshal(Resources.getResource(filePath).openStream());
        } catch (JAXBException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> find(String projectName) {
        final List<String> groups = payload.getProjects().getProject().stream()
            .filter(it -> it.getName().equalsIgnoreCase(projectName))
            .flatMap(it -> it.getGroups().getGroup().stream())
            .map(it -> it.getName())
            .collect(Collectors.toList());
        final List<User> users = payload.getUsers().getUser().stream()
            .filter(it ->
                it.getGroups().getGroup().stream()
                    .map(g -> ((Group) g.getValue()).getName())
                    .anyMatch(groups::contains)
            )
            .sorted(Comparator.comparing(User::getFullName))
            .collect(Collectors.toList());
        return users;
    }
}
