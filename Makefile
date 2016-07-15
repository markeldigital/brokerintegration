SHELL=/bin/sh

.PHONY: all
all: package

.PHONY: package
package:
	mvn package

.PHONY: clean
clean:
	mvn clean
