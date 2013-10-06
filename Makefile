CLASSPATH=~/games/minecraft/dev/auto_builder/libs/bukkit-1.6.2-R0.2-20130813.060124-7.jar:.
SRCDIR=com/gmail/dbenny42/MazeGenPlugin/

MazeGenPlugin.class: RecursiveBTGenerator.class
	javac -cp $(CLASSPATH) $(SRCDIR)MazeGenPlugin.java

RecursiveBTGenerator.class: MazeGenerator.class
	javac -cp $(CLASSPATH) $(SRCDIR)RecursiveBTGenerator.java

MazeGenerator.class:
	javac -cp $(CLASSPATH) $(SRCDIR)MazeGenerator.java
