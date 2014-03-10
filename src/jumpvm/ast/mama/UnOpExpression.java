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
 * Unary Operation {@link Expression}.
 */
public class UnOpExpression extends Expression {
    /** Unary Operator. */
    public static enum UnaryOperator {
        /** Logical not. */
        NOT,

        /** Arithmetic negation. */
        MINUS;

        /**
         * Returns the associated operator from given token or {@code null}.
         * 
         * @param token token
         * @return associated operator
         */
        public static final UnaryOperator fromToken(final MaMaToken token) {
            switch (token) {
            case TOKEN_EXCLAMATIONMARK:
                return NOT;
            case TOKEN_MINUS:
                return MINUS;
            default:
                return null;
            }
        }
    }

    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Operator. */
    private final UnaryOperator operator;

    /** Expression. */
    private final Expression expression;

    /**
     * Create a new UnOpExpression.
     * 
     * @param begin begin
     * @param end end
     * @param operator operator
     * @param expression expression
     */
    public UnOpExpression(final Location begin, final Location end, final UnaryOperator operator, final Expression expression) {
        super(begin, end);
        this.operator = operator;
        this.expression = expression;

        add(expression);
    }

    /**
     * Returns the expression.
     * 
     * @return the expression
     */
    public final Expression getExpression() {
        return expression;
    }

    /**
     * Returns the operator.
     * 
     * @return the operator
     */
    public final UnaryOperator getOperator() {
        return operator;
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
