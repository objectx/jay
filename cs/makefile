# jay/cs

MONO = /Library/Frameworks/Mono.framework/Commands#	http://www.go-mono.com
CSC	= $(MONO)/gmcs

d	= arith recover tree

all::	yyDebug.dll
dist::	all
distclean:: clean	; rm -f yyDebug.dll

all clean dist distclean test::
	for d in $d; do \
	  echo; echo ' ----- jay/cs/'$$d; echo; \
	  ( cd $$d && $(MAKE) $@ ) done

.SUFFIXES: .dll .cs
.cs.dll:		; $(CSC) /target:library $*.cs
