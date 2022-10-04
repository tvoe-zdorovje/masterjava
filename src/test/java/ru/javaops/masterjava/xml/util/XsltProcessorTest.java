package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;

public class XsltProcessorTest {
    @Test
    public void transform() throws Exception {
        try (InputStream xslInputStream = Resources.getResource("cities.xsl").openStream();
             InputStream xmlInputStream = Resources.getResource("payload.xml").openStream()) {

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            System.out.println(processor.transform(xmlInputStream));
        }
    }

    @Test
    public void groupsByProjectName() throws Exception {
        final String projectName = "topjava";
        try (InputStream xslInputStream = Resources.getResource("projectGroups.xsl").openStream();
            InputStream xmlInputStream = Resources.getResource("payload.xml").openStream();
            FileWriter writer = new FileWriter("./target/output/" + projectName + ".html")
        ) {

            XsltProcessor processor = new XsltProcessor(xslInputStream);
            processor.setParam("projectName", projectName);
            processor.transform(xmlInputStream, writer);
        }
    }
}
