@ECHO off

FOR %%n in (Backup, Bot, Core, Market, Masterbook, ModTools, Survival, Teleport) DO FOR /R E:\github\%%n\src %%i IN (*.java) DO javac -verbose -cp E:\github\javalib/* -d E:\github\%%n\bin %%i
FOR /R E:\github\teams\src %%i IN (*.java) DO javac -verbose -cp E:\github\javalib/* -d E:\github\teams\bin %%i