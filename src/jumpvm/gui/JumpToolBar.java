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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.Timer;

import jumpvm.Main;

/**
 * JumpToolBar.
 */
public class JumpToolBar extends JToolBar implements ActionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Main gui. */
    private final JumpGui gui;

    /** Button "File -> New -> Empty". */
    private final JButton fileNewButton;

    /** Button "File -> Close tab". */
    private final JButton fileCloseButton;

    /** Button "File -> Save". */
    private final JButton fileSaveButton;

    /** Button "File -> Save as". */
    private final JButton fileSaveAsButton;

    /** Button "Run -> Compile". */
    private final JButton runCompileButton;

    /** Button "Run -> Step backward". */
    private final JButton runStepBackwardButton;

    /** Button "Run -> Step forward". */
    private final JButton runStepForwardButton;

    /** Button "Run -> Run". */
    private final JToggleButton runRunButton;

    /** Button "Run -> Reset". */
    private final JButton runResetButton;

    /** Button "Help -> Help". */
    private final JButton helpHelpButton;

    /** Timer for the "fast forward" mode. */
    private final Timer timer;

    /**
     * Create a new JumpToolBar.
     * 
     * @param gui main gui
     */
    public JumpToolBar(final JumpGui gui) {
        this.gui = gui;
        this.timer = new Timer(JumpGui.MILLISECONDS_PER_RUNSTEP, this);

        fileNewButton = button("document-new", "New tab");
        fileCloseButton = button("user-trash", "Close tab");
        fileSaveButton = button("document-save", "Save");
        fileSaveAsButton = button("document-save-as", "Save as");
        addSeparator();
        runCompileButton = button("applications-system", "Compile");
        runStepBackwardButton = button("go-previous", "Step backward");
        runStepForwardButton = button("go-next", "Step forward");
        runRunButton = buttonToggle("go-last", "Run");
        runResetButton = button("view-refresh", "Reset");
        add(Box.createHorizontalGlue());
        helpHelpButton = button("help-browser", "Help");

        runRunButton.setModel(gui.getRunningToggle());
        timer.setInitialDelay(0);
        timer.setCoalesce(true);
        timer.start();
        setFloatable(false);
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source == fileNewButton) {
            gui.actionNewTab();
        } else if (source == fileCloseButton) {
            gui.actionCloseTab();
        } else if (source == fileSaveButton) {
            gui.actionSave();
        } else if (source == fileSaveAsButton) {
            gui.actionSaveAs();
        } else if (source == runCompileButton) {
            gui.actionCompile();
        } else if (source == runStepForwardButton) {
            gui.actionStepForward();
        } else if (source == runStepBackwardButton) {
            gui.actionStepBackward();
        } else if (source == runRunButton) {
            return;
        } else if (source == runResetButton) {
            gui.actionReset();
        } else if (source == helpHelpButton) {
            gui.actionHelp();
        } else if (source == timer) {
            if (runRunButton.isSelected()) {
                gui.actionStepForward();
            }
        }
    }

    /**
     * Prepare a button for the tool bar.
     * 
     * @param resource name of the icon
     * @param tipText tool tip text
     * @return prepared button
     */
    private JButton button(final String resource, final String tipText) {
        final JButton button = new JButton();
        button.addActionListener(this);
        button.setToolTipText(tipText);
        button.setIcon(Main.getImageIconResource("/icon32/" + resource + ".png"));
        button.setFocusable(false);
        add(button);
        return button;
    }

    /**
     * Prepare a toggle button for the tool bar.
     * 
     * @param resource name of the icon
     * @param tipText tool tip text
     * @return prepared button
     */
    private JToggleButton buttonToggle(final String resource, final String tipText) {
        final JToggleButton button = new JToggleButton();
        button.addActionListener(this);
        button.setToolTipText(tipText);
        button.setIcon(Main.getImageIconResource("/icon32/" + resource + ".png"));
        button.setFocusable(false);
        add(button);
        return button;
    }
}
