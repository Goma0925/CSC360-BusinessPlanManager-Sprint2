package businessplan.main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;

import businessplan.main.Action;
import businessplan.main.BusinessPlan;
import businessplan.main.Goal;
import businessplan.main.Mission;
import businessplan.main.Objective;
import businessplan.main.Part;
import businessplan.main.Strategy;
import businessplan.main.VMOSA;
import businessplan.main.VMSGOA;
import businessplan.main.Vision;

class BPtest
{

	@Test
	void test()
	{
		//create & initialize the business plan object 1: VMOSA
		String department = "CS";
		BusinessPlan VMOSAplan = new VMOSA("VMOSAtest1", department, 2020);
		VMOSAplan.setName("VMOSAtest1 revised");
		VMOSAplan.setDescription("This is the VMOSA business Plan");
		VMOSAplan.setTimeStamp(00000);
		VMOSAplan.setYear(2020);
		VMOSAplan.setAssessment("Goal Achieved!");
		
		//create tests for set functions of business plan
		assertEquals("VMOSAtest1 revised",VMOSAplan.getName());
		assertEquals(00000,VMOSAplan.getTimeStamp());
		assertEquals(2020,VMOSAplan.getYear());
		assertEquals("This is the VMOSA business Plan",VMOSAplan.getDescription());
		assertEquals("Goal Achieved!",VMOSAplan.getAssessment());
		
		//create parts for object 1:VMOSA
		Part vision1=new Vision();
		Part vision2=new Vision();
		Part mission1=new Mission();
		Part objective1=new Objective();
		Part strategy1=new Strategy();
		Part action1=new Action();
		vision1.setDescription("This is the vision1 for VMOSA");
		vision2.setDescription("This is the vision2 for VMOSA");
		mission1.setDescription("This is the mission1 for VMOSA");
		objective1.setDescription("This is the objective1 for VMOSA");
		strategy1.setDescription("This is the strategy1 for VMOSA");
		action1.setDescription("This is the action1 for VMOSA");
		
		//create tests for setDescription function for those parts
		assertEquals("This is the vision1 for VMOSA",vision1.getDescription());
		assertEquals("This is the mission1 for VMOSA",mission1.getDescription());
		assertEquals("This is the objective1 for VMOSA",objective1.getDescription());
		assertEquals("This is the strategy1 for VMOSA",strategy1.getDescription());
		assertEquals("This is the action1 for VMOSA",action1.getDescription());
		
		//append visions, mission, objective, strategy, and action to the VMOSA.
		VMOSAplan.addLeadingPart(vision1);
		VMOSAplan.addLeadingPart(vision2);
		VMOSAplan.addPart(mission1,vision1);
		VMOSAplan.addPart(objective1,mission1);
		VMOSAplan.addPart(strategy1,objective1);
		VMOSAplan.addPart(action1,strategy1);
		
		//test the appending is correctly implemented or not.
		assertEquals(VMOSAplan.LeadingPart,VMOSAplan.getLeadingPart());
		assertEquals(VMOSAplan.LeadingPart.get(1),vision2);
		assertEquals("strategy",strategy1.getTypeName());
		assertEquals(null,vision2.getParent());
		assertEquals(vision1,mission1.getParent());
		assertEquals(mission1,vision1.getChildren().get(0));
		
		//create new parts to test the addPart function if we add parts in the wrong sequence.
		Part mission2=new Mission();
		Part action2=new Action();
		String VMOSAplanBefore=VMOSAplan.toString();
		VMOSAplan.addPart(action2,mission2);
		String VMOSAplanAfter=VMOSAplan.toString();
		assertEquals(VMOSAplanBefore,VMOSAplanAfter);
		
		//---------------------------
		//create & initialize the business plan object 2: VMSGOA
		BusinessPlan additionalPlan = new VMSGOA("test2", department, 2019);
		additionalPlan.setDescription("This is the VMSGOA business Plan");
		additionalPlan.setTimeStamp(000022);
		additionalPlan.setAssessment("Goal is not achieved!");

		//create tests for set functions of business plan
		assertEquals("test2",additionalPlan.getName());
		assertEquals(000022,additionalPlan.getTimeStamp());
		assertEquals("This is the VMSGOA business Plan",additionalPlan.getDescription());
		assertEquals("Goal is not achieved!",additionalPlan.getAssessment());

		//create parts for VMSGOA.
		Part vision=new Vision();
		Part mission=new Mission();
		Part strategy=new Strategy();
		Part goal=new Goal();
		Part objective=new Objective();
		Part action=new Action();
		vision.setDescription("This is the vision for VMSGOA");
		mission.setDescription("This is the mission for VMSGOA");
		objective.setDescription("This is the objective for VMSGOA");
		strategy.setDescription("This is the strategy for VMSGOA");
		action.setDescription("This is the action for VMSGOA");
		goal.setDescription("This is the goal for VMSGOA");
		
		//test the get functions of parts for VMSGOA
		assertEquals("action",action.getTypeName());
		assertEquals("This is the goal for VMSGOA",goal.getDescription());

		//append vision, mission, strategy, goal, objective, and action to the VMSGOA.
		additionalPlan.addLeadingPart(vision);
		additionalPlan.addPart(mission,vision);
		additionalPlan.addPart(strategy,mission);
		additionalPlan.addPart(goal,strategy);
		additionalPlan.addPart(objective,goal);
		additionalPlan.addPart(action,objective);
		
		//test the append method
		assertEquals(strategy,goal.getParent());
		assertEquals(objective,goal.getChildren().get(0));
		
		;
		//create new parts to test the addPart function if we add parts in the wrong sequence.
		Part goaltest2=new Goal();
		Part objectivetest2=new Objective();
		String additionalPlanBefore=additionalPlan.toString();
		VMOSAplan.addPart(goaltest2,objectivetest2);
		String additionalPlanAfter=additionalPlan.toString();
		assertEquals(additionalPlanBefore,additionalPlanAfter);
		
		//test XML: store & read back
		//Since we got the to string function in both BusinessPlan and Part class, 
		//we can use BusinessPlan.toString() to help us test whether two BusinessPlan is the same.
		File SERIALIZED_FILE = new File(System.getProperty("user.dir") + "/BusinessPlan1.xml");
		VMOSAplan.XMLEncode(SERIALIZED_FILE);
		BusinessPlan VMOSAplanDeCode=VMOSAplan.XMLDecode(SERIALIZED_FILE);
		System.out.println(VMOSAplanDeCode);
		System.out.println(VMOSAplan);
		assertEquals(VMOSAplanDeCode.toString(),VMOSAplan.toString());
		
	}
	
}
