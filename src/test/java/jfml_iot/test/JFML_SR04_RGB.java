package jfml_iot.test;
import jfml.*;
import jfml.knowledgebase.*;
import jfml.knowledgebase.variable.*;
import jfml.rule.*;
import jfml.term.*;
import jfml.rulebase.*;
import java.io.File;




public class JFML_SR04_RGB {

	public static void main(String[] args) {
		
		FuzzyInferenceSystem DistanceRGB = new FuzzyInferenceSystem("PotenciometroServo");
		
		KnowledgeBaseType kb = new KnowledgeBaseType();
		DistanceRGB.setKnowledgeBase(kb);
	    
	    FuzzyVariableType distance = new FuzzyVariableType("distance", 0, 100f);
	    
	    FuzzyTermType dist_low = new FuzzyTermType("low", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 30f}));
	    FuzzyTermType dist_med = new FuzzyTermType("med", FuzzyTermType.TYPE_triangularShape,(new float[] {30f,50f, 60f}));
	    FuzzyTermType dist_alta = new FuzzyTermType("alta", FuzzyTermType.TYPE_rightLinearShape,(new float[] {60f, 100f}));
	    distance.addFuzzyTerm(dist_low);
	    distance.addFuzzyTerm(dist_med);
	    distance.addFuzzyTerm(dist_alta);
	    
	    kb.addVariable(distance);
	    
	    // Rojo
	    FuzzyVariableType R = new FuzzyVariableType("R", 0f, 100f);
	    R.setType("OUTPUT");
	    
	    FuzzyTermType R_off = new FuzzyTermType("R_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    FuzzyTermType R_on = new FuzzyTermType("R_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    
	    R.addFuzzyTerm(R_off);
	    R.addFuzzyTerm(R_on);
	    kb.addVariable(R);
	    
	    // Verde
	    FuzzyVariableType G = new FuzzyVariableType("G", 0f, 100f);
	    G.setType("OUTPUT");
	    
	    FuzzyTermType G_off = new FuzzyTermType("G_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    FuzzyTermType G_on = new FuzzyTermType("G_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    
	    G.addFuzzyTerm(G_off);
	    G.addFuzzyTerm(G_on);
	    kb.addVariable(G);
	    
	    // Azul
	    FuzzyVariableType B = new FuzzyVariableType("B", 0f, 100f);
	    B.setType("OUTPUT");
	    
	    FuzzyTermType B_off = new FuzzyTermType("B_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    FuzzyTermType B_on = new FuzzyTermType("B_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    
	    B.addFuzzyTerm(B_off);
	    B.addFuzzyTerm(B_on);
	    kb.addVariable(B);
	    
	    
	    MamdaniRuleBaseType rules = new MamdaniRuleBaseType("rules");
	    
	    
	    FuzzyRuleType rule1 = new FuzzyRuleType("rule1", "or", "MAX", 1.0f);
	    AntecedentType ant1 = new AntecedentType();
	    ant1.addClause(new ClauseType(distance, dist_low));
	    rule1.setAntecedent(ant1);
	    
	    ConsequentType con1 = new ConsequentType();
	    con1.addThenClause(R, R_on);
	    rule1.setConsequent(con1);
	    rules.addRule(rule1);
	    
	    FuzzyRuleType rule2 = new FuzzyRuleType("rule2", "or", "MAX", 1.0f);
	    AntecedentType ant2 = new AntecedentType();
	    ant2.addClause(new ClauseType(distance, dist_med));
	    rule2.setAntecedent(ant2);
	    
	    ConsequentType con2 = new ConsequentType();
	    con2.addThenClause(G, G_on);
	    rule2.setConsequent(con2);
	    rules.addRule(rule2);
	    
	    FuzzyRuleType rule3 = new FuzzyRuleType("rule3", "or", "MAX", 1.0f);
	    AntecedentType ant3 = new AntecedentType();
	    ant3.addClause(new ClauseType(distance, dist_alta));
	    rule3.setAntecedent(ant3);
	    
	    ConsequentType con3 = new ConsequentType();
	    con3.addThenClause(B, B_on);
	    rule3.setConsequent(con3);
	    rules.addRule(rule3);
	    
	    
	    DistanceRGB.addRuleBase(rules);
	    
	    
	    File xml = new File("./testXML/SRO04_RGB.xml");
	    JFML.writeFSTtoXML(DistanceRGB, xml);
	    
//	    Test
	    
	    DistanceRGB.getVariable("distance").setValue(0f);
	    DistanceRGB.evaluate();
	    System.out.println(DistanceRGB.getVariable("R").getValue());
	    System.out.println(DistanceRGB.getVariable("G").getValue());
	    System.out.println(DistanceRGB.getVariable("B").getValue());
	    
	    DistanceRGB.getVariable("distance").setValue(50f);
	    DistanceRGB.evaluate();
	    System.out.println(DistanceRGB.getVariable("R").getValue());
	    System.out.println(DistanceRGB.getVariable("G").getValue());
	    System.out.println(DistanceRGB.getVariable("B").getValue());
	    
	    DistanceRGB.getVariable("distance").setValue(100f);
	    DistanceRGB.evaluate();
	    System.out.println(DistanceRGB.getVariable("R").getValue());
	    System.out.println(DistanceRGB.getVariable("G").getValue());
	    System.out.println(DistanceRGB.getVariable("B").getValue());
	    
	    
	    
	    
	    
	    

	}

}
