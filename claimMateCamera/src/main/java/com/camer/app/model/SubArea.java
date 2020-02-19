package com.camer.app.model;

import java.io.Serializable;

public class SubArea
  implements Serializable
{
  private static final long serialVersionUID = 1L;
  int area_id;
  String area_name;
  int sub_area_id;
  
  public SubArea(int paramInt1, String paramString, int paramInt2)
  {
    this.sub_area_id = paramInt1;
    this.area_name = paramString;
    this.area_id = paramInt2;
  }
  
  public int getArea_id()
  {
    return this.area_id;
  }
  
  public int getId()
  {
    return this.sub_area_id;
  }
  
  public String getSub_Area_name()
  {
    return this.area_name;
  }
  
  public void setArea_id(int paramInt)
  {
    this.area_id = paramInt;
  }
  
  public void setId(int paramInt)
  {
    this.sub_area_id = paramInt;
  }
  
  public void setSub_Area_name(String paramString)
  {
    this.area_name = paramString;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\SubArea.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */