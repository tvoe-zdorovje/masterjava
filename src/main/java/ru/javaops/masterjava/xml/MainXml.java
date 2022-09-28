package ru.javaops.masterjava.xml;

public class MainXml {
    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("args must contain project name and path to .xml file");
        }

    }
}
