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

import java.util.ArrayList;

import jumpvm.compiler.Location;
import jumpvm.compiler.mama.MaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * List {@link Expression}.
 */
public class ListExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** List elements. */
    private final ArrayList<Expression> expressions;

    /**
     * Create a new ListExpression.
     * 
     * @param begin begin
     * @param end end
     * @param expressions list elements
     */
    public ListExpression(final Location begin, final Location end, final ArrayList<Expression> expressions) {
        super(begin, end);
        this.expressions = expressions;

        if (expressions != null) {
            for (final Expression expression : expressions) {
                add(expression);
            }
        }
    }

    /**
     * Returns the list elements.
     * 
     * @return the list elements
     */
    public final ArrayList<Expression> getExpressions() {
        return expressions;
    }

    @Override
    public final void process(final MaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "List";
    }
}
