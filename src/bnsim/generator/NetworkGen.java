package bnsim.generator;

import bnsim.nodes.BNNode;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Methods for generating a randomly configured Boolean network, and for importing previously saved networks
 */

public class NetworkGen {

    /**
     * Import a previously saved Boolean network
     * @param filepath filepath of saved network
     * @return a TreeMap of String (node name) and BNNode pairs, representing the network
     */
    public static TreeMap<String, BNNode> fileInput(String filepath){
        TreeMap<String, BNNode> output = new TreeMap<>();
        Set<BNNode> outputSet = new TreeSet<>();
        List<String> input;

        try {
            input = Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        for (String line : input) {
            outputSet.add(BNNode.valueOf(line));
        }

        for (BNNode node: outputSet) output.put(node.name, node);

        return output;
    }

    /**
     * Generate a random Boolean network of specified size and number of neighbours per node. Nodes are named
     * numerically (i.e. for a 3 node network nodes are named "0", "1", and "2").
     * @param size number of nodes in network
     * @param neighbours number of neighbour nodes per node
     * @return a TreeMap of String (node name) and BNNode pairs, representing the network
     */
    public static TreeMap<String, BNNode> genRand(Integer size, Integer neighbours){
        TreeMap<String, BNNode> output = new TreeMap<>();
        Set<BNNode> outputSet = new TreeSet<>();
        Random random = new Random();

        //Generate specified number of nodes
        for (int i = 0; i < size; i++) {
            //generates a set of unique neighbour nodes
            LinkedHashSet<String> neighboursIn = new LinkedHashSet<>();
            while (neighboursIn.size() < neighbours){
                int neighbourToAdd = random.nextInt(size);
                if (neighbourToAdd != i) neighboursIn.add(Integer.toString(neighbourToAdd));
            }

            //generates a randomised BitSet representing the truth table outputs for the node
            BitSet truths = new BitSet(1 << neighbours);
            for (int j = 0; j < 1 << neighbours; j++) {
                truths.set(j, random.nextBoolean());
            }

            outputSet.add(new BNNode(Integer.toString(i), random.nextBoolean(), neighboursIn, truths));
        }

        //Add generated node to the mapping
        for (BNNode node: outputSet) output.put(node.name, node);

        return output;
    }
}
