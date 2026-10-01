import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class School {
    private String name;
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();

    public School(String name) { this.name = name; }

    public void add(Student student) { students.add(student); }

    public void add(Instructor instructor) { instructors.add(instructor); }

    public void add(Subject subject) { subjects.add(subject); }

    public void add(Course course) { courses.add(course); }

    public void remove(Student student) {
        for (Student s : students ) {
            if (s.equals(student)) {
                students.remove(s);
                break;
            }
        }
    }

    public void remove(Instructor instructor) {
        for (Instructor i : instructors) {
            if (i.equals(instructor)) {
                instructors.remove(i);
                break;
            }
        }
    }

    public void remove(Subject subject) {
        for (Subject s : subjects ) {
            if (s.equals(subject)) {
                subjects.remove(s);
                break;
            }
        }
    }

    public void remove(Course course) {
        for (Course c : courses) {
            if (c.equals(course)) {
                courses.remove(c);
                break;
            }
        }
    }

    public ArrayList<Student> getStudents() { return students; }

    public ArrayList<Instructor> getInstructors() { return instructors; }

    public ArrayList<Subject> getSubjects() { return subjects; }

    public ArrayList<Course> getCourses() { return courses; }

    @Override
    public String toString() {
        // prints summary of school and relationships
        // name
        // student A studies X
        // demonstrator B with specialism C teaches Z
        System.out.println("Name of school: " + name);
        System.out.println();
        System.out.println("=======================");
        System.out.println("Student Information:");
        System.out.println("=======================");
        for (Student s : getStudents()) {
            System.out.println("Student: " + s.getName());
            System.out.println("Age: " + s.getAge());
            System.out.println("Gender: " + s.getGender());
            System.out.println("Certificates: " + s.getCertificates());
            System.out.println("Enrolled course: " + enrolledCourse(s));
            System.out.println("----------------------");
        }
        System.out.println();
        System.out.println("=======================");
        System.out.println("Instructor Information:");
        System.out.println("=======================");
        for (Instructor i : getInstructors()) {
            System.out.println("Instructor: " + i.getName());
            System.out.println("Age: " + i.getAge());
            System.out.println("Gender: " + i.getGender());
            System.out.println("Subject: " + instructorCourse(i));
            System.out.println("Subjects able to teach: " + instructorTeaches(i));
            System.out.println("----------------------");
        }
        System.out.println();
        System.out.println("=======================");
        System.out.println("Subject Information:");
        System.out.println("=======================");
        for (Subject s : getSubjects()) {
            System.out.println("Subject ID: " + s.getID());
            System.out.println("Description: " + s.getDescription());
            System.out.println("Specialism: " + s.getSpecialism());
            System.out.println("Duration: " + s.getDuration() + " days");
            System.out.println("----------------------");
        }
        System.out.println();
        System.out.println("=======================");
        System.out.println("Course Information:");
        System.out.println("=======================");
        for (Course c : getCourses()) {
            System.out.println("Subject: " + c.getSubject().getID());
            System.out.println("Size: " + c.getSize());
            System.out.println("Current status: " + courseStatus(c.getStatus()));
            System.out.println("Students: " + studentStudying(c));
            System.out.println("----------------------");
        }
        System.out.println("==========END OF REPORT=========");

        return null;
    }

    public void aDayAtSchool() {
        // if subject has no course, create course for it
        Set<Subject> allSubjects = new HashSet<>(getSubjects());
        // go through courses; add subjects to set#
        Set<Subject> courseSubjects = new HashSet<>();
        for (Course c : getCourses()) {
            courseSubjects.add(c.getSubject());
        }
        // check if subject has an assigned course
        for (Subject s : allSubjects) {
            if (!courseSubjects.contains(s)) { // no assigned course
                // create course for subject
                courses.add(new Course(s, 2));
            }
        }

        Iterator<Course> it = getCourses().iterator();
        while (it.hasNext()) {
            Course c = it.next();
            // assign instructors to courses without one
            if (!c.hasInstructor()) {
                for (Instructor i: getInstructors()) {
                    if (i.getAssignedCourse() == null && c.setInstructor(i)) {
                        i.assignCourse(c);
                        break;
                    }
                }
            }
            // advance each course by a day
            c.aDayPasses();

            // remove finished/cancelled courses
            if (c.isCancelled() || c.getStatus() == 0) { it.remove(); }
        }

        // assign free students to courses they can join
        Set<Student> studyingStudents = allStudyingStudents(); // get set of all students studying a course
        for (Student s : getStudents()) {
            // for each student, check if they are in a course
            // if not, check if they have already graduated from the course and course full
            // if both false, add student to course
            if (!studyingStudents.contains(s)) {
                for (Course c : getCourses()) {
                    // check if student is currently studying subject

                    if (!s.hasCertificate(c.getSubject()) && !c.getStudents().contains(s) && c.enrolStudent(s)) {
                        break;
                    }
                }
            }
        }
    }

    // OWN METHODS
    public String enrolledCourse(Student student) {
        // returns subject id of course given student is studying
        for (Course c : getCourses()) {
            if (c.getStudents().contains(student)) {
                return String.valueOf(c.getSubject().getID());
            }
        }
        return "Not enrolled in any courses";
    }

    public String instructorCourse(Instructor instructor) {
        if (instructor.getAssignedCourse() == null) return "No assigned course";
        else return String.valueOf(instructor.getAssignedCourse().getSubject().getID());
    }

    public String instructorTeaches(Instructor instructor) {
        String subjects = "";
        for (Subject s : getSubjects()) {
            if (instructor.canTeach(s)) { subjects += s.getID() + " "; }
        }
        if (subjects.isEmpty()) { subjects = "None";}

        return subjects;
    }

    public String courseStatus(int status) {
        if (status < 0) {
            return Math.abs(status) + " days until starts";
        } else if (status > 0) {
            return status + " days left to run";
        } else return "Course finished";
    }

    public ArrayList<String> studentStudying(Course course) {
        // returns list of all students studying given course
        ArrayList<String> students = new ArrayList<>();
        for (Student s : course.getStudents()) {
            students.add(s.getName());
        }
        return students;
    }

    public Set<Student> allStudyingStudents() {
        Set<Student> studyingStudents = new HashSet<>();
        for (Course c : courses) {
            studyingStudents.addAll(c.getStudents());
        }
        return studyingStudents;
    }
}
