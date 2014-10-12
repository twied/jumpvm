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

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import jumpvm.Main;
import jumpvm.Main.VmType;

/**
 * Empty tab for selecting a new {@link JumpTab}.
 */
public class EmptyTab extends JPanel implements ActionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /**
     * Returns a 32x32 px icon for this tab.
     * 
     * @return a 32x32 px icon for this tab
     */
    public static ImageIcon getIconBig() {
        return Main.getImageIconResource("/icon32/" + "document-new.png");
    }

    /**
     * Returns a 16x16 px icon for this tab.
     * 
     * @return a 16x16 px icon for this tab
     */
    public static ImageIcon getIconSmall() {
        return Main.getImageIconResource("/icon16/" + "document-new.png");
    }

    /** Main gui. */
    private final JumpGui gui;

    /** Create new PaMachine button. */
    private final JButton newPaMaButton;

    /** Create new MaMachine button. */
    private final JButton newMaMaButton;

    /** Create new WiMachine button. */
    private final JButton newWiMaButton;

    /** Create new BfMachine button. */
    private final JButton newBfMaButton;

    /**
     * Create a new EmptyTab.
     * 
     * @param gui main gui
     */
    public EmptyTab(final JumpGui gui) {
        setLayout(new GridLayout(2, 2, JumpGui.DEFAULT_SPACING, JumpGui.DEFAULT_SPACING));
        setName("New VM");

        this.gui = gui;

        this.newPaMaButton = button("PaMachine", "An imperative", "Pascal", "preferences-desktop-multimedia");
        this.newMaMaButton = button("MaMachine", "A functional", "Haskell", "accessories-calculator");
        this.newWiMaButton = button("WiMachine", "A logical", "Prolog", "internet-group-chat");
        this.newBfMaButton = button("BfMachine", "An esoteric", "Brainfuck", "weather-storm");
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source == newPaMaButton) {
            gui.actionNewVM(VmType.PAMA, this);
        } else if (source == newMaMaButton) {
            gui.actionNewVM(VmType.MAMA, this);
        } else if (source == newWiMaButton) {
            gui.actionNewVM(VmType.WIMA, this);
        } else if (source == newBfMaButton) {
            gui.actionNewVM(VmType.BFMA, this);
        }
    }

    /**
     * Prepare "New *-Machine" button.
     * 
     * @param name name of the machine
     * @param type programming paradigm
     * @param similar language paragon
     * @param icon icon
     * @return prepared button
     */
    private JButton button(final String name, final String type, final String similar, final String icon) {
        final JButton button = new JButton();
        button.setText("<html><center>Create new " + name + "<br><br>" + type + " language similar to " + similar + "</center></html>");
        button.setIcon(Main.getImageIconResource("/icon32/" + icon + ".png"));
        button.addActionListener(this);
        button.setVerticalTextPosition(SwingConstants.BOTTOM);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setFocusPainted(false);
        add(button);
        return button;
    }
}
