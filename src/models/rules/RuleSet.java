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
    public boolean validateRule(IRule rule){
        if(rule.type().compareTo("simple") == 0){
            RuleSimple ruleSimple = (RuleSimple)rule;
            IRule currOldRule;
            Iterator<IRule> ruleIterator = rules.iterator();
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
            IRule currOldRule;
            RuleAdvanced currOldAdvancedRule;
            Iterator<IRule> ruleIterator = rules.iterator();
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
        for(RuleExact new_exact : new_rule){
            for(RuleExact old_exact : old_rule){
                if(new_exact.getAliveNeighbours() == old_exact.getAliveNeighbours()
                        && new_exact.getOutcome() != old_exact.getOutcome())
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
     * @param rule
     */
    public void add(IRule rule){
        if(validateRule(rule))
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
