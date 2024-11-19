set JAVAJDK=C:\j2sdk1.4.2_04

@ECHO ON
set path=%path%;%JAVAJDK%\bin
set CLASSPATH=%JAVAJDK%\lib\;.

@ECHO ON

rem ::::::  aplicacion ::::::::  


jarsigner.exe -keystore keyAMAZONIA -storepass mf251176  JSolitario.jar amazonia

pause





