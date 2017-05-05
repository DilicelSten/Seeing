import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class MainClass {
	public static void main(String[] args) throws IOException{
		List<String> nameList = new LinkedList<String>();
		String path = "data\\username.txt";
		File f = new File(path);
		if(f.exists())
		{
			BufferedReader input = new BufferedReader(new FileReader(f));
			String user = input.readLine();
			while(user != null){	
				nameList.add(user);
//				System.out.println(user);
				user = input.readLine();
			}
			input.close();
			System.out.print("user list size: " + nameList.size());
			WeiboUserCrawler crawler = new WeiboUserCrawler();
			crawler.userListCrawler(nameList);
		}
		else{
			System.out.println("file no exit");
		}
	}
}

