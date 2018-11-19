package tianchi.com.risksourcecontrol2.util;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
public class ZipUtils {

    //	/**
    //	 * 解压一个压缩文档 到指定位置
    //	 * @param zipFile 压缩包的名字
    //	 * @param targetDir 指定的路径
    //	 * @throws Exception
    //	 */
    //	public static void UnZipFolder(String zipFile, String targetDir){
    //		android.util.Log.v("XZip", "UnZipFolder(String, String)");
    //		ZipInputStream inZip;
    //		try {
    //			inZip = new ZipInputStream(new FileInputStream(zipFile), Charset.forName("GBK"));
    //			ZipEntry zipEntry;
    //			String szName = "";
    //			while ((zipEntry = inZip.getNextEntry()) != null) {
    //				szName = zipEntry.getName();
    //
    //				if (zipEntry.isDirectory()) {
    //
    //					File folder = new File(targetDir + File.separator + szName);
    //					folder.mkdirs();
    //
    //				} else {
    //
    //					File file = new File(targetDir + File.separator + szName);
    //					file.createNewFile();
    //					// get the output stream of the file
    //					FileOutputStream out = new FileOutputStream(file);
    //					int len;
    //					byte[] buffer = new byte[1024];
    //					while ((len = inZip.read(buffer)) != -1) {
    //						out.write(buffer, 0, len);
    //						out.flush();
    //					}
    //					out.close();
    //				}
    //			}
    //			inZip.close();
    //
    //
    //		} catch (FileNotFoundException e) {
    //			e.printStackTrace();
    //
    //		} catch (IOException e) {
    //			e.printStackTrace();
    //
    //		}
    //	}

    //第一个参数就是需要解压的文件，第二个就是解压的目录
    public static boolean upZipFileDir(File zipFile, String folderPath) {
        ZipFile zfile= null;
        try {
            //转码为GBK格式，支持中文
            zfile = new ZipFile(zipFile,"GBK");
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList=zfile.getEntries();
        ZipEntry ze=null;
        byte[] buf=new byte[1024];
        while(zList.hasMoreElements()){
            ze=(ZipEntry)zList.nextElement();
            //列举的压缩文件里面的各个文件，判断是否为目录
            if(ze.isDirectory()){
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f=new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os= null;
            FileOutputStream fos = null;
            // ze.getName()会返回 script/start.script这样的，是为了返回实体的File
            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is= null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            int readLen=0;
            //进行一些内容复制操作
            try {
                while ((readLen=is.read(buf, 0, 1024))!=-1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     * @param baseDir 指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName){
        String[] dirs=absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;

        if(dirs.length>1){
            for (int i = 0; i < dirs.length-1;i++) {
                substr = dirs[i];
                ret=new File(ret, substr);
            }

            if(!ret.exists())
                ret.mkdirs();
            substr = dirs[dirs.length-1];
            ret=new File(ret, substr);
            return ret;
        }else{
            ret = new File(ret,absFileName);
        }
        return ret;
    }

}
