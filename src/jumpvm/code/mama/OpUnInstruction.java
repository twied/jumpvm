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

import jumpvm.ast.mama.MaMaAstNode;
import jumpvm.ast.mama.UnOpExpression.UnaryOperator;

/**
 * Unary operation.
 * 
 * <pre>
 * ST[SP] := Operator ST[SP];
 * </pre>
 */
public class OpUnInstruction extends MaMaInstruction {
    /** Operator. */
    private final UnaryOperator operator;

    /**
     * Create a new OpUn instruction.
     * 
     * @param sourceNode AstNode that is responsible for this instruction's creation
     * @param operator operator
     */
    public OpUnInstruction(final MaMaAstNode sourceNode, final UnaryOperator operator) {
        super(sourceNode);
        this.operator = operator;
    }

    @Override
    public final String getDisplayHoverText() {
        return "Unary operation";
    }

    @Override
    public final String getMnemonic() {
        return "opun";
    }

    @Override
    public final String getParameter() {
        return operator.toString();
    }

    /**
     * Calculate the result of the unary expression.
     * 
     * @param rhs right hand side
     * @return result
     */
    private int unop(final int rhs) {
        switch (operator) {
        case MINUS:
            return -rhs;
        case NOT:
            if (rhs == 0) {
                return 1;
            } else {
                return 0;
            }
        default:
            throw new RuntimeException();
        }
    }
}
