package io.fabric8.ianaservicehelper;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class Generator {
  public static final String SERVICES_NAMESPACE = "http://www.iana.org/assignments";

  public static final URL IANA_SERVICES_URL;

  static {
    URL temp = null;
    try {
      temp = new URL("https://www.iana.org/assignments/service-names-port-numbers/service-names-port-numbers.xml");
    } catch (MalformedURLException e) {
      System.exit(1);
    }
    IANA_SERVICES_URL = temp;
  }

  public static void main(String[] args) throws Exception {
    if (args.length < 2) {
      System.err.println("Missing destination file(s)");
      System.exit(1);
    }
    String servicePortsMapDestFile = args[0];
    String serviceNamesMapDestFile = args[1];

    URLConnection conn = IANA_SERVICES_URL.openConnection();
    try (InputStream in = conn.getInputStream()) {
      XMLInputFactory factory = XMLInputFactory.newInstance();
      XMLEventReader reader = factory.createXMLEventReader(in);

      Map<PortAndProtocol, Set<String>> servicePortMap = new HashMap<>();
      Map<String, Set<PortAndProtocol>> serviceNameMap = new HashMap<>();

      String previousName = null;
      while (reader.hasNext()) {
        XMLEvent event = reader.nextEvent();
        if (event.isStartElement()) {
          StartElement se = event.asStartElement();
          if (new QName(SERVICES_NAMESPACE, "record").equals(se.getName())) {
            Record record = readRecord(reader, previousName);
            if (record != null) {
              previousName = record.getName();

              if (record.getName() != null && !record.getName().isEmpty()
                  && record.getProtocol() != null && !record.getProtocol().isEmpty()
                  && record.getPort() > 0) {
                final PortAndProtocol pp = new PortAndProtocol(record.getPort(), record.getProtocol().toLowerCase());

                Set<String> serviceNames = servicePortMap.get(pp);
                if (serviceNames == null) {
                  serviceNames = new HashSet<>();
                }
                serviceNames.add(record.getName());
                servicePortMap.put(pp, serviceNames);

                Set<PortAndProtocol> servicePorts = serviceNameMap.get(record.getName());
                if (servicePorts == null) {
                  servicePorts = new HashSet<>();
                }
                servicePorts.add(pp);
                serviceNameMap.put(record.getName(), servicePorts);
              }

            } else {
              previousName = null;
            }
          }
        }
      }

      FileOutputStream fos = new FileOutputStream(servicePortsMapDestFile);
      GZIPOutputStream gz = new GZIPOutputStream(fos);
      try (ObjectOutputStream oos = new ObjectOutputStream(gz)) {
        oos.writeObject(servicePortMap);
      }

      fos = new FileOutputStream(serviceNamesMapDestFile);
      gz = new GZIPOutputStream(fos);
      try (ObjectOutputStream oos = new ObjectOutputStream(gz)) {
        oos.writeObject(serviceNameMap);
      }
    }
  }

  private static Record readRecord(XMLEventReader reader, String previousName) throws Exception {
    Record record = new Record();
    record.setName(previousName);
    while (reader.hasNext()) {
      XMLEvent event = reader.nextEvent();
      if (event.isEndElement()) {
        EndElement se = event.asEndElement();
        if (new QName(SERVICES_NAMESPACE, "record").equals(se.getName())) {
          return record;
        }
      }
      if (event.isStartElement()) {
        StartElement se = event.asStartElement();
        if (new QName(SERVICES_NAMESPACE, "name").equals(se.getName())) {
          record.setName(readContent(reader));
          continue;
        }
        if (new QName(SERVICES_NAMESPACE, "protocol").equals(se.getName())) {
          record.setProtocol(readContent(reader));
          continue;
        }
        if (new QName(SERVICES_NAMESPACE, "number").equals(se.getName())) {
          String number = readContent(reader);
          try {
            int port = Integer.parseInt(number);
            record.setPort(port);
          } catch (NumberFormatException e) {
            return null;
          }
          continue;
        }
      }
    }

    throw new IllegalStateException("Should have hit end element for record");
  }

  private static String readContent(XMLEventReader reader) throws XMLStreamException {
    if (reader.hasNext()) {
      XMLEvent event = reader.nextEvent();
      if (event.isCharacters()) {
        return event.asCharacters().getData();
      }
    }
    return null;
  }

  private static final class Record {
    private String name;
    private String protocol;
    private int port;

    Record() {
    }

    String getName() {
      return name;
    }

    void setName(String name) {
      this.name = name;
    }

    String getProtocol() {
      return protocol;
    }

    void setProtocol(String protocol) {
      this.protocol = protocol;
    }

    int getPort() {
      return port;
    }

    public void setPort(int port) {
      this.port = port;
    }

    @Override
    public String toString() {
      return "Record{" +
          "name='" + name + '\'' +
          ", protocol='" + protocol + '\'' +
          ", port=" + port +
          '}';
    }
  }
}
