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

package jumpvm.ast.pama.designatorpart;

import java.util.ArrayList;

import jumpvm.ast.pama.DesignatorPart;
import jumpvm.ast.pama.Expression;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** List access designator part. */
public class ListAccessDesignatorPart extends DesignatorPart {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Minimum designator size. */
    private static final int MIN_DESIGNATOR_SIZE = 1 + /* lda address */1 + /* dpl */1 /* ldc 0 */;

    /** List of expressions. */
    private final ArrayList<Expression> expressionList;

    /**
     * Create a new ListAccessDesignatorPart.
     *
     * @param begin begin
     * @param end end
     * @param expressionList list of expressions
     */
    public ListAccessDesignatorPart(final Location begin, final Location end, final ArrayList<Expression> expressionList) {
        super(begin, end);
        this.expressionList = expressionList;

        for (final Expression expression : expressionList) {
            add(expression);
        }
    }

    /**
     * Returns the list of expressions.
     *
     * @return the list of expressions
     */
    public final ArrayList<Expression> getExpressionList() {
        return expressionList;
    }

    @Override
    public final int getMaxStackSize() {

        int size = MIN_DESIGNATOR_SIZE;
        for (final Expression expression : expressionList) {
            size = Math.max(size, expression.getMaxStackSize() + MIN_DESIGNATOR_SIZE);
        }
        return size;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "[]";
    }
}
