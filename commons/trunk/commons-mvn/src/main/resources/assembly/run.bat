@echo off
IF "%JAVA_HOME%" == "" (
    echo "������JAVA_HOME"
    exit
)

rem JAVA_HOME����
rem set JAVA_HOME=D:\work\env\jdk1.7.0_80\

echo ================����������====================
echo ����help��ʾ�������
echo exit �˳�����
echo ==============================================

:start
echo ---���ǻ����ķָ���---
@set /p input=�������������:

if "%input%"=="exit" goto exit

"%JAVA_HOME%\bin\java" -jar applicationContext.jar %input%

goto :start
