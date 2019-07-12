# jay/yydebug [leaf]

v4 = /System/Library/Frameworks/JavaVM.framework/Versions/1.4.2/Commands

JAR = $(v4)/jar
JAVAC = $(v4)/javac -classpath ../..

d	= jay/yydebug
go	= yyDebug.class yyDebugAdapter.class yyAnim.class \
	  yyAnimPanel.class yyInputStream.class yyPrintStream.class \
	  yyJInputStream.class

all:	yydebug.jar
clean:			; rm -f *.class src.jar doc-files/*.jar
dist:	all src.jar	; cp yydebug.jar src.jar doc-files
distclean: clean	; rm -f yydebug.jar
test:	all

yydebug.jar: $(go)
	cd ../.. && $(JAR) cf $d/$@ $d/*.class

src.jar: $(go:.class=.java) makefile package.html
	cd ../.. && $(JAR) cf $d/$@ $d/*.java $d/makefile $d/package.html

.SUFFIXES:
.SUFFIXES: .class .java
.java.class:	; $(JAVAC) $*.java
