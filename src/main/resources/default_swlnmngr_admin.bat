@echo off

title Admin
runas /noprofile /savecred /user:Admin ${iDir}\swlnmngr.bat
exit