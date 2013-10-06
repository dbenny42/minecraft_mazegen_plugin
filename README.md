minecraft_mazegen_plugin
========================

A plugin for minecraft bukkit server that allows players to generate
randomized mazes in-game.

BUILDING:

- First, download bukkit server from http://dl.bukkit.org/downloads/craftbukkit/
-- Just like minecraft server, wherever you spin that up, it'll generate
   the necessary world-files in place.

-- I spun it up in a git-ignored directory below the main
   minecraft_mazegen_plugin.  If you do that, the Makefile / build script
   *should* just work.  So like this: 'mkdir test_world; cd test_world; bukkit_server'

- Make the jar file.  It needs to include plugin.yml and the whole package
  path.  build.sh shows you how I did it.

- Copy the jar to plugins/ in the whatever world directory you have.  You
  can see how I did it in build.sh.

- Start the bukkit server in the directory where you first ran bukkit
  server to generate your world files.


PERMISSIONING:

- For permissioning the plugin, I used PermissionsBukkit, which can be
  downloaded / read about here:
  http://dev.bukkit.org/bukkit-plugins/permbukkit/


USAGE NOTES:

READ THIS, IF NOTHING ELSE!

The UI isn't designed real well.  I or someone can fix this.  
In game, if you're permissioned, run this:

/mazegen <width> <length> <#floors> <x-start> <y-start> <z-start> <no ceiling?>



(In game, it's displayed as <x-dim> <y-dim> <z-dim> <x-start> <y-start>
<z-start> <no ceiling>), but it's a bit confusing because:

- the y and z dimensions are mixed up between minecraft and my code.  so,
  pretend like 'z-dim' is really number of floors for now.

- x-dim and y-dim mean the maze will really come out to about ~2*x-dim and
  ~2*y-dim, because the algorithms I considered involve wall-carving, and
  I represented this by putting actual walls between open spots.  So,
  |x-dim| open spots + |x-dim walls| + shell is what the real dimension
  comes out to be.

PLANNED ADDITIONS:

- Since the algorithm can do multiple floors, we'll need to put in ladders
  at some point.  This shouldn't be hard: for each non-shell floor layer,
  place blocks

- Fix the dimensioning problem.

- Make the code easier to read / maintain by fixing the y/z logical mixup.



SOURCE NOTES:

- MazeGenerator.java is *supposed* to be a mostly-abstract base class that
  we can plug sub-classes into for the actual algorithm.

- MazeGenPlugin.java is the actual plugin.  It's *supposed* to be pretty
  removed from the actual maze implementation, but I hacked a bunch of
  logic together in a rush to get something building.  So it's not squeaky
  clean.