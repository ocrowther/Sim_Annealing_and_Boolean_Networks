package simAnneal;

import bnsim.nodes.BNNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Set of utility methods for manipulating Boolean networks and nodes
 */
public class Utility {

    /**
     * Uses a Map of Strings and Boolean states corresponding to the nodes in a network to set the states of nodes in a
     * network
     * @param network network to set states on
     * @param networkState Map of Strings (node names) and Boolean values
     */
    public static void setState(TreeMap<String, BNNode> network, Map<String, Boolean> networkState){
        networkState.forEach((K,V) -> network.get(K).state = V);
    }

    /**
     * Set all node states in a network to a given Boolean
     * @param network network to set states
     * @param newState new state of all nodes
     */
    public static void setStateAll(TreeMap<String, BNNode> network, Boolean newState){
        network.forEach((K,V) -> V.state = newState);
    }

    /**
     * Sets all the truth table outputs of a network to random values
     * @param network network to randomise
     */
    public static void netTruthsRand(TreeMap<String, BNNode> network){
        network.forEach((K,V) -> nodeTruthsRand(V));
    }

    /**
     * Sets the truth table outputs of a given node to a random boolean
     * @param node node to randomise
     */
    public static void nodeTruthsRand(BNNode node){
        Random random = new Random();
        for (int i = 0; i < 1 << node.neighbours.size(); i++) {
            node.truths.set(i, random.nextBoolean());
        }
    }

    /**
     * Copies a given network
     * @param network network to copy
     * @return copied network
     */
    public static TreeMap<String, BNNode> copyNetwork(TreeMap<String, BNNode> network){
        TreeMap<String, BNNode> output = new TreeMap<>();
        network.forEach((K,V) -> output.put(K, BNNode.valueOf(V.toString())));
        return output;
    }

    /**
     * Prints an ArrayList of Strings to a file. Does not specify file format. Will create folder if not present.
     * @param toPrint ArrayList of Strings to print
     * @param fileName name of file to write/write to
     * @param filePath folder to write to
     */
    public static void printArray(ArrayList<String> toPrint, String fileName, String filePath){
        File file = new File(new File(filePath), fileName);

        try {
            Files.createDirectories(Paths.get(filePath));
            Files.write(Path.of(file.getPath()), toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
