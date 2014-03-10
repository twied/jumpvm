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

package jumpvm.compiler.bfma;

import jumpvm.ast.bfma.Input;
import jumpvm.ast.bfma.Left;
import jumpvm.ast.bfma.Loop;
import jumpvm.ast.bfma.Minus;
import jumpvm.ast.bfma.Output;
import jumpvm.ast.bfma.Plus;
import jumpvm.ast.bfma.Program;
import jumpvm.ast.bfma.Right;
import jumpvm.compiler.AstWalker;
import jumpvm.exception.CompileException;

/**
 * BfMachine {@link AstWalker}.
 */
public interface BfMaAstWalker extends AstWalker {
    /**
     * Process an {@link Input} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Input node) throws CompileException;

    /**
     * Process a {@link Left} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Left node) throws CompileException;

    /**
     * Process a {@link Loop} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Loop node) throws CompileException;

    /**
     * Process a {@link Minus} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Minus node) throws CompileException;

    /**
     * Process an {@link Output} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Output node) throws CompileException;

    /**
     * Process a {@link Plus} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Plus node) throws CompileException;

    /**
     * Process a {@link Program} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Program node) throws CompileException;

    /**
     * Process a {@link Right} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(final Right node) throws CompileException;
}
