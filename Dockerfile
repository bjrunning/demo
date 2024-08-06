FROM gradle:7.6.4-jdk17

WORKDIR /

COPY / .

RUN gradle installDist

CMD ./build/install/demo/bin/demo --spring.profiles.active=production