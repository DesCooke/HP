package com.example.des.hp.myutils;

import android.net.Uri;

import org.junit.Test;

import static com.example.des.hp.myutils.MyFileUtils.myFileUtils;
import static org.junit.Assert.*;

/**
 * Created by cooked on 13/09/2017.
 */
public class MyFileUtilsTest
{
    private void baseFilenameFromUriTest(String path, String filename) throws Exception
    {
        Uri lUri;
        String lActualFilename;
    
        lUri = Uri.parse(path + "/" + filename);
        MyString myString = new MyString();
        Boolean lReply=myFileUtils().BaseFilenameFromUri(lUri, myString);
        assertEquals("BaseFilenameFromUri did not return true", lReply, Boolean.TRUE);
        lActualFilename = myString.Value;
        assertEquals("BaseFilenameFromUri did not return correct value", lActualFilename, filename);
    }
    @Test
    public void baseFilenameFromUri() throws Exception
    {
        baseFilenameFromUriTest("", "joe.txt");
        baseFilenameFromUriTest("/directory", "joe.txt");
        baseFilenameFromUriTest("/sub/directory", "joe.txt");
        baseFilenameFromUriTest("//main.com/sub/directory", "joe.txt");
        baseFilenameFromUriTest("https://main.com/sub/directory", "joe.txt");
    }

   
    private void stringToUriTest(String path)
    {
        Uri lUri = Uri.parse(path);
        String lActualPath=lUri.getPath();
        assertEquals("stringToUriTest conversion not successful", lActualPath, path);
    }
    @Test
    public void stringToUri() throws Exception
    {
        stringToUriTest("joe.txt");
        stringToUriTest("/directory/joe.txt");
        stringToUriTest("/sub/directory/joe.txt");
        stringToUriTest("//main.com/sub/directory/joe.txt");
        stringToUriTest("https://main.com/sub/directory/joe.txt");
    }
    
    @Test
    public void openAFile() throws Exception
    {
        
    }
    
    public void getMyFilePathTest(String path, String expectedPath)
    {
        Uri lUri = Uri.parse(path);
        String actualPath=myFileUtils().getMyFilePath(lUri);
        assertEquals("Path not returned correctly", actualPath, expectedPath);
    }
    @Test
    public void getMyFilePath() throws Exception
    {
        getMyFilePathTest("joe.txt", "joe.txt");
        getMyFilePathTest("/directory/joe.txt", "joe.txt");
        getMyFilePathTest("/sub/directory/joe.txt", "joe.txt");
        getMyFilePathTest("//main.com/sub/directory/joe.txt", "joe.txt");
        getMyFilePathTest("https://main.com/sub/directory/joe.txt", "joe.txt");
    }
    
    @Test
    public void copyFileToLocalDir() throws Exception
    {
        
    }
    
}