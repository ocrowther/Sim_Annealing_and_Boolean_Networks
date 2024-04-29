package bnsim.nodes;

import java.util.*;

/**
 * A BNNode object represents a node in a Boolean network
 */
public class BNNode implements Comparable<BNNode> {
    public String name; //Node identifier
    public boolean state; //Represents current state of the node
    public LinkedHashSet<String> neighbours; //Set of neighbour nodes, identified by their String name
    public BitSet truths; //Represents the output of the node's truth table. 0 entry represents the 0 row of table

    public BNNode(String name, boolean state, LinkedHashSet<String> neighbours, BitSet truths){
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.truths = (BitSet) Objects.requireNonNull(truths, "Truths BitSet cannot be null").clone();
        this.neighbours = new LinkedHashSet<>(Objects.requireNonNull(neighbours, "Neighbours set cannot be null"));
        this.state = state;
    }

    /**
     * Updates a node's state based on a mapping of Node names and states (representing the current state of a network).
     * @param currentState Map of Strings (node names) and Integers (node states).
     */
    public void update(Map<String, Boolean> currentState){

        int truthRow = 0;
        for (String neighbour : neighbours){
            truthRow = (truthRow << 1) | (currentState.get(neighbour) ? 1 : 0); //generates truth table 'row'
        }

        state = truths.get(truthRow); //sets the current state to the appropriate 'row' of truths
    }

    /**
     * Method to generate an instance of AbsNode from a String in the format generated by toString.
     * @param nodeString To generate from
     * @return An instance of AbsNode
     */
    public static BNNode valueOf(String nodeString){

        nodeString = nodeString.replaceAll("\\s",""); //remove all whitespace
        String[] split = nodeString.split(";"); //split by ";"
        if (split.length != 4) throw new RuntimeException("String input does not match constructor parameters");

        String nameIn = split[0]; //set name

        boolean stateIn = Boolean.parseBoolean(split[1]); //set starting state

        if (!split[2].matches("\\[.*?]")){
            throw new RuntimeException("String neighbour set not in correct format");
        }

        split[2] = split[2].replaceAll("[\\[\\]]", ""); //remove []
        LinkedHashSet<String> neighboursIn = new LinkedHashSet<>(Arrays.asList(split[2].split(","))); //split and generate neighbours set

        if (!split[3].matches("\\{.*?}")){
            throw new RuntimeException("String neighbour set not in correct format");
        }

        split[3] = split[3].replaceAll("[{}]", ""); //remove {}
        String[] bitsToSet = split[3].split(","); //splits into array of bits to set
        BitSet truthsIn = new BitSet(1 << neighboursIn.size()); //generates bitset of correct size for # of neighbors
        for (String bit : bitsToSet) { //iterates and sets appropriate bits as true
            if (bit.isEmpty()) break; //breaks if no bits to set
            truthsIn.set(Integer.parseInt(bit));
        }

        return new BNNode(nameIn, stateIn, neighboursIn, truthsIn);
    }

    @Override
    public int compareTo(BNNode N){
        if (N == null) throw new NullPointerException();
        else return name.compareTo(N.name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BNNode that = (BNNode) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * Generates a String representation of a node in the following format:
     * <p>
     * A ; true ; [B,C] ; {2,3}
     * <p>
     * Where:
     * 'A' is the node name.
     * 'true' is the node starting state
     * '[B,C]' is a set of neighbour nodes
     * '{2,3}' is a set of the BitSet entries set to true (ie rows of truth table that evaluate to true)
     *
     * @return String representation of an AbsNode
     */
    @Override
    public String toString() {
        return name + " ; "
                + state + " ; "
                + neighbours + " ; "
                + truths;
    }
}
