package shestak.maksym.schedule.src.max;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import shestak.maksym.schedule.src.max.Day;

public class Schedule {

	public static ArrayList<Day> loadSchedule(String group1, String begiDate, String endDate) {
		String urlParameters =
						"data%5BDATE_BEG%5D=" +     begiDate
				+       "&data%5BDATE_END%5D=" +    endDate
				+       "&data%5BKOD_GROUP%5D=" +   group1
				+       "&data%5BID_FIO%5D=" +      "0"
				+       "&data%5BID_AUD%5D=" +      "0"
				+       "&data%5BPUB_DATE%5D=" +    "false"
				+       "&data%5BPARAM%5D=" +       "0";

		try {
			URL url = new URL("http://schedule.sumdu.edu.ua/index/htmlschedule");
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("POST");
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(con.getInputStream(), "windows-1251"));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			//System.out.println(response.toString());


			Document doc = Jsoup.parse(response.toString());

			Elements elements = doc.select("tr");
			Elements e1;
			Elements tmp;

			String title;
			String type;
			String auditorium;
			String lecturer;
			String group;
			String date;
			ArrayList<Day> days = new ArrayList<>();
			Day day = null;
			int size = elements.size();
			String prevDate = "";
			for(int i = 1; i < size; i++) {
				if(elements.get(i).getElementsByClass("none").text().length() > 1) {
					date = elements.get(i).getElementsByClass("none").text();
					if(date.compareTo(prevDate) == 0) continue;
					System.out.println("date: " + date);
					prevDate = date;
					day = new Day(date);
				}
				e1 = elements.get(i).select("td");
				for(int j = 0; j < e1.size(); j++) {
					if(e1.get(j).text().length() == 1) continue;

					System.out.println(j + ":  " + e1.get(j).text());

					tmp = e1.get(j).children();
					if(tmp.size() > 4)
					{
						title = tmp.get(0).text();
						type = tmp.get(1).text();
						auditorium = tmp.get(2).text();
						lecturer = tmp.get(3).text();
						group = tmp.get(4).text();
						//todo "" in classes
						day.classes.add(new Class(title, type, auditorium, lecturer, group, String.valueOf(j+1), ""));
					} else
					if(tmp.size() == 3) { // Фізичне виховання
						title = tmp.get(0).text();
						group = tmp.get(1).text();
						day.classes.add(new Class(title, "", "", "", group, String.valueOf(j+1), ""));
					} else if(tmp.size() == 4) { // дисципліна за вибором
						title = tmp.get(0).text();
						type = tmp.get(1).text();
						group = tmp.get(2).text();
						day.classes.add(new Class(title, type, "", "", group, String.valueOf(j+1), ""));
					} else if(tmp.size() == 2) { // самостійна робота
						title = tmp.get(0).text();
						group = tmp.get(1).text();
						day.classes.add(new Class(title, "", "", "", group, String.valueOf(j+1), ""));
					}
				}
				if(e1.size() > 0)
					days.add(day);
			}
			/*
			for(Day d: days) {
				System.out.println(d.toString());
			}
			*/
			return days;

		} catch(MalformedURLException e) {
			e.printStackTrace();
		} catch(IOException e) {
			e.printStackTrace();
		}


		return null;
	}
}
