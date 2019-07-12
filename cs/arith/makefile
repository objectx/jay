# jay/cs/arith [leaf]

MONO_HOME = /Library/Frameworks/Mono.framework/Commands# http://www.go-mono.com

CSCFLAGS = /lib:.. /reference:yyDebug.dll /main:Arith.Arith
JAYFLAGS = -c -t
MONOPATH = ..

CSC	= $(MONO_HOME)/mcs
MONO	= MONO_PATH=$(MONOPATH) $(MONO_HOME)/mono
# JAY	= ../../src/jay
# SKEL	= ../skeleton.cs
JAY	= java -jar ../../src/jay.jar
SKEL	= /resource/cs/skeleton.cs

all:	arith.exe
clean:			; rm -f arith.cs y.output y.tab.?
dist:	all
distclean: clean	; rm -f arith.exe
test:	arith.exe
	@ set -x; \
	  { echo 36/5; echo '5 * (3+4)'; } | $(MONO) arith.exe trace

.SUFFIXES: .exe .cs .jay
.jay.cs:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
.cs.exe:	; $(CSC) $(CSCFLAGS) $*.cs
.jay.exe:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
		  $(CSC) $(CSCFLAGS) $*.cs
