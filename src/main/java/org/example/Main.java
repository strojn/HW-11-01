package org.example;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class Main {
    public static void main(String[] args) 
    {
        //ім'я ф-лу
        final String fileName = "address.xml";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

        //визначаємо анонімний клас, який розширює DefaultHandler
        DefaultHandler handler = new DefaultHandler()
        {
            //поля, щоб сигналізувати про початок вказаних тегів
            boolean bCity;
            boolean bStreet;
            boolean bBuilding;
            //атрибут size тегу city
            String citySize;
            //метод викликається, коли SAXParser "натикається" на початок тегу
            public void startElement(String uri, String localName, String qName, Attributes attributes) {
                if (qName.equals("city")) {
                    citySize = attributes.getValue("size");
                    bCity = true;
                } else if (qName.equalsIgnoreCase("street")) {
                    bStreet = true;
                } else if (qName.equalsIgnoreCase("building")) {
                    bBuilding = true;
                }
            }
            //метод викликається, коли SAXParser зчитує текст між тегами
            public void characters(char[] ch, int start, int length) {
                if (bCity) {
                    System.out.println("\nCity: " + new String(ch, start, length));
                    System.out.println("Size: " + citySize);
                    bCity = false;
                } else if (bStreet) {
                    System.out.println("Street: " + new String(ch, start, length));
                    bStreet = false;
                } else if (bBuilding) {
                    System.out.println("Building: " + new String(ch, start, length));
                    bBuilding = false;
                }
            }
        };
        //починаємо аналіз XML ф-лу методом parse()
        try {
            parser.parse(fileName, handler);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}