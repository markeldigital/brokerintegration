SHELL=/bin/sh

.PHONY: all
all: package

.PHONY: package
package:
	mvn package

.PHONY: style
style:
	mvn checkstyle:check

.PHONY: pit
pit:
	mvn org.pitest:pitest-maven:mutationCoverage
