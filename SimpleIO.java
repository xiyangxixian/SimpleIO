package test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author yangming
 */
public class SimpleIO {
    
    private File mFile;
    
    public SimpleIO(File file){
        mFile=file;
    }
    
    public SimpleIO(String path){
        mFile=new File(path);
    }
    
    public SimpleIO(URI uri){
        mFile=new File(uri);
    }
    public SimpleIO(String partent,String child){
        mFile=new File(partent, child);
    }
    
    public SimpleIO(File partent,String child){
        mFile=new File(partent, child);
    }
    
    /**
     * 将文件读取为byte数组
     * @return
     */
    public byte[] readAsByte(){
        byte[] bytes=new byte[0];
        if(!mFile.exists()||mFile.isDirectory()){
            return bytes;
        }
        try {
            bytes=readAsByte(new FileInputStream(mFile));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return bytes;
    }
    
    /**
     * 将文件读取为字符串，默认字符集为“utf-8”
     * @return
     */
    public String readAsString(){
        return readAsString("utf-8");
    }
    
    /**
     * 将文件读取为字符串，并指定字符集
     * @param charset
     * 字符集
     * @return
     */
    public String readAsString(String charset){
        String str="";
        byte[] bytes=readAsByte();
        try {
            str=new String(bytes,charset);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }
    
    /**
     * 将文件读取为对象
     * @return
     */
    public Object readAsObject(){
        ByteArrayInputStream bais=null;
        ObjectInputStream ois=null;
        Object object=null;
        try {
            byte bytes[]=readAsByte();
            bais=new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bais);
            object=ois.readObject();
            return object;
        } catch (IOException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(ois!=null){
                try {
                    ois.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(bais!=null){
                try {
                    bais.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return object;
    }
    
    /**
     * 将一个输入流读取为byte数组
     * @param is 
     * 输入流
     * @return
     */
    public static byte[] readAsByte(InputStream is){
        byte[] bytes=new byte[0];
        BufferedInputStream bis=null;
        try {
            bis=new BufferedInputStream(is);
            bytes=new byte[bis.available()];
            bis.read(bytes);
        } catch (IOException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }finally {
            if(bis!=null){
                try {
                    bis.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return bytes;
    }
    
    /**
     * 将输入流读取为字符串，默认字符集为“utf-8”
     * @param is
     * 输入流
     * @return
     */
    public static String readAsString(InputStream is){
        return readAsString(is,"utf-8");
    }
    
    /**
     * 将输入流读取为字符穿，并指定字符集
     * @param is
     * 输入流
     * @param charset
     * 字符集
     * @return
     */
    public static String readAsString(InputStream is,String charset){
        String str="";
        byte[] byes=readAsByte(is);
        try {
            str=new String(byes,charset);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }
    
    /**
     * 将byte数组覆盖写入文件
     * @param bytes
     */
    public void write(byte[] bytes){
        writeData(bytes,false);
    }
    
    /**
     * 将byte数组覆盖写入文件，并指定覆盖写入或追加写入
     * @param bytes
     * @param append
     */
    public void write(byte[] bytes,boolean append){
        writeData(bytes,append);
    }
    
    private void writeData(byte[] bytes,boolean append){
        BufferedOutputStream bos=null;
        make(mFile);
        try {
            bos=new BufferedOutputStream(new FileOutputStream(mFile,append));
            bos.write(bytes);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * 将字符串覆盖写入文件
     * @param str
     */
    public void write(String str){
        write(str,"utf-8");
    }
    
    /**
     * 将字符串写入文件，并指定覆盖写入或追加写入
     * @param str
     * @param append
     */
    public void write(String str,boolean append){
        write(str,"utf-8",append);
    }
    
    /**
     * 将字符串覆盖写入文件，并指定字符集
     * @param str
     * @param charset
     */
    public void write(String str,String charset){
        try {
            write(str.getBytes(charset));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 将字符串覆盖写入文件，并指定字符集和是否追加写入
     * @param str
     * @param charset
     * @param append
     */
    public void write(String str,String charset,boolean append){
        try {
            write(str.getBytes(charset),append);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * 将一个可序列化的对象写入文件
     * @param Object
     */
    public void write(Serializable Object){
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(Object);
            write(baos.toByteArray());
        } catch (IOException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if(oos!=null){
                try {
                    oos.flush();
                    oos.close();
                } catch (IOException ex) {
                 Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * 将一个输入流写入文件
     * @param is
     */
    public void write(InputStream is){
        BufferedOutputStream bos=null;
        make(mFile);
        try {
            byte[] bytes=new byte[1024];
            bos = new BufferedOutputStream(new FileOutputStream(mFile));
            int len=0;
            try {
                while((len=is.read(bytes))!=-1){
                    bos.write(bytes, 0, len);
                }
            } catch (IOException ex) {
                Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            if(bos!=null){
                try {
                    bos.flush();
                    bos.close();
                } catch (IOException ex) {
                    Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
    
    /**
     * 将文件拷贝至另一个文件
     * @param toFile
     */
    public void copyFromFile(File fromFile){
        if(!mFile.getAbsolutePath().equals(fromFile.getAbsolutePath())){
            BufferedInputStream bis=null;
            try {
                bis=new BufferedInputStream(new FileInputStream(fromFile));
                write(bis);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void make(File file){
        File parentFile=file.getParentFile();
        if(parentFile==null){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
            }
            return;
        }
        if(!file.getParentFile().exists()){
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(SimpleIO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
