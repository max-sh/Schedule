package shestak.maksym.schedule.src.max;

import java.lang.*;
import java.util.ArrayList;
import java.util.List;

public class Day {
	String data;
	List<Class> classes;

	@Override
	public String toString() {
		return "Day{" +
				"data='" + data + '\'' +
				", classes=" + classes +
				'}';
	}

	public Day(String data) {
		classes = new ArrayList<>();
		this.data = data;
	}
}
