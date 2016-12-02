Assumptions

1. Multiplication operands have at least one non constant operand. If both operands are constant, Jimple computes the 
result of multiplication.

2. SOOT API only allows retrieving the type of parameter via method.getParameterTypes(). Also, various other methods in SootMethod class, 
do not return the "name" of the parameter. getLocals() also return the name of the variable since it is technically a 
local variable as well.

3. Run the program from Start.java with arguments "PA.Test".

4. All non-private or non-static methods are virtual by default in Java. Also final methods are not virtual. 

5. All statements containing * in Jimple have been counted. The statement containing this operator is printed in the output.

6. printing the head of loop prints the jimple loop head rather than Java source code loop head.
