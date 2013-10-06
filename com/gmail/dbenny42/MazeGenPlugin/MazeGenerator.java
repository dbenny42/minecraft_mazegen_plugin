package com.gmail.dbenny42.MazeGenPlugin;
import java.io.*;

class MazeGenerator {

    // x,y,z indicate dimensions of maze
    public MazeGenerator(int x, int y, int z) {
        d_generated = false;

        // z is only plus two, because each floor is a floor, no extra,
        // plus a shell layer on top and bottom.
        d_maze = new int[z*2 + 1][y*2 + 1][x*2 + 1];

        // initialize the maze to be a grid of walls inside a shell.
        // first, set to ones, representing all walls:
        for (int zIdx = 0; zIdx != d_maze.length; zIdx++) {
            for (int yIdx = 0; yIdx != d_maze[zIdx].length; yIdx++) {
                for (int xIdx = 0; xIdx != d_maze[zIdx][yIdx].length; xIdx++) {
                    d_maze[zIdx][yIdx][xIdx] = 1;
                }
            }
        }

        // now, for every layer except z = 0 and z = n, set every other
        // x/y square to be empty.
        for (int zIdx = 1; zIdx < d_maze.length - 1; zIdx += 2) {
            for (int yIdx = 1; yIdx < d_maze[zIdx].length; yIdx += 2) {
                for (int xIdx = 1; xIdx < d_maze[zIdx][yIdx].length; xIdx += 2) {
                    d_maze[zIdx][yIdx][xIdx] = 0;
                }
            }
        }


    }

    // fill the actual maze
    public void generate() {
        throw new UnsupportedOperationException();
    }


    public void print() {
        if (!d_generated) {
            throw new RuntimeException();
        }

        for (int zIdx = 0; zIdx != d_maze.length; zIdx++) {
            System.out.println("floor " + zIdx);
            for (int yIdx = 0; yIdx != d_maze[zIdx].length; yIdx++) {
                for (int xIdx = 0; xIdx != d_maze[zIdx][yIdx].length; xIdx++) {
                    if (d_maze[zIdx][yIdx][xIdx] > 1) {
                        System.out.print("*");
                    } else {
                        System.out.print(d_maze[zIdx][yIdx][xIdx]);
                    }
                }
                System.out.println(); // go to next row of maze.
            }
        }
    }

    public boolean generated() {
        return d_generated;
    }

    public int[][][] getMaze() {
        return d_maze;
    }

    public void setMaze(int[][][] newRep) {
        d_maze = newRep;
    }

    public int getXLength() {
        return d_maze[0][0].length; 
    }
    public int getYLength() {
        return d_maze[0].length;
    }
    public int getZLength() {
        return d_maze.length;
    }

    protected int getSquare(int x, int y, int z) {
        return d_maze[z][y][x];
    }


    protected void setGenerated(boolean generated) {
        d_generated = generated;
    }
    protected void setSquare(int x, int y, int z, int val) {
        d_maze[z][y][x] = val;
    }

    private boolean d_generated;
    private int d_maze[][][];

};
