del /Q src\main\resources\*
svn update .
copy src\main\resources\crawler.win.xml src\main\resources\crawler.xml /y
copy src\main\resources\hibernate.cfg.win.xml src\main\resources\hibernate.cfg.xml /y
rem call mvn clean
call mvn compile
call mvn exec:java -Dexec.mainClass="Main" > log.txt
pause