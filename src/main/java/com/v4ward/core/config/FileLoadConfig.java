package com.v4ward.core.config;

import java.io.File;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.v4ward.core.common.FileConstant;
import com.v4ward.core.properties.V4wardProperties;
/**
 * 
 * @ClassName: FileLoadConfig 
 * @Description: 在不借助其他服务器的情况下，解决spring boot打成jar包后，jar包外的文件访问方法
 * @author: Zhousheng
 * @date: 2018年3月19日 上午9:21:44
 */
@Configuration
public class FileLoadConfig extends WebMvcConfigurerAdapter{
	
	
	    //需要在application-*.properties中指定图片所在的相应路径：例如: v4ward.filePath=file:/C:/images/
		//如果项目中有拦截器，一定要添加不要拦截图片路径
	    @Autowired
	    private V4wardProperties v4wardProperties;
		
	    //访问文件方法
	    @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	    	String filePath = v4wardProperties.getFilePath();
	    	System.out.println(filePath+"----------------");
	        if(filePath.equals("") || filePath.equals("${v4ward.filePath}")){
	            String path = FileLoadConfig.class.getClassLoader().getResource("").getPath();
	            if(path.indexOf(FileConstant.JAR_SUFFIX)>0){
	            	path = path.substring(0, path.indexOf(FileConstant.JAR_SUFFIX));
	                System.out.println(path);
	            }else if(path.indexOf(FileConstant.CLASS_SUFFIX)>0){
	            	path = "file:"+path.substring(0, path.indexOf(FileConstant.CLASS_SUFFIX));
	                System.out.println(path);
	            }
	            path = path.substring(0, path.lastIndexOf(FileConstant.SEPARATOR))+FileConstant.SEPARATOR+FileConstant.PREFIX+FileConstant.SEPARATOR;
	            System.out.println(path);
	            filePath = path;
	        }
	        LoggerFactory.getLogger(FileLoadConfig.class).info("filePath="+filePath);
	        registry.addResourceHandler(FileConstant.SEPARATOR+FileConstant.PREFIX+FileConstant.SEPARATOR+FileConstant.SUFFIX).addResourceLocations(filePath);
	        super.addResourceHandlers(registry);
	    }

}
