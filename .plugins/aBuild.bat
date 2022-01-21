@ECHO off
ECHO exporting jars..

FOR %%n in (Backup, Bot, Core, Market, Masterbook, ModTools, Survival, Teleport) DO jar cfv %%n.jar -C E:\github\%%n\bin .
jar cfv Teams.jar -C E:\github\teams\bin .
