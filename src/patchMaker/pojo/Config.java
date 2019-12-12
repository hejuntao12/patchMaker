package patchMaker.pojo;

import java.util.List;
import java.util.Properties;

import patchMaker.ReplaceRuleUtil;

public class Config {
	
	private String classRoot;
	
	private String targetRoot;
	
	private String pathsFile;
	
	private List<String[]> replaceRules;

	public String getClassRoot() {
		return classRoot;
	}

	public void setClassRoot(String classRoot) {
		this.classRoot = classRoot;
	}

	public String getTargetRoot() {
		return targetRoot;
	}

	public void setTargetRoot(String targetRoot) {
		this.targetRoot = targetRoot;
	}

	public String getPathsFile() {
		return pathsFile;
	}

	public void setPathsFile(String pathsFile) {
		this.pathsFile = pathsFile;
	}

	public List<String[]> getReplaceRules() {
		return replaceRules;
	}

	public void setReplaceRules(List<String[]> replaceRules) {
		this.replaceRules = replaceRules;
	}
	
	public Config(Properties prop){
		this.classRoot = prop.getProperty("classRoot", "");
		this.targetRoot = prop.getProperty("targetRoot", "");
		this.pathsFile = prop.getProperty("pathsFile", "");
		this.replaceRules = ReplaceRuleUtil.parseReplaceRule(prop.getProperty("replaceRules", ""));
	}

}
