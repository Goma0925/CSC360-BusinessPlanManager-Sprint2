package businessplan.main;
import java.util.*;


public abstract class Part {

	public String typeName;
	public String description;
	public Part parent=null;
	public ArrayList<Part> children=new ArrayList<Part>();
	
	public Part() {}
	
	/**
	 * @return the name
	 */
	public String getTypeName() {
		return typeName;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the parent
	 */
	public Part getParent() {
		return parent;
	}
	/**
	 * @param parent the parent to set
	 */
	public void setParent(Part parent) {
		this.parent = parent;
	}
	/**
	 * @return the children
	 */
	public ArrayList<Part> getChildren() {
		return children;
	}
	@Override
	public String toString()
	{
		return "Part [typeName=" + typeName + ", description=" + description + ", children=" + children + "]";
	}

}
