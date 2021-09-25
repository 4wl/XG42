@echo off

:copy
copy /y "%CD%\build\libs\*-release.jar" "C:\Users\PyWong921\Desktop\test\.minecraft\mods"
xcopy /y "%CD%\build\libs\*-release.jar" "D:\Minecraft Test Mod\mods"
pause
goto copy

