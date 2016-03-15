package shestak.maksym.schedule.src.max;

public class Class {
	String title;
	String type;
	String auditorium;
	String lecturer;
	String group;
	String classN;

	public Class(String title, String type, String auditorium, String lecturer, String group, String classN) {
		this.title = title;
		this.type = type;
		this.auditorium = auditorium;
		this.lecturer = lecturer;
		this.group = group;
		this.classN = classN;
	}

	@Override
	public String toString() {
		return "Class{" +
				"title='" + title + '\'' +
				", type='" + type + '\'' +
				", auditorium='" + auditorium + '\'' +
				", lecturer='" + lecturer + '\'' +
				", group='" + group + '\'' +
				", classN='" + classN + '\'' +
				'}';
	}
}