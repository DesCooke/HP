package com.example.des.hp.InternalImages;


public class InternalImageItem
{
    public int holidayId;
    public String internalImageFilename;

    public InternalImageItem(String argFilename, int argUsageCount, int argHolidayId)
    {
        internalImageFilename=argFilename;
        holidayId=argHolidayId;
    }

}
