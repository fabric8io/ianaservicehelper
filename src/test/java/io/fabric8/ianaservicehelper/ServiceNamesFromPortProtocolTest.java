package io.fabric8.ianaservicehelper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class ServiceNamesFromPortProtocolTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
      {new PortAndProtocol(53, "tcp"), new HashSet<>(Arrays.asList("domain"))},
      {new PortAndProtocol(53, "udp"), new HashSet<>(Arrays.asList("domain"))},
      {new PortAndProtocol(80, "tcp"), new HashSet<>(Arrays.asList("www-http", "http", "www"))},
      {new PortAndProtocol(80, "udp"), new HashSet<>(Arrays.asList("http", "www", "www-http"))},
      {new PortAndProtocol(70000, "none"), null}
    });
  }

  private PortAndProtocol pp;

  private Set<String> serviceNames;

  public ServiceNamesFromPortProtocolTest(PortAndProtocol pp, Set<String> serviceNames) {
    this.pp = pp;
    this.serviceNames = serviceNames;
  }

  @Test
  public void serviceNames() throws Exception {
    assertEquals(serviceNames, Helper.serviceNames(pp));
  }

}
