/**
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.ianaservicehelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public class Helper {

  private static Map<PortAndProtocol, Set<String>> servicePortsMap = null;

  private static Map<String, Set<PortAndProtocol>> serviceNamesMap = null;

  public static Set<String> serviceNames(int portNumber) throws IOException {
    return serviceNames(new PortAndProtocol(portNumber, "tcp"));
  }

  public static Set<String> serviceNames(int portNumber, String protocol) throws IOException {
    return serviceNames(new PortAndProtocol(portNumber, protocol));
  }

  @SuppressWarnings("unchecked")
  public static Set<String> serviceNames(PortAndProtocol pp) throws IOException {
    if (servicePortsMap == null) {
      synchronized (Helper.class) {
        if (servicePortsMap == null) {
          InputStream is = Helper.class.getResourceAsStream("ianaservicenamemap");
          GZIPInputStream gz = new GZIPInputStream(is);

          try (ObjectInputStream ois = new ObjectInputStream(gz)) {
             servicePortsMap = (HashMap<PortAndProtocol, Set<String>>) ois.readObject();
          } catch (ClassNotFoundException e) {
            // Don't rethrow... should never be hit
            e.printStackTrace();
            return null;
          }
        }
      }
    }
    return servicePortsMap.get(pp);
  }

  @SuppressWarnings("unchecked")
  public static Set<PortAndProtocol> servicePorts(String serviceName) throws IOException {
    if (serviceNamesMap == null) {
      synchronized (Helper.class) {
        if (serviceNamesMap == null) {
          InputStream is = Helper.class.getResourceAsStream("ianaserviceportmap");
          GZIPInputStream gz = new GZIPInputStream(is);

          try (ObjectInputStream ois = new ObjectInputStream(gz)) {
             serviceNamesMap = (HashMap<String, Set<PortAndProtocol>>) ois.readObject();
          } catch (ClassNotFoundException e) {
            // Don't rethrow... should never be hit
            e.printStackTrace();
            return null;
          }
        }
      }
    }
    return serviceNamesMap.get(serviceName);
  }

  @SuppressWarnings("unchecked")
  public static PortAndProtocol servicePort(String serviceName) throws IOException {
    return servicePort(serviceName, "tcp");
  }

  @SuppressWarnings("unchecked")
  public static PortAndProtocol servicePort(String serviceName, String protocol) throws IOException {
    Set<PortAndProtocol> servicePorts = servicePorts(serviceName);
    if (servicePorts == null) {
      return null;
    }
    for (PortAndProtocol pp : servicePorts) {
      if (pp.getProtocol().equalsIgnoreCase(protocol)) {
        return pp;
      }
    }
    return null;
  }

}
