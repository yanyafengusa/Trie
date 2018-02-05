import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class trie {
	public static node root = new node(63);
	public static node tempnode = root;
	public static node tempnode2 = root;
	public static DecimalFormat df = new DecimalFormat("0.00000");
	public static int count = 0;
	public static int count2 = 0;
	public static int counter = 0;
	public static int length = 0;
	public static List<String> companyname = new ArrayList<String>();
	public static Map<String, Integer> map = new HashMap<String, Integer>();
	static String[] strarray = null;
	
	public static class node{
		private node[] son;
		private int endcount;
		private int value;
		private boolean isend;
		private String whole_name;
		private String primary_name;
		
		public node(int x) {  
	        this.endcount = 0;  
	        this.son = new node[x];
	        this.value = 0;
	        this.isend = false;
	        this.whole_name = "";
	        this.primary_name = "";
	    }  
	}
	
	public static void main(String argv[]) {
		System.out.println("We treat company name as one word in article. For example: company name:\"AT and T\" is one word in article.");
		System.out.println();
		buildtree();
		readarticle();
		checkarticle(strarray);
		traversal(root);
		HashMap<String, Integer> tm = (HashMap<String, Integer>) sortMap(map);
		int total = 0;
		for(int m=0;m<length+30;m++){
			System.out.print("-");
		}
		System.out.println();
		System.out.println(String.format("%-"+String.valueOf(length+5)+"s", "| Company")
						  +String.format("%-6s", "| Hit Count |")
						  +String.format("%12s", " Relevance |"));
		for(int m=0;m<length+30;m++){
			System.out.print("-");
		}
		System.out.println();
		for(String str : tm.keySet()){
			total = total+tm.get(str);
			System.out.println(String.format("%-"+String.valueOf(length+5)+"s", "| "+str)
							  +String.format("%-6s", "|")
							  +String.format("%-6s", tm.get(str))
							  +String.format("%-1s", "|")
							  +String.format("%12s", df.format((double)tm.get(str)/ counter * 100)+"% |"));
			for(int m=0;m<length+30;m++){
				System.out.print("-");
			}
			System.out.println();
		}
		System.out.println(String.format("%"+String.valueOf(length)+"s", "Total")
						  +String.format("%12s", total)
						  +String.format("%16s", df.format((double)total/ counter * 100)+"%"));
		System.out.println(String.format("%"+String.valueOf(length+6)+"s", "Total words")
					      +String.format("%6s", counter));
	}
	
	public static void checkarticle(String[] strs){
		if(count<strs.length){
			if(tempnode == root){
				if(!check(strs[count],tempnode)){
					if(strs[count].equals("a")||strs[count].equals("an")||strs[count].equals("and")||strs[count].equals("the")
							||strs[count].equals("or")||strs[count].equals("but")||strs[count].equals("")){
						count++;
						count2 = count;
						tempnode = root;
						checkarticle(strarray);
					}
					else{
						counter++;
						count++;
						count2 = count;
						tempnode = root;
						checkarticle(strarray);
					}
				}
				else{
					if(!tempnode.isend){
						if(tempnode.son[0] == null){
							if(strs[count].equals("a")||strs[count].equals("an")||strs[count].equals("and")||strs[count].equals("the")
									||strs[count].equals("or")||strs[count].equals("but")||strs[count].equals("")){
								count++;
								count2 = count;
								tempnode = root;
								//System.out.println("check1");
								checkarticle(strarray);
							}
							else{
								counter++;
								count++;
								count2 = count;
								tempnode = root;
								//System.out.println("check2");
								checkarticle(strarray);
							}
						}
						else{
							count2 = count;
							count++;
							tempnode2 = tempnode;
							tempnode = tempnode.son[0];
							//System.out.println("check3");
							checkarticle(strarray);
						}
					}
					else{
						if(tempnode.son[0] == null){
							tempnode.endcount++;
							counter++;
							count++;
							count2 = count;
							tempnode = root;
							//System.out.println("check4");
							checkarticle(strarray);
						}
						else{
							count2 = count;
							count++;
							tempnode2 = tempnode;
							tempnode = tempnode.son[0];
							//System.out.println("check5");
							checkarticle(strarray);
						}
					}
					
				}
			}
			else{
				if(!check(strs[count],tempnode)){
					count = count2;
					tempnode2.endcount++;
					counter++;
					count++;
					count2++;
					tempnode2 = root;
					tempnode = root;
					//System.out.println("check6");
					checkarticle(strarray);
				}
				else{
					if(!tempnode.isend){
						if(tempnode.son[0] == null){
							tempnode2.endcount++;
							tempnode = root;
							count = count2;
							count++;
							count2++;
							counter++;
							//System.out.println("check7");
							checkarticle(strarray);
						}
						else{
							count++;
							tempnode = tempnode.son[0];
							//System.out.println("check8");
							checkarticle(strarray);
						}
					}
					else{
						if(tempnode.son[0] == null){
							tempnode2 = tempnode;
							tempnode2.endcount++;
							counter++;
							tempnode = root;
							count++;
							count2=count;
							//System.out.println("check9");
							checkarticle(strarray);
						}
						else{
							tempnode2 = tempnode;
							count2 = count;
							count++;
							tempnode = tempnode.son[0];
							//System.out.println("check10");
							checkarticle(strarray);
						}
					}
				}
			}
		}
		else if (count==strs.length && count2<count){
			if(tempnode2.isend){
				tempnode2.endcount++;
				counter++;
				count2++;
				count = count2;
				tempnode = root;
			}
			else{
				if(strs[count].equals("a")||strs[count].equals("an")||strs[count].equals("and")||strs[count].equals("the")
						||strs[count].equals("or")||strs[count].equals("but")||strs[count].equals("")){
					count2++;
					count = count2;
					tempnode = root;
					checkarticle(strarray);
				}
				else{
					counter++;
					count2++;
					count = count2;
					tempnode = root;
					checkarticle(strarray);
				}
			}
		}
		else if (count == strs.length && count2 == strs.length){
			return;
		}
	}
	
	public static void traversal(node root){
		if(root.isend){
			if(map.containsKey(root.primary_name)){
				map.put(root.primary_name, map.get(root.primary_name)+root.endcount);
			}
			else{
				map.put(root.primary_name, root.endcount);
			}
		}
		
		for(int p =0 ; p<=62;p++){
			if(root.son[p]!=null){
				traversal(root.son[p]);
			}
		}
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
			str += a.get(i).replaceAll("\\pP" , "");   
			str += " ";
		}
		strarray = str.split("\\s+");
		sc.close();
	}
	
	public static void buildtree() {
		try {
			File file = new File("companies.dat");
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					String[] allnamestr = lineTxt.split("\t");
					for (int i = 0; i < allnamestr.length; i++) {
						node node =root;
						for (int k = 0; k < allnamestr[i].length(); k++) {
							if (allnamestr[i].charAt(k) >= 'A' && allnamestr[i].charAt(k) <= 'Z'){
								if(node.son[allnamestr[i].charAt(k)-64] == null){
									node.son[allnamestr[i].charAt(k)-64] = new node(63);
									node.son[allnamestr[i].charAt(k)-64].value = allnamestr[i].charAt(k);
									node = node.son[allnamestr[i].charAt(k)-64];
								}
								else{
									node = node.son[allnamestr[i].charAt(k)-64];
								}
							}
							else if(allnamestr[i].charAt(k)==32){
								if(node.son[allnamestr[i].charAt(k)-32] == null){
									node.son[allnamestr[i].charAt(k)-32] = new node(63);
									node.son[allnamestr[i].charAt(k)-32].value = allnamestr[i].charAt(k);
									node = node.son[allnamestr[i].charAt(k)-32];
								}
								else{
									node = node.son[allnamestr[i].charAt(k)-32];
								}
							}
							else if((allnamestr[i].charAt(k) >= 'a' && allnamestr[i].charAt(k) <= 'z')){
								if(node.son[allnamestr[i].charAt(k)-70] == null){
									node.son[allnamestr[i].charAt(k)-70] = new node(63);
									node.son[allnamestr[i].charAt(k)-70].value = allnamestr[i].charAt(k);
									node = node.son[allnamestr[i].charAt(k)-70];
								}
								else{
									node = node.son[allnamestr[i].charAt(k)-70];
								}
							}
							else if((allnamestr[i].charAt(k) >= '0' && allnamestr[i].charAt(k) <= '9')){
								if(node.son[allnamestr[i].charAt(k)+5] == null){
									node.son[allnamestr[i].charAt(k)+5] = new node(63);
									node.son[allnamestr[i].charAt(k)+5].value = allnamestr[i].charAt(k);
									node = node.son[allnamestr[i].charAt(k)+5];
								}
								else{
									node = node.son[allnamestr[i].charAt(k)+5];
								}
							}
						}
						node.whole_name = allnamestr[i].replaceAll("\\pP" , "");    
						node.primary_name = allnamestr[0].replaceAll("\\pP" , "");    
						node.isend = true;
						if(length<node.primary_name.length()){
							length = node.primary_name.length();
						}
					}
				}
				read.close();
			}
		} catch (Exception e) {
			System.out.println("Error, please try again");
			e.printStackTrace();
		}
	}
	
	public static boolean check(String str, node node){
		tempnode = node;
		char[] chars = str.toCharArray();
		for(int i=0;i<chars.length;i++){
			if(chars[i]>='A' && chars[i]<='Z'){
				if(tempnode.son[chars[i]-64]!=null){
					tempnode = tempnode.son[chars[i]-64];
				}
				else{
					return false;
				}
			}
			else if (chars[i]>='a' && chars[i]<='z'){
				if(tempnode.son[chars[i]-70]!=null){
					tempnode = tempnode.son[chars[i]-70];
				}
				else{
					return false;
				}
			}
			else if (chars[i]>='0' && chars[i]<='9'){
				if(tempnode.son[chars[i]+5]!=null){
					tempnode = tempnode.son[chars[i]+5];
				}
				else{
					return false;
				}
			}
			else{
				return false;
			}
		}
		return true;
	}
	
	public static Map<String, Integer> sortMap(Map<String, Integer> oldMap) {
		ArrayList<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(oldMap.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Entry<java.lang.String, Integer> arg0, Entry<java.lang.String, Integer> arg1) {
				return arg1.getValue() - arg0.getValue();
			}
		});
		Map<String, Integer> newMap = new LinkedHashMap<String, Integer>();
		for (int i = 0; i < list.size(); i++) {
			newMap.put(list.get(i).getKey(), list.get(i).getValue());
		}
		return newMap;
	}
}
