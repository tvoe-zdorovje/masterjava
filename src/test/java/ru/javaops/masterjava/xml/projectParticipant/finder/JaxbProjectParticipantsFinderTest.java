package ru.javaops.masterjava.xml.projectParticipant.finder;

import org.hamcrest.core.IsEqual;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import java.util.List;

import static org.junit.Assert.assertThat;

public class JaxbProjectParticipantsFinderTest {
    private static final ProjectParticipantsFinder projectParticipantsFinder =
        new JaxbProjectParticipantsFinder("payload.xml");

    @Test
    public void findUsersByProjectTest() {
        final List<User> expected = Util.TOPJAVA_PARTICIPANTS;

        final List<User> actual = projectParticipantsFinder.find("topjava");

        assertThat(actual.size(), new IsEqual<>(expected.size()));

        for (int i = 0; i < actual.size(); i++) {
            final User actualUser = actual.get(i);
            final User expectedUser = expected.get(i);

            assertThat(actualUser.getFullName(), new IsEqual<>(expectedUser.getFullName()));
            assertThat(actualUser.getEmail(), new IsEqual<>(expectedUser.getEmail()));
        }
    }
}
