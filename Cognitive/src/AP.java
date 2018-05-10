import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Formatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Scanner;
import java.util.LinkedList;

/*
 * 1:Created Date
 * 5:Complaint Type
 * 13:City
 */

public class AP {
	static HashMap<String, ItemSet> root = new HashMap<String, ItemSet>();
	static int totalLines = 0;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if (Objects.equals(args[0], "-g") || Objects.equals(args[0], "-generate")) {
			generate(1);
		} else if (Objects.equals(args[0], "-v") || Objects.equals(args[0], "-verbose")) {
			verbose(Float.parseFloat(args[1]), Float.parseFloat(args[2]), 1);
		} else if (Objects.equals(args[0], "-m") || Objects.equals(args[0], "-master")) {
			master(Float.parseFloat(args[1]), Float.parseFloat(args[2]), args[3], args[4]);
		}
	}
	
	private static HashMap<HashSet<String>, Integer> generate(int mode) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		HashMap<HashSet<String>, Integer> HMap = new HashMap<HashSet<String>, Integer>();
		if (mode == 1) {
			System.out.println("UID,your data");
		}
		String buf;
		String[] bufCol;
		try {
			br.readLine(); // skip a first line
			while ((buf = br.readLine()) != null){
				//totalLines++; // for orginal lines
				//System.out.println(buf.split(",").length);
				bufCol = buf.split(",");
				if (root.containsKey(bufCol[13])) {
					HashSet<String> set = root.get(bufCol[13]).add(bufCol[5], bufCol[1]);
					// if null -> do nothing
					if (set != null) {
						if (mode == 1) {
							System.out.print(totalLines + ",");
							set.forEach(i -> System.out.print(i + ";"));
							System.out.print("\n");
						}
						
						
						totalLines++; // for new transaction lines
						if (HMap.containsKey(set)) {
							HMap.replace(set, HMap.get(set) + 1);
						} else {
							HMap.put(set, 1);
						}
						
					}
				} else {
					root.put(bufCol[13], new ItemSet(bufCol[5], bufCol[1], 4));
				}
			}
		} catch (IOException e) {
			System.out.println("fail");
			e.printStackTrace();
		}
		//s.close();
		//System.out.println(totalLines); // remember to remove)
		return HMap;
	}
	
	private static HashMap<HashSet<String>, Integer> verbose(float minsup, float minconf, int mode) {
		int minsupCount = Double.valueOf(Math.ceil(totalLines * minsup)).intValue();
		int minconfCount = Double.valueOf(Math.ceil(totalLines * minconf)).intValue();
		HashMap<HashSet<String>, Integer> HM1 = generate(0);
		HashMap<HashSet<String>, Integer> HM2 = new HashMap<HashSet<String>, Integer>();
		HashMap<HashSet<String>, Integer> HM3 = new HashMap<HashSet<String>, Integer>();
		//move HM1 to HM2 and breaks 3,4 itemsets to 1,2(,3) itemsets
		HM1.forEach((i, v) -> {
			ArrayList<String> sList = new ArrayList<String>();
			HashSet<HashSet<String>> sets = new HashSet<HashSet<String>>();
			//sets.add(i);
			i.forEach(item -> { // loop for 1 itemsets
				HashSet<String> s = new HashSet<String>();
				s.add(item);
				sets.add(s);
			});
			
			//i.forEach(z -> System.out.println("start:\t" + z));
			for (int count = 0; count < i.size() - 1; count++) { // size -1 with sets.add(i) good?
				i.forEach(item -> {
					//sets.forEach(z -> System.out.println("sets:\t" + z));
					
					HashSet<HashSet<String>> tempSS = new HashSet<HashSet<String>>();
					sets.forEach(s -> {
						HashSet<String> tempS = new HashSet<String>();
						//System.out.println("1:\t" + tempS);
						s.forEach(z -> tempS.add(z));
						tempS.add(item);
						tempSS.add(tempS);
						//System.out.println("2:\t" + tempS);
						//System.out.println("3:\t" + tempSS);
						//tempS.clear();
						//tempS.add(s);
					});
					//sets.clear();
					sets.addAll(tempSS);
					//tempSS.forEach(s -> sets.add(s));
					
				});  // <- pick combine and put
				//tempS.forEach(z -> System.out.println("mid:\t" + z));
				//tempS.forEach(s -> sets.add(s));  // check
				//tempS.clear();
			}
			//sets.forEach(z -> System.out.println("end:\t" + z));
			// add to HM2
			sets.forEach(item -> {
				if (HM2.containsKey(item)) {
					HM2.replace(item, HM2.get(item) + v);
				} else {
					HM2.put(item, v);
				}
				
			});
			//HM2.forEach((z, x) -> System.out.println("out:\t" + z + "\t" + x));
		});
		//trim the itemsets with low sup
		//System.out.println("minsup: " + String.valueOf(Double.valueOf(Math.ceil(totalLines * minsup)).intValue()) + " in lines: " + String.valueOf(totalLines) + " input: " + String.valueOf(minsup));
		HM2.forEach((z, x) -> {
			if (x < Double.valueOf(Math.ceil(totalLines * minsup)).intValue()) {
				if (z.size() > 2 && mode == 1) {
					System.out.println(z + "support = " + String.valueOf((double)x / totalLines) + " pruned");
				}
				//System.out.println(z + "confident = " + String.valueOf((double)x / totalLines) + " pruned");
				//HM2.remove(z);
			} else {
				//System.out.println(z + "confident = " + String.valueOf((double)x / totalLines) + " not pruned");
				if (z.size() > 2 && mode == 1) {
					System.out.println(z + "support = " + String.valueOf((double)x / totalLines) + " not pruned");
				}
				HM3.put(z, x);
			}
		});
		return HM3;
	}
	
	private static void master(float minsup, float minconf, String input, String output) {
		try {// https://stackoverflow.com/questions/9686060/is-there-a-way-to-redirect-input-from-file-to-stdin-in-netbeans
			FileInputStream is = new FileInputStream(new File(input));
			System.setIn(is);
			PrintStream os = new PrintStream(new File(output));
			System.setOut(os);;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//generate();
		HashMap<HashSet<String>, Integer> HM = verbose(minsup, minconf, 0);
		//HM.forEach((z, x) -> System.out.println("master:\t" + z + "\t" + x));
		HM.forEach((z, x) -> {
			HM.forEach((a, b) -> {
				if (z.containsAll(a)) {
					if (((double)x / b ) > minconf) {
						HashSet<String> temp = new HashSet<String>();
						z.forEach(i -> temp.add(i));
						temp.removeAll(a);
						if (temp.isEmpty()) {
							System.out.println(a + "(sup = " + String.valueOf((double)b / totalLines) + " conf= " + String.valueOf((double)x / b) + ")" );
						} else {
							System.out.println(a + " -> " + temp + "(sup = " + String.valueOf((double)b / totalLines) + " conf= " + String.valueOf((double)x / b) + ")" );
						}
						
					}
					
				}
			});
		});
	}
	
	static class ItemSet {
		int setSize;
		LinkedList<String> list;
		LinkedList<LocalDateTime> timeList;
		LocalDateTime time;
		DateTimeFormatter format = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		protected ItemSet (String item, String dateTime, int setSize) {
			this.setSize = setSize;
			list = new LinkedList<String>();
			list.add(item);
			try {
				LocalDateTime tempTime = LocalDateTime.parse(dateTime, format);
				time = tempTime;
				timeList = new LinkedList<LocalDateTime>();
				timeList.add(tempTime);
			} catch (DateTimeParseException e) {
				System.out.println(e);
				
			}
			
		}
		
		protected HashSet<String> add (String item, String dateTime) {
			//HashSet<String> response = null;
			//if time duration is too long, clear first (3 hours?)
			LocalDateTime tempTime = LocalDateTime.parse(dateTime, format);
			if (time == null || ((time.getDayOfYear() - tempTime.getDayOfYear()) * 24 + (time.getHour() - tempTime.getHour())) * 60 + time.getMinute() - tempTime.getMinute() > 360) {
				list.clear();
				timeList.clear();
			}
			//update time
			time = tempTime;
			//if at the head, skip the reset and return null.
			if (list.getFirst().equals(item)) {
				return null;
			}
			//if exist, move to first. else add to first.
			/*
			if (list.remove(item)) {
				list.addFirst(item);
			}
			*/
			if (list.indexOf(item) > -1) {
				timeList.remove(list.indexOf(item));
				list.remove(item);
			}
			timeList.addFirst(tempTime);
			list.addFirst(item);
			//if the oldest is too old, remove that
			if (((timeList.getLast().getDayOfYear() - tempTime.getDayOfYear()) * 24 + (timeList.getLast().getHour() - tempTime.getHour())) * 60 + timeList.getLast().getMinute() - tempTime.getMinute() > 1440) {
				list.removeLast();
				timeList.removeLast();
			}
			if (list.size() < 3) {
				return null;
			}
			//if size > setSize, remove last
			if (list.size() > setSize) {
				list.removeLast();
				timeList.removeLast();
			}
			//return as a set
			HashSet<String> response = new HashSet<String>();// ((Collection<? extends String>) list.descendingIterator()); // need check
			list.forEach((i) -> response.add(i)); // maybe this works?
			return response;
		}
		
		
		
	}
}

