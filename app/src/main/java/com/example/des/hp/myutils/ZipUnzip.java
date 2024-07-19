package com.example.des.hp.myutils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/** @noinspection unused*/
class ZipUnzip
{
    private static final int BUFFER_SIZE=8192;//2048;
    private static final String TAG=ZipUnzip.class.getName();
    private static String parentPath="";

    static void zip(String sourcePath, String destinationPath, String destinationFileName)
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

            parentPath=new File(sourcePath).getParent() + "/";

            zipFile(zipOutputStream, sourcePath);

        }
        catch(Exception ioe)
        {
            Log.d(TAG, Objects.requireNonNull(ioe.getMessage()));
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

    }

    private static void zipFile(ZipOutputStream zipOutputStream, String sourcePath) throws IOException
    {

        java.io.File files=new java.io.File(sourcePath);
        java.io.File[] fileList=files.listFiles();

        String entryPath;
        BufferedInputStream input;
        assert fileList != null;
        for(java.io.File file : fileList)
        {
            if(file.isDirectory())
            {
                zipFile(zipOutputStream, file.getPath());
            } else
            {
                byte[] data =new byte[BUFFER_SIZE];
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
            zis=new ZipInputStream(new BufferedInputStream(Files.newInputStream(Paths.get(sourceFile))));
            ZipEntry ze;
            int count;
            byte[] buffer=new byte[BUFFER_SIZE];
            while((ze=zis.getNextEntry()) != null)
            {
                String fileName=ze.getName();
                fileName=fileName.substring(fileName.indexOf("/") + 1);
                File file=new File(destinationFolder, fileName);
                File dir=ze.isDirectory() ? file : file.getParentFile();

                assert dir != null;
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
            Log.d(TAG, Objects.requireNonNull(ioe.getMessage()));
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
            try (FileOutputStream fileOutputStream = new FileOutputStream(file, true)) {
                fileOutputStream.write((data + System.lineSeparator()).getBytes());
            }

        }
        catch(Exception ex)
        {
            Log.d(TAG, Objects.requireNonNull(ex.getMessage()));
        }
    }


}

