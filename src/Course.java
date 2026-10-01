import java.util.ArrayList;

public class Course {
    private Subject subject;
    private int daysUntilStarts, daysToRun;
    private ArrayList<Student> students = new ArrayList<>();
    private Instructor instructor;
    private boolean cancelled = false;

    public Course(Subject subject, int daysUntilStarts) {
        this.subject = subject;
        this.daysUntilStarts = daysUntilStarts;
        daysToRun = subject.getDuration();
    }

    public Subject getSubject() { return subject; }

    public int getStatus() {
        if (daysUntilStarts > 0)
            // course has not started yet
            return -1 * daysUntilStarts;
        else if (daysToRun > 0)
            // course has started
            return daysToRun;
        else
            // course is finished
            return 0;
    }

    public void aDayPasses() {
        if (daysUntilStarts > 0) {
            // course has not started yet
            daysUntilStarts--;
        } else if (daysToRun > 0) {
            // course in progress (or about to start)
            // check if course can start
            if (!hasInstructor() || students.isEmpty()) {
                cancelled = true;
                if (instructor != null) {
                    instructor.unassignCourse();
                    instructor = null;
                }
            } else {
                // course in progress
                daysToRun--;
            }
        }

        if (getStatus() == 0 && !cancelled) {
            for (Student s : students) {
                s.graduate(getSubject());
            }
            instructor.unassignCourse();
            instructor = null;
        }
    }

    public boolean enrolStudent(Student student) {
        if (students.size() == 3 || getStatus() >= 0) {
            return false;
        } else {
            students.add(student);
            return true;
        }
    }

    public int getSize() {
        return students.size();
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public boolean setInstructor(Instructor instructor) {
        if (instructor.canTeach(subject)) {
            this.instructor = instructor;
            return true;
        } else return false;
    }

    public boolean hasInstructor() {
        // if instructor == null, returns false
        return instructor != null;
    }

    public boolean isCancelled() {
        return cancelled;
    }

}
