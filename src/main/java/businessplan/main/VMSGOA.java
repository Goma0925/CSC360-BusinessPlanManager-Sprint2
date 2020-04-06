package businessplan.main;

public class VMSGOA extends BusinessPlan
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public VMSGOA(){}
	public VMSGOA(String name, String department, int year)  {
		super(name, department, year) ;
	}
	public void addLeadingPart(Part part1) {
		if (part1.typeName=="vision") {
			this.LeadingPart.add(part1);
		}
	}
	//only allows a sequence of vision,mission,strategy,goal,objective,action pattern.
	public void addPart(Part part1,Part part2) {
		if ((part1.typeName=="mission"& part2.typeName=="vision")
			||(part1.typeName=="strategy"& part2.typeName=="mission")
			||(part1.typeName=="goal"& part2.typeName=="strategy")
			||(part1.typeName=="objective"& part2.typeName=="goal")
			||(part1.typeName=="action"& part2.typeName=="objective")) {
			part1.parent=part2;
			part2.children.add(part1);
		}
	}

}
