import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;

public class Administrator {
    Administrator admin;
    School school;

    public Administrator(School school) {
        this.school = school;
    }

    public void run() {
        while (true) {
            admitStudents();
            admitInstructor();
            school.aDayAtSchool();
            freeInstructors();
            freeStudents();
        }
    }

    public void run(int days) {
        for (int i = 0; i < days; i++) {
            admitStudents();
            admitInstructor();
            school.aDayAtSchool();
            freeInstructors();
            freeStudents();

            // report
            school.toString();
        }
    }

    public static void main(String[] args) {
        // filename is args[0], numdays is args[1] (if given)
        if (args.length == 0) {
            School school1 = new School("New School");
            Student student1 = new Student("Jeremy", 'M', 18);
            school1.add(student1);
            Student student2 = new Student("Jade", 'F', 22);
            school1.add(student2);
            Subject subject1 = new Subject("Comp123", 30, 2, 2);
            school1.add(subject1);
            Subject subject2 = new Subject("Quant2345", 12, 1, 4);
            school1.add(subject2);
            Subject subject3 = new Subject("Elec2384", 23, 2, 3);
            school1.add(subject3);
            Course course1 = new Course(subject1, 4);
            school1.add(course1);
            Instructor instructor1 = new Teacher("Rosie", 'F', 53);
            school1.add(instructor1);
            Instructor instructor2 = new OOTrainer("Bob", 'M', 28);
            school1.add(instructor2);

            Administrator admin = new Administrator(school1);
            admin.run(1000);
        } else if (args.length == 1 || args.length == 2) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(args[0]));

                School mySchool;
                Administrator admin = null;

                String line = reader.readLine();
                while (line != null) {
                    String[] lineSplit = line.split(":");

                    if (Objects.equals(lineSplit[0], "school")) {
                        mySchool = new School(lineSplit[1]);
                        admin = new Administrator(mySchool);
                    } else if (Objects.equals(lineSplit[0], "subject") && admin != null) {
                        admin.addSubject(lineSplit[1].split(","));
                    } else if (Objects.equals(lineSplit[0], "student") && admin != null) {
                        admin.addStudent(lineSplit[1].split(","));
                    } else if (Objects.equals(lineSplit[0], "Teacher") && admin != null) {
                        admin.addTeacher(lineSplit[1].split(","));
                    } else if (Objects.equals(lineSplit[0], "Demonstrator") && admin != null) {
                        admin.addDemonstrator(lineSplit[1].split(","));
                    } else if (Objects.equals(lineSplit[0], "OOTrainer") && admin != null) {
                        admin.addOOTrainer(lineSplit[1].split(","));
                    } else if (Objects.equals(lineSplit[0], "GUITrainer") && admin != null) {
                        admin.addGUITrainer(lineSplit[1].split(","));
                    }

                    line = reader.readLine();

                    if (args.length == 2) { admin.run(Integer.parseInt(args[1])); }
                    else admin.run();
                }
            } catch (IOException e) {
                System.out.println("Error reading file");
            } catch (NullPointerException e) {
                System.out.println("Error in assigning admin");
            }
        } else {
            System.out.println("Invalid number of arguments given, must be between 0-2 (inclusive)");
        }



    }

    public void saveSimulation(String filename) {

    }

    // OWN METHODS
    private void admitStudents() {
        // admit up to 2 new students
        int numStudents = (int) (Math.random() * 3);
        if (numStudents == 1) {
            school.add(new Student("Student1", 'M', 20));
        } else if (numStudents == 2) {
            school.add(new Student("Student1", 'M', 20));
            school.add(new Student("Student2", 'F', 21));
        }
    }

    private void admitInstructor() {
        // randomly admit up to one instructor
        double prob = Math.random();

        if (prob < 0.2) {
            school.add(new Teacher("Teacher1", 'M', 35));
        }
        if (prob < 0.3) {
            school.add(new Demonstrator("Demonstrator1", 'F', 24));
        }
        if (prob < 0.35) {
            school.add(new OOTrainer("OOTrainer1", 'M', 42));
        }
        if (prob < 0.40) {
            school.add(new GUITrainer("GUITrainer1", 'F', 56));
        }
    }

    private void freeInstructors() {
        // instructors with no assigned course have 20% chance of leaving school

        ArrayList<Instructor> remove = new ArrayList<>();
        for (Instructor i : school.getInstructors()) {
            if (i.getAssignedCourse() == null) {
                if (Math.random() < 0.2) {
                    remove.add(i);
                }
            }
        }
        for (Instructor i : remove) {
            school.remove(i);
        }
    }

    private void freeStudents() {
        // get all subject ids in the school
        ArrayList<Integer> subjectIDs = new ArrayList<>();
        for (Subject s : school.getSubjects()) {
            subjectIDs.add(s.getID());
        }

        // students leave if have all certificates or 0.05% of leaving
        ArrayList<Student> remove = new ArrayList<>();
        for (Student s : school.getStudents()) {
            if ((s.getCertificates()).containsAll(subjectIDs) || Math.random() < 0.0005) {
                remove.add(s);
            }
        }
        for (Student s : remove) {
            school.remove(s);
        }
    }

    private void addSubject(String[] params) {
        school.add(new Subject(params[0],
                   Integer.parseInt(params[1]),
                   Integer.parseInt(params[2]),
                   Integer.parseInt(params[3])));
    }

    private void addStudent(String[] params) {
        school.add(new Student(params[0], params[1].charAt(0), Integer.parseInt(params[2])));
    }

    private void addTeacher(String[] params) {
        school.add(new Teacher(params[0], params[1].charAt(0), Integer.parseInt(params[2])));
    }

    private void addDemonstrator(String[] params) {
        school.add(new Demonstrator(params[0], params[1].charAt(0), Integer.parseInt(params[2])));
    }

    private void addOOTrainer(String[] params) {
        school.add(new OOTrainer(params[0], params[1].charAt(0), Integer.parseInt(params[2])));
    }

    private void addGUITrainer(String[] params) {
        school.add(new GUITrainer(params[0], params[1].charAt(0), Integer.parseInt(params[2])));
    }
}
