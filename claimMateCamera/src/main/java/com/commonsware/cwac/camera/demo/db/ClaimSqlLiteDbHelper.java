package com.commonsware.cwac.camera.demo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.commonsware.cwac.camera.demo.model.QueModel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;

public class ClaimSqlLiteDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
    public static  String claimdb_PATH = "";//"/data/data/com.claimmate.claimmatecamera21_20/databases/";
    //	public static final String claimdb_PATH = "/data/data/com.claimmate.claimmatecamera18/databases/";
//	public static final String claimdb_NAME = "claimmatecamera.sql";
    public static final String claimdb_NAME = "claimmatecamera.sqlite";
    public static SQLiteDatabase DB;
    Context context;

    public ClaimSqlLiteDbHelper(Context context) {
        super(context, claimdb_NAME, null, DATABASE_VERSION);
        this.context = context;

        claimdb_PATH = context.getFilesDir().getPath();
        // TODO Auto-generated constructor stub
    }

    private final String name = "Name", tbQue = "Que", id = "Id", folderName = "FolderName", folderSubName = "FolderSubName", rei = "REI", damage = "Damage", material = "Material", slop = "SlopRoom", area = "Area", ocb = "OCB", type = "Type", macroName = "MacroName", m1 = "M1", m2 = "M2", Room = "Room";
    private final String tbRoofLayer = "RoofLayer", userId = "UserId", claimId = "ClaimId", noLayer = "NoLayer", layerType = "LayerType",
            tbRoofPitch = "RoofPitch", pitchValue = "PitchValue", slope = "Slope",
            tbRoofShingle = "RoofShingle", years = "Years", tab = "Tab",
            tbRoof = "Roof", qty = "Qty",
            tbElevation = "Elevation", elevation = "Elevation",
            tbInteriorArea = "InteriorArea", areaType = "AreaType", insulation = "Insulation",
            tbRiskMacroDes = "RiskMacroDes", story = "Story", typeOfConstruction = "TypeOfConstruction", rci = "RCI", singleFamily = "SingleFamily", garageAttached = "GarageAttached", typeOfExteriorSiding = "TypeOfExteriorSiding", TbInterior = "TbInterior", TBLCeiling = "TBLCeiling", TBLWall = "TBLWall", TBLFloor = "TBLFloor";

    @Override
    public void onCreate(SQLiteDatabase DB) {
        String sql = "CREATE TABLE " + tbQue + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + name + " TEXT, " + folderName + " TEXT, " + folderSubName + " TEXT, " + rei + " text, " + damage + " text, " + material + " text, " + slop + " text, " + area + " text, " + ocb + " text, " + type + " text, " + macroName + " text, " + m1 + " text, " + m2 + " text)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbRoofLayer + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + noLayer + " TEXT, " + layerType + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbRoofPitch + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + pitchValue + " TEXT, " + slope + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbRoofShingle + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + years + " TEXT, " + tab + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbRoof + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + material + " TEXT, " + qty + " TEXT, " + slope + " TEXT, " + damage + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbElevation + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + material + " TEXT, " + qty + " TEXT, " + elevation + " TEXT, " + damage + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbInteriorArea + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + areaType + " TEXT, " + material + " TEXT, " + insulation + " TEXT, " + qty + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + tbRiskMacroDes + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + story + " TEXT, " + typeOfConstruction + " TEXT, " + rci + " TEXT, " + singleFamily + " TEXT, " + garageAttached + " TEXT, " + typeOfExteriorSiding + " TEXT)";
        DB.execSQL(sql);

        sql = "CREATE TABLE " + TbInterior + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + damage + " TEXT, " + areaType + " TEXT, " + material + " TEXT, " + Room + " TEXT, " + insulation + " TEXT, " + qty + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + TBLCeiling + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + damage + " TEXT, " + areaType + " TEXT, " + material + " TEXT, " + Room + " TEXT, " + insulation + " TEXT, " + qty + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + TBLWall + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + damage + " TEXT, " + areaType + " TEXT, " + material + " TEXT, " + Room + " TEXT, " + insulation + " TEXT, " + qty + " TEXT)";
        DB.execSQL(sql);
        sql = "CREATE TABLE " + TBLFloor + " (" + id + " INTEGER PRIMARY KEY AUTOINCREMENT, " + userId + " TEXT, " + claimId + " TEXT, " + damage + " TEXT, " + areaType + " TEXT, " + material + " TEXT, " + Room + " TEXT, " + insulation + " TEXT, " + qty + " TEXT)";
        DB.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
    }

    //copy DB from Assets folder
    public void CopyDataBaseFromAsset() throws IOException {
        InputStream in = context.getAssets().open(claimdb_NAME);

        String outputFileName = claimdb_PATH + claimdb_NAME;

        File databaseFile = new File(claimdb_PATH);
        if (!databaseFile.exists()) {
            databaseFile.mkdir();
        }

        OutputStream out = new FileOutputStream(outputFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = in.read(buffer)) > 0) {
            out.write(buffer, 0, length);
        }
        out.flush();
        out.close();
        in.close();


    }

    //open DB
    public void openDataBase() throws SQLException {
        String path = claimdb_PATH + claimdb_NAME;
        DB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void addQue(QueModel queModel) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(name, queModel.getName());
        contentValues.put(folderName, queModel.getFolderName());
        contentValues.put(folderSubName, queModel.getFolderSubName());
        contentValues.put(rei, queModel.getRei());
        contentValues.put(damage, queModel.getDamage());
        contentValues.put(material, queModel.getMaterial());
        contentValues.put(slop, queModel.getSlop());
        contentValues.put(area, queModel.getArea());
        contentValues.put(ocb, queModel.getOcb());
        contentValues.put(type, queModel.getType());
        contentValues.put(macroName, queModel.getMacroName());
        contentValues.put(m1, queModel.getM1());
        contentValues.put(m2, queModel.getM2());
        db.insert(tbQue, null, contentValues);
        db.close();
    }

    public ArrayList<QueModel> getQue() {
        ArrayList<QueModel> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbQue, null);
        if (cursor.moveToFirst()) {
            do {
                QueModel queModel = new QueModel();
                queModel.setId(cursor.getString(cursor.getColumnIndex(id)));
                queModel.setName(cursor.getString(cursor.getColumnIndex(name)));
                queModel.setFolderName(cursor.getString(cursor.getColumnIndex(folderName)));
                queModel.setFolderSubName(cursor.getString(cursor.getColumnIndex(folderSubName)));
                queModel.setRei(cursor.getString(cursor.getColumnIndex(rei)));
                queModel.setDamage(cursor.getString(cursor.getColumnIndex(damage)));
                queModel.setMaterial(cursor.getString(cursor.getColumnIndex(material)));
                queModel.setSlop(cursor.getString(cursor.getColumnIndex(slop)));
                queModel.setArea(cursor.getString(cursor.getColumnIndex(area)));
                queModel.setOcb(cursor.getString(cursor.getColumnIndex(ocb)));
                queModel.setType(cursor.getString(cursor.getColumnIndex(type)));
                queModel.setMacroName(cursor.getString(cursor.getColumnIndex(macroName)));
                queModel.setM1(cursor.getString(cursor.getColumnIndex(m1)));
                queModel.setM2(cursor.getString(cursor.getColumnIndex(m2)));
                arrayList.add(queModel);
            } while (cursor.moveToNext());
        }
        return arrayList;
    }

    public void removeQue(String qId) {
        getWritableDatabase().delete(tbQue, id + "=" + qId, null);
    }

    public void removeAllQue() {
        getWritableDatabase().delete(tbQue, null, null);
    }

    public void addRoof(String userId, String claimId, String material, String qty, String slope, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.qty, qty);
        contentValues.put(this.slope, slope);
        contentValues.put(this.damage, damage);
        db.insert(tbRoof, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRoof() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbRoof, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("material", cursor.getString(cursor.getColumnIndex(material)));
                hashMap.put("qty", cursor.getString(cursor.getColumnIndex(qty)));
                hashMap.put("slope", cursor.getString(cursor.getColumnIndex(slope)));
                hashMap.put("damage", cursor.getString(cursor.getColumnIndex(damage)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbRoof, null, null);

        return arrayList;
    }

    public void addindata(String userId, String claimId, String Room, String material, String insulation, String areaType, String qty, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.Room, Room);
        contentValues.put(this.insulation, insulation);
        contentValues.put(this.areaType, areaType);
        contentValues.put(this.qty, qty);
        contentValues.put(this.damage, damage);
        db.insert(TbInterior, null, contentValues);
        db.close();
    }

    public void adddataCeiling(String userId, String claimId, String Room, String material, String insulation, String areaType, String qty, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.Room, Room);
        contentValues.put(this.insulation, insulation);
        contentValues.put(this.areaType, areaType);
        contentValues.put(this.qty, qty);
        contentValues.put(this.damage, damage);
        db.insert(TBLCeiling, null, contentValues);
        db.close();
    }

    public void adddataWall(String userId, String claimId, String Room, String material, String insulation, String areaType, String qty, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.Room, Room);
        contentValues.put(this.insulation, insulation);
        contentValues.put(this.areaType, areaType);
        contentValues.put(this.qty, qty);
        contentValues.put(this.damage, damage);
        db.insert(TBLWall, null, contentValues);
        db.close();
    }

    public void adddataFloor(String userId, String claimId, String Room, String material, String insulation, String areaType, String qty, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.Room, Room);
        contentValues.put(this.insulation, insulation);
        contentValues.put(this.areaType, areaType);
        contentValues.put(this.qty, qty);
        contentValues.put(this.damage, damage);
        db.insert(TBLFloor, null, contentValues);
        db.close();
    }


    public ArrayList<HashMap<String, String>> getindata() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + TbInterior, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("material", cursor.getString(cursor.getColumnIndex(material)));
                hashMap.put("Room", cursor.getString(cursor.getColumnIndex(Room)));
                hashMap.put("insulation", cursor.getString(cursor.getColumnIndex(insulation)));
                hashMap.put("areaType", cursor.getString(cursor.getColumnIndex(areaType)));
                hashMap.put("qty", cursor.getString(cursor.getColumnIndex(qty)));
                hashMap.put("damage", cursor.getString(cursor.getColumnIndex(damage)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(TbInterior, null, null);

        return arrayList;
    }


    public void addElevation(String userId, String claimId, String material, String qty, String elevation, String damage) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.material, material);
        contentValues.put(this.qty, qty);
        contentValues.put(this.elevation, elevation);
        contentValues.put(this.damage, damage);
        db.insert(tbElevation, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getElevation() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbElevation, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("material", cursor.getString(cursor.getColumnIndex(material)));
                hashMap.put("qty", cursor.getString(cursor.getColumnIndex(qty)));
                hashMap.put("elevation", cursor.getString(cursor.getColumnIndex(elevation)));
                hashMap.put("damage", cursor.getString(cursor.getColumnIndex(damage)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbElevation, null, null);

        return arrayList;
    }

    public void addInteriorArea(String userId, String claimId, String areaType, String material, String insulation, String qty) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.areaType, areaType);
        contentValues.put(this.material, material);
        contentValues.put(this.insulation, insulation);
        contentValues.put(this.qty, qty);
        db.insert(tbInteriorArea, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getInteriorArea() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbInteriorArea, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("areaType", cursor.getString(cursor.getColumnIndex(areaType)));
                hashMap.put("material", cursor.getString(cursor.getColumnIndex(material)));
                hashMap.put("insulation", cursor.getString(cursor.getColumnIndex(insulation)));
                hashMap.put("qty", cursor.getString(cursor.getColumnIndex(qty)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbInteriorArea, null, null);

        return arrayList;
    }

    public void addRoofLayer(String userId, String claimId, String noLayer, String layerType) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.noLayer, noLayer);
        contentValues.put(this.layerType, layerType);
        db.insert(tbRoofLayer, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRoofLayer() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbRoofLayer, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("noLayer", cursor.getString(cursor.getColumnIndex(noLayer)));
                hashMap.put("layerType", cursor.getString(cursor.getColumnIndex(layerType)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbRoofLayer, null, null);

        return arrayList;
    }

    public void addRoofPitch(String userId, String claimId, String pitchValue, String slope) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.pitchValue, pitchValue);
        contentValues.put(this.slope, slope);
        db.insert(tbRoofPitch, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRoofPitch() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbRoofPitch, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("pitchValue", cursor.getString(cursor.getColumnIndex(pitchValue)));
                hashMap.put("slope", cursor.getString(cursor.getColumnIndex(slope)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbRoofPitch, null, null);

        return arrayList;
    }

    public void addRoofShingle(String userId, String claimId, String years, String tab) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.years, years);
        contentValues.put(this.tab, tab);
        db.insert(tbRoofShingle, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRoofShingle() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbRoofShingle, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("years", cursor.getString(cursor.getColumnIndex(years)));
                hashMap.put("tab", cursor.getString(cursor.getColumnIndex(tab)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbRoofShingle, null, null);

        return arrayList;
    }

    public void addRiskMacroDes(String userId, String claimId, String story, String typeOfConstruction, String rci, String singleFamily, String garageAttached, String typeOfExteriorSiding) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(this.userId, userId);
        contentValues.put(this.claimId, claimId);
        contentValues.put(this.story, story);
        contentValues.put(this.typeOfConstruction, typeOfConstruction);
        contentValues.put(this.rci, rci);
        contentValues.put(this.singleFamily, singleFamily);
        contentValues.put(this.garageAttached, garageAttached);
        contentValues.put(this.typeOfExteriorSiding, typeOfExteriorSiding);
        db.insert(tbRiskMacroDes, null, contentValues);
        db.close();
    }

    public ArrayList<HashMap<String, String>> getRiskMacroDes() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = getReadableDatabase().rawQuery("SELECT * FROM " + tbRiskMacroDes, null);
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("userId", cursor.getString(cursor.getColumnIndex(userId)));
                hashMap.put("claimId", cursor.getString(cursor.getColumnIndex(claimId)));
                hashMap.put("story", cursor.getString(cursor.getColumnIndex(story)));
                hashMap.put("typeOfConstruction", cursor.getString(cursor.getColumnIndex(typeOfConstruction)));
                hashMap.put("rci", cursor.getString(cursor.getColumnIndex(rci)));
                hashMap.put("singleFamily", cursor.getString(cursor.getColumnIndex(singleFamily)));
                hashMap.put("garageAttached", cursor.getString(cursor.getColumnIndex(garageAttached)));
                hashMap.put("typeOfExteriorSiding", cursor.getString(cursor.getColumnIndex(typeOfExteriorSiding)));

                arrayList.add(hashMap);
            } while (cursor.moveToNext());
        }

        getWritableDatabase().delete(tbRiskMacroDes, null, null);

        return arrayList;
    }
}
