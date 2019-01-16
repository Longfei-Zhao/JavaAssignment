#JFX_PATH = /usr/lib/jvm/jdk1.7.0_15/jre/lib/jfxrt.jar
JFXP = ${JAVAFX_PATH}
SOURCES = $(wildcard splotch/*.java)


all: compile 

compile: $(SOURCES)
	javac -d bin -cp .:$(JFXP) $(SOURCES)

run: compile
	java -cp bin:$(JFXP) splotch.Smoother  &

clean:
	@@echo "Deleting all generated files"
	@@rm -rf bin/*

diag:
	@@echo $(JFXP)
	@@echo $(SOURCES)
