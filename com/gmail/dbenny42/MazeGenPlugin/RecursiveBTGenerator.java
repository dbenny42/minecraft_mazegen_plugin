package com.gmail.dbenny42.MazeGenPlugin;


class RecursiveBTGenerator extends MazeGenerator {
    public RecursiveBTGenerator(int x, int y, int z) {
        super(x, y, z);
    }

    public void generate() {
        // TODO: fill this in.

        // pick a random start point, then call the generate loop.
        int startX = X_MIN + (int)(Math.random() * ((X_MAX - X_MIN) + 1));
        int startY = Y_MIN + (int)(Math.random() * ((Y_MAX - Y_MIN) + 1));
        int startZ = Z_MIN + (int)(Math.random() * ((Z_MAX - Z_MIN) + 1));

        // if even, it's in a wall.  no good.
        while (startX % 2 == 0) {
            startX = X_MIN + (int)(Math.random() * ((X_MAX - X_MIN) + 1));
        }

        while (startY % 2 == 0) {
            startY = Y_MIN + (int)(Math.random() * ((Y_MAX - Y_MIN) + 1));
        }

        while (startZ % 2 == 0) {
            startZ = Z_MIN + (int)(Math.random() * ((Z_MAX - Z_MIN) + 1));
        }


        backtrackerLoop(startX, startY, startZ);
        setGenerated(true);
    }

    // convert the maze so that each layer has enough height for a player
    // to walk through.
    public void addHeightToLayout(int height) {
        int[][][] oldRep = getMaze();
        int numNonFloorLayers = (oldRep.length + 1) / 2;
        int numFloorLayers = (oldRep.length - 1) / 2;
        

        int[][][] newRep = new int[(numFloorLayers * height) + numNonFloorLayers][oldRep[0].length][oldRep[0][0].length];

        // replicate each floor-layer 'height' times in the new layout.
        int oldZ = 0;
        int newZ = 0;
        while (oldZ != oldRep.length && newZ != newRep.length) {
            int numRepeats = 1;
            if ((oldZ % 2) == 1) {
                numRepeats = height;
            }

            int rep = 0;
            for (; rep != numRepeats; ++rep) {
                for (int y = 0; y != oldRep[oldZ].length; ++y) {
                    for (int x = 0; x != oldRep[oldZ][y].length; ++x) {
                        newRep[newZ + rep][y][x] = oldRep[oldZ][y][x];
                    }
                }
            }


            // TODO update:
            ++oldZ;
            newZ += rep;
        }

        setMaze(newRep);
    }
    

    private enum Neighbor {
        LESSX,
        GREATERX,
        GREATERY,
        LESSY,
        LESSZ,
        GREATERZ
    }

    private boolean isVisited(int x, int y, int z) {
        return (getSquare(x, y, z) == VISITED);
    }

    private final int NEIGHBORS_LEN = Neighbor.values().length;
    private final int X_IDX = 0;
    private final int Y_IDX = 1;
    private final int Z_IDX = 2;
    private final int VISITED = 7;
    private final int X_MAX = getXLength() - 2; // zero-indexed, and don't
                                                // visit the last one
                                                // (wall), so minus 2.
    private final int Y_MAX = getYLength() - 2;
    private final int Z_MAX = getZLength() - 2;
    private final int X_MIN = 1; // the zero-index accesses a wall.  thus,
                                 // 1.
    private final int Y_MIN = 1;
    private final int Z_MIN = 1;

    private Neighbor[] getRandomNeighborOrder() {
        Neighbor[] squareIdxsToVisit =
            {Neighbor.LESSX,
             Neighbor.GREATERX,
             Neighbor.LESSY,
             Neighbor.GREATERY,
             Neighbor.LESSZ,
             Neighbor.GREATERZ};

        // 50 chosen at random.  tune this.
        Neighbor temp = Neighbor.LESSX;
        for (int i = 0; i < 50; ++i) {
            // pick two at random, swap them.
            // two indices between 0 and #neighbors (5)
            final int min = 0;
            final int max = NEIGHBORS_LEN - 1;
            int idx1 = min + (int)(Math.random() * ((max - min) + 1));
            int idx2 = min + (int)(Math.random() * ((max - min) + 1));

            temp = squareIdxsToVisit[idx1];
            squareIdxsToVisit[idx1] = squareIdxsToVisit[idx2];
            squareIdxsToVisit[idx2] = temp;
        }
        return squareIdxsToVisit;
    }

    private int[] getUnvisitedNeighbor(int currX, int currY, int currZ, Neighbor neighbor) {

        int neighborX = currX;
        int neighborY = currY;
        int neighborZ = currZ;
        switch(neighbor) {
        case LESSX:
            neighborX -= 2;
            if (neighborX < X_MIN) {
                return null;
            }
            break;
        case GREATERX:
            neighborX += 2;
            if (neighborX > X_MAX) {
                return null;
            }
            break;
        case LESSY:
            neighborY -= 2;
            if (neighborY < Y_MIN) {
                return null;
            }
            break;
        case GREATERY:
            neighborY += 2;
            if (neighborY > Y_MAX) {
                return null;
            }
            break;
        case LESSZ:
            neighborZ -= 2;
            if (neighborZ < Z_MIN) {
                return null;
            }
            break;
        case GREATERZ:
            neighborZ += 2;
            if (neighborZ > Z_MAX) {
                return null;
            }
            break;
        default:
            throw new RuntimeException();
        }

        // lastly, check if it's visited:
        if (getSquare(neighborX, neighborY, neighborZ) == VISITED) {
            return null;
        }

        int[] neighborCoords = {neighborX, neighborY, neighborZ};
        return neighborCoords;
    }

    // precondtion: the neighbor indicated is a valid one, generated by
    // getUnvisitedNeighbor()
    private void carveWall(int currX, int currY, int currZ, Neighbor neighbor) {
        switch(neighbor) {
        case LESSX:
            setSquare(currX - 1, currY, currZ, VISITED);
            break;
        case GREATERX:
            setSquare(currX + 1, currY, currZ, VISITED);
            break;
        case LESSY:
            setSquare(currX, currY - 1, currZ, VISITED);
            break;
        case GREATERY:
            setSquare(currX, currY + 1, currZ, VISITED);
            break;
        case LESSZ:
            setSquare(currX, currY, currZ - 1, VISITED);
            break;
        case GREATERZ:
            setSquare(currX, currY, currZ + 1, VISITED);
            break;
        default:
            throw new RuntimeException();
        }

    }
     
    private void backtrackerLoop(int currX, int currY, int currZ) {
        // recursively visit all six possible squares around the current
        // square, then return

        // first, set the current square to visited:
        setSquare(currX, currY, currZ, VISITED);

        Neighbor[] toVisit = getRandomNeighborOrder();

        for (int i = 0; i < NEIGHBORS_LEN; ++i) {
            int[] neighborCoords =
                getUnvisitedNeighbor(currX, currY, currZ, toVisit[i]);
            if (neighborCoords != null) {
                // carve the wall between the two spaces; recurse.
                carveWall(currX, currY, currZ, toVisit[i]);
                backtrackerLoop(neighborCoords[X_IDX],
                                neighborCoords[Y_IDX],
                                neighborCoords[Z_IDX]);
            } // else continue
        }
        return;
    }
};
