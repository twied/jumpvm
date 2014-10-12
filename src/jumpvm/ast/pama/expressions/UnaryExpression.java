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

package jumpvm.ast.pama.expressions;

import jumpvm.ast.pama.Expression;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.compiler.pama.PaMaToken;
import jumpvm.exception.CompileException;

/** Unary {@link Expression}. */
public class UnaryExpression extends Expression {
    /** Unary operators. */
    public static enum UnaryOperator {
        /** Logical not. */
        NOT,

        /** Negation. */
        NEG;

        /**
         * Get the corresponding UnaryOperator from a {@link PaMaToken}.
         *
         * @param token PaMaToken
         * @return the corresponding UnaryOperator or {@code null} if the token is not a UnaryOperator
         */
        public static UnaryOperator fromToken(final PaMaToken token) {
            switch (token) {
            case KEYWORD_NOT:
                return NOT;
            case TOKEN_MINUS:
                return NEG;
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
     * Create a new UnaryExpression.
     *
     * @param begin begin
     * @param end end
     * @param operator operator
     * @param expression expression
     */
    public UnaryExpression(final Location begin, final Location end, final UnaryOperator operator, final Expression expression) {
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

    @Override
    public final int getMaxStackSize() {
        return expression.getMaxStackSize();
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
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return String.valueOf(operator);
    }
}
