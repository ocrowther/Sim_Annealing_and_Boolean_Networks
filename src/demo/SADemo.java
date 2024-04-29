package demo;

import bnsim.nodes.BNNode;
import simAnneal.Cooling;
import simAnneal.NeighbourGen;
import simAnneal.Utility;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static bnsim.generator.NetworkGen.*;
import static bnsim.process.RunNetwork.*;
import static simAnneal.Objective.scoreNetwork;

/**
 * An example implementation of the Simulated Annealing algorithm using components from the simAnneal package. Prints
 * scores to console.
 */
public class SADemo {
    public static void main(String[] args) {
        //Load a previously generated test network (can run netSimDemo to generate)
        TreeMap<String, BNNode> testNet = fileInput("\\Users\\ocrow\\Desktop\\Demo\\NetSim\\SaveFile.txt");

        //Specify a desired trace (network attractor)
        String desiredTrace = "1111111,0000000,1111111";

        //Set starting temperature & cooling factor
        double tempCurrent = 1000.0;
        double coolingFactor = 0.90;

        //Save the initial state of the test network
        Map<String, Boolean> initialState = iterateMap(testNet, 0);

        //Set initial solution
        TreeMap<String, BNNode> currentSol = testNet;

        //Set initial score
        int currentScore = scoreNetwork(testNet, desiredTrace);

        //Simulated annealing loop
        Random random = new Random();
        System.out.println("Starting score: " + currentScore);
        for (int i = 0; i < 1000; i++) {
            //Decrement temperature
            tempCurrent = Cooling.expCool(i, coolingFactor, tempCurrent);

            //Generate a new candidate and set starting state
            TreeMap<String, BNNode> candidate = NeighbourGen.genRandFixed(currentSol, 8, 4);
            Utility.setState(candidate, initialState);

            //Score candidate and calculate difference with current solution
            int candScore = scoreNetwork(candidate, desiredTrace);
            int delta = candScore - currentScore;

            //Accept if improvement
            if (delta < 0) {
                currentSol = candidate;
                currentScore = candScore;
                System.out.println(i + " : " + candScore);
            }

            //Accept if worse but passes acceptance criterion
            else if (delta > 0 && random.nextDouble(0,1) < Math.exp(-delta/tempCurrent)) {
                currentSol = candidate;
                currentScore = candScore;
                System.out.println(i + " : " + candScore);
            }
        }
        System.out.println("Final score: " + currentScore);
    }
}
