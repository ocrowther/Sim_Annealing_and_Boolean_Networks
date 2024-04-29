package simAnneal;

import bnsim.nodes.BNNode;
import bnsim.process.RunNetwork;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

import static bnsim.process.RunNetwork.iterateStrTrace;

/**
 * Objective functions for scoring candidate networks by comparing their network trace to a desired trace.
 * Score based on Hamming distance between the actual network trace and desired network trace
 */
public class Objective {

    /**
     * Iterates a network number of times defined by desired trace (comma separated, e.g.: 000,001,000). Returns hamming
     * distance between the trace of the candidate network trace and desired trace.
     * @param network network to score
     * @param desiredTrace String representation of the desired trace
     * @return an int score based on hamming distance
     */
    public static int scoreNetwork(TreeMap<String, BNNode> network, String desiredTrace){
        StringBuilder netTrace = new StringBuilder();

        for (int i = 0; i < desiredTrace.split(",").length; i++) {
            String iterationTrace = RunNetwork.iterateStrTrace(network, 1);
            netTrace.append(iterationTrace).append(",");
        }
        return hammingDist(desiredTrace, netTrace.toString());
    }

    /**
     * Iterates a network until it falls into an attractor. Compares that attractor to the desired attractor. Scores
     * the network based on Hamming distance between the network trace and desired attractor network trace. Desired
     * trace represented by a comma separated String (e.g.: 000,001,000).
     * @param network network to score
     * @param desiredTrace network trace of desired attractor
     * @return int score based on Hamming distance
     */
    public static int scoreNet2(TreeMap<String, BNNode> network, String desiredTrace){
        TreeSet<String> netTraces = new TreeSet<>();
        String finalTrace = null;
        StringBuilder attractor = new StringBuilder();

        for (int i = 0; i < 500; i++) {
            String iterTrace = iterateStrTrace(network, 1);
            if (!netTraces.add(iterTrace)) {
                finalTrace = iterTrace;
                break;
            }
        }

        Iterator iterator = netTraces.tailSet(finalTrace).iterator();


        for (int i = 0; i < desiredTrace.split(",").length; i++) {
            attractor.append(iterator.next());
        }

        return hammingDist(desiredTrace, attractor.toString());
    }

    /**
     * Takes two Strings of the same length and computes hamming distance
     */
    private static int hammingDist(String str1, String str2){
        if (str1.length() == 0) return 0;

        int dist = 0;

        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) dist ++;
        }

        return dist;
    }
}