package ru.javaops.masterjava.xml.projectParticipant.finder;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema.Group;
import ru.javaops.masterjava.xml.schema.Project;
import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

public class StaxProjectParticipantsFinder extends AbstractProjectParticipantsFinder {
    public StaxProjectParticipantsFinder(String filePath) {
        super(filePath);
    }

    @Override
    public List<User> find(String projectName) {
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Resources.getResource(fileName).openStream())) {

            final List<String> projectGroups = getProjectGroups(projectName);

            final List<User> users = new ArrayList<>();

            while (doUntil(processor, User.class.getSimpleName())) {
                final String email = processor.getAttributeValue("email");
                final String name = processor.getElementValue("fullName");

                final User user = new User();
                user.setFullName(name);
                user.setEmail(email);

                final String groupsName = User.Groups.class.getSimpleName();
                final String groupName = Group.class.getSimpleName().toLowerCase();
                if (!doUntil(processor, groupsName)) {
                    continue;
                }
                while (
                    !(
                        processor.getReader().getEventType() == XMLEvent.END_ELEMENT &&
                        processor.getValue(XMLEvent.END_ELEMENT).equals(groupsName)
                    )
                ) {
                    final int event = processor.getReader().next();
                    if (event == XMLEvent.START_ELEMENT && processor.getValue(event).equals(groupName)) {
                        final String group = processor.getReader().getElementText();
                        if (projectGroups.contains(group)) {
                            users.add(user);
                            break;
                        }
                    }
                }
            }

            users.sort(Comparator.comparing(User::getFullName));
            return users;
        } catch (XMLStreamException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getProjectGroups(String project) {
        final List<String> groups = new ArrayList<>();
        try (StaxStreamProcessor processor = new StaxStreamProcessor(Resources.getResource(fileName).openStream())) {

            while (processor.doUntil(XMLEvent.START_ELEMENT, Project.class.getSimpleName())) {
                final String projectName = processor.getAttributeValue("name");
                if (projectName == null) {
                    throw new NoSuchElementException(String.format("Project %s not found", project));
                }
                if (!projectName.equals(project)) {
                    continue;
                }

                final String groupsName = User.Groups.class.getSimpleName();
                final String groupName = Group.class.getSimpleName();
                if (!doUntil(processor, groupsName)) {
                    return groups;
                }
                while (
                    !(
                        processor.getReader().getEventType() == XMLEvent.END_ELEMENT &&
                        processor.getValue(XMLEvent.END_ELEMENT).equals(groupsName)
                    )
                ) {
                    final int event = processor.getReader().next();
                    if (event == XMLEvent.START_ELEMENT && processor.getValue(event).equals(groupName)) {
                        final String group = processor.getAttributeValue("name");
                        groups.add(group);
                    }
                }
                return groups;
            }
        } catch (IOException | XMLStreamException e) {
            throw new RuntimeException(e);
        }
        return groups;
    }


    private static boolean doUntil(StaxStreamProcessor processor, String element) throws XMLStreamException {
        return processor.doUntil(XMLEvent.START_ELEMENT, element);
    }
}
