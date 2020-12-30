package patchMaker;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import patchMaker.pojo.Config;

public class PatchMakerApp {

	private static final Logger log = Logger.getLogger("PatchMakerApp");

	public static void main(String[] args){
		Config config = new Config(loadProperties());
		List<String> fileList = FileUtil.getFileList(config.getPathsFile());
		FileUtil.patchCopy(fileList, config);
	}

	private static Properties loadProperties(){
		PropertiesEx prop = new PropertiesEx();
		try (FileInputStream in = new FileInputStream(new File("config.properties"))){
			prop.load(in);
		} catch (IOException e) {
			log.info("加载配置文件失败");
		}
		return prop;
	}
}
