
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Extract {
	/**
	 * 获取用户首页或搜索页面的微博正文
	 * @param page: 用户首页源代码或搜索页面源代码
	 * @param flag: flag为真时page为用户首页源代码，否则page为搜索页面源代码
	 * @return: 微博正文列表
	 * @throws InterruptedException 
	 */
	public static List<String> getBlog(String page, boolean flag) throws InterruptedException{
		String regex = null;
		if (flag){
			regex = "<div id=\"M_.*?<span class=\"ctt\">(.*?)<.*?>赞";
		}
		else{
			regex = "<div class=\"c\" id=\"M_.*?<span class=\"ctt\">(.*?)</span>";
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		List<String> blogList = new LinkedList<String>();
		while(matcher.find()){
			String blog = matcher.group(1);
			if (blog.indexOf(">全文</a>") != -1){//若博文不完整，尝试获取完整博文
				String detailBlog = getDetailBog(blog);
				if (detailBlog != null){
					blog = detailBlog;
				}
			}
			blog = blog.replaceAll("<.*?>", "");//去除多余
			System.out.println(blog);
			blogList.add(blog);
		}
		return blogList;
	}
	
	/**
	 * 从不完整的微博正文中找出详细的博文链接进而获取完整的微博正文
	 * @param blog: 不完整的微博正文
	 * @return: 完整的微博正文
	 * @throws InterruptedException
	 */
	public static String getDetailBog(String blog) throws InterruptedException{
		String regex = "<a href=\"(.*?)\">全文";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(blog);
		String detailBlog = null;
		String url = null;
		if(matcher.find()){
			url = matcher.group(1);
		}else{
			return detailBlog;
		}
		String detailUrl = "http://weibo.cn/" + url;
		Download down = new Download();
		String detailPage = down.getPage(detailUrl, false);
		String detailRegex = "<span class=\"ctt\">:(.*?)</span>";
		Pattern detailPattern = Pattern.compile(detailRegex);
		Matcher detailMatcher = detailPattern.matcher(detailPage);
		if(detailMatcher.find()){
			detailBlog = detailMatcher.group(1);
		}
		return detailBlog;
	}
	
	/**
	 * 获取某个页面所有微博正文的评论数
	 * @param page：某页面源代码
	 * @return：评论数列表
	 */
	public static List<String> getCommentNum(String page){
		List<String> numList = new LinkedList<String>();
		String regex = ">评论\\[(.*?)]</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		while(matcher.find()){
			String num = matcher.group(1);
			numList.add(num);
		}
		return numList;
	}
	
	/**
	 * 获取某个页面所有微博正文的点赞数
	 * @param page：某页面源代码
	 * @return：点赞数列表
	 */
	public static List<String> getLikeNum(String page){
		List<String> numList = new LinkedList<String>();
		String regex = ">赞\\[(.*?)]</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		while(matcher.find()){
			String num = matcher.group(1);
			numList.add(num);
		}
		return numList;
	}
	
	/**
	 * 获取某个页面所有微博正文的转发数
	 * @param page：某页面源代码
	 * @return：转发数列表
	 */
	public static List<String> getRepostNum(String page){
		List<String> numList = new LinkedList<String>();
		String regex = ">转发\\[(.*?)]</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		while(matcher.find()){
			String num = matcher.group(1);
			numList.add(num);
		}
		return numList;
	}
	
	/**
	 * 获取微博正文的标题
	 * @param blog：微博正文
	 * @return：标题
	 */
	public static String getTitle(String blog){
		String title = null;
		String regex = "【(.*?)】";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(blog);
		if(matcher.find()){
			title = matcher.group(1);
			title = title.replaceAll("<.*?>", "");
		}
		return title;
	}
	
	/**
	 * 从搜索结果页面获取用户名列表
	 * @param searchPage：搜索结果页面源代码
	 * @return：用户名列表
	 */
	public static List<String> getUserName(String searchPage){
		List<String> nameList = new LinkedList<String>();
		String regex = "class=\"nk.*?>(.*?)</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(searchPage);
		while(matcher.find()){
			String name = matcher.group(1);
			nameList.add(name);
		}
		return nameList;
	}
	
	/**
	 * 获取用户id
	 * @param infoPage：用户首页源代码
	 * @return：用户id
	 */
	public static String getUidInfoPage(String infoPage){
		String uid = null;
//		String regex = "<a href=\"/(\\d*?)/info\">资料<";
                String regex = "\\['oid']='(\\d*?)';";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(infoPage);
		if(matcher.find()){
			uid = matcher.group(1);
//                        System.out.println(uid);
		}
		return uid;
	}
        /**
	 * 获取用户资料页面id
	 * @param infoPage：用户首页源代码
	 * @return：用户资料页面id
	 */
	public static String getPidInfoPage(String infoPage){
		String pid = null;
        String regex = "\\[.page_id.]=.(\\d*?).;";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(infoPage);
		if(matcher.find()){
			pid = matcher.group(1);
//          System.out.println(pid);
		}
		return pid;
	}
        /**
         * 判断当前关注页面是否有效
         * @param followPage:用户关注页面
         * @return: 有效为true，否则为false
         */
        public static boolean isValidF(String followPage){
            String regex = "通过.*?关注";
//            String regex = "<.em>关注";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(followPage);
            if(matcher.find()){
                return true;
            }
            return false;
        }
        /**
         * 判断当前页面是否有效
         * @param infoPage:用户博文页面
         * @return : 有效为true，否则为false
         */
	public static boolean isValidI(String infoPage){
//            String regex = "第&nbsp;(\\d*?)&nbsp;页";
//            String regex2 = "没有发过微博";
//            Pattern pattern = Pattern.compile(regex);
//            Matcher matcher = pattern.matcher(infoPage);
//            if(infoPage.contains(regex2)){
//                return false;
//            }
//            if(matcher.find()){
//                return false;
//            }
            if(infoPage.contains("收藏")){
                if(infoPage.contains("评论"))
                    if(infoPage.contains("转发"))
                        return true;
            }
            return false;
        }
	/**
	 * 在某页面获取博文id列表
	 * @param page：某页面源代码
	 * @return：博文id列表
	 */
	public static List<String> getBlogId(String page){
		List<String> blogIdList = new LinkedList<String>();
		String regex = "<div class=\"c\" id=\"M_(.*?)\">";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		while(matcher.find()){
			String blogId = matcher.group(1);
			blogIdList.add(blogId);
		}
		return blogIdList;
	}
	
	/**
	 * 获取页面数
	 * @param page：页面源代码
	 * @return：页面数
	 */
	public static int getPageNum(String page){
		int pageNum = 1;
		String regex = "1/(\\d*?)页<";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(page);
		if(matcher.find()){
			pageNum = Integer.parseInt(matcher.group(1));
		}
		return pageNum;
	}
	
	/**
	 * 获取评论列表
	 * @param commentPage：评论页面源代码
	 * @return：评论列表
	 */
	public static List<String> getComment(String commentPage){
		List<String> commentList = new LinkedList<String>();
		String regex = ":<span class=\"ctt\">(.*?)</span>.*?>举报";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(commentPage);
		while(matcher.find()){
			String comment = matcher.group(1);
			comment = comment.replaceAll("<.*?>", "");
			commentList.add(comment);
		}
		return commentList;
	}
	
	/**
	 * 获取评论者列表
	 * @param commentPage：评论页面源代码
	 * @return：评论者列表
	 */
	public static List<String> getCommenter(String commentPage){
		List<String> commenterList = new LinkedList<String>();
		String regex = "<div class=\"c\" id=.*?>.*?<a href=.*?>(.*?)</a>.*?:<span";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(commentPage);
		while(matcher.find()){
			String commenter = matcher.group(1);
			commenter = commenter.replaceAll("<.*?>", "");
			commenterList.add(commenter);
		}
		return commenterList;
	}
	
	/**
	 * 获取点赞者列表
	 * @param likePage：点赞页面源代码
	 * @return：点赞者列表
	 */
	public static List<String> getLiker(String likePage){
		List<String> likerList = new LinkedList<String>();
		String regex = "<div class=\"c\"><a href=.*?>(.*?)</a>.*?<span class=\"ct\">";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(likePage);
		while(matcher.find()){
			String liker = matcher.group(1);
			liker = liker.replaceAll("<.*?>", "");
			likerList.add(liker);
		}
		likerList.remove(0);
		return likerList;
	}
	
	/**
	 * 获取该微博的转发路径列表
	 * @param repostPage：转发页面源代码
	 * @return：转发路径列表
	 */
	public static List<String> getRepost(String repostPage){
		List<String> repostList = new LinkedList<String>();
		String first = "";
		//first为微博的原创者
		String firstRegex = "<div class=\"c\" id=\"M_\">.*?<a href=.*?>(.*?)</a>";
		Pattern firstPattern = Pattern.compile(firstRegex);
		Matcher firstMatcher = firstPattern.matcher(repostPage);
		if(firstMatcher.find()){
			first = firstMatcher.group(1);
		}
		String regex = "<div class=\"c\">(.*?)赞\\[.*?]";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repostPage);
		while(matcher.find()){
			String repost = matcher.group(1);
			repost = repost.replaceAll("<.*?>", "");
			repost = repost + "//" + first;
			repostList.add(repost);
		}
		return repostList;
	}
	
	/**
	 * 获取转发者列表
	 * @param repostPage：转发页面源代码
	 * @return：转发者列表
	 */
	public static List<String> getReposter(String repostPage){
		List<String> reposterList = new LinkedList<String>();
		String regex = "<div class=\"c\">.*?<a href=.*?>(.*?)</a>";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(repostPage);
		while(matcher.find()){
			String reposter = matcher.group(1);
			reposter = reposter.replaceAll("<.*?>", "");
			reposterList.add(reposter);
		}
		return reposterList;
	}
}

