/**
 * 
 */

package businessplan.main;

/**
 * @author CrazyMKing
 *
 */
public class VMOSA extends BusinessPlan {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public VMOSA(){}
	public VMOSA(String name, String department, int year)  {
		super(name, department, year) ;
	}
	public void addLeadingPart(Part part1) {
		if (part1.typeName=="vision") {
			this.LeadingPart.add(part1);
		}
	}
	//only allows a sequence of vision,mission,objective,strategy,action pattern.
	public void addPart(Part part1,Part part2) {
		if ((part1.typeName=="mission"& part2.typeName=="vision") 
			||(part1.typeName=="objective"& part2.typeName=="mission")
			||(part1.typeName=="strategy"& part2.typeName=="objective")
			||(part1.typeName=="action"& part2.typeName=="strategy")){
			part1.parent=part2;
			part2.children.add(part1);
			//why the sequence matters? It will cause nullPointerError.
		}
	}
}
