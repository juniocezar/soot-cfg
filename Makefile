IN := dots
OUT := pdfs
SOURCES := $(wildcard $(IN)/*.dot)
OBJECTS := $(patsubst $(IN)/%.dot, $(OUT)/%.pdf, $(SOURCES))


all: build run pdf

build:
	javac -cp bin src/dot/graph/DotNode.java -d bin
	javac -cp bin src/dot/graph/DotGraph.java -d bin
	javac -cp bin:jar-libs/jasminclasses-2.5.0.jar:jar-libs/polyglotclasses-1.3.5.jar:jar-libs/soot-trunk-2.5.1.jar src/sootparser/SimpleParser.java -d bin

run:
	clear
	java -cp .:input:bin:jar-libs/jasminclasses-2.5.0.jar:jar-libs/polyglotclasses-1.3.5.jar:jar-libs/soot-trunk-2.5.1.jar sootparser.SimpleParser -p jb use-original-names -f S $(FILE)
	mv -v *dot dots


pdf: $(OBJECTS)

$(OUT)/%.pdf: $(IN)/%.dot
	dot -Tpdf $< > $@


android:
	java -cp .:./bin:jar-libs/soot-trunk-2.5.1.jar sootparser.SimpleParser -android-jars jar-libs -allow-phantom-refs -src-prec apk -f S -process-dir /home/junio/repositorios/cfg-soot/app2.apk 

clean:
	rm pdfs/* dots/*
