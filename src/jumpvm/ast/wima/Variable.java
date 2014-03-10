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

package jumpvm.ast.wima;

import jumpvm.compiler.Location;
import jumpvm.compiler.wima.WiMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Variable.
 */
public class Variable extends Term {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Identifier. */
    private final String identifier;

    /**
     * Create a new Variable.
     * 
     * @param begin begin in resource
     * @param end end in resource
     * @param identifier identifier
     */
    public Variable(final Location begin, final Location end, final String identifier) {
        super(begin, end);
        this.identifier = identifier;
    }

    /**
     * Returns the identifier.
     * 
     * @return the identifier
     */
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public final void process(final WiMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return identifier;
    }
}
