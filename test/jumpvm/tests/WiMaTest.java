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
import jumpvm.compiler.wima.WiMaCompiler;
import jumpvm.compiler.wima.WiMaDotBackend;
import jumpvm.compiler.wima.WiMaLexer;
import jumpvm.compiler.wima.WiMaParser;
import jumpvm.compiler.wima.WiMaToken;
import jumpvm.exception.CompileException;
import jumpvm.exception.ParseException;
import jumpvm.vm.WiMa;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/** JUnit testcase for WiMa. */
@RunWith(Parameterized.class)
public class WiMaTest {
    /**
     * Returns the source files.
     * 
     * @return the source files.
     */
    @Parameterized.Parameters(name = "{0}")
    public static List<String[]> getParameters() {
        return JumpVMTest.getSourceFiles(VmType.WIMA);
    }

    /** Source file for this test. */
    private final String sourceFile;

    /**
     * Create a new WiMaTest.
     * 
     * @param sourceFile source file
     */
    public WiMaTest(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Create a compiler for the given source file and compile.
     * 
     * @return the compiler
     * @throws CompileException on failure
     * @throws ParseException on failure
     */
    private WiMaCompiler createCompiler() throws CompileException, ParseException {
        final WiMaCompiler compiler = new WiMaCompiler();
        final WiMaParser parser = createParser();
        compiler.processProgram(parser.parse());

        return compiler;
    }

    /**
     * Create a lexer for the given source file.
     * 
     * @return the lexer
     */
    private WiMaLexer createLexer() {
        return new WiMaLexer(JumpVMTest.getSourceReader(sourceFile));
    }

    /**
     * Create a parser for the given source file.
     * 
     * @return the parser
     */
    private WiMaParser createParser() {
        return new WiMaParser(createLexer());
    }

    /**
     * Create a vm for the given source file, compile and load.
     * 
     * @return the vm
     * @throws CompileException on failure
     * @throws ParseException on failure
     */
    private WiMa createVM() throws CompileException, ParseException {
        final ArrayList<Instruction> instructions = createCompiler().getInstructions();
        Assert.assertNotEquals(instructions.size(), 0);

        final WiMa vm = new WiMa();
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
        final WiMaCompiler compiler = createCompiler();
        JumpVMTest.compare(getExpectFile("compiler"), JumpVMTest.toStrings(compiler));
    }

    /**
     * Test dot backend.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testDot() throws Exception {
        final WiMaParser parser = createParser();
        final WiMaDotBackend backend = new WiMaDotBackend();
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
        final WiMaLexer lexer = createLexer();

        final ArrayList<Token> tokenList = new ArrayList<Token>();
        while (true) {
            final Token token = lexer.nextToken();
            tokenList.add(token);
            if (token == WiMaToken.EOF) {
                break;
            }
        }

        Assert.assertNotEquals(tokenList.size(), 0);
        Assert.assertFalse(tokenList.contains(WiMaToken.UNKNOWN));

        JumpVMTest.compare(getExpectFile("lexer"), JumpVMTest.toStrings(tokenList));
    }

    /**
     * Test run.
     * 
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testRun() throws Exception {
        final WiMa vm = createVM();

        final StringWriter stringWriter = new StringWriter();
        vm.setWriter(stringWriter);

        while (vm.isRunning()) {
            vm.step();
        }

        JumpVMTest.compare(getExpectFile("run"), stringWriter.toString());
    }
}
