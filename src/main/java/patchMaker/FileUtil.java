package patchMaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringUtils;

import patchMaker.pojo.Config;
import patchMaker.pojo.Patch;

public class FileUtil {
	
	private static final Logger log = Logger.getLogger("FileUtil");
	
	public static List<String> getFileList(String pathName){
		ArrayList<String> fileList = new ArrayList<String>();
		BufferedReader br = null;
		try{
			FileInputStream in = new FileInputStream(pathName);
			InputStreamReader isr = new InputStreamReader(in, "utf-8");
			br = new BufferedReader(isr);
			String line = "";
			while(null != (line = br.readLine())){
				if(StringUtils.isNotBlank(line)){
					fileList.add(line);
				}
			}
		}catch (Exception e){
			log.info("读取路径文件出错");
		}finally {
			try {
				if(null != br){
					br.close();
				}
			} catch (IOException e) {
			}
		}
		return fileList;
	}
	
	public static void patchCopy(List<String> fileList, Config config){
		for(String file: fileList){
			final Patch patch = ReplaceRuleUtil.parsePatchFile(file, config);
			File dir = new File(patch.getPath());
			if(dir.exists()){
				File[] files = dir.listFiles(new FilenameFilter() {
					@Override
					public boolean accept(File dir, String name) {
						return ReplaceRuleUtil.isFileMatch(patch, name);
					}
				});
				for(File copyFile: files){
					String srcPath = copyFile.getAbsolutePath();
					copyFile(srcPath, ReplaceRuleUtil.getTargetPath(srcPath, config));
				}
			}
		}
	}
	
	public static void copyFile(String oldPath, String newPath){
		File src = new File(oldPath);
		File dest = new File(newPath);
		if(src.exists() && !dest.exists()){
			try {
				File dir = new File(dest.getParent());
				if(!dir.exists()){
					dir.mkdirs();
				}
				Files.copy(src.toPath(), dest.toPath());
			} catch (IOException e) {
				log.info(String.format("复制文件失败：%s", oldPath));
			}
		}
	}
}
