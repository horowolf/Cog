import java.util.Objects;
import java.util.Scanner;

import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

/*
0:Unique Key
1:Created Date
2:Closed Date
3:Agency
4:Agency Name
5:Complaint Type
6:Descriptor
7:Location Type
8:Incident Zip
9:Incident Address
10:Street Name
11:Cross Street 2
12:Address Type
13:City
14:Status
15:Due Date
16:Resolution Description
17:Resolution Action Updated Date
18:Community Board
19:Borough
20:X Coordinate (State Plane)
21:Y Coordinate (State Plane)
22:Park Facility Name
23:Park Borough
24:School Name
25:School Code
26:School Phone Number
27:School Address
28:School City
29:School State
30:School Zip
31:School Not Found
32:School or Citywide Complaint
33:Vehicle Type
34:Taxi Company Borough
35:Taxi Pick Up Location
36:Bridge Highway Name
37:Bridge Highway Direction
38:Road Ramp
39:Bridge Highway Segment
40:Garage Lot Name
41:Ferry Direction
42:Ferry Terminal Name
43:Latitude
44:Longitude
45:Location
 */

public class AP {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (Objects.equals(args[0], "-g") || Objects.equals(args[0], "-generate")) {
			generate();
		} else if (Objects.equals(args[0], "-v") || Objects.equals(args[0], "-verbose")) {
			verbose(Float.parseFloat(args[1]), Float.parseFloat(args[2]));
		} else if (Objects.equals(args[0], "-m") || Objects.equals(args[0], "-master")) {
			master(Float.parseFloat(args[1]), Float.parseFloat(args[2]), args[3], args[4]);
		}
		
	}
	
	private static void generate() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Scanner s = new Scanner(System.in);
		System.out.println("UID,your data");
		while (s.hasNextLine()) {
			//do something
			System.out.println(s.nextLine());
		}
		s.close();
	}
	
	private static void verbose(float minsup, float minconf) {
		Scanner s = new Scanner(System.in);
		
		while (s.hasNextLine()) {
			//do something
		}
		s.close();
	}
	
	private static void master(float minsup, float minconf, String input, String output) {
		
	}
	
	private static String timeLength (String start, String end) { // 1, 2
		String tl = "";
		
		return tl;
	}
	
	private static String timeGrouping (String time) { //1
		String tl = "";
		
		return tl;
	}
	
	private static String trimTime (String time) { // for timeLenght and timeGrouping use.
		String t = "";
		
		return t;
	}

}
