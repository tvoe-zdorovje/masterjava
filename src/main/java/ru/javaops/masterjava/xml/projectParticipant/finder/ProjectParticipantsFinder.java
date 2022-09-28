package ru.javaops.masterjava.xml.projectParticipant.finder;

import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

public interface ProjectParticipantsFinder {
    List<User> find(String projectName);
}
