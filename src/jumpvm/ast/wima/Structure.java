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

import java.util.ArrayList;

import jumpvm.compiler.Location;
import jumpvm.compiler.wima.WiMaAstWalker;
import jumpvm.exception.CompileException;

/**
 * Structure.
 */
public class Structure extends Predicate {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Atom. */
    private final Atom atom;

    /** Operands. */
    private final ArrayList<Term> termList;

    /**
     * Create a new Structure.
     * 
     * @param begin begin in resource
     * @param end end in resource
     * @param atom atom
     * @param termList operands
     */
    public Structure(final Location begin, final Location end, final Atom atom, final ArrayList<Term> termList) {
        super(begin, end);
        this.atom = atom;
        this.termList = termList;

        for (final Term term : termList) {
            add(term);
        }
    }

    @Override
    public final int getArity() {
        return termList.size();
    }

    /**
     * Returns the atom.
     * 
     * @return the atom
     */
    public final Atom getAtom() {
        return atom;
    }

    @Override
    public final String getIdentifier() {
        return atom.getIdentifier();
    }

    /**
     * Returns the operands.
     * 
     * @return the operands
     */
    public final ArrayList<Term> getTermList() {
        return termList;
    }

    @Override
    public final void process(final WiMaAstWalker treewalker) throws CompileException {
        treewalker.process(this);
    }

    @Override
    public final String toString() {
        return "Structure " + atom.getIdentifier() + "/" + termList.size();
    }
}
