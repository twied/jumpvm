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

import jumpvm.ast.AstNode;
import jumpvm.ast.wima.Atom;
import jumpvm.ast.wima.Clause;
import jumpvm.ast.wima.Numeral;
import jumpvm.ast.wima.Predicate;
import jumpvm.ast.wima.Program;
import jumpvm.ast.wima.Query;
import jumpvm.ast.wima.Structure;
import jumpvm.ast.wima.Term;
import jumpvm.ast.wima.Variable;
import jumpvm.compiler.DotBackend;
import jumpvm.exception.CompileException;

/**
 * WiMachine {@link DotBackend}.
 */
public class WiMaDotBackend extends DotBackend implements WiMaAstWalker {
    /**
     * Create a new WiMaDotBackend.
     */
    public WiMaDotBackend() {
        super(true);
    }

    @Override
    public final void process(final Atom node) {
        emitNode(node, node.getIdentifier());
    }

    @Override
    public final void process(final Clause node) throws CompileException {
        emitNode(node, "Clause");

        node.getHead().process(this);
        emitEdge(node, node.getHead(), "Head");

        if (node.getBody().size() == 0) {
            final Object object = new Object();
            emitNode(object, "Always");
            emitEdge(node, object, "Body");
        } else {
            for (final Predicate predicate : node.getBody()) {
                predicate.process(this);
                emitEdge(node, predicate, "Body");
            }
        }
    }

    @Override
    public final void process(final Numeral node) {
        emitNode(node, node.getIdentifier());
    }

    @Override
    public final void process(final Program node) throws CompileException {
        begin();
        for (final Clause clause : node.getClauseList()) {
            clause.process(this);
            emitEdge(ROOT, clause);
        }

        node.getQuery().process(this);
        emitEdge(ROOT, node.getQuery());
        end();
    }

    @Override
    public final void process(final Query node) throws CompileException {
        emitNode(node, "Query");
        for (final Predicate predicate : node.getPredicateList()) {
            predicate.process(this);
            emitEdge(node, predicate);
        }
    }

    @Override
    public final void process(final Structure node) throws CompileException {
        emitNode(node, node.getAtom().getIdentifier());

        for (final Term term : node.getTermList()) {
            term.process(this);
            emitEdge(node, term);
        }
    }

    @Override
    public final void process(final Variable node) {
        emitNode(node, node.getIdentifier());
    }

    @Override
    public final void processProgram(final AstNode<?> program) throws CompileException {
        try {
            process((Program) program);
        } catch (final RuntimeException e) {
            throw new CompileException(program, e);
        }
    }
}
