package org.simple.jdbc.util;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Desc:properties文件获取工具类
 */
public class PropertyUtil {
    private static Map<String, Object> configMap = new HashMap<String, Object>();
    //@description 销毁配置存储信息
    public static void release() {
		configMap.clear();
	}
	
	public static void addConfig(String key,String value){
		configMap.put(key, value);
	}
	public static void init(String file) {
		//第一步，初始化Properties对象
		Properties conFig = new Properties();
		//第二步，初始化InputStream对象
		InputStream inputStream = null;
		try {
			//第三步，读取配置文件，转换成流对象
			inputStream = new FileInputStream(file);
			//第四步，Properties对象加载流对象信息
			conFig.load(inputStream);
			//第五步，判断是否为空对象
			if (!conFig.isEmpty()) {
				Iterator<Map.Entry<Object, Object>> it = conFig.entrySet()
						.iterator();
				while (it.hasNext()) {
					Map.Entry<Object, Object> entry = it.next();
					Object key = entry.getKey();
					Object value = entry.getValue();
					configMap.put(key.toString(), value.toString());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//第六步，关闭流对象信息
			try {
				if(inputStream != null){
					inputStream.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getValue(final String key) {
		if(key != null && !key.equals("")){
			Object object = configMap.get(key);
			final String result = object == null ?"":object.toString().trim();
			return result;
		}
		return "";
	}
}

