package com.kjt.ec.ioc.bean;

import java.io.File;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageScanner {

    public static List<Class> loadClassFromPackage(String basePack){
        List<Class> result=new ArrayList<Class>();
        loadClassFileFromPackage(basePack,result);
        //先把包名转换为路径,首先得到项目的classpath
        List<String> classPaths = new ArrayList<String>();
        String classpath = PackageScanner.class.getResource("/").getPath();
        //然后把我们的包名basPach转换为路径名
        basePack = basePack.replace(".", File.separator);
        //然后把classpath和basePack合并
        String searchPath = classpath + basePack;
        doPath(new File(searchPath),classPaths);
        for (String s : classPaths) {
            try{
                s = s.replace(classpath.replace("/","\\").replaceFirst("\\\\",""),"").replace("\\",".").replace(".class","");
                Class cls = Class.forName(s);
                result.add(cls);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return result;
    }

    private static void loadClassFileFromPackage(String basePack,List<Class> result){
        try{
            Enumeration<URL> urlEnumeration = Thread.currentThread().getContextClassLoader().getResources(basePack.replace(".", "/"));
            while (urlEnumeration.hasMoreElements()) {
                URL url = urlEnumeration.nextElement();
                String protocol = url.getProtocol();
                if ("jar".equalsIgnoreCase(protocol)) {
                    JarURLConnection connection = (JarURLConnection) url.openConnection();
                    if (connection != null) {
                        JarFile jarFile = connection.getJarFile();
                        if (jarFile != null) {
                            //得到该jar文件下面的类实体
                            Enumeration<JarEntry> jarEntryEnumeration = jarFile.entries();
                            while (jarEntryEnumeration.hasMoreElements()) {
                                JarEntry entry = jarEntryEnumeration.nextElement();
                                String jarEntryName = entry.getName();
                                //这里我们需要过滤不是class文件和不在basePack包名下的类
                                if (jarEntryName.contains(".class") && jarEntryName.replaceAll("/",".").startsWith(basePack)) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replace("/", ".");
                                    Class cls = Class.forName(className);
                                    result.add(cls);
                                }
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void doPath(File file, List<String> classPaths) {
        if (file.isDirectory()) {//文件夹
            //文件夹我们就递归
            File[] files = file.listFiles();
            for (File f1 : files) {
                doPath(f1,classPaths);
            }
        } else {//标准文件
            //标准文件我们就判断是否是class文件
            if (file.getName().endsWith(".class")) {
                //如果是class文件我们就放入我们的集合中。
                classPaths.add(file.getPath());
            }
        }
    }
}
