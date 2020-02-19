package com.camer.app.model;

import java.io.Serializable;

public class TempletsType
  implements Serializable
{
  private static final long serialVersionUID = 7L;
  String claim_date;
  String claim_detail_1;
  String claim_detail_2;
  String claim_detail_3;
  String claim_no;
  String claim_overview;
  String claim_time;
  String claim_user;
  String damage_type;
  String image_url;
  String materials_type;
  int templet_id;
  String templet_name;
  
  public TempletsType() {}
  
  public TempletsType(int paramInt, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7, String paramString8, String paramString9, String paramString10, String paramString11, String paramString12)
  {
    this.templet_id = paramInt;
    this.templet_name = paramString1;
    this.claim_no = paramString2;
    this.claim_date = paramString3;
    this.claim_time = paramString4;
    this.claim_user = paramString5;
    this.claim_overview = paramString6;
    this.claim_detail_1 = paramString7;
    this.claim_detail_2 = paramString8;
    this.claim_detail_3 = paramString9;
    this.damage_type = paramString10;
    this.image_url = paramString11;
    this.materials_type = paramString12;
  }
  
  public String getClaim_date()
  {
    return this.claim_date;
  }
  
  public String getClaim_detail_1()
  {
    return this.claim_detail_1;
  }
  
  public String getClaim_detail_2()
  {
    return this.claim_detail_2;
  }
  
  public String getClaim_detail_3()
  {
    return this.claim_detail_3;
  }
  
  public String getClaim_no()
  {
    return this.claim_no;
  }
  
  public String getClaim_overview()
  {
    return this.claim_overview;
  }
  
  public String getClaim_time()
  {
    return this.claim_time;
  }
  
  public String getClaim_user()
  {
    return this.claim_user;
  }
  
  public String getDamage_type()
  {
    return this.damage_type;
  }
  
  public String getImage_url()
  {
    return this.image_url;
  }
  
  public String getMaterials_type()
  {
    return this.materials_type;
  }
  
  public int getTemplet_id()
  {
    return this.templet_id;
  }
  
  public String getTemplet_name()
  {
    return this.templet_name;
  }
  
  public void setClaim_date(String paramString)
  {
    this.claim_date = paramString;
  }
  
  public void setClaim_detail_1(String paramString)
  {
    this.claim_detail_1 = paramString;
  }
  
  public void setClaim_detail_2(String paramString)
  {
    this.claim_detail_2 = paramString;
  }
  
  public void setClaim_detail_3(String paramString)
  {
    this.claim_detail_3 = paramString;
  }
  
  public void setClaim_no(String paramString)
  {
    this.claim_no = paramString;
  }
  
  public void setClaim_overview(String paramString)
  {
    this.claim_overview = paramString;
  }
  
  public void setClaim_time(String paramString)
  {
    this.claim_time = paramString;
  }
  
  public void setClaim_user(String paramString)
  {
    this.claim_user = paramString;
  }
  
  public void setDamage_type(String paramString)
  {
    this.damage_type = paramString;
  }
  
  public void setImage_url(String paramString)
  {
    this.image_url = paramString;
  }
  
  public void setMaterials_type(String paramString)
  {
    this.materials_type = paramString;
  }
  
  public void setTemplet_id(int paramInt)
  {
    this.templet_id = paramInt;
  }
  
  public void setTemplet_name(String paramString)
  {
    this.templet_name = paramString;
  }
}


/* Location:              C:\Users\CTINFO\Desktop\apk\Claim Mate\classes-dex2jar.jar!\com\camer\app\model\TempletsType.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */