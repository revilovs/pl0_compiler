# pl0_compiler
This is a compiler for the [PL/0 language](https://en.wikipedia.org/wiki/PL/0), which I am constructing for the course [Compiler/ Interpreter](http://www.informatik.htw-dresden.de/~beck/Compiler/) at [HTW Dresden](https://www.htw-dresden.de/startseite.html).

It uses the rather uncommon graph based approach of a parser, as taught in the course at HTW.

It does not generate machine code, it generates a byte code that works with the virtual machine supplied in the course.

The grammar implemented has one minor difference to the actual PL/0 grammar: It does not permit *statement* to be empty.

## Requirements
This project uses Java 8 and maven. If you don't have maven, you can also compile with javac.

## Building
To build the compiler, run
```bash
mvn package
```
This runs all tests and generates an executable jar file. If you don't have maven installed, you can also use `javac`.

## Running
You can run the compiler by executing
```
java -jar path/to/jar <source file> [<output file>]
```

## Testing
You can run the tests with
```bash
mvn test
```

## Grammar
The implemented grammar is described by syntax graphs, which are implemented in the class `de.htw_dresden.informatik.s75924.parser.Graph`. These are their visual representations:

### Program
![Graph for program](doc/graphs/program.png)

### Block
![Graph for block](doc/graphs/block.png)

### Constant declaration list
![Graph for constant declaration list](doc/graphs/constantDeclarationList.png)

### Constant declaration list
![Graph for constant declaration](doc/graphs/constantDeclaration.png)

### Variable declaration list
![Graph for variable declaration list](doc/graphs/variableDeclarationList.png)

### Variable declaration
![Graph for variable declaration](doc/graphs/variableDeclaration.png)

### Procedure declaration
![Graph for procedure declaration](doc/graphs/procedureDefinition.png)

### Statement
![Graph for statement](doc/graphs/statement.png)

### Assignment statement
![Graph for assignment statement](doc/graphs/assignmentStatement.png)

### Conditional statement
![Graph for conditional statement](doc/graphs/conditionalStatement.png)

### Loop statement
![Graph for loop statement](doc/graphs/loopStatement.png)

### Compound statement
![Graph for compound statement](doc/graphs/compoundStatement.png)

### Procedure call
![Graph for procedure call](doc/graphs/procedureCall.png)

### Input statement
![Graph for input statement](doc/graphs/inputStatement.png)

### Output statement
![Graph for output statement](doc/graphs/outputStatement.png)

### Expression
![Graph for expression](doc/graphs/expression.png)

### Term
![Graph for term](doc/graphs/term.png)

### Factor
![Graph for factor](doc/graphs/factor.png)

### Condition
![Graph for condition](doc/graphs/condition.png)