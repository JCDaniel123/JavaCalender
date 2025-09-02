package datastore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class EventFileAccessor {
private Map<Date, String> eventList;
private SimpleDateFormat sdFormat = new SimpleDateFormat("M-d-yyyy");
private String fileName;
public EventFileAccessor(String fileName) throws Exception {
this.fileName = fileName;
eventList = new HashMap<Date, String>();
String[] parts;
Date eventDate;
String eventText;
Scanner input = new Scanner(new File(fileName));
while (input.hasNextLine()) {
parts = input.nextLine().split(",");
eventDate = sdFormat.parse(parts[0]);
eventText = parts[1];
eventList.put(eventDate, eventText);
System.out.println(eventText + "@" + eventDate);
}
}
public Map<Date, String> getEventList() {
return this.eventList;
}
public static void main(String[] args) throws Exception {
EventFileAccessor fileAccessor =
new EventFileAccessor("EventsList.csv");
System.out.println(fileAccessor.getEventList());
}
}
