package org.simple.jdbc.util;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FileUtil {
	
	 private static String filesp = "\\\\";
	 static {
		if("/".equals(File.separator)) {
			filesp = "/";
		}
	 }
	 private static List<String> fileNameList;
	 //获取src包下的类
	 private static void getAllFileName(String path,boolean flag,final String keyword) {
		 String filepath = path.replaceAll("\\.",filesp);
		 URI url = null ;
		try {
			url = FileUtil.class.getClassLoader().getResource(filepath).toURI();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File file = new File(url);
        File[] tempList = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				if(keyword == null) {
					if(pathname.isDirectory()
							||pathname.isFile()){
						return true;
					}
				}else {
					if(pathname.isDirectory()
							||(pathname.isFile()&&pathname.getName().length() > keyword.length() && pathname.getName().contains(keyword) && !pathname.getName().contains("$")))//目录或文件包含关键字
					{	return true;}
				}
				return false;
			}
		});
        if(tempList != null) {
        	for (int i = 0; i < tempList.length; i++) {
        		if (tempList[i].isFile()) {
        			fileNameList.add(path+"."+tempList[i].getName().replaceAll(".class", ""));
        		}
        		if (tempList[i].isDirectory()) {
        			if(flag) {
        				getAllFileName(tempList[i].getAbsolutePath(),flag,keyword);
        			}
        		}
        	}
        }
        return;
    }
	 private static void initParams() {
		 if(fileNameList != null) {
			 fileNameList.clear();
		 }else {
			 fileNameList = new ArrayList<String>();
		 }
	 }
	 /**
	  * 
	 * @Title: getFileNameList
	 * @Description: TODO
	 * @param    path:文件夹目录   flag:是否查找子目录   fileType:过滤文件类型（选中）
	 * @return List<String>    
	 * @throws
	  */
	 public static List<String> getFileNameList(String path,boolean flag,String fileType){
		 initParams();
		 getAllFileName(path,flag,fileType);
		 return fileNameList;
	 }
	 /**
	  * 获取文件路径，若路径不存在则创建
	  */
	 public static String getFilePath(String path) {
		 File file = new File(path);
		 if(!file.exists()) {
			 file.mkdirs();
		 }
		 if(file.isDirectory()) {
			 return file.getAbsolutePath();
		 }
		 return null;
	 }

	public static String getUrlPathSuffix(String urlPath){
	 	int n = urlPath.lastIndexOf(".");
	 	String res = "";
	 	if(n > 0){
			res = urlPath.substring(n,urlPath.length());
		}
	 	return res;
	}

	private static HashSet<String> classNames = new HashSet<>();

	public static HashSet<String> doScanPackage(String basePackage) {
		URL resource = FileUtil.class.getResource("/" + basePackage.replaceAll("\\.", "/"));
		String fileStr = resource.getFile();
		File file = new File(fileStr);
		String[] listFiles = file.list();
		for (String path : listFiles) {
			File filePath = new File(fileStr +File.separator+ path);
			// 如果当前是目录，则递归
			if (filePath.isDirectory()) {
				doScanPackage(basePackage + "." + path);
				// 如果是文件，则直接添加到classNames
			} else {
				if (filePath.getName().contains(".class") && file.getName().length() > 6) {
					classNames.add(basePackage + "." + filePath.getName());
				}
			}
		}
		return classNames;
	}
	 
}
