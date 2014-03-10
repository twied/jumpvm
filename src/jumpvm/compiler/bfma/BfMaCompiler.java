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

import jumpvm.ast.AstNode;
import jumpvm.ast.bfma.BfMaAstNode;
import jumpvm.ast.bfma.Input;
import jumpvm.ast.bfma.Left;
import jumpvm.ast.bfma.Loop;
import jumpvm.ast.bfma.Minus;
import jumpvm.ast.bfma.Output;
import jumpvm.ast.bfma.Plus;
import jumpvm.ast.bfma.Program;
import jumpvm.ast.bfma.Right;
import jumpvm.code.bfma.BackwardInstruction;
import jumpvm.code.bfma.DecPtrInstruction;
import jumpvm.code.bfma.DecValInstruction;
import jumpvm.code.bfma.ForwardInstruction;
import jumpvm.code.bfma.HaltInstruction;
import jumpvm.code.bfma.IncPtrInstruction;
import jumpvm.code.bfma.IncValInstruction;
import jumpvm.code.bfma.InputInstruction;
import jumpvm.code.bfma.OutputInstruction;
import jumpvm.compiler.Compiler;
import jumpvm.exception.CompileException;
import jumpvm.memory.Label;

/**
 * BfMachine {@link Compiler}.
 */
public class BfMaCompiler extends Compiler implements BfMaAstWalker {
    /**
     * Create a new BfMaCompiler.
     */
    public BfMaCompiler() {
    }

    @Override
    public final void process(final Input node) throws CompileException {
        emit(new InputInstruction(node));
    }

    @Override
    public final void process(final Left node) throws CompileException {
        emit(new DecPtrInstruction(node));
    }

    @Override
    public final void process(final Loop node) throws CompileException {
        final Label begin = new Label("begin");
        final Label end = new Label("end");

        emit(new ForwardInstruction(node, end));
        begin.setAddress(getCurrentPosition());

        for (final BfMaAstNode character : node.getCharacters()) {
            character.process(this);
        }

        emit(new BackwardInstruction(node, begin));
        end.setAddress(getCurrentPosition());
    }

    @Override
    public final void process(final Minus node) throws CompileException {
        emit(new DecValInstruction(node));
    }

    @Override
    public final void process(final Output node) throws CompileException {
        emit(new OutputInstruction(node));
    }

    @Override
    public final void process(final Plus node) throws CompileException {
        emit(new IncValInstruction(node));
    }

    @Override
    public final void process(final Program node) throws CompileException {
        for (final BfMaAstNode character : node.getCharacters()) {
            character.process(this);
        }
        emit(new HaltInstruction(node));
    }

    @Override
    public final void process(final Right node) throws CompileException {
        emit(new IncPtrInstruction(node));
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
