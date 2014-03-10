/*
 * JumpVM: The Java Unified Multi Paradigm Virtual Machine.
 * Copyright (C) 2013 Tim Wiederhake
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */

package jumpvm.code.mama;

import jumpvm.ast.mama.BinOpExpression.BinaryOperator;
import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.exception.ExecutionException;
import jumpvm.memory.Stack;
import jumpvm.memory.objects.BasicValueObject;
import jumpvm.vm.MaMa;

/**
 * Binary operation.
 * 
 * <pre>
 * ST[SP - 1] := ST[SP - 1] BinOp ST[SP];
 * SP := SP - 1;
 * </pre>
 */
public class OpBinInstruction extends MaMaInstruction {
    /** Operator. */
    private final BinaryOperator operator;

    /**
     * Create a new OpBin instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param operator operator
     */
    public OpBinInstruction(final MaMaAstNode sourceNode, final BinaryOperator operator) {
        super(sourceNode);
        this.operator = operator;
    }

    /**
     * Convert int value to boolean.
     * 
     * @param i int value
     * @return true if i != 0, false otherwise
     */
    private boolean b(final int i) {
        return i != 0;
    }

    /**
     * Calculate the result of the binary expression.
     * 
     * @param lhs left hand side
     * @param rhs right hand side
     * @return result
     */
    private int binop(final int lhs, final int rhs) {
        switch (operator) {
        case ADD:
            return lhs + rhs;
        case AND:
            return i(b(lhs) && b(rhs));
        case DIV:
            return lhs / rhs;
        case EQL:
            return i(lhs == rhs);
        case GT:
            return i(lhs > rhs);
        case GTE:
            return i(lhs >= rhs);
        case LT:
            return i(lhs < rhs);
        case LTE:
            return i(lhs <= rhs);
        case MUL:
            return lhs * rhs;
        case NEQ:
            return i(lhs != rhs);
        case OR:
            return i(b(lhs) || b(rhs));
        case SUB:
            return lhs - rhs;
        default:
            throw new RuntimeException();
        }
    }

    @Override
    public final void execute(final MaMa vm) throws ExecutionException {
        final Stack st = vm.getStack();
        final int rhs = st.pop().getIntValue();
        final int lhs = st.pop().getIntValue();
        final int result = binop(lhs, rhs);
        st.push(new BasicValueObject(result, null, null));
    }

    @Override
    public final String getDisplayHoverText() {
        return "Binary operation";
    }

    @Override
    public final String getMnemonic() {
        return "opbin";
    }

    @Override
    public final String getParameter() {
        return operator.toString();
    }

    /**
     * Convert boolean value to int.
     * 
     * @param b boolean value
     * @return 1 if b == 1, 0 otherwise
     */
    private int i(final boolean b) {
        if (b) {
            return 1;
        } else {
            return 0;
        }
    }
}
