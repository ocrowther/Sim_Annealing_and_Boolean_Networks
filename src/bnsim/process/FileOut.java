package bnsim.process;

import bnsim.nodes.BNNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Set of utility methods for saving networks, outputting Graphviz visualisations, and saving Markdown representations
 * of Boolean network truth tables.
 */
public class FileOut {
    /**
     * Save a Boolean network, as represented by a mapping of String node names and BNNodes
     * @param network Boolean network to save
     * @param fileName
     * @param filePath
     */
    public static void output(TreeMap<String, BNNode> network, String fileName, String filePath) {
        ArrayList<String> toPrint = new ArrayList<>();
        for (BNNode node : network.values()) toPrint.add(node.toString());

        printArray(toPrint, fileName + ".txt", filePath);
    }

    /**
     * Save the network as a Graphviz (.gv) compatible file
     * @param network Boolean network to save
     * @param fileName
     * @param filePath
     */
    public static void outputGraph(TreeMap<String, BNNode> network, String fileName, String filePath){
        Set<String> edges = new TreeSet<>();
        for (BNNode node : network.values()) {
            for (String neighbour : node.neighbours){
                edges.add(neighbour + " -> " + node.name);
            }
        }

        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("digraph " + fileName + " {");
        toPrint.addAll(edges);
        toPrint.add("}");

        printArray(toPrint, fileName + ".gv", filePath);

    }

    /**
     * Save the network as a Graphviz (.gv) compatible file before running Graphviz with some default settings to
     * generate a png visualisation of the network. Requires installation of Graphviz
     * @param network Boolean network to visualise
     * @param fileName
     * @param filePath
     */
    public static void outputGraphPNG(TreeMap<String, BNNode> network, String fileName, String filePath) {
        outputGraph(network, fileName, filePath);

        ProcessBuilder processBuild = new ProcessBuilder("circo", "-Tpng" , filePath + "\\" + fileName + ".gv", "-O");

        try {
            Process process = processBuild.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Generates a Markdown file showing the truth tables of nodes in a network
     * @param network
     * @param fileName
     * @param filePath
     */
    public static void outputNetMD(TreeMap<String, BNNode> network, String fileName, String filePath){
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("# " + fileName);

        for (BNNode node : network.values()){
            toPrint.add(" ");
            toPrint.add("### " + node.name);
            toPrint.addAll(generateNodeMD(node));
        }

        printArray(toPrint, fileName + ".md", filePath);
    }

    /**
     * Generates a Markdown file of a single BNNode
     * @param node
     * @param fileName
     * @param filePath
     */
    public static void outputNodeMD(BNNode node, String fileName, String filePath){
        ArrayList<String> toPrint = new ArrayList<>();
        toPrint.add("# " + fileName);
        toPrint.add(" ");
        toPrint.add("## " + node.name);
        toPrint.addAll(generateNodeMD(node));

        printArray(toPrint, fileName + ".md", filePath);
    }

    private static ArrayList<String> generateNodeMD(BNNode node){
        ArrayList<String> output = new ArrayList<>(); //array of lines of the table

        //Arraylist of neighbours and the node name
        ArrayList<String> headers = new ArrayList<>(node.neighbours);
        headers.add(node.name);

        //Generates StringBuilder of the headings, and stores heading column widths
        StringBuilder headings = new StringBuilder("|");
        ArrayList<Integer> columnWidths = new ArrayList<>();
        for (String header: headers){
            columnWidths.add(header.length());
            headings.append(header).append("|");
        }


        //Generate table horizontal break line
        StringBuilder breakLine = new StringBuilder("|");
        for (Integer column : columnWidths){
            String cell = (new String(new char[column]).replace("\0", "-")) + "|";
            breakLine.append(cell);
        }

        //Generate table rows
        ArrayList<String> rows = new ArrayList<>();
        int neighboursSize = node.neighbours.size();

        //Iterates through each row (2 to power of # of neighbours)
        for (int i = 0; i < 1 << neighboursSize; i++) {

            //for each cell (j) uses bitwise operations to generate corresponding digit of binary number (padded to left with zeroes)
            StringBuilder row = new StringBuilder("|");
            for (int j = 0; j < neighboursSize; j++) {
                int cellValue = (i >>> (neighboursSize - j - 1)) & 1;
                row.append(String.format("%" + columnWidths.get(j) + "s", cellValue)).append("|");
            }

            //Append the final cell to the row as determined by the node's truths BitSet
            row.append(String.format("%" + columnWidths.get(neighboursSize) + "s", (node.truths.get(i)? 1 : 0))).append("|");

            //Add the generated row to String Array of rows
            rows.add(row.toString());
        }

        //Add all table lines to output array
        output.add(headings.toString());
        output.add(breakLine.toString());
        output.addAll(rows);

        return output;
    }

    private static void printArray(ArrayList<String> toPrint, String fileName, String filePath){
        File file = new File(new File(filePath), fileName);

        try {
            Files.createDirectories(Paths.get(filePath));
            Files.write(Path.of(file.getPath()), toPrint);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
