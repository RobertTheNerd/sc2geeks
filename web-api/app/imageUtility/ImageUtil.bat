del /Q src\main\resources\*
svn update .
copy src\main\resources\config.win.xml src\main\resources\config.xml /y
call mvn compile
call mvn exec:java -Dexec.mainClass="com.sc2geeks.ImageUtil.Worker"
pause