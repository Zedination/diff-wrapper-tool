#!/usr/bin/bash
mvn clean install
jpackage --name DiffWrapperTool --input "D:\Source Code\JavaFX Project\diff-wrapper-tool\target" --module-path "D:\Java\openjfx-19.0.2.1_windows-x64_bin-jmods\javafx-jmods-19.0.2.1" --main-jar diff-wrapper-tool-1.0-SNAPSHOT-shaded.jar --main-class com.zedination.diffwrappertool.Main1 --type exe --win-shortcut --win-menu