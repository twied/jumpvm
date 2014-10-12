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

import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Writeln {@link Statement}. */
public class WritelnStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Output. */
    private final Expression expression;

    /**
     * Create a new WritelnStatement.
     *
     * @param begin begin
     * @param end end
     * @param expression output
     */
    public WritelnStatement(final Location begin, final Location end, final Expression expression) {
        super(begin, end);
        this.expression = expression;

        add(expression);
    }

    /**
     * Returns the output expression.
     *
     * @return the output expression
     */
    public final Expression getExpression() {
        return expression;
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
        return "Writeln";
    }
}
