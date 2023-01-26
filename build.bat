@echo off
ECHO exporting jars..

FOR %%i IN (Core, Claims, ModTools, Survival) DO CALL mvn -f %cd:~0,3%\github\repositories\%%i\ clean package
FOR %%e IN (Core, Claims, ModTools, Survival) DO xcopy /y %cd:~0,3%\github\repositories\%%e\target\%%e-jar-with-dependencies.jar %cd:~0,3%\github\build\%%e.jar

PAUSE