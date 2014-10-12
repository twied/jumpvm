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

/** Formal parameter to a PaMa function / procedure. */
public class FormalParameter extends PaMaAstNode implements NamedNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** If this parameter is a reference. */
    private final boolean reference;

    /** Parameter name. */
    private final String identifier;

    /** Parameter type. */
    private final Type type;

    /**
     * Create a new FormalParameter.
     *
     * @param begin begin
     * @param end end
     * @param reference is reference?
     * @param identifier parameter name
     * @param type parameter type
     */
    public FormalParameter(final Location begin, final Location end, final boolean reference, final String identifier, final Type type) {
        super(begin, end);
        this.reference = reference;
        this.identifier = identifier;
        this.type = type;

        add(type);
    }

    @Override
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the parameter type.
     * 
     * @return the parameter type
     */
    public final Type getType() {
        return type;
    }

    /**
     * Returns if the parameter is a reference.
     * 
     * @return if the parameter is a reference
     */
    public final boolean isReference() {
        return reference;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return identifier;
    }
}
