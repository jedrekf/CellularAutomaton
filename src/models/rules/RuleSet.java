package models.rules;

import models.Cell;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by jedrek on 05.05.16.
 * Singleton containing set of rules for a current automaton
 */
public class RuleSet implements Serializable{

    private List<Rule> rules = new ArrayList<Rule>();
    private int cellState;

    /**
     * Returns a current list of all Rules.
     * @return Current list of IRules.
     */
    public List<Rule> getList() {
        return rules;
    }

    public List<RuleSimple> getListSimple(){
        List<RuleSimple> l= new ArrayList<>();

        for(Rule r : rules){
            if(r.type() == "simple"){
                l.add((RuleSimple) r);
            }
        }
        return l;
    }

    public List<RuleAdvanced> getListAdvanced(){
        List<RuleAdvanced> l= new ArrayList<>();

        for(Rule r : rules){
            if(r.type() == "advanced"){
                l.add((RuleAdvanced) r);
            }
        }
        return l;
    }
    /**
     * Calculates the next state of a Cell based on a set of rules.
     *
     * @param oldGrid
     * @param cell
     * @return Next state of a cell
     */
    public Cell apply(HashMap<Point, Cell> oldGrid, Cell cell){
        int outcome;
        cellState = cell.getState();
        Iterator<Rule> ruleIterator = rules.iterator();
        while(ruleIterator.hasNext()){
            if((outcome = ruleIterator.next().evaluate(oldGrid, cell)) >= 0) {
                cellState = outcome;
                break; // The rules are not supposed to collide so the value can't change
            }
        }
        cell.setState(cellState);
        return cell;
    }
    /**
     * Validate if a rule can be added to a current set of rules.
     * @param rule
     * @return true if a rule can be added to a set of rules, false otherwise
     */
    public boolean validateRule(Rule rule){
        if(rule.type().compareTo("simple") == 0){
            RuleSimple ruleSimple = (RuleSimple)rule;
            Rule currOldRule;
            Iterator<Rule> ruleIterator = rules.iterator();
            while(ruleIterator.hasNext()){
                //check intersections
                currOldRule = ruleIterator.next();
                if(!isRuleSimpleCorrect(currOldRule.getExacts(), rule.getExacts()))
                    return false;
            }
            return true;
        }
        else{
            RuleAdvanced newRuleAdvanced = (RuleAdvanced) rule;
            Rule currOldRule;
            RuleAdvanced currOldAdvancedRule;
            Iterator<Rule> ruleIterator = rules.iterator();
            while(ruleIterator.hasNext()){
                //check intersections
                currOldRule = ruleIterator.next();
                if(currOldRule.type().compareTo("advanced") == 0){
                    currOldAdvancedRule = (RuleAdvanced) currOldRule;
                    if(isRuleAdvancedDuplicated(currOldAdvancedRule.getCells(), newRuleAdvanced.getCells()))
                        if(currOldAdvancedRule.getOutcome() != newRuleAdvanced.getOutcome())
                            return false;
                }
                else {
                    if (!isRuleSimpleCorrect(currOldRule.getExacts(), rule.getExacts()))
                        return false;
                }
            }
            return true;
        }
    }
    /**
     * Checks sets of Exact Rules for intersections with opposite outcomes
     * @param old_rule ExactRules representing the old rule
     * @param new_rule ExactRules representing the new rule
     * @return TRUE if a Rule can be added to a set of Rules, else FALSE
     */
    private boolean isRuleSimpleCorrect(RuleExact[] old_rule, RuleExact[] new_rule){

        for(int i=0; i<new_rule.length; i++){
            for(int j=0; j<old_rule.length; j++){
                if(new_rule[i].getAliveNeighbours() == old_rule[j].getAliveNeighbours()
                        && new_rule[i].getOutcome() != old_rule[j].getOutcome())
                    return false;
            }
        }
        return true;
    }
    /**
     * Checks if new_rule is a duplicate of a given one
     * @param old_rule Values of cells for a rule
     * @param new_rule Values of cells for a rule
     * @return If a rule is a duplicate it returns TRUE, else FALSE
     */
    private boolean isRuleAdvancedDuplicated(int[][] old_rule, int[][] new_rule){

        for(int i=0; i<old_rule.length; i++){
            for (int j = 0; j < old_rule[0].length; j++) {
                if(!(i==2 && j==2)){
                    if(old_rule[i][j] != new_rule[i][j])
                        return false;
                }
            }
        }
        return true;
    }
    /**
     * Adds a rule to the set of rules.
     * @param rule Rule to be added to the set.
     * @return True if the rule is added successfully, False if rejected.
     */
    public boolean add(Rule rule){
        if(validateRule(rule)) {
            rules.add(rule);
            return true;
        }
        return false;
    }
    /**
     * Removes a rule at index from the set of rules.
     * @param idx Index of a rule to be removed
     */
    public void remove(int idx){
        rules.remove(idx);
    }
    /**
     * Removes provided rule from the set
     * @param rule Rule to be deleted.
     */
    public void remove(Rule rule){
        rules.remove(rule);
    }

}
