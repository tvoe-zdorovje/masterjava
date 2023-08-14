package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class CityProcessor {
    private static final int CHUNK_SIZE = 10;

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static final CityDao cityDao = DBIProvider.getDao(CityDao.class);

    public void process(final StaxStreamProcessor processor) throws XMLStreamException, JAXBException {
        log.debug("Process cities...");

        val unmarshaller = jaxbParser.createUnmarshaller();

        final List<City> cities = new ArrayList<>();

        while (processor.startElement("City", "Cities")) {
            ru.javaops.masterjava.xml.schema.CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.CityType.class);
            final City user = new City(xmlCity.getId(), xmlCity.getValue());
            cities.add(user);
        }

        final int[] result = cityDao.insertBatch(cities, CHUNK_SIZE);
        logFailed(result, cities);
    }

    private List<City> calculateFailed(int[] insertionResult, List<City> cities) {
        return IntStream.range(0, insertionResult.length)
            .boxed()
            .map(it -> insertionResult[it] == 0 ? cities.get(it) : null)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    private void logFailed(int[] insertionResult, List<City> cities) {
        final List<City> failedCities = calculateFailed(insertionResult, cities);
        if (failedCities.isEmpty()) return;
        log.warn("Procession failed for cities: {}", failedCities);
    }
}
