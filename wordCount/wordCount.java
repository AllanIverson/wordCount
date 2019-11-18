

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.*;

public class wordCount {


	private static String input;//输入文件名
	private static String output;//输出文件名
	private static int m=1;//词组词数
	private static int n=10;//词频前n
	private static String word; //p是正则规则
	private static String letter;
	private static int words = 0;
	private static int letters = 0;
	private static int length;
	private  static ArrayList<String> strings = new ArrayList<>();//输入的串数组，每行进入一个数组


	//初始化命令行语句
	public static void loadArgs(String args[]) {
		for(int i=0;i<args.length;i++) {
			String str=args[i];
			switch(str) {
			case "-i":
				input=args[i+1];
				break;
			case "-o":
				output=args[i+1];
				break;
			case "-m":
				m = Integer.valueOf(args[i+1]).intValue();
				if(m==0) {
					m=1;
				}
				break;
			case "-n":
				n = Integer.valueOf(args[i+1]).intValue();
				if(n==0) {
					n=10;
				}
				break;
			}
		}

	}
	//统计字符数
	public static void coundwords(String input_string){
		//word = "([a-z,0-9,A-Z]{1})"; //写出能匹配字符的正则表达式
		word = "[\\s\\S]";
		Pattern pattern = Pattern.compile(word);   //创建正则模板
		Matcher m_word = pattern.matcher(input_string);   //根据模板创建mat来从input中匹配多项式
		while (m_word.find()) {
			words++;
		}
	}
	private static HashMap<String,Integer> cizus=new HashMap<>();
	//统计单词数
	public static void countletters(String input_string){
		letter = "\\p{Alpha}{4}\\p{Alnum}*"; //写出能匹配单词数的正则表达式
		Pattern pattern = Pattern.compile(letter);   //创建正则模板
		Matcher m_letter = pattern.matcher(input_string);   //根据模板创建mat来从input中匹配多项式
		while (m_letter.find()) {
			letters++;
		}
	}
	//统计词频并输出
	public static void countcipin(String in){
		letter = "\\p{Alpha}{4}\\p{Alnum}*"; //写出能匹配单词数的正则表达式
		Pattern pattern = Pattern.compile(letter);   //创建正则模板
		Matcher m_letter = pattern.matcher(in);   //根据模板创建mat来从input中匹配多项式
		int count=m-1;//需要匹配几次空格
		int add=0;
		String cizu;
		while(m_letter.find(add)) {
			int i=0;
			cizu=m_letter.group(0);
			add=m_letter.end();
			for(i=0;i<count;i++) {
				int begin=m_letter.end();
				if(m_letter.end()==in.length()) {
					break;
				}
				int j;
				int end=0;
				if(m_letter.find()) {
					end=m_letter.start()-1;
				}
				for(j=begin;j<=end;j++) {
					if(!(in.charAt(j)==' ')) {
						break;
					}
				}
				if(j!=end+1) {
					break;
				}
				cizu+=" "+m_letter.group(0);
			}
			if(i==count) {
				cizus.put(cizu, cizus.getOrDefault(cizu, 0)+1);
			}
		}
	}
	private static List<Entry<String,Integer>> list;
	public static void outcipin() {
		list = new ArrayList<Entry<String,Integer>>(cizus.entrySet());
		Collections.sort(list,new Comparator<Entry<String,Integer>>() {
	            //降序排序
	            public int compare(Entry<String, Integer> o1,
	                    Entry<String, Integer> o2) {
	            	if(o2.getValue()==o1.getValue()) {
	            		return o1.getKey().compareTo(o2.getKey());
	            	}
	                return o2.getValue().compareTo(o1.getValue());
	            }
	            
	        });
	        
	}
	
	public static void readToBuffer(String filePath) throws IOException {
		InputStream is = new FileInputStream(filePath);
		String line; // 用来保存每行读取的内容
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		int i = 0;
		line = reader.readLine(); // 读取第一行
		while (line != null) { // 如果 line 为空说明读完了
			line.replaceAll("[\\s|\\n]+","");
			if(line!=""&&!line.isEmpty()) {
				length++;
				strings.add(line.toLowerCase());
			}else words++;
			line = reader.readLine(); // 读取下一行
		}
		reader.close();
		is.close();
	}


	public static void writeTOBuffer(){
		try{
			int t=words+length;
			File file = new File(output);
			if(!file.exists()){
				file.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			bw.write("characters: "+t);

			bw.newLine();
			bw.write("words: "+letters);

			bw.newLine();
			bw.write("lines: "+length);

			bw.newLine();
			//外带词频写入
			for(int i=0;i<n&&i<list.size();i++){ 
				Entry<String,Integer> mapping=list.get(i);
	            bw.write("<"+mapping.getKey()+">"+": "+mapping.getValue()); 
	            bw.newLine();
	        } 
			bw.flush();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void main(String[] args) throws IOException {
		loadArgs(args);//初始话命令行参数
		readToBuffer(input);//文件读入
		for(int i=0;i<length;i++) {
			coundwords(strings.get(i));
			countletters(strings.get(i));
			countcipin(strings.get(i));
		}
		outcipin();
		writeTOBuffer();//文件输出
	}
}
