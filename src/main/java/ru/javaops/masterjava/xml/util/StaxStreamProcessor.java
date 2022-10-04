package ru.javaops.masterjava.xml.util;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.io.OutputStream;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory INPUT_FACTORY = XMLInputFactory.newInstance();

    private static final XMLOutputFactory OUTPUT_FACTORY = XMLOutputFactory.newInstance();

    private final XMLStreamReader reader;

    private final XMLStreamWriter writer;

    public StaxStreamProcessor(InputStream is) throws XMLStreamException {
        reader = INPUT_FACTORY.createXMLStreamReader(is);
        writer = null;
    }

    public StaxStreamProcessor(OutputStream os) throws XMLStreamException {
        this.reader = null;
        this.writer = OUTPUT_FACTORY.createXMLStreamWriter(os);
    }

    public XMLStreamReader getReader() {
        return reader;
    }

    public XMLStreamWriter getWriter() {
        return writer;
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    public String getText() throws XMLStreamException {
        return reader.getElementText();
    }

    public String getAttributeValue(String attrName) {
        return reader.getAttributeValue(null, attrName);
    }

    public XMLStreamWriter startBody(String title) throws XMLStreamException {
        writer.writeDTD("<!DOCTYPE html>");

        writer.writeStartElement("html");
        writer.writeAttribute("lang", "en");

        writer.writeStartElement("head");
        writer.writeDTD("<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
        writer.writeStartElement("title");
        writer.writeCharacters(title);
        writer.writeEndElement(); // title
        writer.writeEndElement(); // head

        writer.writeStartElement("body");

        return getWriter();
    }

    public void endBody() throws XMLStreamException {
        writer.writeEndElement(); // body
        writer.writeEndDocument(); // html
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }
    }
}
