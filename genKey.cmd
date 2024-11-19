set JAVAJDK=C:\j2sdk1.4.2_04


@ECHO ON
set path=%path%;%JAVAJDK%\bin
set CLASSPATH=%JAVAJDK%\lib\;.

@ECHO ON

keytool.exe -genKey -keystore keyAMAZONIA -alias amazonia
pause
keytool -selfcert -alias amazonia -keystore keyAMAZONIA
pause
keytool -list -keystore keyAMAZONIA
pause