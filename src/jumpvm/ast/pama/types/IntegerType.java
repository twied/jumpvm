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

package jumpvm.ast.pama.types;

import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa integer type. */
public class IntegerType extends Type {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new IntegerType.
     * 
     * @param begin begin
     * @param end end
     */
    public IntegerType(final Location begin, final Location end) {
        super(begin, end);
    }

    @Override
    public final Type getResolvedType() {
        return this;
    }

    @Override
    public final int getSize() {
        return 1;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Integer";
    }
}
