import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.openqa.selenium.JavascriptExecutor;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class Download {
	
	File file = null;
	ChromeDriverService service = null;
	WebDriver dr = null;
	private int flag = 0;
	
	public Download(){
		this.file = new File("ChromeDriver\\chromedriver.exe");
		this.service = new ChromeDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort().build();
            try {
                this.service.start();
            } catch (IOException ex) {
                Logger.getLogger(Download.class.getName()).log(Level.SEVERE, null, ex);
            }
		this.dr = new RemoteWebDriver(this.service.getUrl(), DesiredCapabilities.chrome());
	}
	
	public void mylogin(){//打开浏览器并跳到登录页面
		Scanner input = new Scanner(System.in);
		int num = 0;
		do{
			dr.get("http://www.weibo.com");
			System.out.print("请输入1继续，否则输入任意数");
			num = input.nextInt();
		}while(num != 1);
		input.close();
	}
	
	public void myclose(){//关闭浏览器
		this.dr.close();
	}
	/**
	 * 获取某个页面源代码
	 * @param url：页面链接
         * @param isRoll:是否下拉页面
	 * @return：页面源代码
	 * @throws InterruptedException
	 */
	public String getPage(String url, boolean isRoll) throws InterruptedException{
		this.dr.get(url);
        dr.manage().window().maximize();
        JavascriptExecutor js = (JavascriptExecutor) dr;
        int num = 10000;
        if(isRoll)
        for(int i=0;i<3;i++){
            js.executeScript("scrollTo(0,"+ String.valueOf(num)+")");
            Thread.sleep(3000);
            num = num * 10;
        }
		String source = this.dr.getPageSource();
		Random rand = new Random();
		int minute = rand.nextInt(5000)+3000;
		Thread.sleep(minute);
		String busyStr = "系统繁忙";
		String clickStr = "点击这里";
		String nonexistStr = "您请求的页面不存在";
		String s404 = "<title>404错误</title>";
		if ((source.indexOf(busyStr) != -1)&&(source.indexOf(clickStr) != -1)&&(source.indexOf(s404) != -1)){//系统繁忙页面
			if(this.flag == 5){//重试5次后放弃此页面，直接返回
				System.out.println("busy 5");
				this.flag = 0;
				return source;
			}
			else{	
				this.flag += 1;
				System.out.println("busy " + this.flag);
				return getPage(url,false);
			}
		}
		if (source.indexOf(nonexistStr) != -1){
			if(this.flag == 3){//重试3次后放弃此页面，直接返回
				System.out.println("nonexist 3");
				this.flag = 0;
				return source;
			}
			else{
				this.flag += 1;
				System.out.println("nonexist " + this.flag);
				return getPage(url, false);
			}
		}
		this.flag = 0;
		return source;
	}
	
//	/**
//	 * 获取用户页面源代码
//	 * @param uid：用户id
//	 * @param pageNum：用户页面页数
//	 * @return：用户页面源代码
//	 * @throws InterruptedException
//	 */
//	public String getInfoPage(int uid, int pageNum) throws InterruptedException{
//		String url = "http://weibo.cn/" + uid + "?page=" + pageNum;
//		String infoPage = getPage(url);
//		return infoPage;
//	}
	
	/**
	 * 获取用户页面源代码
	 * @param userName：用户名或用户id
	 * @param pageNum：用户页面页数
	 * @param flag:若flag为真值，则userName为用户名，否则为用户id
	 * @return：用户页面源代码
	 * @throws InterruptedException
	 */
	public String getInfoPage(String userName, int pageNum, boolean flag) throws InterruptedException{
		String url = null;
		if(flag){
//			url = "http://weibo.cn/n/" + userName + "?page=" + pageNum;
            url = "http://weibo.com/n/" + userName + "?page=" + pageNum;
		}else{
//			url = "http://weibo.cn/" + userName + "?page=" + pageNum;
            url = "http://weibo.com/u/" + userName + "?page=" + pageNum;
		}
		String infoPage = getPage(url,false);
		return infoPage;
	}
	
	/**
	 * 获取搜索结果页面源代码
	 * @param keyword：搜索的关键词
	 * @param pageNum：搜索结果页数
	 * @return：对应的搜索结果页面源代码
	 * @throws InterruptedException
	 */
	public String getSearchPage(String keyword, int pageNum) throws InterruptedException{
		String url = "http://weibo.cn/search/mblog?hideSearchFrame=&keyword=" + keyword + "&sort=hot&page=" + pageNum;
		String searchPage = getPage(url,false);
		return searchPage;
	}
	
//	public static void main(String[] args) throws IOException{
//		Download down = new Download();
//		down.mylogin();
//	}
}
