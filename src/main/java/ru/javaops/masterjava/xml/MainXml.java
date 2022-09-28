package ru.javaops.masterjava.xml;

import ru.javaops.masterjava.xml.projectParticipant.finder.JaxbProjectParticipantsFinder;
import ru.javaops.masterjava.xml.projectParticipant.finder.ProjectParticipantsFinder;
import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

public class MainXml {
    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("args must contain project name and path to .xml file");
        }

        ProjectParticipantsFinder projectParticipantsFinder = new JaxbProjectParticipantsFinder(args[1]);
        final List<User> topjava = projectParticipantsFinder.find(args[0]);
        System.out.println(topjava);
    }
}
