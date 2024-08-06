build:
	./gradlew clean build

install-dist:
	./gradlew clean installDist

start-dist:
	./build/install/demo/bin/demo

.PHONY: build