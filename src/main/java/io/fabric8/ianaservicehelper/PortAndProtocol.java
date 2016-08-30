package io.fabric8.ianaservicehelper;

import java.io.Serializable;

public class PortAndProtocol implements Serializable {

  private static final long serialVersionUID = 5877584040494514281L;

  private final Integer port;

  private final String protocol;

  public PortAndProtocol(int port, String protocol) {
    this.port = port;
    this.protocol = protocol;
  }

  public Integer getPort() {
    return port;
  }

  public String getProtocol() {
    return protocol;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PortAndProtocol that = (PortAndProtocol) o;

    if (port != null ? !port.equals(that.port) : that.port != null) return false;
    return protocol != null ? protocol.equals(that.protocol) : that.protocol == null;

  }

  @Override
  public int hashCode() {
    int result = port != null ? port.hashCode() : 0;
    result = 31 * result + (protocol != null ? protocol.hashCode() : 0);
    return result;
  }
}
