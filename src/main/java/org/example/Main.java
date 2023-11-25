package org.example;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args)
    {
// 1. Використовуємо StAX для створення XML-ф-лу (наче може і парсити)
// 2. Використовуємо SAXParser для парсингу попередньо створеного XML-ф-лу (може ТІЛЬКИ парсити)

//StAX
        FileOutputStream fileOutputStream;
        try {
            fileOutputStream = new FileOutputStream("address_created.xml");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
        XMLStreamWriter writer;
        try {
//            writer = outputFactory.createXMLStreamWriter(System.out);
            writer = outputFactory.createXMLStreamWriter(fileOutputStream);
            writer.writeStartDocument("1.0");
            writer.writeStartElement("places");

            writer.writeStartElement("address");//address 1 ->

            writer.writeStartElement("city");
            writer.writeAttribute("size", "big");
            writer.writeCharacters("Kyiv");
            writer.writeEndElement();

            writer.writeStartElement("street");
            writer.writeCharacters("Khreshchatyk");
            writer.writeEndElement();

            writer.writeStartElement("building");
            writer.writeCharacters("26");
            writer.writeEndElement();

            writer.writeEndElement();////address 1 <-

            writer.writeStartElement("address");//address 2 ->

            writer.writeStartElement("city");
            writer.writeAttribute("size", "medium");
            writer.writeCharacters("Brovary");
            writer.writeEndElement();

            writer.writeStartElement("street");
            writer.writeCharacters("Nezalezhnosti boulevard");
            writer.writeEndElement();

            writer.writeStartElement("building");
            writer.writeCharacters("11-A");
            writer.writeEndElement();

            writer.writeEndElement();////address 2 <-

            writer.writeEndElement();//places
            writer.writeEndDocument();

            writer.flush();
            writer.close();

        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }


//SAXParser
        //ім'я ф-лу
//        final String fileName = "address.xml";
        final String fileName = "address_created.xml";

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser;
        try {
            parser = factory.newSAXParser();
        } catch (ParserConfigurationException | SAXException e) {
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
        } catch (SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}