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

package jumpvm.ast.pama;

import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** The range of a dimension in an array. */
public class Range extends PaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Lower bound. */
    private final int low;

    /** Upper bound. */
    private final int high;

    /**
     * Create a new Range.
     *
     * @param begin begin
     * @param end end
     * @param low lower bound
     * @param high upper bound
     */
    public Range(final Location begin, final Location end, final int low, final int high) {
        super(begin, end);
        this.low = low;
        this.high = high;
    }

    /**
     * Returns the upper bound.
     * 
     * @return the upper bound
     */
    public final int getHigh() {
        return high;
    }

    /**
     * Returns the lower bound.
     * 
     * @return the lower bound
     */
    public final int getLow() {
        return low;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
    }

    @Override
    public final String toString() {
        return String.valueOf(low) + " .. " + String.valueOf(high);
    }

}
