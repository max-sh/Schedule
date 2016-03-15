package shestak.maksym.schedule.src.max;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Data {
	static public HashMap<String, String> GROUPS = new HashMap<>();
	static public HashMap<String, String> TEACHERS = new HashMap<>();
	static public HashMap<String, String> AUDITORIUMS = new HashMap<>();
	static private Document doc;


	static public void load() {
		try {
			doc = Jsoup.connect("http://schedule.sumdu.edu.ua/").get();
		} catch(IOException e) {
			e.printStackTrace();
		}

		loadGroups();
		loadTeachers();
		loadAuditoriums();
	}
	static public void loadGroups(){
		Element group = doc.getElementById("group");
		Elements groups = group.children();
		for(Element g : groups) {
			if(g.text().length() > 1) {
				//System.out.println(g.text() + "######" + g.val());
				GROUPS.put(g.text(), g.val());
			}
		}
	}
	static public void loadTeachers() {
		Element teacher = doc.getElementById("teacher");
		Elements teachers = teacher.children();
		for(Element t : teachers) {
			if(t.text().length() > 1) {
				TEACHERS.put(t.text(), t.val());
			}
		}
	}
	static public void loadAuditoriums() {
		Element auditorium = doc.getElementById("auditorium");
		Elements auditoriums = auditorium.children();
		for(Element a : auditoriums) {
			if(a.text().length() > 1) {
				AUDITORIUMS.put(a.text(), a.val());
			}
		}
	}

}
