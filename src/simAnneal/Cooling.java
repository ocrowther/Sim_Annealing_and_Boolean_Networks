package simAnneal;

/**
 * Set of cooling schedule methods based on the equations detailed by Peprah et al. (2017) 'An Optimal Cooling Schedule
 * Using a Simulated Annealing Based Approach', and Lundy & Mees (1984) 'Convergence of an annealing algorithm'
 */
public class Cooling {

    /**
     * A linear cooling schedule that decrements the temperature by a fixed amount each iteration, reaching a specified
     * minimum temperature on the last iteration.
     * @param startingTemp initial temperature
     * @param endTemp desired final temperature
     * @param iterationLimit number of iterations
     * @param currentTemp temperature set by the previous iteration
     * @return temperature for the current iteration
     */
    public static Double linearCool(Integer startingTemp, Integer endTemp, Integer iterationLimit, Double currentTemp){
        return (currentTemp - ((startingTemp - endTemp) / (iterationLimit - 1)));
    }

    /**
     * Reduces temperature geometrically according to a supplied cooling factor. Cooling factor should be a value
     * between 0.8 and 0.99 (see Peprah et al.).
     * @param iteration current iteration
     * @param coolingFactor constant governing the rate of change
     * @param currentTemp temperature set by the previous iteration
     * @return temperature for the current iteration
     */
    public static Double expCool(Integer iteration, Double coolingFactor, Double currentTemp){
        return Math.pow(coolingFactor, iteration) * currentTemp;
    }

    /**
     * A cooling schedule based on that proposed in Lundy & Mees. The cooling factor should be a small number (e.g.
     * 0.001).
     * @param currentTemp temperature set by the previous iteration
     * @param coolingFactor constant governing the rate of change
     * @return temperature for the current iteration
     */
    public static Double lundyCool(Double currentTemp, Double coolingFactor){
        return (currentTemp)/(1 + (coolingFactor * currentTemp));
    }
}
