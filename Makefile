IN := dots
OUT := pdfs
SOURCES := $(wildcard $(IN)/*.dot)
OBJECTS := $(patsubst $(IN)/%.dot, $(OUT)/%.pdf, $(SOURCES))


all: build run pdf

build:
	javac -cp bin src/dot/graph/DotNode.java -d bin
	javac -cp bin src/dot/graph/DotGraph.java -d bin
	javac -cp bin:jar-libs/jasminclasses-2.5.0.jar:jar-libs/polyglotclasses-1.3.5.jar:jar-libs/sootclasses-2.5.0.jar src/sootparser/SimpleParser.java -d bin

run:
	clear
	java -cp .:input:bin:jar-libs/jasminclasses-2.5.0.jar:jar-libs/polyglotclasses-1.3.5.jar:jar-libs/sootclasses-2.5.0.jar sootparser.SimpleParser -p jb use-original-names -f S simple
	mv -v *dot dots


pdf: $(OBJECTS)

$(OUT)/%.pdf: $(IN)/%.dot
	dot -Tpdf $< > $@


clean:
	rm pdfs/* dots/*
