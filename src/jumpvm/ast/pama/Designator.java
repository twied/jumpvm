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

import java.util.ArrayList;

import jumpvm.compiler.Location;
import jumpvm.compiler.pama.PaMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * A Designator.
 *
 * Variable or parameter or function / procedure name including specifiers like array indices or indirections etc.
 */
public class Designator extends Expression {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Variable name. */
    private final String identifier;

    /** Designator parts. */
    private final ArrayList<DesignatorPart> designatorPartList;

    /**
     * Createa a new Designator.
     *
     * @param begin begin
     * @param end end
     * @param identifier variable name
     * @param designatorPartList designator parts
     */
    public Designator(final Location begin, final Location end, final String identifier, final ArrayList<DesignatorPart> designatorPartList) {
        super(begin, end);
        this.identifier = identifier;
        this.designatorPartList = designatorPartList;

        for (final DesignatorPart designatorPart : designatorPartList) {
            add(designatorPart);
        }
    }

    /**
     * Returns the list of designator parts.
     *
     * @return the list of designator parts
     */
    public final ArrayList<DesignatorPart> getDesignatorPartList() {
        return designatorPartList;
    }

    /**
     * Returns the full name of the variable including all designator parts.
     *
     * @return the full name of the variable including all designator parts
     */
    public final String getFullIdentifier() {
        final StringBuilder stringBuilder = new StringBuilder(identifier);
        for (final DesignatorPart designatorPart : designatorPartList) {
            stringBuilder.append(designatorPart.toString());
        }
        return stringBuilder.toString();
    }

    /**
     * Returns the variable name.
     *
     * @return the variable name
     */
    public final String getIdentifier() {
        return identifier;
    }

    @Override
    public final void process(final PaMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return identifier + "[" + getType() + "]";
    }
}
