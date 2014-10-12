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

import jumpvm.ast.pama.NamedNode;
import jumpvm.ast.pama.PaMaAstNode;
import jumpvm.ast.pama.Type;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa variable declaration. */
public class VarDecl extends PaMaAstNode implements NamedNode {
    /** Function / procedure parameters get added to the declarations as variables for convenience. The {@link VarDecl#source} field keeps track of its origin. */
    public static enum Source {
        /** This is a formal variable. */
        LOCAL_DECLARATION,

        /** This is a value parameter. */
        VALUE_PARAMETER,

        /** This is a reference parameter. */
        REFERENCE_PARAMETER
    }

    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;;

    /** Variable name. */
    private final String identifier;

    /** Variable type. */
    private final Type type;

    /** Origin of this declaration. */
    private final Source source;

    /**
     * Create a new VarDecl with source = LOCAL_DECLARATION.
     *
     * @param begin begin
     * @param end end
     * @param identifier variable name
     * @param type variable type
     */
    public VarDecl(final Location begin, final Location end, final String identifier, final Type type) {
        this(begin, end, identifier, type, Source.LOCAL_DECLARATION);
    }

    /**
     * Create a new VarDecl.
     *
     * @param begin begin
     * @param end end
     * @param identifier variable name
     * @param type variable type
     * @param source declaration origin
     */
    public VarDecl(final Location begin, final Location end, final String identifier, final Type type, final Source source) {
        super(begin, end);
        this.identifier = identifier;
        this.type = type;
        this.source = source;

        add(type);
    }

    @Override
    public final String getIdentifier() {
        return identifier;
    }

    /**
     * Returns this declaration's origin.
     * 
     * @return this declaration's origin
     */
    public final Source getSource() {
        return source;
    }

    /**
     * Returns the variable type.
     *
     * @return the variable type
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
