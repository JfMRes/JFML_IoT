package jfml_iot.test;
import jfml.*;
import jfml.knowledgebase.*;
import jfml.knowledgebase.variable.*;
import jfml.rule.*;
import jfml.term.*;
import jfml.rulebase.*;
import java.io.File;

public class JFML_PotentiometerStepper {

	public static void main(String[] args) {
		
		FuzzyInferenceSystem PotentiometerStepper = new FuzzyInferenceSystem("PotentiometerStepper");
		
		KnowledgeBaseType kb = new KnowledgeBaseType();
		PotentiometerStepper.setKnowledgeBase(kb);
	    
	    FuzzyVariableType potentiometer = new FuzzyVariableType("potentiometer", 0, 100f);
	    
	    FuzzyTermType left_pot = new FuzzyTermType("left", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 50f}));
	    FuzzyTermType rigth_pot = new FuzzyTermType("rigth", FuzzyTermType.TYPE_rightLinearShape,(new float[] {50f, 100f}));
	    potentiometer.addFuzzyTerm(left_pot);
	    potentiometer.addFuzzyTerm(rigth_pot);
	    
	    kb.addVariable(potentiometer);
	    
	    FuzzyVariableType stepper = new FuzzyVariableType("stepper", 0f, 100f);
	    stepper.setType("OUTPUT");
	    
	    FuzzyTermType stepper_left = new FuzzyTermType("left", FuzzyTermType.TYPE_singletonShape,(new float[] {0f}));
	    FuzzyTermType stepper_rigth = new FuzzyTermType("rigth", FuzzyTermType.TYPE_singletonShape,(new float[] {100f}));
	    stepper.addFuzzyTerm(stepper_left);
	    stepper.addFuzzyTerm(stepper_rigth);
	    kb.addVariable(stepper);
	    
	    
	    MamdaniRuleBaseType rules = new MamdaniRuleBaseType("rules");
	    
	    
	    FuzzyRuleType rule1 = new FuzzyRuleType("rule1", "or", "MAX", 1.0f);
	    
	    AntecedentType ant1 = new AntecedentType();
	    ant1.addClause(new ClauseType(potentiometer, left_pot));
	    rule1.setAntecedent(ant1);
	    
	    ConsequentType con1 = new ConsequentType();
	    con1.addThenClause(stepper, stepper_left);
	    rule1.setConsequent(con1);
	    
	    rules.addRule(rule1);
	    
	    FuzzyRuleType rule2 = new FuzzyRuleType("rule2", "or", "MAX", 1.0f);
	    
	    AntecedentType ant2 = new AntecedentType();
	    ant2.addClause(new ClauseType(potentiometer, rigth_pot));
	    rule2.setAntecedent(ant2);

	    ConsequentType con2 = new ConsequentType();
	    con2.addThenClause(stepper, stepper_rigth);
	    rule2.setConsequent(con2);
	    
	    rules.addRule(rule2);
	    
	    PotentiometerStepper.addRuleBase(rules);
	    
	    
	    File xml = new File("./testXML/PotentiometerStepper.xml");
	    JFML.writeFSTtoXML(PotentiometerStepper, xml);
	    
// 		Test some values 
	    
	    PotentiometerStepper.getVariable("potentiometer").setValue(0f);
	    PotentiometerStepper.evaluate();
	    System.out.println(PotentiometerStepper.getVariable("stepper").getValue());
	    
	    PotentiometerStepper.getVariable("potentiometer").setValue(100f);
	    PotentiometerStepper.evaluate();
	    System.out.println(PotentiometerStepper.getVariable("stepper").getValue());
	    
	    

	}

}
