# pl0_compiler
This is a compiler for the [PL/0 language](https://en.wikipedia.org/wiki/PL/0), which I am constructing for the course [Compiler/ Interpreter](http://www.informatik.htw-dresden.de/~beck/Compiler/) at [HTW Dresden](https://www.htw-dresden.de/startseite.html).

It uses the rather uncommon graph based approach of a parser, as taught in the course at HTW.

It does not generate machine code, it generates a byte code that works with the virtual machine supplied in the course.

The grammar implemented has one minor difference to the actual PL/0 grammar: It does not permit *statement* to be empty.
