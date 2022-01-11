set args=--help
set /p args=Arguments:
echo %args%
java -jar ${iDir}\SteamWarLinkManager.jar %args% %*
pause