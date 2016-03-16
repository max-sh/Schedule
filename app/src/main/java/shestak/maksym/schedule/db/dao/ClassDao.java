package shestak.maksym.schedule.db.dao;

public class ClassDao {
    public String title;
    public String type;
    public String auditorium;
    public String lecturer;
    public String group;
    public String classN;
    public String date;

    public ClassDao(String title, String type, String auditorium, String lecturer, String group, String classN, String date) {
        this.title = title;
        this.type = type;
        this.auditorium = auditorium;
        this.lecturer = lecturer;
        this.group = group;
        this.classN = classN;
        this.date = date;
    }
}
