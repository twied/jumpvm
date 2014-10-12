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

import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.Expression;
import jumpvm.ast.pama.Statement;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Assignment {@link Statement}.
 */
public class AssignmentStatement extends Statement {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Target. */
    private final Designator designator;

    /** Value. */
    private final Expression expression;

    /**
     * Create a new AssignmentStatement.
     *
     * @param begin begin
     * @param end end
     * @param designator left side
     * @param expression right side
     */
    public AssignmentStatement(final Location begin, final Location end, final Designator designator, final Expression expression) {
        super(begin, end);
        this.designator = designator;
        this.expression = expression;

        add(designator);
        add(expression);
    }

    /**
     * Returns the designator.
     *
     * @return the designator
     */
    public final Designator getDesignator() {
        return designator;
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
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Assignment";
    }
}
