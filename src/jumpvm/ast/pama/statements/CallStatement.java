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

package jumpvm.ast.pama.statements;

import java.util.ArrayList;

import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.Statement;
import jumpvm.ast.pama.expressions.CallExpression;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Call {@link Statement}. */
public class CallStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** See {@link CallExpression}. */
    private final CallExpression expression;

    /**
     * Create a new CallStatement.
     *
     * @param begin begin
     * @param end end
     * @param identifier function name
     * @param expressionList arguments
     */
    public CallStatement(final Location begin, final Location end, final String identifier, final ArrayList<Expression> expressionList) {
        super(begin, end);
        expression = new CallExpression(begin, end, identifier, expressionList);
        add(expression);
    }

    /**
     * Returns the argument expressions.
     *
     * @return the argument expressions
     */
    public final ArrayList<Expression> getExpressionList() {
        return expression.getExpressionList();
    }

    /**
     * Returns the function name.
     *
     * @return the function name
     */
    public final String getIdentifier() {
        return expression.getIdentifier();
    }

    @Override
    public final int getMaxStackSize() {
        return expression.getMaxStackSize();
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return expression.getIdentifier();
    }
}
