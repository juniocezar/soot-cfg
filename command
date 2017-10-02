java -cp soot-2.5.0.jar soot.Main -cp .:/usr/lib/jvm/java-8-oracle/jre/lib/rt.jar:/home/junio/Documentos/UFMG/Soot/jasmin.jar HelloWorld
java -cp soot-2.5.0.jar soot.Main -cp .:/usr/lib/jvm/java-7-openjdk-amd64/jre/lib/rt.jar:/home/junio/Documentos/UFMG/Soot/jasmin.jar HelloWorld
java -cp soot-2.5.0.jar soot.Main --help


update-java-alternatives --list

sudo update-java-alternatives --set /usr/lib/jvm/java-1.7.0-openjdk-amd64
sudo update-java-alternatives --set /usr/lib/jvm/java-8-oracle


/usr/lib/jvm/java-7-openjdk-amd64/bin/java -classpath /home/juniocezar/eclipse/kepler-workspace/3-Soot-GettingStated/bin:/home/juniocezar/Apps/soot/pre-compiled-jars/jasminclasses-2.5.0.jar:/home/juniocezar/Apps/soot/pre-compiled-jars/polyglotclasses-1.3.5.jar:/home/juniocezar/Apps/soot/pre-compiled-jars/sootclasses-2.5.0.jar MySootMainExtension -f S HelloWorld


java -classpath /home/juniocezar/eclipse/kepler-workspace/3-Soot-GettingStated/bin:/home/juniocezar/Apps/soot/pre-compiled-jars/jasminclasses-2.5.0.jar:/home/juniocezar/Apps/soot/pre-compiled-jars/polyglotclasses-1.3.5.jar:/home/juniocezar/Apps/soot/pre-compiled-jars/sootclasses-2.5.0.jar MySootMainExtension -f S HelloWorld
