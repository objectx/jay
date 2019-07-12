# jay/cs/recover [leaf]

MONO_HOME = /Library/Frameworks/Mono.framework/Commands# http://www.go-mono.com

CSCFLAGS = /lib:.. /reference:yyDebug.dll
JAYFLAGS = -c -t
MONOPATH = ..

CSC	= $(MONO_HOME)/mcs
MONO	= MONO_PATH=$(MONOPATH) $(MONO_HOME)/mono
# JAY	= ../../src/jay
# SKEL	= < ../skeleton.cs
JAY	= java -jar ../../src/jay.jar
SKEL	= /resource/cs/skeleton.cs

all:	recover.exe
clean:			; rm -f recover.cs y.output y.tab.?
dist:	all
distclean: clean	; rm -f recover.exe
test:	recover.exe
	@ set -x; \
	  { echo opt; echo opt ,; echo opt 1 2; } | $(MONO) recover.exe; \
	  { echo seq 1; echo seq; echo seq 1,; } | $(MONO) recover.exe; \
	  { echo list 1,2; echo list; echo list 1,; } | $(MONO) recover.exe

.SUFFIXES: .exe .cs .jay
.jay.cs:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
.cs.exe:	; $(CSC) $(CSCFLAGS) $*.cs
.jay.exe:	; $(JAY) $(JAYFLAGS) $*.jay $(SKEL) > $*.cs
		  $(CSC) $(CSCFLAGS) $*.cs
