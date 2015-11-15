@echo off

javac -cp libraries\box2d_processing.jar;libraries\core.jar;libraries\jbox2d-library-2.2.1-ds.jar src\*.java src\resources\*.java -d .

copy src\resources\appIco.png resources

jar cfm out.jar META-INF\MANIFEST.MF *.class resources\*.class resources\appIco.png

del *.class



rmdir /S /Q resources


