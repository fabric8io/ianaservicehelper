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
public class ServiceNamesTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
      {53, "tcp", new HashSet<>(Arrays.asList("domain"))},
      {53, "udp", new HashSet<>(Arrays.asList("domain"))},
      {80, "tcp", new HashSet<>(Arrays.asList("http", "www", "www-http"))},
      {80, "udp", new HashSet<>(Arrays.asList("http", "www", "www-http"))},
      {70000, "none", null}
    });
  }

  private int port;

  private String protocol;

  private Set<String> serviceNames;

  public ServiceNamesTest(int port, String protocol, Set<String> serviceNames) {
    this.port = port;
    this.protocol = protocol;
    this.serviceNames = serviceNames;
  }

  @Test
  public void serviceNames() throws Exception {
    assertEquals(serviceNames, Helper.serviceNames(port, protocol));
  }

}
