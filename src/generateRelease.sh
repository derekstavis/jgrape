#!/bin/sh

# generateRelease.sh
# jGrape
#
# Created by Derek Stavis on 27/11/09.

echo "Compilando...";

javac JGrape.java
		
if [ $? -lt 1 ]; then
	echo "Limpando ultimo release";
	rm -v ./release/*;
	rm -v ./release/InterfaceGrafica/*;
	echo "Criando nova release";
	cp -v *.class ./release;
	cp -v ./InterfaceGrafica/*.class ./release/InterfaceGrafica/;
	cp -v ./Graph/*.class ./release/Graph/;
	cp -v ./images/*.png ./release;
	echo "Release criada!";

	echo "Removendo os binários do diretório raiz";
	rm -v *.class;
	rm -v ./InterfaceGrafica/*.class;
	rm -v ./InterfaceGrafica/*.class;
	echo "Diretório limpo!";

	cd release;
	java JGrape;
else
	echo "Erro de compilação!!";
fi
