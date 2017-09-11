package com.example.des.hp.myutils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

class ZipUnzip
{
    private static final int BUFFER_SIZE=8192;//2048;
    private static String TAG=ZipUnzip.class.getName();
    private static String parentPath="";

    static boolean zip(String sourcePath, String destinationPath, String destinationFileName, Boolean includeParentFolder)
    {
        FileOutputStream fileOutputStream;
        ZipOutputStream zipOutputStream=null;
        try
        {
            File f=new File(destinationPath);
            if(!f.exists())
                if(!f.mkdirs())
                    throw new Exception("Error in creating directory " + destinationPath);
            if(!destinationPath.endsWith("/"))
                destinationPath+="/";
            String destination=destinationPath + destinationFileName;
            File file=new File(destination);
            if(!file.exists())
                if(!file.createNewFile())
                    throw new Exception("Error in creating file " + destination);

            fileOutputStream=new FileOutputStream(file);
            zipOutputStream=new ZipOutputStream(new BufferedOutputStream(fileOutputStream));

            if(includeParentFolder)
                parentPath=new File(sourcePath).getParent() + "/";
            else
                parentPath=sourcePath;

            zipFile(zipOutputStream, sourcePath);

        }
        catch(Exception ioe)
        {
            Log.d(TAG, ioe.getMessage());
            return false;
        }
        finally
        {
            if(zipOutputStream != null)
                try
                {
                    zipOutputStream.close();
                }
                catch(IOException e)
                {
                    //
                }
        }

        return true;

    }

    private static void zipFile(ZipOutputStream zipOutputStream, String sourcePath) throws IOException
    {

        java.io.File files=new java.io.File(sourcePath);
        java.io.File[] fileList=files.listFiles();

        String entryPath;
        BufferedInputStream input;
        for(java.io.File file : fileList)
        {
            if(file.isDirectory())
            {
                zipFile(zipOutputStream, file.getPath());
            } else
            {
                byte data[]=new byte[BUFFER_SIZE];
                FileInputStream fileInputStream=new FileInputStream(file.getPath());
                input=new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                entryPath=file.getAbsolutePath().replace(parentPath, "");

                ZipEntry entry=new ZipEntry(entryPath);
                zipOutputStream.putNextEntry(entry);

                int count;
                while((count=input.read(data, 0, BUFFER_SIZE)) != -1)
                {
                    zipOutputStream.write(data, 0, count);
                }
                input.close();
            }
        }


    }

    static Boolean unzip(String sourceFile, String destinationFolder)
    {
        ZipInputStream zis=null;

        try
        {
            zis=new ZipInputStream(new BufferedInputStream(new FileInputStream(sourceFile)));
            ZipEntry ze;
            int count;
            byte[] buffer=new byte[BUFFER_SIZE];
            while((ze=zis.getNextEntry()) != null)
            {
                String fileName=ze.getName();
                fileName=fileName.substring(fileName.indexOf("/") + 1);
                File file=new File(destinationFolder, fileName);
                File dir=ze.isDirectory() ? file : file.getParentFile();

                if(!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Invalid path: " + dir.getAbsolutePath());
                if(ze.isDirectory())
                    continue;
                FileOutputStream fout=new FileOutputStream(file);
                while((count=zis.read(buffer)) != -1)
                    fout.write(buffer, 0, count);
                fout.close();
            }
        }
        catch(IOException ioe)
        {
            Log.d(TAG, ioe.getMessage());
            return false;
        }
        finally
        {
            if(zis != null)
                try
                {
                    zis.close();
                }
                catch(IOException e)
                {
                    //
                }
        }
        return true;
    }


    public static void saveToFile(String destinationPath, String data, String fileName)
    {
        try
        {
            File f=new File(destinationPath);
            if(!f.mkdirs())
                throw new Exception("unable to mkdirs " + destinationPath);
            File file=new File(destinationPath + fileName);
            if(!file.exists())
            {
                if(!file.createNewFile())
                    throw new Exception("unable to create new file  " + destinationPath + fileName);
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file, true);
            fileOutputStream.write((data + System.getProperty("line.separator")).getBytes());

        }
        catch(Exception ex)
        {
            Log.d(TAG, ex.getMessage());
        }
    }


}

