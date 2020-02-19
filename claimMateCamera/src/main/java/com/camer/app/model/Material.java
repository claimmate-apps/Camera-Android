package com.camer.app.model;

import java.io.Serializable;

public class Material
  implements Serializable
{
  private static final long serialVersionUID = 5L;
  int area_id;
  int materail_id;
  String materail_name;
  
  public Material(int paramInt1, String paramString, int paramInt2)
  {
    this.materail_id = paramInt1;
    this.materail_name = paramString;
    this.area_id = paramInt2;
  }
  
  public int getArea_id()
  {
    return this.area_id;
  }
  
  public int getMaterail_id()
  {
    return this.materail_id;
  }
  
  public String getMaterail_name()
  {
    return this.materail_name;
  }
  
  public void setArea_id(int paramInt)
  {
    this.area_id = paramInt;
  }
  
  public void setMaterail_id(int paramInt)
  {
    this.materail_id = paramInt;
  }
  
  public void setMaterail_name(String paramString)
  {
    this.materail_name = paramString;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\Material.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */