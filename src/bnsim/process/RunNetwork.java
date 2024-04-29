package bnsim.process;

import bnsim.nodes.BNNode;

import java.util.*;

/**
 * Set of methods to iterate ("run") a Boolean network
 */
public class    RunNetwork {

    /**
     * Iterates a network specified number of times, returns the network state after that many iterations as mapping of
     * String node names and Boolean node states
     * @param network network to iterate
     * @param iterations number of iterations, zero gives the current (unmodified) network state
     * @return mapping of String names and Boolean states
     */
    public static Map<String, Boolean> iterateMap(TreeMap<String, BNNode> network, int iterations){
        iterateNet(network, iterations);

        LinkedHashMap<String, Boolean> finalTrace = new LinkedHashMap<>();
        for (BNNode node : network.values()) finalTrace.put(node.name, node.state);

        return finalTrace;
    }

    /**
     * Iterates network specified number of times, returns a String representing node states. 1 = true, 0 = false.
     * Ordered according to natural ordering of node names
     * @param network network to iterate
     * @param iterations number of iterations, zero gives the current (unmodified) network state
     * @return String representation of network state
     */
    public static String iterateStrTrace(TreeMap<String, BNNode> network, int iterations){
        iterateNet(network, iterations);

        StringBuilder finalTrace = new StringBuilder();
        for (BNNode node : network.values()) finalTrace.append(node.state ? 1 : 0);

        return finalTrace.toString();
    }

    /**
     * Iterates a network specified number of times. Outputs a BitSet of node states, states ordered according to
     * natural order of node names
     * @param network network to iterate
     * @param iterations number of iterations, zero gives the current (unmodified) network state
     * @return BitSet representation of network state
     */
    public static BitSet iterateBitSet(TreeMap<String, BNNode> network, int iterations){
        iterateNet(network, iterations);

        BitSet finalTrace = new BitSet(network.size());
        int counter = 0;
        for (BNNode node : network.values()) {
            finalTrace.set(counter, node.state);
            counter++;
        }

        return finalTrace;
    }

    /**
     * Iterates a network specified number of times
     * @param network network to iterate
     * @param iterations number of iterations
     */
    private static void iterateNet(TreeMap<String, BNNode> network, int iterations) {
        HashMap<String, Boolean> currentState = new HashMap<>();

        for (int i = 0; i < iterations; i++) {
            for (BNNode node : network.values()) currentState.put(node.name, node.state);
            for (BNNode node : network.values()) node.update(currentState);
        }
    }
}
