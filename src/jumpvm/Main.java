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

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import jumpvm.gui.JumpGui;

/**
 * JumpVM entry point.
 * 
 * Also, this class pools all resource access.
 */
public final class Main {
    /** Types of JumpVM. */
    public static enum VmType {
        /** BfMachine. */
        BFMA,

        /** MaMachine. */
        MAMA,

        /** PaMachine. */
        PAMA,

        /** WiMachine. */
        WIMA;
    }

    /** JumpVM version. */
    public static final String VERSION = "0.5";

    /**
     * Convenience method to read an ImageIcon resource.
     * 
     * @param name name of the resource
     * @return the resource as ImageIcon
     */
    public static ImageIcon getImageIconResource(final String name) {
        return new ImageIcon(getResource(name));
    }

    /**
     * Convenience method to get a resource.
     * 
     * @param name name of the resource
     * @return the resource as URL
     */
    public static URL getResource(final String name) {
        return Main.class.getResource(name);
    }

    /**
     * Convenience method to get a resource as Stream.
     * 
     * @see {@link Class#getResourceAsStream(String)}
     * @param name name of the resource
     * @return {@link InputStream} or null
     */
    public static InputStream getResourceAsStream(final String name) {
        return Main.class.getResourceAsStream(name);
    }

    /**
     * Convenience method to read a text file resource.
     * 
     * @param name name of the resource
     * @return the content of the file or an empty {@link ArrayList} on failure.
     */
    public static ArrayList<String> getResourceAsStringArray(final String name) {
        final ArrayList<String> content = new ArrayList<String>();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(getResourceAsStream(name)));
        try {
            while (true) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                content.add(line);
            }
        } catch (final IOException e) {
            return new ArrayList<String>();
        }

        return content;
    }

    /**
     * JumpVM entry point.
     * 
     * @param args arguments (ignored)
     */
    public static void main(final String... args) {
        final Runnable createGui = new Runnable() {
            @Override
            public void run() {
                final JumpGui jumpGui = new JumpGui();
                jumpGui.setVisible(true);
            }
        };

        EventQueue.invokeLater(createGui);
    }

    /**
     * Not meant for instantiation.
     */
    private Main() {
    }
}
