SHELL=/bin/sh

ifeq ($(JAVA_HOME),/usr/lib/jvm/java-6-openjdk-amd64/jre)
	SKIP_CHECKSTYLE := -Dcheckstyle.skip=true
endif

.PHONY: all
all: package

.PHONY: package
package:
	mvn package $(SKIP_CHECKSTYLE)
