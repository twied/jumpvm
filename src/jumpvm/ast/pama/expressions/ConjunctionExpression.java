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

/** Conjunction {@link Expression}. */
public class ConjunctionExpression extends Expression {
    /** Conjunction operators. */
    public static enum ConjunctionOperator {
        /** Logical or. */
        OR,

        /** Addition. */
        ADD,

        /** Subtraction. */
        SUB;

        /**
         * Get the corresponding ConjunctionOperator from a {@link PaMaToken}.
         *
         * @param token PaMaToken
         * @return the corresponding ConjunctionOperator or {@code null} if the token is not a ConjunctionOperator
         */
        public static ConjunctionOperator fromToken(final PaMaToken token) {
            switch (token) {
            case KEYWORD_OR:
                return OR;
            case TOKEN_PLUS:
                return ADD;
            case TOKEN_MINUS:
                return SUB;
            default:
                return null;
            }
        }
    }

    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Operator. */
    private final ConjunctionOperator operator;

    /** Left hand side expression. */
    private final Expression lhs;

    /** Right hand side expression. */
    private final Expression rhs;

    /**
     * Create a new ConjunctionExpression.
     *
     * @param begin begin
     * @param end end
     * @param operator operator
     * @param lhs left hand side expression
     * @param rhs right hand side expression
     */
    public ConjunctionExpression(final Location begin, final Location end, final ConjunctionOperator operator, final Expression lhs, final Expression rhs) {
        super(begin, end);
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;

        add(lhs);
        add(rhs);
    }

    /**
     * Returns the left hand side expression.
     *
     * @return the left hand side expression
     */
    public final Expression getLhs() {
        return lhs;
    }

    /**
     * Returns the operator.
     *
     * @return the operator
     */
    public final ConjunctionOperator getOperator() {
        return operator;
    }

    /**
     * Returns the right hand side expression.
     *
     * @return the right hand side expression
     */
    public final Expression getRhs() {
        return rhs;
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
