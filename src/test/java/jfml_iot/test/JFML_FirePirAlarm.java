package jfml_iot.test;
import jfml.*;
import jfml.JFML;
import jfml.knowledgebase.*;
import jfml.knowledgebase.variable.*;
import jfml.rule.*;
import jfml.term.*;
import jfml.rulebase.*;
import java.io.File;



public class JFML_FirePirAlarm {

	public static void main(String[] args) {
		
		FuzzyInferenceSystem FuzzySystem = new FuzzyInferenceSystem("FirePirAlarm");
		
		KnowledgeBaseType kb = new KnowledgeBaseType();
		FuzzySystem.setKnowledgeBase(kb);
		
//		Inputs
	    
	    FuzzyVariableType fire = new FuzzyVariableType("fire", 0f, 100f);
	    FuzzyTermType fire_on = new FuzzyTermType("fire_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    FuzzyTermType fire_off = new FuzzyTermType("fire_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    fire.addFuzzyTerm(fire_on);
	    fire.addFuzzyTerm(fire_off);
	    kb.addVariable(fire);
	    
	    FuzzyVariableType motion = new FuzzyVariableType("motion", 0f, 100f);
	    FuzzyTermType motion_on = new FuzzyTermType("motion_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    FuzzyTermType motion_off = new FuzzyTermType("motion_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    motion.addFuzzyTerm(motion_on);
	    motion.addFuzzyTerm(motion_off);
	    kb.addVariable(motion);
	    
	    FuzzyVariableType alarm = new FuzzyVariableType("alarm", 0f, 100f);
	    FuzzyTermType alarm_on = new FuzzyTermType("alarm_on", FuzzyTermType.TYPE_rightLinearShape,(new float[] {90f, 100f}));
	    FuzzyTermType alarm_off = new FuzzyTermType("alarm_off", FuzzyTermType.TYPE_leftLinearShape,(new float[] {0f, 10f}));
	    alarm.addFuzzyTerm(alarm_on);
	    alarm.addFuzzyTerm(alarm_off);
	    kb.addVariable(alarm);
	    
//	    Outputs
	    
	    FuzzyVariableType buzzer = new FuzzyVariableType("buzzer", 0f, 100f);
	    buzzer.setType("OUTPUT");
	    
	    FuzzyTermType buzzer_off = new FuzzyTermType("buzzer_off", FuzzyTermType.TYPE_singletonShape,(new float[] {0f}));
	    FuzzyTermType buzzer_on = new FuzzyTermType("buzzer_on", FuzzyTermType.TYPE_singletonShape,(new float[] {100f}));
	    
	    buzzer.addFuzzyTerm(buzzer_off);
	    buzzer.addFuzzyTerm(buzzer_on);
	    kb.addVariable(buzzer);
	    
	    FuzzyVariableType red = new FuzzyVariableType("red", 0f, 100f);
	    red.setType("OUTPUT");
	    
	    FuzzyTermType red_off = new FuzzyTermType("red_off", FuzzyTermType.TYPE_singletonShape,(new float[] {0f}));
	    FuzzyTermType red_on = new FuzzyTermType("red_on", FuzzyTermType.TYPE_singletonShape,(new float[] {100f}));
	    
	    red.addFuzzyTerm(red_off);
	    red.addFuzzyTerm(red_on);
	    kb.addVariable(red);
	    
	    FuzzyVariableType green = new FuzzyVariableType("green", 0f, 100f);
	    green.setType("OUTPUT");
	    
	    FuzzyTermType green_off = new FuzzyTermType("green_off", FuzzyTermType.TYPE_singletonShape,(new float[] {0f}));
	    FuzzyTermType green_on = new FuzzyTermType("green_on", FuzzyTermType.TYPE_singletonShape,(new float[] {100f}));
	    
	    green.addFuzzyTerm(green_off);
	    green.addFuzzyTerm(green_on);
	    kb.addVariable(green);
	    
	    FuzzyVariableType blue = new FuzzyVariableType("blue", 0f, 100f);
	    blue.setType("OUTPUT");
	    
	    FuzzyTermType blue_off = new FuzzyTermType("blue_off", FuzzyTermType.TYPE_singletonShape,(new float[] {0f}));
	    FuzzyTermType blue_on = new FuzzyTermType("blue_on", FuzzyTermType.TYPE_singletonShape,(new float[] {100f}));
	    
	    blue.addFuzzyTerm(blue_off);
	    blue.addFuzzyTerm(blue_on);
	    kb.addVariable(blue);
	    
	    
//	    RB
	    
	    MamdaniRuleBaseType rules = new MamdaniRuleBaseType("rules");
	    
//	    Emergency ON

	    ConsequentType emergency = new ConsequentType();
	    emergency.addThenClause(red,red_on);
	    emergency.addThenClause(green,green_off);
	    emergency.addThenClause(blue,blue_off);
	    emergency.addThenClause(buzzer,buzzer_on);
	    
//	    Alarm ON no emergency
	    
	    ConsequentType alarmON = new ConsequentType();
	    alarmON.addThenClause(red,red_off);
	    alarmON.addThenClause(green,green_off);
	    alarmON.addThenClause(blue,blue_on);
	    alarmON.addThenClause(buzzer,buzzer_off);
	    
	    
//	    Alarm OFF and no emergency
	    
	    ConsequentType alarmOFF = new ConsequentType();
	    alarmOFF.addThenClause(red,red_off);
	    alarmOFF.addThenClause(green,green_on);
	    alarmOFF.addThenClause(blue,blue_off);
	    alarmOFF.addThenClause(buzzer,buzzer_off);
	    
	    
	    
	
//	    Rule 1
	    FuzzyRuleType rule1 = new FuzzyRuleType("rule1", "or", "MAX", 1.0f);
	    
	    AntecedentType ant1 = new AntecedentType();
	    ant1.addClause(new ClauseType(fire,fire_on));
	    
	    rule1.setAntecedent(ant1);
	    
	    rule1.setConsequent(emergency);
	    rules.addRule(rule1);
	    
//	    Rule 2
	    FuzzyRuleType rule2 = new FuzzyRuleType("rule2", "and", "min", 1.0f);
////	    
	    AntecedentType ant2 = new AntecedentType();
	    ant2.addClause(new ClauseType(motion,motion_on));
	    ant2.addClause(new ClauseType(alarm,alarm_on));
	    
	    rule2.setAntecedent(ant2);
	    rule2.setConsequent(emergency);

	    rules.addRule(rule2);
//	    
//	    Rule 3
	    FuzzyRuleType rule3 = new FuzzyRuleType("rule3", "and", "MIN", 1.0f);
//	    
	    AntecedentType ant3 = new AntecedentType();
	    ant3.addClause(new ClauseType(fire,fire_off));
	    ant3.addClause(new ClauseType(alarm,alarm_off));
//	    
	    rule3.setAntecedent(ant3);
//	    
	    rule3.setConsequent(alarmOFF);
	    rules.addRule(rule3);
	    
//	    Rule 4
	    FuzzyRuleType rule4 = new FuzzyRuleType("rule4", "and", "MIN", 1.0f);
//	    
	    AntecedentType ant4 = new AntecedentType();
	    ant4.addClause(new ClauseType(alarm,alarm_on));
	    ant4.addClause(new ClauseType(fire,fire_off));
	    ant4.addClause(new ClauseType(motion,motion_off));
	    
	    
	    
	    rule4.setAntecedent(ant4);
	    rule4.setConsequent(alarmON);
	    rules.addRule(rule4);
	    
	    FuzzySystem.addRuleBase(rules);
	    
	    File xml = new File("./testXML/FirePirAlarm.xml");
	    JFML.writeFSTtoXML(FuzzySystem, xml);
	    
	    for (float vfire = 0f; vfire <= 100f; vfire += 100f) {
	        for (float vmotion = 0f; vmotion <= 100f; vmotion += 100f) {
	            for (float valarm = 0f; valarm <= 100f; valarm += 100f) {
	            	
	                FuzzySystem.getVariable("fire").setValue(vfire);
	                FuzzySystem.getVariable("motion").setValue(vmotion);
	                FuzzySystem.getVariable("alarm").setValue(valarm);

	                FuzzySystem.evaluate();

	                System.out.println("Fire " + vfire + ", Motion " + vmotion + ", Alarm " + valarm + ":");
	                System.out.print("\nBuzzer: ");
	                System.out.println(FuzzySystem.getVariable("buzzer").getValue());
	                System.out.print("Red: ");
	                System.out.println(FuzzySystem.getVariable("red").getValue());
	                System.out.print("Green: ");
	                System.out.println(FuzzySystem.getVariable("green").getValue());
	                System.out.print("Blue: ");
	                System.out.println(FuzzySystem.getVariable("blue").getValue() + "\n\n");
	            }
	        }
	    }
	    

	}

}
