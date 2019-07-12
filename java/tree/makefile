# jay/java/tree [leaf]

yydebug = ../../yydebug/yydebug.jar

CLASSPATH = ..:$(yydebug)
JAYFLAGS = -t

JAVAC	= javac -classpath $(CLASSPATH) -Xlint:unchecked
# JAY	= ../../src/jay < ../skeleton.java
JAY	= java -jar ../../src/jay.jar
# JAYT	= ../../src/jay
# TABLE	= ../skeleton.tables
JAYT	= java -jar ../../src/jay.jar
TABLE	= /resource/java/skeleton.tables

all:	tree.jar tables.jar
clean:			; rm -rf Tree.java Tree.tables \
				manifest *.class y.output yacc.?
dist:	all
distclean: clean	; rm -f *.jar
test:	all
	@ set -x; \
	  for i in tree.jar tables.jar; do \
	    { echo 36/5; echo '5 * (3+4)'; } | java -jar $$i trace; done

tree.jar: Tree.jay
	$(JAY) $(JAYFLAGS) Tree.jay > Tree.java
	$(JAVAC) $(JAVACFLAGS) Tree.java
	echo 'Main-Class: tree.Tree' > manifest
	( cd .. && \
	    jar um tree/manifest tree/*.class ) < $(yydebug) > $@
	$(MAKE) clean

tables.jar: Tree.jay
	$(JAYT) $(JAYFLAGS) Tree.jay $(TABLE) | tee Tree.java | \
	  sed -n 's/^\/\/yy//p' > Tree.tables
	$(JAVAC) $(JAVACFLAGS) Tree.java
	echo 'Main-Class: tree.Tree' > manifest
	( cd .. && \
	    jar um tree/manifest tree/*.class tree/Tree.tables ) \
		< $(yydebug) > $@
	$(MAKE) clean
