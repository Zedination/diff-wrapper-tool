#!/usr/bin/bash
rm diff-wrapper-tool-1.0-SNAPSHOT-shaded.jar
mvn clean install
cp target/diff-wrapper-tool-1.0-SNAPSHOT-shaded.jar diff-wrapper-tool-1.0-SNAPSHOT-shaded.jar
jpackage --name "Diff Wrapper Tool" --input "D:\Source Code\JavaFX Project\diff-wrapper-tool\target" --vendor Zedination --main-jar diff-wrapper-tool-1.0-SNAPSHOT-shaded.jar --main-class com.zedination.diffwrappertool.Main1 --type exe --win-shortcut --win-menu