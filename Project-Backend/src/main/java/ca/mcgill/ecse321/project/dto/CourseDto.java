package ca.mcgill.ecse321.project.dto;


public class CourseDto {

	private String courseName;
	private String description;	
	
	//Data transfer object session.
	public CourseDto() {
	}

	public CourseDto(String courseName, String description) {
		this.courseName = courseName;
		this.description = description;
	}

	//get rating
	public String getCourseName() {
		return courseName;
	}
	
	public String getDescription() {
		return description;
	}


}
