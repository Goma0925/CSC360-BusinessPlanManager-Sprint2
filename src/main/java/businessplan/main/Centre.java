package businessplan.main;
public class Centre extends BusinessPlan
{
	public Centre(){}
	public Centre(String name) {
		super(name);
	}
	public void addLeadingPart(Part part1) {
		if (part1.typeName=="goal") {
			this.LeadingPart.add(part1);
		}
	}
	//only allows a sequence of goal,slo,tool,target,results pattern.
	public void addPart(Part part1,Part part2) {
		if ((part1.typeName=="slo"& part2.typeName=="goal")
			||(part1.typeName=="tool"& part2.typeName=="slo") 
			||(part1.typeName=="target"& part2.typeName=="tool")
			||(part1.typeName=="results"& part2.typeName=="target")){
			part1.parent=part2;
			part2.children.add(part1);			
		}
	}
	
}
