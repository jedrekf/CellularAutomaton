package models.rules;

import com.sun.deploy.security.ruleset.Rule;
import models.Cell;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by jedrek on 05.05.16.
 * Singleton containing set of rules for a current automaton
 */
public class RuleSet {

    private List<IRule> rules = new ArrayList<IRule>();
    private int cellState;

    /**
     * Calculates the next state of a Cell based on a set of rules.
     * @param cell
     * @return Next state of a cell
     */
    public int apply(Cell cell){
        int outcome;
        cellState = cell.getState();
        Iterator<IRule> ruleIterator = rules.iterator();
        while(ruleIterator.hasNext()){
            if((outcome = ruleIterator.next().evaluate(cell.getNeighbours())) >= 0) {
                cellState = outcome;
                break; // The rules are not supposed to collide so the value can't change
            }
        }
        return cellState;
    }
    /**
     * Validate if a rule can be added to a current set of rules.
     * @param rule
     * @return true if a rule can be added to a set of rules, false otherwise
     */
    public boolean validate(IRule rule){
        if(rule.type().compareTo("simple") == 0){
            RuleSimple ruleS = (RuleSimple)rule;
            Iterator<IRule> ruleIterator = rules.iterator();
            while(ruleIterator.hasNext()){
                //ruleIterator.next().
            }

        }
        else{
            RuleAdvanced ruleA = (RuleAdvanced) rule;


        }
        return true;
    }

    /**
     * Adds a rule to the set of rules.
     * @param rule
     */
    public void add(IRule rule){
        rules.add(rule);
    }

    /**
     * Removes a rule from the set of rules.
     * @param idx
     */
    public void remove(int idx){
        rules.remove(idx);
    }

}
