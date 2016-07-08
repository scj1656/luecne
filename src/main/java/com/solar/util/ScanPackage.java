package com.solar.util;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.solar.annotation.solarHandler;
import com.solar.protocol.SolarRequest;

public class ScanPackage {
	
	private static ScanPackage scanPackage=null;
	
	private ScanPackage(){
		
	}
	
	public static ScanPackage getInstance(){
		if(scanPackage==null){
			synchronized (ScanPackage.class) {
				if(scanPackage==null){
					scanPackage = new ScanPackage();
				}
			}
		}
		return scanPackage;
	}
	
	public Set<Class<?>> getClass(String pack){
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		boolean recuresive = true;//是否循环迭代
		String packageDir = pack.replace(".", "/");
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(packageDir);
			while(dirs.hasMoreElements()){
				URL url = dirs.nextElement();//获取下一个元素
				String protocol = url.getProtocol();//得到协议名称	
				if("file".equals(protocol)){
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					findAndClassesInPackageByFile(pack,filePath,recuresive,classes);
				}else if("jar".equals(protocol)){//如果是jar包文件
					JarFile jar;
					jar = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries =jar.entries();
					while(entries.hasMoreElements()){
						// 获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if(name.charAt(0)=='/'){//如果是以／开头的
							name = name.substring(1);
						}
						if(name.startsWith(packageDir)){//如果前半部分和定义的包名相同
							int idx = name.lastIndexOf("/");
							if(idx!=-1){//如果以“／”结尾 是一个包
								pack = name.substring(0,idx).replace("/", ".");
							}
							if(idx!=-1||recuresive){
								if(name.endsWith(".class")&& !entry.isDirectory()){
									String className = name.substring(pack.length()+1, name.length()-6);//去掉后面的“.class”，获取真正的类名
									try {
										classes.add(Class.forName(pack+"."+className));
									} catch (ClassNotFoundException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classes;
	}

	private void findAndClassesInPackageByFile(String packageName, String packagePath,
			final boolean recuresive, Set<Class<?>> classes) {
		File dir = new File(packagePath);//获取此包的目录，建立一个file
		if(!dir.exists()||!dir.isDirectory()){//不是目录就直接返回
			return;
		}
		File[] dirfiles = dir.listFiles(new FileFilter() {
			
			public boolean accept(File file) {
				return (recuresive&&file.isDirectory()||(file.getName().endsWith(".class")));
			}
		});
		
		for(File file:dirfiles){
			if(file.isDirectory()){
				findAndClassesInPackageByFile(packageName+"."+file.getName(), file.getAbsolutePath(), recuresive, classes);
			}else{
				String className = file.getName().substring(0,file.getName().length()-6);
				try {
					classes.add(Thread.currentThread().getContextClassLoader().loadClass(packageName+"."+className));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public static void main(String[] args) throws IllegalAccessException {
		ScanPackage sc = ScanPackage.getInstance();
		Set<Class<?>> classes = sc.getClass("com.solar.handler");
		for(Class obj:classes){
			Annotation annotation = obj.getAnnotation(solarHandler.class);
			if(annotation!=null){
				Method[] methods = obj.getMethods();
				for(Method method:methods){
					try {
						method.invoke(obj.newInstance(), new SolarRequest());
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println(obj.getName());
		}
	}
}
