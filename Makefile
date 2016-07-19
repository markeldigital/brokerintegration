SHELL=/bin/sh

ifeq ($(JAVA_HOME), "/usr/lib/jvm/java-6-openjdk-amd64/jre")
	JARGS := -source 1.6 -target 1.6
endif

.PHONY: all
all: package

.PHONY: package
package:
	mvn $(JARGS) package

.PHONY: clean
clean:
	mvn clean
