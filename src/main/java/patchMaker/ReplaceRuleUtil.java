package patchMaker;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import patchMaker.pojo.Config;
import patchMaker.pojo.Patch;

public class ReplaceRuleUtil {
	
	public static List<String[]> parseReplaceRule(String rules){
		List<String[]> ruleList = new ArrayList<String[]>(); 
		if(StringUtils.isBlank(rules)){
			return ruleList;
		}
		String[] ruleArr = StringUtils.split(rules, "||");
		for(String rule: ruleArr){
			ruleList.add(StringUtils.split(rule, ":"));
		}
		return ruleList;
	}
	
	public static Patch parsePatchFile(String file, Config config){
		for(String[] rule: config.getReplaceRules()){
			String replacement = rule.length > 1 ? rule[1] : "";
			if(file.contains(rule[0])){
				String regex = rule[0].replace("\\", "\\\\");
				replacement = replacement.replace("\\", "\\\\");
				file = file.replaceFirst(regex, replacement);
				break;
			}
		}
		String path = config.getClassRoot() + file;
		int fileIndex = path.lastIndexOf("\\") + 1;
		String fileName = path.substring(fileIndex);
		int suffixIndex = fileName.lastIndexOf(".");
		
		Patch patch = new Patch();
		patch.setPath(path.substring(0, fileIndex));
		patch.setName(fileName.substring(0, suffixIndex));
		patch.setSuffix(fileName.substring(suffixIndex));
		return patch;
	}
	
	public static boolean isClassMatch(Patch patch, String className){
		Pattern pattern = Pattern.compile(patch.getName() + "(\\$.+)?\\.class");
		Matcher matcher = pattern.matcher(className);
		return matcher.matches() ? true : false;
	}
	
	public static boolean isFileMatch(Patch patch, String fileName){
		if(".java".equals(patch.getSuffix())){
			return isClassMatch(patch, fileName);
		} else {
			return StringUtils.equals(fileName, patch.getName()+patch.getSuffix());
		}
	}
	
	public static String getTargetPath(String srcPath, Config config){
		String classRoot = config.getClassRoot().replace("\\", "\\\\");
		String tarRoot = config.getTargetRoot().replace("\\", "\\\\");
		return srcPath.replaceFirst(classRoot, tarRoot);
	}
}
