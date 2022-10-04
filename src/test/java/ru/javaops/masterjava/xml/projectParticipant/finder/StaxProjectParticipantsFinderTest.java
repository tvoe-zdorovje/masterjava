package ru.javaops.masterjava.xml.projectParticipant.finder;

import com.google.common.io.Resources;
import org.hamcrest.core.IsEqual;
import org.junit.Test;
import ru.javaops.masterjava.xml.schema.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.junit.Assert.assertThat;

public class StaxProjectParticipantsFinderTest {
    private static final String OUTPUT_FOLDER_PATH = "./target/output/";

    private static final ProjectParticipantsFinder STAX_PROJECT_PARTICIPANTS_FINDER =
        new StaxProjectParticipantsFinder("payload.xml");

    @Test
    public void findUsersByProjectTest() {
        final List<User> expected = Util.TOPJAVA_PARTICIPANTS;

        final List<User> actual = STAX_PROJECT_PARTICIPANTS_FINDER.find("topjava");

        assertThat(actual.size(), new IsEqual<>(expected.size()));

        for (int i = 0; i < actual.size(); i++) {
            final User actualUser = actual.get(i);
            final User expectedUser = expected.get(i);

            assertThat(actualUser.getFullName(), new IsEqual<>(expectedUser.getFullName()));
            assertThat(actualUser.getEmail(), new IsEqual<>(expectedUser.getEmail()));
        }
    }

    @Test
    public void findUsersByProjectHTMLTest() throws IOException, URISyntaxException {
        final String project = "topjava";
        final String fileName = project + ".html";
        final File actual = STAX_PROJECT_PARTICIPANTS_FINDER.findAndWriteHTML(project, OUTPUT_FOLDER_PATH);
        final File expected = new File(Resources.getResource(fileName).toURI());
        try (
            BufferedReader actualReader = new BufferedReader(new FileReader(actual));
            BufferedReader expectedReader = new BufferedReader(new FileReader(expected));
        ) {
            assertThat(actual.getName(), new IsEqual<>(fileName));

            while (actualReader.ready()) {
                final String actualStr = actualReader.readLine();
                final String expectedStr = expectedReader.readLine();

                assertThat(actualStr, new IsEqual<>(expectedStr));
            }
        }

    }
}
