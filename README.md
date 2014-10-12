JumpVM
======

![screenshot](doc/screenshot.png "screenshot")

JumpVM is "The Java Unified Multi Paradigm Virtual Machine", a gui, parser, compiler and VM for several different programming languages and paradigms.

The main goal of this tool is to support the "Einführung in den Übersetzerbau" and "Übersetzung fortgeschrittener Sprachkonzepte" lectures of Ulm University.

The names and capabilities of the different languages (![PaMa](res/icon16/preferences-desktop-multimedia.png "PaMa") PaMa, ![MaMa](res/icon16/accessories-calculator.png "MaMa") MaMa, ![WiMa](res/icon16/internet-group-chat.png "WiMa") WiMa and ![BfMa](res/icon16/weather-storm.png "BfMa") BfMa) orient themselves toward the book "Übersetzerbau - Theorie, Konstruktion, Generierung" by R. Wilhelm and D. Maurer which is used in said lectures.


Getting started
---------------

* Open an example file: Select ≡ File → Example → MaMa → example01.mama
* Compile the program: Click on ![Compile](res/icon16/applications-system.png "Compile") or select ≡ Run → Compile
* Run the program: Click on ![Run](res/icon16/go-last.png "Run") or select ≡ Run → Run


Menus
-----

* File
    * ![New](res/icon16/document-new.png "New") **New:** Create a new VM of the given type.
    * ![Open](res/icon16/document-open.png "Open") **Open:** Create a new VM of the given type and read the source code from a file.
    * ![Example](res/icon16/applications-system.png "Example") **Example:** Create a new VM of the given type and load example source code.
    * ![Save](res/icon16/document-save.png "Save") **Save:** Save the source code of the current tab to a file.
    * ![Save As](res/icon16/document-save-as.png "Save As") **Save as:** Save the source code of the current tab to a different file.
    * ![Export](res/icon16/document-save-as.png "Export") **Export dot:** Export the source code's abstract syntax tree (given it compiles) as a [graphviz](http://www.graphviz.org/) "dot" file.
    * ![Export](res/icon16/document-save-as.png "Export") **Export asm:** Export the compiled mnemonics to a file.
    * ![Close](res/icon16/user-trash-full.png "Close") **Close tab:** Close the current tab.
    * ![Quit](res/icon16/system-log-out.png "Quit") **Quit:** Exit the program.
* Edit
	* ![Registers](res/icon16/preferences-desktop.png "Registers") **Registers:** Edit the values of the current VM's registers.
	* ![Memories](res/icon16/preferences-desktop.png "Memories") **Memories:** Edit the elements in the current VM's memory.
* Run
    * ![Compile](res/icon16/applications-system.png "Compile") **Compile:** Compile the source code into mnemonics fit to be run in the current VM.
    * ![Go backward](res/icon16/go-previous.png "Go backward") **Step backward:** Goes back one step on the current VM.
    * ![Go forward](res/icon16/go-next.png "Go forward") **Step forward:** Execute the next step on the current VM / start executing.
    * ![Run](res/icon16/go-last.png "Run") **Run:** Executes the VM in "fast forward" mode.
    * ![Reset](res/icon16/view-refresh.png "Reset") **Reset:** Reset the VM.
* Help
    * ![Help](res/icon16/help-browser.png "Help") **Help:** Help text.
    * ![About](res/icon16/dialog-information.png "About") **About:** Author and license information.


Toolbar
-------

* ![New](res/icon16/document-new.png "New") **New tab:** Create an empty tab.
* ![Close](res/icon16/user-trash-full.png "Close") **Close tab:** Same as ≡ File → Close tab.
* ![Save](res/icon16/document-save.png "Save") **Save:** Same as ≡ File → Save.
* ![Save As](res/icon16/document-save-as.png "Save As") **Save as:** Same as ≡ File → Save as.
* ![Compile](res/icon16/applications-system.png "Compile") **Compile:** Same as ≡ Run → Compile.
* ![Go backward](res/icon16/go-previous.png "Go backward") **Step backward:** Same as ≡ Run → Step backward.
* ![Go forward](res/icon16/go-next.png "Go forward") **Step forward:** Same as ≡ Run → Step forward.
* ![Run](res/icon16/go-last.png "Run") **Run:** Same as ≡ Run → Run. Shift + click to run without delay.
* ![Reset](res/icon16/view-refresh.png "Reset") **Reset:** Same as ≡ Run → Reset.
* ![Help](res/icon16/help-browser.png "Help") **Help:** Same as ≡ Help → Help.


Tabs
----

* **Registers:** A list of registers the current VM has. Move the cursor above a register to get its full name in the tooltip.
* **Program** / **Stack** / **Heap:** These are the different types of memory the current VM has. Once you compile a program you will find the compiled program in "Program" with the current instruction highlighted. The tooltip gives a short explanation of each instruction. Click on an instruction to highlight the originating node in the abstract syntax tree and text passage in the source code. Values in the "Stack" and "Heap" portions of memory show their type and value as well as a short description of the value's meaning.
* **Source:** You can enter your program source code here or load source code via ≡ File → Open. Use the ![Compile](res/icon16/applications-system.png "Compile") button in the toolbar to turn this sourcecode into mnemonics. 
* **Tree:** Once you compile a program, this area will show the program's abstract syntax tree. Click on a node to highlight the origin passage in the source code and the created instructions in the program memory from this node. 
* **Output:** Programs that generate output will display it here. 


Limits
------

* ![PaMa](res/icon16/preferences-desktop-multimedia.png "PaMa") PaMa
	* The PaMa doesn't do static arrays. All arrays are considered dynamic.
	* Case values must start at 0 and be continuous.
	* Input (`readln`) is not supported and does nothing.
* ![MaMa](res/icon16/accessories-calculator.png "MaMa") MaMa
    * The MaMa doesn't do variable reordering in letrec expressions which makes it fail on certain valid programs. See `example2.mama`.
* ![WiMa](res/icon16/internet-group-chat.png "WiMa") WiMa
    * The WiMa doesn't do lists, see `sublist.wima` for an example of explicit lists.
    * The WiMa doesn't do arithmetic evaluates, especially the predicate `is/2` is missing.
* ![BfMa](res/icon16/weather-storm.png "BfMa") BfMa
    * Only the positive half of the infinite band is accessible.
    * Input (character `,`) is not supported yet and does nothing.


License
-------
* Code: (c) 2013, 2014 Tim Wiederhake, licensed under GPLv3 or greater.
* Art: [Tango](http://tango.freedesktop.org/), licensed under CC0.
