package ca.mcgill.ecse321.project.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.List;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.project.service.TutoringAppService;
import ca.mcgill.ecse321.project.model.*;
import ca.mcgill.ecse321.project.ErrorStrings;
import ca.mcgill.ecse321.project.dao.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SessionTest {
	
	@Autowired
	private TutoringAppService service;

	@Autowired 
	private AvailabilityRepository availabilityRepository;
	@Autowired
	private CourseOfferingRepository courseOfferingRepository;  
	@Autowired
	private CourseRepository courseRepository;
	@Autowired
	private ReviewRepository reviewRepository;  
	@Autowired
	private RoleRepository roleRepository; 
	@Autowired
	private RoomRepository roomRepository; 
	@Autowired
	private SessionRepository sessionRepository; 
	@Autowired
	private UniversityRepository universityRepository; 
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TutorRepository tutorRepository; 
	@Autowired
	private StudentRepository studentRepository; 
	
	
	@Before
	public void setUp(){
		// create the necessary objects for session creation
		service.createUniversity("McGill", "3040 University");
		service.createCourse("Intro to Software","ECSE 321", service.getAllUniversities().get(0).getUniversityID());
		service.createCourseOffering(Term.Fall, 2019, service.getAllCourses().get(0).getCourseID());
		service.createUser("aName", "tutor.tester@mcgill.ca", 22, "5145555555");
		service.createUser("aName_student", "student.tester@mcgill.ca", 22, "5145555555");
		service.createTutor("username", "password", "tutor.tester@mcgill.ca", 12, 3, Education.highschool);
		service.createStudent("studentUser", "password2", "student.tester@mcgill.ca");
		service.createAvailability(Date.valueOf(LocalDate.now().plusDays(2)), Time.valueOf("11:11:11"), "username");
		service.createRoom(4);
	}
	
	@After
	public void clearDatabase() {
		// clear in order of dependencies
		sessionRepository.deleteAll();
		roomRepository.deleteAll();
		reviewRepository.deleteAll();
		courseOfferingRepository.deleteAll();
		courseRepository.deleteAll();
		universityRepository.deleteAll();
		availabilityRepository.deleteAll();
		roleRepository.deleteAll();
		tutorRepository.deleteAll();
		studentRepository.deleteAll();
		userRepository.deleteAll();
	}
	
	@Test
	public void testCreateSession() {
		 		
		Date date = Date.valueOf(LocalDate.now().plusDays(2));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();
		
		String studentUser = service.getAllStudents().get(0).getUsername();
		String tutorUser = service.getAllTutors().get(0).getUsername();

		try {
			service.createSession(coID, date, time, amountPaid, studentUser, tutorUser);
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}
		
		List<Session> allSessions = service.getAllSessions();
		
		
		assertEquals(1, allSessions.size());
		assertEquals(time, allSessions.get(0).getTime());
		assertEquals(amountPaid, allSessions.get(0).getAmountPaid(), 0.05);
		assertEquals(date, allSessions.get(0).getDate());
		assertEquals(coID, allSessions.get(0).getCourseOffering().getCourseOfferingID());
		assertEquals("studentUser", allSessions.get(0).getStudent().get(0).getUsername());
		assertEquals("username", allSessions.get(0).getTutor().getUsername());
		
	}
	
	@Test
	public void testUpdateSession() {
		//assertEquals(0, service.getAllSessions().size());
				
		Date date = Date.valueOf(LocalDate.now().plusDays(2));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}
		
		List<Session> allSessions = service.getAllSessions();

		assertEquals(1, allSessions.size());
		assertEquals(time, allSessions.get(0).getTime());
		assertEquals(amountPaid, allSessions.get(0).getAmountPaid(), 0.05);
		assertEquals(date, allSessions.get(0).getDate());
		assertEquals(coID, allSessions.get(0).getCourseOffering().getCourseOfferingID());
		assertEquals("studentUser", allSessions.get(0).getStudent().get(0).getUsername());
		assertEquals("username", allSessions.get(0).getTutor().getUsername());
		
		date = Date.valueOf("2021-02-01");
		time = Time.valueOf("10:10:10");
		amountPaid = 24;
		
		try {
			service.updateSession(allSessions.get(0).getSessionID(), coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}
		
		allSessions = service.getAllSessions();
		
		assertEquals(1, allSessions.size());
		assertEquals(time, allSessions.get(0).getTime());
		assertEquals(amountPaid, allSessions.get(0).getAmountPaid(), 0.05);
		assertEquals(date, allSessions.get(0).getDate());
		assertEquals(coID, allSessions.get(0).getCourseOffering().getCourseOfferingID());
		assertEquals("studentUser", allSessions.get(0).getStudent().get(0).getUsername());
		assertEquals("username", allSessions.get(0).getTutor().getUsername());
		
	}
	
	@Test
	public void testDeleteSession() {
		assertEquals(0, service.getAllSessions().size());
		
		 		
		Date date = Date.valueOf(LocalDate.now().plusDays(2));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}
		
		List<Session> allSessions = service.getAllSessions();
		assertEquals(1, allSessions.size());
		
		try {
			service.deleteSession(allSessions.get(0).getSessionID());
		} catch (IllegalArgumentException e) {
			// Check that no error occurred
			fail();
		}

		 allSessions = service.getAllSessions();

		assertEquals(0, allSessions.size());
	}
	
	@Test
	public void testCreateSessionNullCourseOffering() {
		assertEquals(0, service.getAllSessions().size());

		Date date = Date.valueOf(LocalDate.now().plusDays(2));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;

		String error = null;

		try {
			service.createSession(100, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_FindCourseOfferingByID, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());

	}
	
	@Test
	public void testCreateSessionNullTutor() {
		assertEquals(0, service.getAllSessions().size());

		Date date = Date.valueOf(LocalDate.now().plusDays(3));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		String error = null;

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "wrongusername");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_FindTutorByUsername, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());

	}
	
	@Test
	public void testCreateSessionNullStudent() {
		assertEquals(0, service.getAllSessions().size());

		Date date = Date.valueOf(LocalDate.now().plusDays(2));
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		String error = null;

		try {
			service.createSession(coID, date, time, amountPaid, "wrongstudentUser", "username");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_FindStudentByUsername, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());
	}
	
	
	@Test
	public void testCreateSessionInvalidAmountPaid() {
		assertEquals(0, service.getAllSessions().size());

		long millis=System.currentTimeMillis();  		
		Date date = new java.sql.Date(millis);
		Time time = new java.sql.Time(millis);
		double amountPaid = -23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		String error = null;

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_NegativeAmountPaid, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());
	}
	
	@Test
	public void testCreateSessionNullTime() {
		assertEquals(0, service.getAllSessions().size());

		Date date = Date.valueOf("2020-02-01");
		
		Time time = null;
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		String error = null;

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_DateTime, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());
	}
	
	@Test
	public void testCreateSessionNullDate() {
		assertEquals(0, service.getAllSessions().size());
		
		Date date = null;
		
		Time time = Time.valueOf("11:11:11");
		double amountPaid = 23;
		int coID = service.getAllCourseOfferings().get(0).getCourseOfferingID();

		String error = null;

		try {
			service.createSession(coID, date, time, amountPaid, "studentUser", "username");
		} catch (IllegalArgumentException e) {
			error = e.getMessage();
		}

		// check error
		assertEquals(ErrorStrings.Invalid_Session_DateTime, error);

		// check no change in memory
		assertEquals(0, service.getAllSessions().size());
	}
	
}