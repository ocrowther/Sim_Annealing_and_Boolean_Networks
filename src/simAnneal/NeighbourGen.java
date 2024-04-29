package simAnneal;

import bnsim.nodes.BNNode;

import java.util.*;

import static simAnneal.Utility.copyNetwork;


/**
 * Set of methods for generating new candidate networks by randomising node truth tables
 */
public class NeighbourGen {

    /**
     * Copies a network then randomises a given number of truth table rows on a given number of nodes in a network.
     * Care should be taken to ensure the number of truth table rows to randomise is not larger than the smallest
     * truth table.
     * @param currentSol network to randomise
     * @param nodes number of nodes to randomise
     * @param truthRows number of truth table rows to randomise (per node)
     * @return a randomised copy of the submitted network
     */
    public static TreeMap<String, BNNode> gen1(TreeMap<String, BNNode> currentSol, Integer nodes, Integer truthRows){
        TreeMap<String, BNNode> output = copyNetwork(currentSol);

        Random random = new Random();

        ArrayList<String> keyArray = new ArrayList<>(output.keySet());
        ArrayList<String> nodeKeys  = new ArrayList<>();
        while (nodeKeys.size() < nodes) nodeKeys.add(keyArray.get(random.nextInt(currentSol.size()))); //get random String keys

        for (String key: nodeKeys) randNodeTruths(output.get(key), truthRows);

        return output;
    }

    /**
     * Neighbour generation that flips a specified number of random bits across the network. Can duplicate (ie flip a
     * bit that has already been flipped). Does not modify the provided network, returns a copied and modified version
     * @param network network to randomise
     * @param rows number of bits to flip
     * @param truthsMax size of smallest node truth table
     * @return new, randomised network
     */
    public static TreeMap<String, BNNode> genRandFixed(TreeMap<String, BNNode> network, Integer rows, Integer truthsMax){
        TreeMap<String, BNNode> output = copyNetwork(network);
        ArrayList<String> keyArray = new ArrayList<>(output.keySet());
        Random random = new Random();

        HashMap<String, Integer> toRand = new HashMap<>();

        for (int i = 0; i < rows; i++) {
            String node = keyArray.get(random.nextInt(keyArray.size()));
            if (toRand.containsKey(node) && toRand.get(node) < truthsMax){
                Integer newInt = toRand.get(node) + 1;

                toRand.put(node, newInt);
            } else {
                toRand.put(node, 1);
            }
        }

        toRand.forEach((k,v) -> randNodeTruths(output.get(k), v));

        return output;
    }

    /**
     * Randomises a random number of truth table rows within the specified bounds (inclusive of the bounds). Randomness
     * is even across the range.
     * @param network network to randomise
     * @param upper upper bound of truth table rows to randomise
     * @param truthsMax size of smallest node truth table
     * @return a randomised copy of the submitted network
     */
    public static TreeMap<String, BNNode> genRand(TreeMap<String, BNNode> network, Integer upper, Integer truthsMax){
        TreeMap<String, BNNode> output = copyNetwork(network);
        ArrayList<String> keyArray = new ArrayList<>(output.keySet());
        Random random = new Random();

        int rows = random.nextInt(1, upper + 1);

        HashMap<String, Integer> toRand = new HashMap<>();

        for (int i = 0; i < rows; i++) {
            String node = keyArray.get(random.nextInt(keyArray.size()));
            if (toRand.containsKey(node) && toRand.get(node) < truthsMax){
                Integer newInt = toRand.get(node) + 1;

                toRand.put(node, newInt);
            } else {
                toRand.put(node, 1);
            }
        }

        toRand.forEach((k,v) -> randNodeTruths(output.get(k), v));

        return output;
    }

    /**
     * Randomises a random number of truth table rows within a Gaussian/normal distribution
     * @param network network to randomise
     * @param median median for the Gaussian distribution
     * @param stddev standard deviation for the Gaussian distribution
     * @param totalTruths Total number of truth table rows in the network
     * @return a randomised copy of the submitted network
     */
    public static TreeMap<String, BNNode> genRandGauss(TreeMap<String, BNNode> network, Integer totalTruths, Integer median, Double stddev, Integer truthsMax){
        TreeMap<String, BNNode> output = copyNetwork(network);
        ArrayList<String> keyArray = new ArrayList<>(output.keySet());
        Random random = new Random();

        int amount; //number of rows to randomise
        do {
            amount = (int) Math.round(Math.abs(random.nextGaussian(median, stddev))); //generate random positive from Gaussian
        } while (amount > totalTruths); //Check not larger than # of truth tables in network, if so generate new number

        HashMap<String, Integer> toRand = new HashMap<>();

        for (int i = 0; i < amount; i++) {
            String node = keyArray.get(random.nextInt(keyArray.size()));
            if (toRand.containsKey(node) && toRand.get(node) < truthsMax){
                Integer newInt = toRand.get(node) + 1;

                toRand.put(node, newInt);
            } else {
                toRand.put(node, 1);
            }
        }

        toRand.forEach((k,v) -> randNodeTruths(output.get(k), v));

        return output;
    }

    /**
     * Copies a network then randomises all truth table entries for each node.
     * @param network network to randomise
     * @return copied and randomised network
     */
    public static TreeMap<String, BNNode> randAllTruths(TreeMap<String, BNNode> network){
        TreeMap<String, BNNode> output = copyNetwork(network);

        //Randomise truth table entries for each node in the output network
        Random random = new Random();
        output.forEach((K,V) -> {
            for (int i = 0; i < 1 << V.neighbours.size(); i++) {
                V.truths.set(i, random.nextBoolean());
            }
        });

        return output;
    }

    /**
     * Flips a given number of random rows on a node truth table. Make sure the number of rows required to flip is not
     * larger than the node's truth table.
     * @param node node to randomise
     * @param rows number of rows to randomise
     */
    private static void randNodeTruths(BNNode node, Integer rows){
        Random random = new Random();

        HashSet<Integer> rowsToRand = new HashSet<>();
        while (rowsToRand.size() < rows) rowsToRand.add(random.nextInt(1 << node.neighbours.size()));

        for (Integer row: rowsToRand) node.truths.flip(row);
    }
}
