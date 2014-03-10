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

package jumpvm.tests;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import jumpvm.JumpVMTest;
import jumpvm.Main.VmType;
import jumpvm.code.Instruction;
import jumpvm.compiler.Token;
import jumpvm.compiler.bfma.BfMaCompiler;
import jumpvm.compiler.bfma.BfMaDotBackend;
import jumpvm.compiler.bfma.BfMaLexer;
import jumpvm.compiler.bfma.BfMaParser;
import jumpvm.compiler.bfma.BfMaToken;
import jumpvm.compiler.mama.MaMaToken;
import jumpvm.exception.CompileException;
import jumpvm.exception.ParseException;
import jumpvm.vm.BfMa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/** JUnit testcase for BfMa. */
@RunWith(Parameterized.class)
public class BfMaTest {
    /**
     * Returns the source files.
     * 
     * @return the source files.
     */
    @Parameterized.Parameters(name = "{0}")
    public static List<String[]> getParameters() {
        return JumpVMTest.getSourceFiles(VmType.BFMA);
    }

    /** Source file for this test. */
    private final String sourceFile;

    /**
     * Create a new BfMaTest.
     * 
     * @param sourceFile source file
     */
    public BfMaTest(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Create a compiler for the given source file and compile.
     * 
     * @return the compiler
     * @throws CompileException on failure
     * @throws ParseException on failure
     */
    private BfMaCompiler createCompiler() throws CompileException, ParseException {
        final BfMaCompiler compiler = new BfMaCompiler();
        final BfMaParser parser = createParser();
        compiler.processProgram(parser.parse());

        return compiler;
    }

    /**
     * Create a lexer for the given source file.
     * 
     * @return the lexer
     */
    private BfMaLexer createLexer() {
        return new BfMaLexer(JumpVMTest.getSourceReader(sourceFile));
    }

    /**
     * Create a parser for the given source file.
     * 
     * @return the parser
     */
    private BfMaParser createParser() {
        return new BfMaParser(createLexer());
    }

    /**
     * Create a vm for the given source file, compile and load.
     * 
     * @return the vm
     * @throws CompileException on failure
     * @throws ParseException on failure
     */
    private BfMa createVM() throws CompileException, ParseException {
        final ArrayList<Instruction> instructions = createCompiler().getInstructions();
        Assert.assertNotEquals(instructions.size(), 0);

        final BfMa vm = new BfMa();
        vm.reset(instructions);
        return vm;
    }

    /**
     * Returns the name of the expect file for the given source file and test name.
     * 
     * @param testName name of the current test
     * @return the name of the expect file
     */
    private File getExpectFile(final String testName) {
        return JumpVMTest.getExpectFile(testName, sourceFile);
    }

    /**
     * Test compiler.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testCompiler() throws Exception {
        final BfMaCompiler compiler = createCompiler();

        JumpVMTest.compare(getExpectFile("compiler"), JumpVMTest.toStrings(compiler));
    }

    /**
     * Test dot backend.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testDot() throws Exception {
        final BfMaParser parser = createParser();
        final BfMaDotBackend backend = new BfMaDotBackend();
        backend.process(parser.parse());

        JumpVMTest.compare(getExpectFile("dot"), backend.getContent());
    }

    /**
     * Test lexer.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testLexer() throws Exception {
        final BfMaLexer lexer = createLexer();

        final ArrayList<Token> tokenList = new ArrayList<Token>();
        while (true) {
            final Token token = lexer.nextToken();
            tokenList.add(token);
            if (token == BfMaToken.EOF) {
                break;
            }
        }

        Assert.assertNotEquals(tokenList.size(), 0);
        Assert.assertFalse(tokenList.contains(MaMaToken.UNKNOWN));

        JumpVMTest.compare(getExpectFile("lexer"), JumpVMTest.toStrings(tokenList));
    }

    /**
     * Test run.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testRun() throws Exception {
        final BfMa vm = createVM();

        final StringWriter stringWriter = new StringWriter();
        vm.setWriter(stringWriter);

        while (vm.isRunning()) {
            vm.step();
        }

        JumpVMTest.compare(getExpectFile("run"), stringWriter.toString());
    }
}
