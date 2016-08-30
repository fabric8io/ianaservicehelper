# IANA Service Helper[![Join the chat at https://gitter.im/fabric8io/users](https://badges.gitter.im/fabric8io/users.svg)](https://gitter.im/fabric8io/users?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

[![license](https://img.shields.io/github/license/fabric8io/ianaservicehelper.svg?maxAge=2592000)](https://github.io/fabric8io/ianaservicehelper/blob/master/LICENSE.txt)
[![Maven Central](https://img.shields.io/maven-central/v/io.fabric8/ianaservicehelper.svg?maxAge=2592000)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Aio.fabric8%20a%3Aianaservicehelper)
[![Javadocs](http://www.javadoc.io/badge/io.fabric8/ianaservicehelper.svg?color=blue)](http://www.javadoc.io/doc/io.fabric8/ianaservicehelper)
[![CircleCI](https://img.shields.io/circleci/project/fabric8io/ianaservicehelper.svg?maxAge=2592000)](https://circleci.com/gh/fabric8io/ianaservicehelper)
[![Dependency Status](https://dependencyci.com/github/fabric8io/ianaservicehelper/badge)](https://dependencyci.com/github/fabric8io/ianaservicehelper)

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
Set<String> services = Helper.serviceNames(80, "tcp");
```

Lookup service names by port (protocol defaults to `"tcp"`):

```
Set<String> services = Helper.serviceNames(80);
```
