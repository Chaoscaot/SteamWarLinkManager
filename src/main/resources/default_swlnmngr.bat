@echo off
:: Variables
SET JavaHome=java

:: Code
title Request Admin

Net session >nul 2>&1 || (PowerShell start -verb runas '%~0' & exit)

title SteamWarLinkManager
set args=--help
set /p args=Arguments:
echo %args%
%JavaHome% -jar ${iDir}\SteamWarLinkManager.jar %args% %*
pause
exit