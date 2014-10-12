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

/** PaMa custom type. */
public class CustomType extends Type {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Name of the type. */
    private final String identifier;

    /** Resolved type. */
    private Type resolvedType;

    /**
     * Create a new CustomType.
     *
     * @param begin begin
     * @param end end
     * @param identifier name of the type
     */
    public CustomType(final Location begin, final Location end, final String identifier) {
        super(begin, end);
        this.identifier = identifier;
    }

    /**
     * Returns the name of the type.
     *
     * @return the name of the type
     */
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public final Type getResolvedType() {
        return resolvedType;
    }

    @Override
    public final int getSize() {
        return resolvedType.getSize();
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    /**
     * Sets the resolved type.
     * 
     * @param resolvedType the resolved type
     */
    public final void setResolvedType(final Type resolvedType) {
        this.resolvedType = resolvedType;
    }

    @Override
    public final String toString() {
        return identifier + "(" + resolvedType + ")";
    }
}
