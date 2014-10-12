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
import java.util.ArrayList;
import java.util.List;

import jumpvm.JumpVMTest;
import jumpvm.Main.VmType;
import jumpvm.compiler.Token;
import jumpvm.compiler.pama.PaMaLexer;
import jumpvm.compiler.pama.PaMaToken;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/** JUnit testcase for PaMa. */
@RunWith(Parameterized.class)
public class PaMaTest {
    /**
     * Returns the source files.
     *
     * @return the source files.
     */
    @Parameterized.Parameters(name = "{0}")
    public static List<String[]> getParameters() {
        return JumpVMTest.getSourceFiles(VmType.PAMA);
    }

    /** Source file for this test. */
    private final String sourceFile;

    /**
     * Create a new PaMaTest.
     *
     * @param sourceFile source file
     */
    public PaMaTest(final String sourceFile) {
        this.sourceFile = sourceFile;
    }

    /**
     * Create a lexer for the given source file.
     *
     * @return the lexer
     */
    private PaMaLexer createLexer() {
        return new PaMaLexer(JumpVMTest.getSourceReader(sourceFile));
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
     * Test lexer.
     *
     * @throws Exception on failure
     */
    @Test(timeout = JumpVMTest.TIMEOUT)
    public final void testLexer() throws Exception {
        final PaMaLexer lexer = createLexer();

        final ArrayList<Token> tokenList = new ArrayList<Token>();
        while (true) {
            final Token token = lexer.nextToken();
            tokenList.add(token);
            if (token == PaMaToken.EOF) {
                break;
            }
        }

        Assert.assertNotEquals(tokenList.size(), 0);
        Assert.assertFalse(tokenList.contains(PaMaToken.UNKNOWN));

        JumpVMTest.compare(getExpectFile("lexer"), JumpVMTest.toStrings(tokenList));
    }
}
