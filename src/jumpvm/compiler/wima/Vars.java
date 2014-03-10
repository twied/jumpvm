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

package jumpvm.compiler.wima;

import java.util.ArrayList;

import jumpvm.ast.wima.Atom;
import jumpvm.ast.wima.Clause;
import jumpvm.ast.wima.Numeral;
import jumpvm.ast.wima.Predicate;
import jumpvm.ast.wima.Program;
import jumpvm.ast.wima.Query;
import jumpvm.ast.wima.Structure;
import jumpvm.ast.wima.Term;
import jumpvm.ast.wima.Variable;
import jumpvm.ast.wima.WiMaAstNode;
import jumpvm.exception.CompileException;

/**
 * List of all variables in the given node.
 */
public class Vars extends ArrayList<String> implements WiMaAstWalker {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Create a new list of variables for the given WiMa AST node.
     * 
     * @param node AST node
     * @throws CompileException on failure
     */
    public Vars(final WiMaAstNode node) throws CompileException {
        node.process(this);
    }

    @Override
    public final void process(final Atom node) throws CompileException {
        return;
    }

    @Override
    public final void process(final Clause node) throws CompileException {
        node.getHead().process(this);
        for (final Predicate predicate : node.getBody()) {
            predicate.process(this);
        }
    }

    @Override
    public final void process(final Numeral node) throws CompileException {
        return;
    }

    @Override
    public final void process(final Program node) throws CompileException {
        node.getQuery().process(this);
        for (final Clause clause : node.getClauseList()) {
            clause.process(this);
        }
    }

    @Override
    public final void process(final Query node) throws CompileException {
        for (final Predicate predicate : node.getPredicateList()) {
            predicate.process(this);
        }
    }

    @Override
    public final void process(final Structure node) throws CompileException {
        node.getAtom().process(this);
        for (final Term term : node.getTermList()) {
            term.process(this);
        }
    }

    @Override
    public final void process(final Variable node) throws CompileException {
        if (!contains(node.getIdentifier())) {
            add(node.getIdentifier());
        }
    }
}
