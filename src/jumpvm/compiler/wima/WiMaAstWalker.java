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

import jumpvm.ast.wima.Atom;
import jumpvm.ast.wima.Clause;
import jumpvm.ast.wima.Numeral;
import jumpvm.ast.wima.Program;
import jumpvm.ast.wima.Query;
import jumpvm.ast.wima.Structure;
import jumpvm.ast.wima.Variable;
import jumpvm.compiler.AstWalker;
import jumpvm.exception.CompileException;

/**
 * WiMachine AST tree walker.
 */
public interface WiMaAstWalker extends AstWalker {
    /**
     * Process an {@link Atom} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Atom node) throws CompileException;

    /**
     * Process a {@link Clause} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Clause node) throws CompileException;

    /**
     * Process a {@link Numeral} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Numeral node) throws CompileException;

    /**
     * Process a {@link Program} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Program node) throws CompileException;

    /**
     * Process a {@link Query} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Query node) throws CompileException;

    /**
     * Process a {@link Structure} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Structure node) throws CompileException;

    /**
     * Process a {@link Variable} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Variable node) throws CompileException;
}
