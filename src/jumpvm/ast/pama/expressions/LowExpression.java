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

import jumpvm.ast.pama.Designator;
import jumpvm.ast.pama.Expression;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** Low {@link Expression}. */
public class LowExpression extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Target array. */
    private final Designator designator;

    /** Dimension to query. */
    private final int dimension;

    /**
     * Create a new LowExpression.
     *
     * @param begin begin
     * @param end end
     * @param designator target array
     * @param dimension dimension to query
     */
    public LowExpression(final Location begin, final Location end, final Designator designator, final int dimension) {
        super(begin, end);
        this.designator = designator;
        this.dimension = dimension;

        add(designator);
    }

    /**
     * Returns the target array.
     *
     * @return the target array
     */
    public final Designator getDesignator() {
        return designator;
    }

    /**
     * Returns the dimension to query.
     *
     * @return the dimension to query
     */
    public final int getDimension() {
        return dimension;
    }

    @Override
    public final int getMaxStackSize() {
        return Math.max(designator.getMaxStackSize(), 2);
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Low " + dimension;
    }
}
