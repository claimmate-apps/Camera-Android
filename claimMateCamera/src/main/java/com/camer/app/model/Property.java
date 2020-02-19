package com.camer.app.model;

import java.io.Serializable;

public class Property
  implements Serializable
{
  String Property_data;
  String Property_desc;
  String id;
  
  public Property(String paramString1, String paramString2, String paramString3)
  {
    this.id = paramString1;
    this.Property_data = paramString2;
    this.Property_desc = paramString3;
  }
  
  public String getId()
  {
    return this.id;
  }
  
  public String getProperty_data()
  {
    return this.Property_data;
  }
  
  public String getProperty_desc()
  {
    return this.Property_desc;
  }
  
  public void setId(String paramString)
  {
    this.id = paramString;
  }
  
  public void setProperty_data(String paramString)
  {
    this.Property_data = paramString;
  }
  
  public void setProperty_desc(String paramString)
  {
    this.Property_desc = paramString;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\Property.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */