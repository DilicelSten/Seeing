import java.io.File;
import java.util.List;

public class WeiboUserCrawler {
	Download down;
	String basePath = "data\\";//保存资料的基本路径
	public WeiboUserCrawler(){
            down = new Download();
	}
	
	/**
	 * 判断某用户的所有相关资料是否已存在
	 * @param username：用户名
	 * @return：已存在返回真，否则返回假
	 */
	public boolean isExit(String username){
		String path = this.basePath + username + "\\like_05.html";//按需更改
		File file = new File(path);
		if(file.exists()){
			return true;
		}
		return false;
	}
	
	/**
	 * 获取用户相关资料页面
	 * @param username：用户名
	 */
	public void userCrawler(String username){
		try{
			if(!isExit(username)){
//				System.out.println(username + "t");
//				username = username.trim();
//				System.out.println(username + "t");
//				String filePath = this.basePath + username + "\\mainPage.html";
				String mainPage = this.down.getInfoPage(username, 1, true);//获取用户首页
//				Save.saveUserPage(filePath, mainPage);
//				String uid = Extract.getUidInfoPage(mainPage);
                String pid = Extract.getPidInfoPage(mainPage);
				if(pid != null){
//					String infoUrl = "http://weibo.cn/" + uid + "/info";//获取用户资料页面
                    String infoUrl = "http://weibo.com/p/"+ pid +"/info?mod=pedit_more";
					String detailInfoPage = this.down.getPage(infoUrl, false);
					String detailInfoPath = this.basePath + username + "\\information.html";
					Save.saveUserPage(detailInfoPath, detailInfoPage);
					
////					int infoNum = 5;
//					for(int i=1; i<6; i++){//获取用户微博前5页
////						if(i > infoNum){
////							System.out.println("only " + infoNum + "info page");
////							break;
////						}
////						String infoPage = this.down.getInfoPage(uid, i, false);
//                        String infoPageUrl = "http://weibo.com/p/" + pid + "?page=" + i;
//                        String infoPage = this.down.getPage(infoPageUrl, true);
//						String infoPath = this.basePath + username + "\\content_0" + i + ".html";
//						if(Extract.isValidI(infoPage)){
//                             Save.saveUserPage(infoPath, infoPage);
//                        }else{
//                            break;
//                        }
////						if(i == 1){
////							infoNum = Extract.getPageNum(infoPage);
////						}
//					}
					
//					int followNum = 5;
					for(int i=1; i<6; i++){//获取用户关注前5页
//						if(i > followNum){
//							System.out.println("only " + followNum + "follow page");
//							break;
//						}
//						String followUrl = "http://weibo.cn/" + uid + "/follow?page=" + i;
						String followUrl = "http://weibo.com/p/" + pid + "/follow?page=" + i;
                        String followPage = this.down.getPage(followUrl,false);
						String followPath = this.basePath + username + "\\like_0" + i + ".html";
						if(Extract.isValidF(followPage)){
                            Save.saveUserPage(followPath, followPage);
                        }else{
                            break;
                        }
//						if(i == 1){
//							followNum = Extract.getPageNum(followPage);
//						}
					}
					System.out.println(username + " is ok");
				}
				else{
					System.out.println("didn't find pid");
				}
			}
			else{
				System.out.println(username + "file exist");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * 获取用户相关资料
	 * @param userList：用户名列表
	 */
	public void userListCrawler(List<String> userList){
		down.mylogin();
		try{
			for(int i=0; i<userList.size(); i++){
				userCrawler(userList.get(i));
			}
		}
		finally{
			down.myclose();
		}
	}
}
