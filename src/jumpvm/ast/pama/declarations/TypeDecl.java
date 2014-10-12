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

package jumpvm.ast.pama.declarations;

import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa type declaration. */
public class TypeDecl extends PaMaAstNode {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Type name. */
    private final String identifier;

    /** Type specification. */
    private final Type type;

    /**
     * Create a new TypeDecl.
     *
     * @param begin begin
     * @param end end
     * @param identifier type name
     * @param type type specification
     */
    public TypeDecl(final Location begin, final Location end, final String identifier, final Type type) {
        super(begin, end);
        this.identifier = identifier;
        this.type = type;

        add(type);
    }

    /**
     * Returns the type name.
     * 
     * @return the type name
     */
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the type specification.
     * 
     * @return the type specification
     */
    public final Type getType() {
        return type;
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
