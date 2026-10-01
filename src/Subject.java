public class Subject {
    private int id, specialism, duration;
    private String description;

    public Subject(String description, int id, int specialism, int duration) {
        this.description = description;
        this.id = id;
        this.specialism = specialism;
        this.duration = duration;
    }

    public int getID() { return id; }

    public int getSpecialism() { return specialism; }

    public int getDuration() { return duration; }

    public String getDescription() { return description; }

    public void setDescription(String description) {
        this.description = description;
    }
}
