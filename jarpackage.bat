set JAVAJDK=C:\j2sdk1.4.2_04

@ECHO ON
set path=%path%;%JAVAJDK%\bin
set CLASSPATH=%JAVAJDK%\lib\;.

@ECHO ON

rem ::::::  aplicacion ::::::::  

jar cmf mainClass JSolitario.jar *.class *.java *.jpg *.gif Cards

pause

