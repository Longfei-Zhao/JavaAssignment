SOURCES = $(wildcard src/*.java src/*/*.java)

all: compile doc

compile: $(SOURCES) pre-build
	@@javac -d bin -Xlint:unchecked $(SOURCES)
	@@cp -r src/view bin/

doc: $(SOURCES)
	@@javadoc @doc_args $(SOURCES)

run: compile
	@@echo "Press 'p' to pause, 'r' to resume, 'cmd-q' to quit"
	@@java -cp bin RunningHeadline data_stream.json &

runcli: compile
	@@java -cp bin model.Feeder  data_stream.json &

pre-build:
	@@mkdir -p bin

clean:
	@@echo "Deleting all generated files"
	@@rm -rf bin/*
	@@rm -rf docs/*

diag:
	@@echo $(SOURCES)

.PHONY: all compile doc run runcli pre-build clean diag
	
help:
	@@echo "To run GUI client: make run"
	@@echo "To run CLI client: make runcli"
