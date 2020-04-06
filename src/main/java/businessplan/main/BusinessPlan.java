package businessplan.main;
import java.util.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.time.*;

public abstract class BusinessPlan implements Serializable{
	public String name;
	public String description;
	public String department;
	public int year;
	public int timeStamp;
	public ArrayList<Part> LeadingPart;
	public String assessment;
	private boolean isEditable;
    private static final long serialVersionUID = 237L;

	public BusinessPlan() {}
	public BusinessPlan(String name, String department, int year) {
		super();
		this.name = name;
		this.year=LocalDate.now().getYear();
		this.LeadingPart=new ArrayList<Part>();	
		this.timeStamp = 0;
		this.isEditable = true;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the timeStamp
	 */
	public int getTimeStamp() {
		return timeStamp;
	}
	/**
	 * @return the assessment
	 */
	public String getAssessment()
	{
		return assessment;
	}
	/**
	 * @param assessment the assessment to set
	 */
	public void setAssessment(String assessment)
	{
		this.assessment = assessment;
	}
	@Override
	public String toString()
	{
		return "BusinessPlan [name=" + name + ", description=" + description + ", year=" + year + ", timeStamp="
				+ timeStamp + ", LeadingPart=" + LeadingPart + ", assessment=" + assessment + "]";
	}
	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}
	/**
	 * @return the leadingPart
	 */
	public ArrayList<Part> getLeadingPart() {
		return LeadingPart;
	}

	// add parts
	public abstract void addLeadingPart (Part part1);
	//part 1 is the child of part 2
	public abstract void addPart (Part part1, Part part2);
	
	public void XMLEncode(File outFile) {
		XMLEncoder encoder=null;
		String filename = outFile.getAbsolutePath();
		try{
			encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			}
		catch(FileNotFoundException fileNotFound){
				System.out.println("ERROR: While Creating or Opening the File dvd.xml");
			}
			encoder.writeObject(this);
			encoder.close();
	}
	public static BusinessPlan XMLDecode(File inFile) {
		XMLDecoder decoder=null;
		String filename = inFile.getAbsolutePath();
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File "+filename+" not found");
		}
		return (BusinessPlan)decoder.readObject();
	}
	public String getDepartment()
	{
		return department;
	}
	public void setDepartment(String department)
	{
		this.department = department;
	}
	public boolean isEditable()
	{
		return this.isEditable;
	}
	public void setIsEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}
}
