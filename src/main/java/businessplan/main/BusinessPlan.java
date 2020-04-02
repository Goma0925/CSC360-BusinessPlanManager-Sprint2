package businessplan.main;
import java.util.*;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.*;

public abstract class BusinessPlan {
	public String name;
	public String description;
	public int year;
	public int timeStamp=0;
	public ArrayList<Part> LeadingPart;
	public String assessment;	

	public BusinessPlan() {}
	public BusinessPlan(String name) {
		super();
		this.name = name;
		this.year=LocalDate.now().getYear();
		this.LeadingPart=new ArrayList<Part>();	
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
	
	public void XMLEncode(String filename) {
		XMLEncoder encoder=null;
		try{
			encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(filename)));
			}
		catch(FileNotFoundException fileNotFound){
				System.out.println("ERROR: While Creating or Opening the File dvd.xml");
			}
			encoder.writeObject(this);
			encoder.close();
	}
	public BusinessPlan XMLDecode(String filename) {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(filename)));
		} catch (FileNotFoundException e) {
			System.out.println("ERROR: File "+filename+" not found");
		}
		return (BusinessPlan)decoder.readObject();
	}
}
