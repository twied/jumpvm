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
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ButtonModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import jumpvm.Main;
import jumpvm.Main.VmType;

/**
 * JumpVM menu bar.
 */
public class JumpMenuBar extends JMenuBar implements ActionListener {
    /** Default serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /** Main gui. */
    private final JumpGui gui;

    /** List of examples in the "File -> Example" menu with their corresponding VM type. */
    private final HashMap<JMenuItem, VmType> exampleList;

    /** Menu "File". */
    private final JMenu fileMenu;

    /** Menu "File -> New". */
    private final JMenu fileNewMenu;

    /** Menu item "File -> New -> Empty". */
    private final JMenuItem fileNewEmptyItem;

    /** Menu item "File -> New -> PaMa". */
    private final JMenuItem fileNewPaMaItem;

    /** Menu item "File -> New -> MaMa". */
    private final JMenuItem fileNewMaMaItem;

    /** Menu item "File -> New -> WiMa". */
    private final JMenuItem fileNewWiMaItem;

    /** Menu item "File -> New -> BfMa". */
    private final JMenuItem fileNewBfMaItem;

    /** Menu "File -> Open". */
    private final JMenu fileOpenMenu;

    /** Menu item "File -> Open -> PaMa". */
    private final JMenuItem fileOpenPaMaItem;

    /** Menu item "File -> Open -> MaMa". */
    private final JMenuItem fileOpenMaMaItem;

    /** Menu item "File -> Open -> WiMa". */
    private final JMenuItem fileOpenWiMaItem;

    /** Menu item "File -> Open -> BfMa". */
    private final JMenuItem fileOpenBfMaItem;

    /** Menu "File -> Example". */
    private final JMenu fileExampleMenu;

    /** Menu "File -> Example -> PaMa". */
    private final JMenu fileExamplePaMaMenu;

    /** Menu "File -> Example -> MaMa". */
    private final JMenu fileExampleMaMaMenu;

    /** Menu "File -> Example -> WiMa". */
    private final JMenu fileExampleWiMaMenu;

    /** Menu "File -> Example -> BfMa". */
    private final JMenu fileExampleBfMaMenu;

    /** Menu item "File -> Close tab". */
    private final JMenuItem fileCloseItem;

    /** Menu item "File -> Save". */
    private final JMenuItem fileSaveItem;

    /** Menu item "File -> Save as". */
    private final JMenuItem fileSaveAsItem;

    /** Menu item "File -> Export dot". */
    private final JMenuItem fileExportDotItem;

    /** Menu item "File -> Export asm". */
    private final JMenuItem fileExportAsmItem;

    /** Menu item "File -> Quit". */
    private final JMenuItem fileQuitItem;

    /** Menu "Edit". */
    private final JMenu editMenu;

    /** Menu item "Edit -> Registers". */
    private final JMenuItem editRegistersItem;

    /** Menu item "Edit -> Memories". */
    private final JMenuItem editMemoriesItem;

    /** Menu item "Edit -> Export state". */
    private final JMenuItem editExportStateItem;

    /** Menu "Run". */
    private final JMenu runMenu;

    /** Menu item "Run -> Compile". */
    private final JMenuItem runCompileItem;

    /** Menu item "Run -> Step backward". */
    private final JMenuItem runStepBackwardItem;

    /** Menu item "Run -> Step forward". */
    private final JMenuItem runStepForwardItem;

    /** Menu item "Run -> Run". */
    private final JCheckBoxMenuItem runRunItem;

    /** Menu item "Run -> Reset". */
    private final JMenuItem runResetItem;

    /** Menu "Help". */
    private final JMenu helpMenu;

    /** Menu item "Help -> Help". */
    private final JMenuItem helpHelpItem;

    /** Menu item "Help -> About". */
    private final JMenuItem helpAboutItem;

    /**
     * Create a new JumpMenuBar.
     * 
     * @param gui main gui
     */
    public JumpMenuBar(final JumpGui gui) {
        this.gui = gui;
        this.exampleList = new HashMap<JMenuItem, VmType>();

        fileMenu = new JMenu("File");

        fileNewMenu = menu("New", "document-new");
        fileNewEmptyItem = item("Empty", "document-new");
        fileNewEmptyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        fileNewPaMaItem = item("PaMa", "preferences-desktop-multimedia");
        fileNewMaMaItem = item("MaMa", "accessories-calculator");
        fileNewWiMaItem = item("WiMa", "internet-group-chat");
        fileNewBfMaItem = item("BfMa", "weather-storm");

        fileOpenMenu = menu("Open", "document-open");
        fileOpenPaMaItem = item("PaMa", "preferences-desktop-multimedia");
        fileOpenMaMaItem = item("MaMa", "accessories-calculator");
        fileOpenWiMaItem = item("WiMa", "internet-group-chat");
        fileOpenBfMaItem = item("BfMa", "weather-storm");

        fileExampleMenu = menu("Example", "applications-system");
        fileExamplePaMaMenu = menu("PaMa", "preferences-desktop-multimedia");
        fileExampleMaMaMenu = menu("MaMa", "accessories-calculator");
        fileExampleWiMaMenu = menu("WiMa", "internet-group-chat");
        fileExampleBfMaMenu = menu("BfMa", "weather-storm");

        fileCloseItem = item("Close tab", "user-trash-full");
        fileCloseItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK));

        fileSaveItem = item("Save", "document-save");
        fileSaveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
        fileSaveAsItem = item("Save as", "document-save-as");
        fileExportDotItem = item("Export dot", "document-save-as");
        fileExportAsmItem = item("Export asm", "document-save-as");
        fileQuitItem = item("Quit", "system-log-out");
        fileQuitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));

        editMenu = new JMenu("Edit");
        editRegistersItem = item("Registers", "preferences-desktop");
        editMemoriesItem = item("Memories", "preferences-desktop");
        editExportStateItem = item("Export state", "document-save-as");

        runMenu = new JMenu("Run");

        runCompileItem = item("Compile", "applications-system");
        runStepBackwardItem = item("Step backward", "go-previous");
        runStepForwardItem = item("Step forward", "go-next");
        runRunItem = item("Run", "go-last", gui.getRunningToggle());
        runResetItem = item("Reset", "view-refresh");

        helpMenu = new JMenu("Help");

        helpHelpItem = item("Help", "help-browser");
        helpHelpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0));
        helpAboutItem = item("About", "dialog-information");

        fileNewMenu.add(fileNewEmptyItem);
        fileNewMenu.add(fileNewPaMaItem);
        fileNewMenu.add(fileNewMaMaItem);
        fileNewMenu.add(fileNewWiMaItem);
        fileNewMenu.add(fileNewBfMaItem);

        fileOpenMenu.add(fileOpenPaMaItem);
        fileOpenMenu.add(fileOpenMaMaItem);
        fileOpenMenu.add(fileOpenWiMaItem);
        fileOpenMenu.add(fileOpenBfMaItem);

        fileExampleMenu.add(fileExamplePaMaMenu);
        fileExampleMenu.add(fileExampleMaMaMenu);
        fileExampleMenu.add(fileExampleWiMaMenu);
        fileExampleMenu.add(fileExampleBfMaMenu);

        example(fileExamplePaMaMenu, VmType.PAMA);
        example(fileExampleMaMaMenu, VmType.MAMA);
        example(fileExampleWiMaMenu, VmType.WIMA);
        example(fileExampleBfMaMenu, VmType.BFMA);

        fileMenu.add(fileNewMenu);
        fileMenu.add(fileOpenMenu);
        fileMenu.add(fileExampleMenu);
        fileMenu.addSeparator();
        fileMenu.add(fileSaveItem);
        fileMenu.add(fileSaveAsItem);
        fileMenu.add(fileExportDotItem);
        fileMenu.add(fileExportAsmItem);
        fileMenu.addSeparator();
        fileMenu.add(fileCloseItem);
        fileMenu.addSeparator();
        fileMenu.add(fileQuitItem);

        editMenu.add(editRegistersItem);
        editMenu.add(editMemoriesItem);
        editMenu.addSeparator();
        editMenu.add(editExportStateItem);

        runMenu.add(runCompileItem);
        runMenu.addSeparator();
        runMenu.add(runStepBackwardItem);
        runMenu.add(runStepForwardItem);
        runMenu.add(runRunItem);
        runMenu.addSeparator();
        runMenu.add(runResetItem);

        helpMenu.add(helpHelpItem);
        helpMenu.add(helpAboutItem);

        add(fileMenu);
        add(editMenu);
        add(runMenu);
        add(helpMenu);
    }

    @Override
    public final void actionPerformed(final ActionEvent e) {
        final Object source = e.getSource();
        if (source == fileNewEmptyItem) {
            gui.actionNewTab();
        } else if (source == fileNewPaMaItem) {
            gui.actionNewTab(VmType.PAMA);
        } else if (source == fileNewMaMaItem) {
            gui.actionNewTab(VmType.MAMA);
        } else if (source == fileNewWiMaItem) {
            gui.actionNewTab(VmType.WIMA);
        } else if (source == fileNewBfMaItem) {
            gui.actionNewTab(VmType.BFMA);
        } else if (source == fileOpenPaMaItem) {
            gui.actionOpen(VmType.PAMA);
        } else if (source == fileOpenMaMaItem) {
            gui.actionOpen(VmType.MAMA);
        } else if (source == fileOpenWiMaItem) {
            gui.actionOpen(VmType.WIMA);
        } else if (source == fileOpenBfMaItem) {
            gui.actionOpen(VmType.BFMA);
        } else if (source == fileCloseItem) {
            gui.actionCloseTab();
        } else if (source == fileSaveItem) {
            gui.actionSave();
        } else if (source == fileSaveAsItem) {
            gui.actionSaveAs();
        } else if (source == fileExportDotItem) {
            gui.actionExportDot();
        } else if (source == fileExportAsmItem) {
            gui.actionExportAsm();
        } else if (source == fileQuitItem) {
            gui.actionQuit();
        } else if (source == editRegistersItem) {
            gui.actionEditRegisters();
        } else if (source == editMemoriesItem) {
            gui.actionEditMemories();
        } else if (source == editExportStateItem) {
            gui.actionExportState();
        } else if (source == runCompileItem) {
            gui.actionCompile();
        } else if (source == runStepForwardItem) {
            gui.actionStepForward();
        } else if (source == runStepBackwardItem) {
            gui.actionStepBackward();
        } else if (source == runResetItem) {
            gui.actionReset();
        } else if (source == helpHelpItem) {
            gui.actionHelp();
        } else if (source == helpAboutItem) {
            gui.actionAbout();
        } else if (exampleList.keySet().contains(source)) {
            final JMenuItem item = (JMenuItem) source;
            final VmType type = exampleList.get(source);
            final JumpTab tab = gui.actionNewTab(type);
            tab.setSource(Main.getResourceAsStream("/source/" + item.getText()));
            gui.setTitle(tab, item.getText());
        }
    }

    /**
     * Create the example entries in the "File -> Example -> *" menues.
     * 
     * @param menu parent menu
     * @param type VM type
     */
    private void example(final JMenu menu, final VmType type) {
        final ArrayList<String> sourceFiles = Main.getResourceAsStringArray("/source/" + type);

        for (final String sourceFile : sourceFiles) {
            final JMenuItem item = new JMenuItem(sourceFile);
            item.addActionListener(this);

            menu.add(item);
            exampleList.put(item, type);
        }
    }

    /**
     * Create a new JMenuItem with the given text and icon.
     * 
     * @param name button text
     * @param icon icon name
     * @return new JMenuItem
     */
    private JMenuItem item(final String name, final String icon) {
        final JMenuItem menuItem = new JMenuItem();
        menuItem.setText(name);
        menuItem.addActionListener(this);
        menuItem.setIcon(Main.getImageIconResource("/icon16/" + icon + ".png"));
        return menuItem;
    }

    /**
     * Create a new JCheckBoxMenuItem with the given text, icon and model.
     * 
     * @param name button text
     * @param icon icon name
     * @param buttonModel button model
     * @return new JMenuItem
     */
    private JCheckBoxMenuItem item(final String name, final String icon, final ButtonModel buttonModel) {
        final JCheckBoxMenuItem menuItem = new JCheckBoxMenuItem();
        menuItem.setText(name);
        menuItem.addActionListener(this);
        menuItem.setIcon(Main.getImageIconResource("/icon16/" + icon + ".png"));
        menuItem.setModel(buttonModel);
        return menuItem;
    }

    /**
     * Create a new JMenu with the given text and icon.
     * 
     * @param name button text
     * @param icon icon name
     * @return new JMenu
     */
    private JMenu menu(final String name, final String icon) {
        final JMenu menu = new JMenu();
        menu.setText(name);
        menu.setIcon(Main.getImageIconResource("/icon16/" + icon + ".png"));
        return menu;
    }

    /** Update GUI elements. */
    public final void update() {
        if (gui.getTabCount() == 0) {
            fileCloseItem.setEnabled(false);
        } else {
            fileCloseItem.setEnabled(true);
        }

        final JumpTab currentTab = gui.getCurrentTab();
        if (currentTab == null) {
            fileSaveItem.setEnabled(false);
            fileSaveAsItem.setEnabled(false);
            fileExportAsmItem.setEnabled(false);
            fileExportDotItem.setEnabled(false);
            editMemoriesItem.setEnabled(false);
            editRegistersItem.setEnabled(false);
            editExportStateItem.setEnabled(false);
            runCompileItem.setEnabled(false);
            runStepForwardItem.setEnabled(false);
            runStepBackwardItem.setEnabled(false);
            runRunItem.setEnabled(false);
            runResetItem.setEnabled(false);
        } else {
            fileSaveItem.setEnabled(true);
            fileSaveAsItem.setEnabled(true);
            runCompileItem.setEnabled(true);
            if (currentTab.getVm().getProgram().getSize() == 0) {
                fileExportAsmItem.setEnabled(false);
                fileExportDotItem.setEnabled(false);
                editMemoriesItem.setEnabled(false);
                editRegistersItem.setEnabled(false);
                editExportStateItem.setEnabled(false);
                runStepForwardItem.setEnabled(false);
                runStepBackwardItem.setEnabled(false);
                runRunItem.setEnabled(false);
                runResetItem.setEnabled(false);
            } else {
                fileExportAsmItem.setEnabled(true);
                fileExportDotItem.setEnabled(true);
                editMemoriesItem.setEnabled(true);
                editRegistersItem.setEnabled(true);
                editExportStateItem.setEnabled(true);
                runStepForwardItem.setEnabled(true);
                runStepBackwardItem.setEnabled(true);
                runRunItem.setEnabled(true);
                runResetItem.setEnabled(true);
            }
        }
    }
}
