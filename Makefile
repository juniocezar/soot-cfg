IN := dots
OUT := pdfs
SOURCES := $(wildcard $(IN)/*.dot)
OBJECTS := $(patsubst $(IN)/%.dot, $(OUT)/%.pdf, $(SOURCES))
JAVAHOME := $$HOME/Apps/java8/


all: build run pdf

build:
	mkdir -p bin pdfs dots
	javac -cp bin src/dot/graph/DotNode.java -d bin
	javac -cp bin src/dot/graph/DotGraph.java -d bin
	javac -cp bin:jar-libs/sootclasses-trunk-jar-with-dependencies.jar src/sootparser/SimpleParser.java -d bin

run:
	clear
	mkdir -p dots
	java -cp .:input:bin:jar-libs/sootclasses-trunk-jar-with-dependencies.jar sootparser.SimpleParser -cp sample:$(JAVAHOME)/jre/lib/rt.jar -src-prec class -p jb use-original-names -f J $(FILE)
	mv -v *dot dots

pdf: $(OBJECTS)

$(OUT)/%.pdf: $(IN)/%.dot
	mkdir -p pdfs
	dot -Tpdf $< > $@

clean:
	rm -rf pdfs dots sootOutput
