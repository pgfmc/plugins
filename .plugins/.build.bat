@ECHO off

FOR %%n in (Backup, Bot, Core, Market, Masterbook, ModTools, Survival, Teleport) DO FOR /R E:\github\%%n\src %%i IN (*.java) DO javac -verbose -cp E:\github\.plugins/* -d E:\github\%%n\bin %%i
FOR %%n in (Backup, Bot, Core, Market, Masterbook, ModTools, Survival, Teleport) DO jar cfv %%n.jar -C E:\github\%%n\bin .
FOR /R E:\github\teams\src %%i IN (*.java) DO javac -verbose -cp E:\github\.plugins/* -d E:\github\teams\bin %%i
jar cfv Teams.jar -C E:\github\teams\bin .