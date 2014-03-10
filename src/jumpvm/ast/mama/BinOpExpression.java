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

package jumpvm.ast.mama;

import jumpvm.compiler.Location;
import jumpvm.compiler.mama.MaMaAstWalker;
import jumpvm.compiler.mama.MaMaToken;
import jumpvm.exception.CompileException;

/**
 * Binary Operation {@link Expression}.
 */
public class BinOpExpression extends Expression {
    /** Binary Operator. */
    public static enum BinaryOperator {
        /** Addition. */
        ADD,

        /** Subtraction. */
        SUB,

        /** Division. */
        DIV,

        /** Multiplication. */
        MUL,

        /** Equals. */
        EQL,

        /** Not equal. */
        NEQ,

        /** Greater than. */
        GT,

        /** Greater than or equal to. */
        GTE,

        /** Less than. */
        LT,

        /** Less than or equal to. */
        LTE,

        /** And. */
        AND,

        /** Or. */
        OR;

        /**
         * Returns the associated operator from given token or {@code null}.
         * 
         * @param token token
         * @return associated operator
         */
        public static BinaryOperator fromToken(final MaMaToken token) {
            switch (token) {
            case TOKEN_PLUS:
                return ADD;
            case TOKEN_MINUS:
                return SUB;
            case TOKEN_SLASH:
                return DIV;
            case TOKEN_STAR:
                return MUL;
            case TOKEN_EQUALS:
                return EQL;
            case TOKEN_NOTEQUAL:
                return NEQ;
            case TOKEN_GREATER:
                return GT;
            case TOKEN_GREATEREQUAL:
                return GTE;
            case TOKEN_LESS:
                return LT;
            case TOKEN_LESSEQUAL:
                return LTE;
            case TOKEN_AND:
                return AND;
            case TOKEN_OR:
                return OR;
            default:
                return null;
            }
        }
    }

    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Operator. */
    private final BinaryOperator operator;

    /** Left-hand side expression. */
    private final Expression lhs;

    /** Right-hand side expression. */
    private final Expression rhs;

    /**
     * Create a new BinOpExpression.
     * 
     * @param begin begin
     * @param end end
     * @param operator operator
     * @param lhs left-hand side expression
     * @param rhs right-hand side expression
     */
    public BinOpExpression(final Location begin, final Location end, final BinaryOperator operator, final Expression lhs, final Expression rhs) {
        super(begin, end);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;

        add(lhs);
        add(rhs);
    }

    /**
     * Returns the left-hand side expression.
     * 
     * @return the left-hand side expression
     */
    public final Expression getLhs() {
        return lhs;
    }

    /**
     * Returns the operator.
     * 
     * @return the operator
     */
    public final BinaryOperator getOperator() {
        return operator;
    }

    /**
     * Returns the right-hand side expression.
     * 
     * @return the right-hand side expression
     */
    public final Expression getRhs() {
        return rhs;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return operator.toString();
    }
}
