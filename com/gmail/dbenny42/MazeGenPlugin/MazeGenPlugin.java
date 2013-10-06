package com.gmail.dbenny42.MazeGenPlugin;

import org.bukkit.block.Block;
import org.bukkit.World;
import org.bukkit.Material;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public final class MazeGenPlugin extends JavaPlugin {


    public boolean onCommand(CommandSender sender,
                             Command cmd,
                             String label,
                             String[] args) {


        if(cmd.getName().equalsIgnoreCase("mazegen")){
            if (args.length != 8) {
                sender.sendMessage("usage: <x-dim> <y-dim> <z-dim> <x-start> <y-start> <z-start> <no ceiling?> <mob spawners?>");
                return true;
            }

            // If the player typed /basic then do the following...

            // generate the maze, place in the map:
            // args[0] should be x-dim
            // args[1] should be y-dim
            // args[2] should be z-dim

            // create the skeleton of the maze:
            int xdim = Integer.parseInt(args[0]);
            int ydim = Integer.parseInt(args[1]);
            int zdim = Integer.parseInt(args[2]);


            RecursiveBTGenerator btgen =
                new RecursiveBTGenerator(xdim, ydim, zdim);
            btgen.generate();
            // at this point, the maze includes the following things:

            // - a complete shell of wall around the outside.
            // - one layer per floor, indicating the layout of that floor.
            // - one layer between every traversible floor, indicating the
            //   ladder-points between those floors.


            // we need to:
            // 1. extend the floor layers to be tall enough to permit
            //    players to pass
            // 2. add ladders, if possible.
            // 3. perform the actual block translation.

            // this line means that each floor will be three blocks, so
            // that's two for the player and one of space above them
            btgen.addHeightToLayout(3);

            // get the start location of the maze:
            int worldX = Integer.parseInt(args[3]);
            int worldY = Integer.parseInt(args[4]);
            int worldZ = Integer.parseInt(args[5]);
            int glassCeiling = Integer.parseInt(args[6]);
            int mobSpawners = Integer.parseInt(args[7]);

            World world = getServer().getWorlds().get(0);


            // build the actual maze!
            for (int y = 0; y != btgen.getZLength(); ++y) {
                for (int z = 0; z != btgen.getYLength(); ++z) {
                    for (int x = 0; x != btgen.getXLength(); ++x) {
                
                        // loop through the maze representation, converting the blocks
                        // as you go.
                        Block block = world.getBlockAt(worldX + x,
                                                       worldY + y,
                                                       worldZ + z);
                        if (btgen.getSquare(x, z, y) > 1) {
                            block.setType(Material.AIR);
                        } else if ((glassCeiling == 1) && (y == (btgen.getZLength() - 1))) {
                            block.setType(Material.AIR); // glass is
                                                         // actually
                                                         // pretty tough
                                                         // to see
                                                         // through.
                        } else {

                            if (shouldBeCobblestone(btgen, x, y, z)) {
                                block.setType(Material.COBBLESTONE);
                            } else {
                                block.setType(Material.GLOWSTONE);
                            }
                        }
                    }
                }
            }
            

            // If this has happened the function will return true. 
            return true;
        } 


        // If this hasn't happened the a value of false will be returned.
        return false; 
    }


    private boolean shouldBeCobblestone(RecursiveBTGenerator btgen,
                                        int x,
                                        int y,
                                        int z) {
        // to make the maze easy on the eyes, we'll
        // set the following to cobblestone:
        // - the outside shell
        // - anything that's not a floor or ceiling
        // - all squares with odd-numbered indexes

        if ((x == (btgen.getXLength() - 1)) ||
            (x == 0) ||
            (y == (btgen.getZLength() - 1)) ||
            (y == 0) || 
            (z == (btgen.getYLength() - 1)) ||
            (z == 0)) {
            return true;
        }

        // at this point, we're inside the shell.  below and above are NOT
        // air, so just make it cobblestone:

        // if it's visible and fully-odd indexed, make it glowstone.
        // this statement says: "is it visible from any direction?
        if (btgen.getSquare(x - 1, z, y) > 1 ||
            btgen.getSquare(x + 1, z, y) > 1 ||
            btgen.getSquare(x, z - 1, y) > 1 ||
            btgen.getSquare(x, z + 1, y) > 1 ||
            btgen.getSquare(x, z, y - 1) > 1 ||
            btgen.getSquare(x, z, y + 1) > 1) {

            // now, 'is it fully odd-indexed?'
            if (x % 2 == 0 &&
                y % 2 == 0 &&
                z % 2 == 0)
            return false;
        }

        if (mobSpawners == 1) {
            // generate some spawners for each floor...
            // TODO
        }

        // otherwise, just make it cobblestone
        return true;
    }

}

