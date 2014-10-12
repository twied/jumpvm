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

import java.util.ArrayList;

import jumpvm.ast.pama.Type;
import jumpvm.ast.pama.declarations.VarDecl;
import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/** PaMa record type. */
public class RecordType extends Type {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** List of elements. */
    private final ArrayList<VarDecl> varDeclList;

    /**
     * Create a new RecordType.
     *
     * @param begin begin
     * @param end end
     * @param varDeclList list of elements
     */
    public RecordType(final Location begin, final Location end, final ArrayList<VarDecl> varDeclList) {
        super(begin, end);
        this.varDeclList = varDeclList;

        for (final VarDecl varDecl : varDeclList) {
            add(varDecl);
        }
    }

    @Override
    public final Type getResolvedType() {
        return this;
    }

    @Override
    public final int getSize() {
        int size = 0;
        for (final VarDecl varDecl : varDeclList) {
            size += varDecl.getType().getSize();
        }
        return size;
    }

    /**
     * Return the element declaration with the given name.
     *
     * @param identifier name
     * @return the element declaration or {@null} if there is no such declaration
     */
    public final VarDecl getVarDecl(final String identifier) {
        for (final VarDecl varDecl : varDeclList) {
            if (varDecl.getIdentifier().equals(identifier)) {
                return varDecl;
            }
        }
        return null;
    }

    /**
     * Returns the list of elements.
     *
     * @return the list of elements
     */
    public final ArrayList<VarDecl> getVarDeclList() {
        return varDeclList;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Record";
    }
}
