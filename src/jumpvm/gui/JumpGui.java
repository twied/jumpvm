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

package jumpvm.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton.ToggleButtonModel;

import jumpvm.Main;

/**
 * Main gui.
 */
public class JumpGui extends JFrame {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Speed of the "fast forward" mode. */
    public static final int MILLISECONDS_PER_RUNSTEP = 100;

    /** Default spacing for all gui elements. */
    public static final int DEFAULT_SPACING = 5;

    /** Default monospaced font. */
    public static final Font FONT_MONOSPACED = new Font(Font.MONOSPACED, 0, 12);

    /** Bold monospaced font. */
    public static final Font FONT_BOLDMONOSPACED = new Font(Font.MONOSPACED, Font.BOLD, 12);

    /** Window width. */
    private static final int WINDOW_WIDTH = 800;

    /** Window height. */
    private static final int WINDOW_HEIGHT = 600;

    /**
     * Convenient method to present an Exception in a dialog window.
     * 
     * @param parent parent component
     * @param throwable exception
     * @param message message
     * @param title title
     */
    public static final void showExceptionDialog(final Component parent, final Throwable throwable, final Object message, final String title) {
        final String[] strings = {"OK", "Details"};

        final int result = JOptionPane.showOptionDialog(parent, message, title, JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE, null, strings, strings[0]);

        if (result != 1) {
            return;
        }

        final CharArrayWriter writer = new CharArrayWriter();
        throwable.printStackTrace(new PrintWriter(writer));

        JOptionPane.showMessageDialog(parent, new JScrollPane(new JTextArea(writer.toString())), title, JOptionPane.ERROR_MESSAGE);
    }

    /** GUI element: JumpTab container. */
    private final JTabbedPane tabbedPane;

    /** If the VM is running in "fast forward" mode. */
    private final ToggleButtonModel runningToggle;

    /**
     * Create a new JumpGui.
     */
    public JumpGui() {
        super("JumpVM - " + Main.VERSION);
        runningToggle = new ToggleButtonModel();
        tabbedPane = new JTabbedPane();

        add(new JumpToolBar(this), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setJMenuBar(new JumpMenuBar(this));
        setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Returns the "fast forward" toggle button model.
     * 
     * @return the "fast forward" toggle button model
     */
    public final ToggleButtonModel getRunningToggle() {
        return runningToggle;
    }
}
