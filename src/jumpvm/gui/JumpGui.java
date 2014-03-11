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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JToggleButton.ToggleButtonModel;

import jumpvm.Main;
import jumpvm.Main.VmType;
import jumpvm.exception.ExecutionException;

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

    /**
     * Create a new JumpTab for the given VmType.
     * 
     * @param type type of the VM
     * @return a new JumpTab
     */
    public static JumpTab tabFromVmType(final VmType type) {
        switch (type) {
        case BFMA:
            return new BfMaTab();

        case MAMA:
            return new MaMaTab();

        case PAMA:
            throw new RuntimeException();

        case WIMA:
            return new WiMaTab();

        default:
            /* does not happen */
            throw new IllegalArgumentException();
        }
    }

    /** GUI element: JumpTab container. */
    private final JTabbedPane tabbedPane;

    /** GUI element: file chooser. */
    private final JFileChooser fileChooser;

    /** If the VM is running in "fast forward" mode. */
    private final ToggleButtonModel runningToggle;

    /**
     * Create a new JumpGui.
     */
    public JumpGui() {
        super("JumpVM - " + Main.VERSION);
        fileChooser = new JFileChooser();
        runningToggle = new ToggleButtonModel();
        tabbedPane = new JTabbedPane();

        /* Initial empty tab. */
        actionNewTab();

        add(new JumpToolBar(this), BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        setJMenuBar(new JumpMenuBar(this));
        setSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setMinimumSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    /**
     * Action "about".
     */
    public final void actionAbout() {
        showHtmlDialog("/about.html");
    }

    /**
     * Action "close tab".
     */
    public final void actionCloseTab() {
        tabbedPane.remove(tabbedPane.getSelectedComponent());
    }

    /**
     * Action "compile".
     */
    public final void actionCompile() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionCompile();
    }

    /**
     * Action "export as asm".
     */
    public final void actionExportAsm() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionExportAsm(fileChooser);
    }

    /**
     * Action "export as dot".
     */
    public final void actionExportDot() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionExportDot(fileChooser);
    }

    /**
     * Action "help".
     */
    public final void actionHelp() {
        showHtmlDialog("/help.html");
    }

    /**
     * Action "new tab".
     * 
     * @return the new tab
     */
    public final Component actionNewTab() {
        final Component tab = new EmptyTab(this);
        tabbedPane.addTab(tab.getName(), EmptyTab.getIconSmall(), tab);
        tabbedPane.setSelectedComponent(tab);
        return tab;
    }

    /**
     * Action "new tab".
     * 
     * @param type type of the tab
     * @return the new tab
     */
    public final JumpTab actionNewTab(final VmType type) {
        final JumpTab tab = tabFromVmType(type);
        tabbedPane.addTab(tab.getName(), tab.getIconSmall(), tab);
        tabbedPane.setSelectedComponent(tab);
        return tab;
    }

    /**
     * Action "new tab".
     * 
     * @param type type of the tab
     * @param replace tab this tab replaces
     * @return the new tab
     */
    public final JumpTab actionNewVM(final VmType type, final Component replace) {
        tabbedPane.remove(replace);
        return actionNewTab(type);
    }

    /**
     * Action "open file".
     * 
     * @param type vm type
     */
    public final void actionOpen(final VmType type) {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        final JumpTab tab = actionNewTab(type);
        final File file = fileChooser.getSelectedFile();
        try {

            tab.setSource(new FileInputStream(file));
            tab.setAssociatedFile(file);
            setTitle(tab, file.getName());
        } catch (final FileNotFoundException e) {
            showExceptionDialog(this, e, "File could not be read.", "JumpVM error");
        }
    }

    /**
     * Action "quit program".
     */
    public final void actionQuit() {
        dispose();
    }

    /**
     * Action "reset vm".
     */
    public final void actionReset() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionReset();
    }

    /**
     * Action "save".
     */
    public final void actionSave() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        if (tab.getAssociatedFile() == null) {
            tab.actionSaveAs(fileChooser);
        } else {
            tab.actionSave();
        }

        if (tab.getAssociatedFile() != null) {
            setTitle(tab, tab.getAssociatedFile().getName());
        }
    }

    /**
     * Action "save as".
     */
    public final void actionSaveAs() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionSaveAs(fileChooser);
        if (tab.getAssociatedFile() != null) {
            setTitle(tab, tab.getAssociatedFile().getName());
        }
    }

    /**
     * Action "step backward".
     */
    public final void actionStepBackward() {
        runningToggle.setSelected(false);

        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        tab.actionStepBackward();
    }

    /**
     * Action "step forward".
     */
    public final void actionStepForward() {
        final JumpTab tab = getCurrentTab();
        if (tab == null) {
            return;
        }

        try {
            tab.actionStepForward();
        } catch (final ExecutionException e) {
            runningToggle.setSelected(false);
            showExceptionDialog(tab, e.getCause() != null ? e.getCause() : e, "The VM was unable to execute the instruction " + (e.getInstruction() == null ? "" : e.getInstruction().getMnemonic()), "JumpVM failure");
        }

        if (!tab.getVm().isRunning()) {
            runningToggle.setSelected(false);
        }
    }

    /**
     * Returns current {@link JumpTab} or null, if current tab is not a JumpTab.
     * 
     * @return current JumpTab
     */
    private JumpTab getCurrentTab() {
        final Component component = tabbedPane.getSelectedComponent();
        if (component instanceof JumpTab) {
            return (JumpTab) component;
        } else {
            return null;
        }
    }

    /**
     * Returns the "fast forward" toggle button model.
     * 
     * @return the "fast forward" toggle button model
     */
    public final ToggleButtonModel getRunningToggle() {
        return runningToggle;
    }

    /**
     * Set the title of the given tab.
     * 
     * @param tab JumpTab
     * @param text new title
     */
    public final void setTitle(final JumpTab tab, final String text) {
        for (int index = 0; index < tabbedPane.getTabCount(); ++index) {
            if (tab == tabbedPane.getComponentAt(index)) {
                tabbedPane.setTitleAt(index, text);
            }
        }
    }

    /**
     * Convenient method to present an html file in a dialog window.
     * 
     * @param file name of the resource
     */
    private void showHtmlDialog(final String file) {
    }
}