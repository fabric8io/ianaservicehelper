# IANA Service Helper

This utility provides a means to look up IANA registered services either by port
or service name. The IANA registered services are embedded in a Map inside the
JAR so no external connection is required. Maps are lazily initialized to ensure
it's only allocated as used.

### Usage

Look up service ports by service name:

```
Set<PortAndProtocol> ports = Helper.servicePorts("http");
```

Look up a service port by service name and protocol:

```
PortAndProtocol port = Helper.servicePort("http", "tcp");
```

Look up a service port by service name (protocol defaults to `"tcp"`):

```
PortAndProtocol port = Helper.servicePort("http");
```

Lookup service names by port and protocol:

```
Set<String> services = Helper.serviceNames("80", "tcp");
```

Lookup service names by port (protocol defaults to `"tcp"`):

```
Set<String> services = Helper.serviceNames("80");
```
