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

package jumpvm.ast.bfma;

import jumpvm.compiler.Location;
import jumpvm.compiler.bfma.BfMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Plus {@link BfMaAstNode}.
 */
public class Plus extends BfMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new Plus.
     * 
     * @param begin begin
     * @param end end
     */
    public Plus(final Location begin, final Location end) {
        super(begin, end);
    }

    @Override
    public final void process(final BfMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Plus";
    }
}
