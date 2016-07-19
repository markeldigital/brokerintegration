SHELL=/bin/sh

.PHONY: all
all: package

.PHONY: package
package:
	mvn $(JARGS) package

.PHONY: clean
clean:
	mvn clean
