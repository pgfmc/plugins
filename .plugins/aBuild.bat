@ECHO off
ECHO exporting jars..

FOR %%n in (Backup, Bot, Core, Market, Masterbook, ModTools, Survival, Teleport, Friends, Duels, Claims, FFA) DO jar cfv %%n.jar -C E:\github\%%n\bin .
