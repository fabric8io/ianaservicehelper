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
public class ServicePortsTest {

  @Parameters
  public static Collection<Object[]> parameters() {
    return Arrays.asList(new Object[][] {
      {"domain", new HashSet<>(Arrays.asList(new PortAndProtocol(53, "tcp"), new PortAndProtocol(53, "udp")))},
      {"http", new HashSet<>(Arrays.asList(new PortAndProtocol(80, "tcp"), new PortAndProtocol(80, "udp"), new PortAndProtocol(80, "sctp")))},
      {"www-http", new HashSet<>(Arrays.asList(new PortAndProtocol(80, "tcp"), new PortAndProtocol(80, "udp")))},
      {"nonexistent", null}
    });
  }

  private String name;

  private Set<String> servicePorts;

  public ServicePortsTest(String name, Set<String> servicePorts) {
    this.name = name;
    this.servicePorts = servicePorts;
  }

  @Test
  public void serviceNames() throws Exception {
    assertEquals(servicePorts, Helper.servicePorts(name));
  }

}
