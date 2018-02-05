import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainsearch {
	public static DecimalFormat df = new DecimalFormat("0.00000");
	public static int Count = 0;
	public static int countword = 0;
	public static int Len;
	public static List<List<String>> companyname = new ArrayList<List<String>>();

	public static void main(String argv[]) {
		System.out.println("We treat company name as one word in article. For example: company name:\"AT and T\" is one word in article.");
		System.out.println();
		readcompanies();
		readarticle();

		 System.out.println(String.format("%-"+Len+"s", "Total") + "  "+String.format("%5s", Count) + "      " + df.format((double) Count/ countword * 100) + "%");
		 System.out.println(String.format("%-"+Len+"s", "Total Words") + "  " + String.format("%5s", countword));
	}

	public static void readarticle() {
		System.out.println("please enter article(enter a line with only a single period \".\" to end input):");
		Scanner sc = new Scanner(System.in);
		String str = "";
		ArrayList<String> a = new ArrayList<String>();
		String line;
		Pattern regex = Pattern.compile("\\.+");
		Matcher matcher = regex.matcher(line = sc.nextLine());
		while (matcher.matches() == false) {
			a.add(line);
			matcher = regex.matcher(line = sc.nextLine());
		}
		if (a.size() == 0) {
			System.out.println("article is empty!");
			System.exit(0);
		}
		for (int i = 0; i < a.size(); i++) {
			str += a.get(i);
			str += " ";
		}
		StringBuffer sb = new StringBuffer();
		char[] chararray = str.toCharArray();
		for (int i = 0; i < str.length(); i++) {
			if ((chararray[i] >= 'a' && chararray[i] <= 'z') || (chararray[i] >= 'A' && chararray[i] <= 'Z')
					|| (chararray[i] <= '9' && chararray[i] >= '0') || chararray[i] == 32)
				sb.append(chararray[i]);
		}
		String[] array = sb.toString().split("\\s+");
		countword = 0;
		StringBuffer sbb = new StringBuffer();
		for (int i = 0; i < array.length; i++) {
			int len = 1;
			boolean flag = false;
			sbb.append(array[i]);
			if (checkobtain(sbb.toString()) == true) {
				flag = true;
			}
			for (int j = i + 1; j < array.length; j++) {
				sbb.append(" ");
				sbb.append(array[j]);
				if (checkobtain(sbb.toString()) == true) {
					flag = true;
					if (sbb.toString().split(" ").length >= len) {
						len = sbb.toString().split(" ").length;
					}
				}
			}
			if (flag == false) {
				if (sbb.toString().split(" ")[0].equals("and") == false
						&& sbb.toString().split(" ")[0].equals("a") == false
						&& sbb.toString().split(" ")[0].equals("an") == false
						&& sbb.toString().split(" ")[0].equals("the") == false
						&& sbb.toString().split(" ")[0].equals("or") == false
						&& sbb.toString().split(" ")[0].equals("but") == false) {
					countword++;
				}
			} else {
				countword++;
				i += len - 1;
			}
			sbb.delete(0, sbb.length());
		}
		Len = companyname.get(0).get(0).length();
		for (int i = 0; i < companyname.size(); i++) {
			for (int j = 0; j < companyname.get(i).size(); j++) {
				int len = companyname.get(i).get(j).length();
				if (len > Len) {
					Len = len;
				}
			}
		}

		StringBuffer sb2 = new StringBuffer();
		System.out.println(String.format("%-" + Len + "s", "Company") + "Hit Count" + " " + " Relevence");
		for (int j = 0; j < companyname.size(); j++) {
			int count = 0;
			for (int i = 0; i < array.length; i++) {
				outloop: for (int k = 0; k < companyname.get(j).size(); k++) {
					sb2.append(array[i]);
					if (sb2.toString().equals(companyname.get(j).get(k))) {
						count++;
						break outloop;
					} else {
						for (int m = i + 1; m < array.length; m++) {
							sb2.append(" ");
							sb2.append(array[m]);
							if (sb2.toString().equals(companyname.get(j).get(k))) {
								count++;
								break outloop;
							}
						}
					}
					sb2.delete(0, sb2.length());
				}

			}
			Count += count;

			System.out.println(String.format("%-"+Len+"s", companyname.get(j).get(0)) + "  " + String.format("%5s", count) + "      "
					+ df.format((double) count / countword * 100) + "%");
		}
	}

	public static boolean checkobtain(String s) {
		for (int i = 0; i < companyname.size(); i++) {
			for (int j = 0; j < companyname.get(i).size(); j++) {
				if (s.equals(companyname.get(i).get(j))) {
					return true;

				}
			}
		}
		return false;
	}

	public static void readcompanies() {
		try {
			File file = new File("companies.dat");
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					List<String> allname = new ArrayList<String>();
					String[] allnamestr = lineTxt.split("\t");
					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < allnamestr.length; i++) {
						for (int k = 0; k < allnamestr[i].length(); k++) {
							if ((allnamestr[i].charAt(k) >= 'A' && allnamestr[i].charAt(k) <= 'Z')
									|| allnamestr[i].charAt(k) == 32
									|| (allnamestr[i].charAt(k) >= 'a' && allnamestr[i].charAt(k) <= 'z')
									|| (allnamestr[i].charAt(k) >= '0' && allnamestr[i].charAt(k) <= '9')) {
								sb.append(allnamestr[i].charAt(k));
							}
						}
						allname.add(sb.toString());
						sb.delete(0, sb.length());
					}
					companyname.add(allname);
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("");
			e.printStackTrace();
		}
	}
}
