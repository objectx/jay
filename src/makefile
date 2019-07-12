# jay/src [leaf]

# jay.jar requires <http://darcs.brianweb.net/nestedvm/> in ../etc

MIPSCC = ../etc/upstream/install/bin/mips-unknown-elf-gcc \
	 -DDEFAULT_TEMPLATE_FILE='"/resource/java/skeleton.java"' \
	 -O3 -mmemcpy -ffunction-sections -fdata-sections \
	 -falign-functions=512 -fno-rename-registers -fno-schedule-insns \
	 -fno-delayed-branch -freduce-all-givs -march=mips1 -I. \
	 --static -Wl,--gc-sections # -Wall -Wno-unused -Werror
JAVA = java -cp .:../etc/build:../etc/upstream/build/classgen/build

# Windows Services For Unix:
#   comment 'depend' stuff below.
#   make will create jay.exe with GNU C.
#   make CC=c89 will create jay.exe with Visual C++.

CC	= `if [ -n "$$COMSPEC" ]; then echo gcc; else echo cc; fi`
CFLAGS	= -s # -g

c	= closure.c error.c lalr.c lr0.c main.c mkpar.c output.c reader.c \
	  symtab.c verbose.c warshall.c
e	= `if [ -n "$$COMSPEC" ]; then echo jay.exe; else echo jay; fi`

all: jay jay.jar
clean:			; rm -f *.o manifest ../Main.class jay.mips
dist: all
distclean: clean	; rm -f jay jay.exe jay.jar
test: all

jay: $(c:.c=.o)		; $(CC) -o $e $(CFLAGS) $(c:.c=.o)

jay.jar: clean ../Main.class
	echo Main-Class: jay.Main > manifest
	( cd ../.. && \
	  jar cm jay/src/manifest jay/Main.class 2>/dev/null ) | \
	( cd .. && jar u java/skeleton.* cs/skeleton.* 2>/dev/null ) | \
	( cd ../etc/build && jar u org 2>/dev/null ) | \
	( cd ../etc/upstream/build/classgen/build && jar u org ) > $@

../Main.class: jay.mips
	$(JAVA) org.ibex.nestedvm.Compiler -outformat class -o unixruntime \
		-outfile $@ jay.Main jay.mips

jay.mips: $c
	$(MIPSCC) -o $@ $c

depend: $c		; $(CC) -MM $(CFLAGS) $c > $@
-include depend
