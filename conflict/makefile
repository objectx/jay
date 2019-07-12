# jay/conflict [leaf]

DIFF	= diff - # cat >
# JAY	= ../src/jay
JAY	= java -jar ../src/jay.jar

all:
clean:
dist:
distclean:
test:
	$(JAY) Conflict.jay ../java/skeleton.java | \
	  $(DIFF) Conflict.java
	$(JAY) Conflict.jay ../java/skeleton.tables | \
	  $(DIFF) Conflict.tables
	$(JAY) Conflict.jay ../cs/skeleton.cs | \
	  $(DIFF) Conflict.cs
