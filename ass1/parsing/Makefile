.SUFFIXES: .java .class
SOURCES = $(wildcard *.java)
CLASSES = $(SOURCES:.java=.class)
JARS = $(wildcard jackson/*-2.8.7.jar)   
JACKSON_JARS = $(subst r ,r:,$(JARS))

compile: $(SOURCES)
	@@rm -rf bin >> /dev/null
	@@mkdir bin
	@@javac -d bin -cp .:$(JACKSON_JARS) $(SOURCES)

run: compile
	@@java -cp bin:$(JACKSON_JARS) JacksonSampler ../tweets.json

clean:
	@@echo "Deleting all generated files"
	@@rm -rf bin/*

diag:
	@@echo $(SOURCES)
	@@echo $(JARS)
	@@echo $(JACKSON_JARS)
