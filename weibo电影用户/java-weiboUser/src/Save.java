import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Save {
	/**
	 * 保存文件
	 * @param filePath：文件路径
	 * @param userPage：文件内容
	 */
	public static void saveUserPage(String filePath, String userPage){
		File file = new File(filePath);
		File parent = file.getParentFile();
		if((parent != null) && (!parent.exists())){
			parent.mkdirs();
		}
		try {
			if(!file.exists()){
				file.createNewFile();		
			}
		DataOutputStream output = new DataOutputStream(new FileOutputStream(file));
		output.write(userPage.getBytes("utf-8"));
		output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
