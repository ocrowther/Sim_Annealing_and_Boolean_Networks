package demo;

import java.util.TreeMap;

import bnsim.nodes.BNNode;

import static bnsim.generator.NetworkGen.*;
import static bnsim.process.FileOut.*;
import static bnsim.process.RunNetwork.*;

/**
 * A small demo showing random network generation and iteration. Along with saving to file, network visualisation and
 * truth table outputs. Network visualisation requires installation of Graphviz. Network iterated 15 times with network
 * trace outputting to console
 */
public class netSimDemo {
    public static void main(String[] args) {
        String saveFolder = "\\Users\\ocrow\\Desktop\\Demo\\NetSim";
        TreeMap<String, BNNode> testNet = genRand(7, 2); //random network, 7 nodes and 2 neighbours per node

        output(testNet, "SaveFile", saveFolder);
        outputGraphPNG(testNet, "PNGTest", saveFolder); //Requires installation of Graphviz and adding to system path
        outputNetMD(testNet, "MDTest", saveFolder);

        for (int i = 0; i < 15; i++) {
            System.out.println(iterateStrTrace(testNet, 1) + " " + i);
        }
    }
}
