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

package jumpvm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import jumpvm.Main.VmType;
import jumpvm.compiler.LocatedReader;
import jumpvm.tests.BfMaTest;
import jumpvm.tests.MaMaTest;
import jumpvm.tests.WiMaTest;

import org.junit.Assert;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/** JumpVM test suite. */
@RunWith(Suite.class)
@SuiteClasses({BfMaTest.class, MaMaTest.class, WiMaTest.class})
public final class JumpVMTest {
    /** Maximum processing time in milliseconds. */
    public static final int TIMEOUT = 5000;

    /**
     * Compare a list of Strings to the content of a file.
     * 
     * @param expectFile file
     * @param actual list of Strings
     * @throws IOException on failure to read the file
     */
    public static void compare(final File expectFile, final ArrayList<String> actual) throws IOException {
        final ArrayList<String> expected = JumpVMTest.readLines(expectFile);

        Assert.assertNotNull(expected);
        Assert.assertNotEquals(expected.size(), 0);
        /* Eclipse junit plugin handles assertEquals(String, String) specially and provides a fancy "diff" dialog. */
        Assert.assertEquals(String.valueOf(expected).replace(",", "\n"), String.valueOf(actual).replace(",", "\n"));
    }

    /**
     * Compare a String to the content of a file.
     * 
     * @param expectFile file
     * @param actual String
     * @throws IOException failure to read the file
     */
    public static void compare(final File expectFile, final String actual) throws IOException {
        final Scanner scanner = new Scanner(expectFile);
        scanner.useDelimiter("\\Z");
        final String expected = scanner.next();
        scanner.close();
        Assert.assertNotNull(expected);
        Assert.assertNotEquals(expected.length(), 0);
        Assert.assertEquals(expected, actual);
    }

    /**
     * Returns the file with the expected test results.
     * 
     * @param testName name of the test
     * @param sourceFile name of the source code file
     * @return the file with the expected test results
     */
    public static File getExpectFile(final String testName, final String sourceFile) {
        return new File("test/jumpvm/expect/" + testName + "_" + sourceFile);
    }

    /**
     * Returns the list of example code filenames for the given VmType.
     * 
     * @param type type
     * @return the list of example code filenames for the given VmType
     */
    public static List<String[]> getSourceFiles(final VmType type) {
        final ArrayList<String> sourceFiles = Main.getResourceAsStringArray("/source/" + type);
        final String[][] result = new String[sourceFiles.size()][1];
        for (int i = 0; i < result.length; ++i) {
            result[i][0] = sourceFiles.get(i);
        }
        return Arrays.asList(result);
    }

    /**
     * Returns a LocatedReader instance for the given file.
     * 
     * @param file source code file
     * @return a LocatedReader instance for the given file
     */
    public static LocatedReader getSourceReader(final String file) {
        final InputStream inputStream = Main.getResourceAsStream("/source/" + file);
        Assert.assertNotNull(inputStream);
        return new LocatedReader(new InputStreamReader(inputStream), file);
    }

    /**
     * Reads the content of a file into an ArrayList of Strings.
     * 
     * @param file file to be read
     * @return file content line by line
     * @throws IOException on failure
     */
    public static ArrayList<String> readLines(final File file) throws IOException {
        final ArrayList<String> content = new ArrayList<String>();
        final BufferedReader reader = new BufferedReader(new FileReader(file));
        while (true) {
            final String line = reader.readLine();
            if (line == null) {
                reader.close();
                return content;
            }
            content.add(line);
        }
    }

    /**
     * Turn an ArrayList of Objects into an ArrayList of Strings.
     * 
     * @param list ArrayList of Objects
     * @return ArrayList of Strings
     */
    public static ArrayList<String> toStrings(final ArrayList<?> list) {
        final ArrayList<String> strings = new ArrayList<String>();
        for (final Object o : list) {
            strings.add(String.valueOf(o));
        }
        return strings;
    }

    /**
     * Writes an ArrayList of Objects to a file.
     * 
     * @param file target file
     * @param objects ArrayList of Objects
     * @throws IOException on failure
     */
    public static void writeLines(final File file, final ArrayList<?> objects) throws IOException {
        System.err.println("Modified: " + file);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        for (final Object object : objects) {
            writer.write(String.valueOf(object));
            writer.write("\n");
        }
        writer.close();
    }

    /** Utility class. */
    private JumpVMTest() {
    }
}
