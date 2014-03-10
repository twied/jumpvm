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

package jumpvm.compiler.mama;

import jumpvm.ast.mama.BinOpExpression;
import jumpvm.ast.mama.CallExpression;
import jumpvm.ast.mama.ConsExpression;
import jumpvm.ast.mama.HeadExpression;
import jumpvm.ast.mama.IdentifierExpression;
import jumpvm.ast.mama.IfExpression;
import jumpvm.ast.mama.IsNilExpression;
import jumpvm.ast.mama.LambdaExpression;
import jumpvm.ast.mama.LetrecExpression;
import jumpvm.ast.mama.ListExpression;
import jumpvm.ast.mama.NumeralExpression;
import jumpvm.ast.mama.Program;
import jumpvm.ast.mama.TailExpression;
import jumpvm.ast.mama.UnOpExpression;
import jumpvm.ast.mama.VariableDeclaration;
import jumpvm.compiler.AstWalker;
import jumpvm.exception.CompileException;

/**
 * MaMachine {@link AstWalker}.
 */
public interface MaMaAstWalker extends AstWalker {
    /**
     * Process a {@link BinOpExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(BinOpExpression node) throws CompileException;

    /**
     * Process a {@link CallExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(CallExpression node) throws CompileException;

    /**
     * Process a {@link ConsExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(ConsExpression node) throws CompileException;

    /**
     * Process a {@link HeadExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(HeadExpression node) throws CompileException;

    /**
     * Process an {@link IdentifierExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(IdentifierExpression node) throws CompileException;

    /**
     * Process an {@link IfExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(IfExpression node) throws CompileException;

    /**
     * Process an {@link IsNilExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(IsNilExpression node) throws CompileException;

    /**
     * Process a {@link LambdaExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(LambdaExpression node) throws CompileException;

    /**
     * Process a {@link LetrecExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(LetrecExpression node) throws CompileException;

    /**
     * Process a {@link ListExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(ListExpression node) throws CompileException;

    /**
     * Process a {@link NumeralExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(NumeralExpression node) throws CompileException;

    /**
     * Process a {@link Program} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(Program node) throws CompileException;

    /**
     * Process a {@link TailExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(TailExpression node) throws CompileException;

    /**
     * Process an {@link UnOpExpression} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(UnOpExpression node) throws CompileException;

    /**
     * Process a {@link VariableDeclaration} node.
     * 
     * @param node node
     * @throws CompileException on failure
     */
    void process(VariableDeclaration node) throws CompileException;
}
