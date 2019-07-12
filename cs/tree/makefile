# jay/cs/tree [leaf]

MONO_HOME = /Library/Frameworks/Mono.framework/Commands# http://www.go-mono.com

CSCFLAGS = /lib:.. /reference:yyDebug.dll
JAYFLAGS = -c -t
MONOPATH = ..

CSC	= $(MONO_HOME)/gmcs # generics
MONO	= MONO_PATH=$(MONOPATH) $(MONO_HOME)/mono
# JAY	= ../../src/jay
# SKEL	= < ../skeleton.cs
JAY	= java -jar ../../src/jay.jar
SKEL	= /resource/cs/skeleton.cs

all:	tree.exe
clean:			; rm -f tree.cs y.output y.tab.?
dist:	all
distclean: clean	; rm -f tree.exe
test:	tree.exe
	@ set -x; \
	  { echo 36/5; echo '5 * (3+4)'; echo +10; } | $(MONO) tree.exe trace

.SUFFIXES: .exe .cs .jay
.jay.cs:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
.cs.exe:	; $(CSC) $(CSCFLAGS) $*.cs
.jay.exe:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
		  $(CSC) $(CSCFLAGS) $*.cs
