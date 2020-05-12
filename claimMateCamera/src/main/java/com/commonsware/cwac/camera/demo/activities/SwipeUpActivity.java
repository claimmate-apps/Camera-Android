package com.commonsware.cwac.camera.demo.activities;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonsware.cwac.camera.demo.adpt.ElevationFirstAdapter;
import com.commonsware.cwac.camera.demo.adpt.FloortAdapter;
import com.commonsware.cwac.camera.demo.adpt.InteriorAdapter;
import com.commonsware.cwac.camera.demo.adpt.RoofFirstAdapter;
import com.commonsware.cwac.camera.demo.adpt.WalltAdapter;
import com.commonsware.cwac.camera.demo.adpt.cellingAdapter;
import com.commonsware.cwac.camera.demo.db.ClaimSqlLiteDbHelper;
import com.commonsware.cwac.camera.demo.model.ElevationRes;
import com.commonsware.cwac.camera.demo.model.FloorItem;
import com.commonsware.cwac.camera.demo.model.CeilingItem;
import com.commonsware.cwac.camera.demo.model.RoofRes;
import com.commonsware.cwac.camera.demo.model.Wallitems;
import com.example.claimmate.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class SwipeUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SwipeUpActivity";
    private Context mContext;
    String myPath;
    Cursor Cur;
    String SELECT_SQL;

//    TextView txtBNm, lbltitle, txtBSubNm;
//    Button btnBcancel;

    ClaimSqlLiteDbHelper claimDbHelper;
    SQLiteDatabase DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_up);
//        init();
        initView();
    }

    private TextView txtTitle;
    private Spinner spinnerMain, spinnerSub;
    private LinearLayout llLWH;
    private EditText edtL, edtW, edtH;

    private RecyclerView recyclerInterior, recyclerRoof, recyclerElevation;
    private Button btnAdd, btnAddInteriorSection, btnSave;

    ArrayList<String> mainArray, interiorArray, roofArray, roomArray;
    ArrayList<String> roofMainArray, roofArray1, roofArray2, roofArray3, roofArray4,
            roofArray5, roofArray6, roofArray7, roofArray8,
            roofArray9, roofArray10, roofArray11;

    ArrayList<String> elevationMainArray, elevationArray1, elevationArray2, elevationArray3, elevationArray4,
            elevationArray5, elevationArray6, elevationArray7, elevationArray8,
            elevationArray9, elevationArray10, elevationArray11, elevationArray12,
            elevationArray13, elevationArray14, elevationArray15, elevationArray16,
            elevationArray17, elevationArray18;

    InteriorAdapter interiorAdapter;
    RoofFirstAdapter roofAdapter;
    ElevationFirstAdapter elevationAdapter;

    ListView lvmenuCeiling;
    ListView lvmenuWall;
    ListView lvmenuFloor;
//    ListView lvmenuAreaType;
//    ListView lvmenuQty;
//    ListView lvmenuDamage;

    ArrayList<CeilingItem> Ceilingitems;
    ArrayList<Wallitems> Wallitems;
    ArrayList<FloorItem> Flooritems;
//    ArrayList<AreaTypeItem> nameitemsAreaType;
//    ArrayList<QtyItem> nameitemsQty;
//    ArrayList<DamageItem> nameitemsDamage;

    cellingAdapter adapterCeiling;
    WalltAdapter adapterWall;
    FloortAdapter adapterFloor;

//    AreaTypeAdapter adapterAreaType;
//    QtyAdapter adapterQty;
//    DamageAdapter adapterDamage;

    private void initView() {
        mContext = SwipeUpActivity.this;
        ButterKnife.bind(this);

//        txtTitle = findViewById(R.id.txtTitle);
//
//        spinnerMain = findViewById(R.id.spinnerMain);
//        spinnerSub = findViewById(R.id.spinnerSub);
//
//        llLWH = findViewById(R.id.llLWH);
//        edtL = findViewById(R.id.edtL);
//        edtW = findViewById(R.id.edtW);
//        edtH = findViewById(R.id.edtH);

//        recyclerRoof = findViewById(R.id.recyclerRoof);
//        recyclerElevation = findViewById(R.id.recyclerElevation);
//        recyclerInterior = findViewById(R.id.recyclerInterior);
//
//        btnAdd = findViewById(R.id.btnAdd);
//        btnAddInteriorSection = findViewById(R.id.btnAddInteriorSection);
//        btnSave = findViewById(R.id.btnSave);
//
//        txtTitle.setOnClickListener(this);
//        btnAdd.setOnClickListener(this);

        //  initArray();
        // setAdpts();
        //  setListener();

        lvmenuCeiling = (ListView) findViewById(R.id.lvmenuCeiling);
        lvmenuWall = (ListView) findViewById(R.id.lvmenuWall);
        lvmenuFloor = (ListView) findViewById(R.id.lvmenuFloor);
//        lvmenuAreaType = (ListView) findViewById(R.id.lvmenuAreaType);
//        lvmenuQty = (ListView) findViewById(R.id.lvmenuQty);
//        lvmenuDamage = (ListView) findViewById(R.id.lvmenuDamage);

        opendatabase();
        Ceilingitems = new ArrayList<>();
        Wallitems = new ArrayList<>();
        Flooritems = new ArrayList<>();

//        nameitemsAreaType = new ArrayList<>();
//        nameitemsQty = new ArrayList<>();
//        nameitemsDamage = new ArrayList<>();

        SELECT_SQL = "SELECT * FROM TBLCeiling";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                CeilingItem item = new CeilingItem();

                String value = Cur.getString(Cur.getColumnIndex("Material"));
                item.setMaterial(value);
                System.out.println("CeilingItem Material :-" + value);

                String roomvalue = Cur.getString(Cur.getColumnIndex("Room"));
                item.setRoom(roomvalue);
                System.out.println("CeilingItem Room:-" + roomvalue);

                String Insulationvalue = Cur.getString(Cur.getColumnIndex("Insulation"));
                item.setInsulation(Insulationvalue);

                System.out.println("CeilingItem Insulation:-" + Insulationvalue);

                String AreaTypevalue = Cur.getString(Cur.getColumnIndex("AreaType"));
                item.setAreaType(AreaTypevalue);
                System.out.println("CeilingItem AreaType:-" + AreaTypevalue);

                String qtyvalue = Cur.getString(Cur.getColumnIndex("Qty"));
                item.setQty(qtyvalue);
                System.out.println("CeilingItem Qty:-" + qtyvalue);

                String damgevalue = Cur.getString(Cur.getColumnIndex("Damage"));
                item.setDamage(damgevalue);
                System.out.println("CeilingItem Damage:-" + damgevalue);

                Ceilingitems.add(item);

            }
            while (Cur.moveToNext());
        }

        Cur.close();
        DB.close();


        adapterCeiling = new cellingAdapter(this, Ceilingitems);
        lvmenuCeiling.setAdapter(adapterCeiling);
        lvmenuCeiling.invalidate();

        opendatabase();

        SELECT_SQL = "SELECT * FROM TBLWall";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                Wallitems item = new Wallitems();

                String value = Cur.getString(Cur.getColumnIndex("Material"));
                item.setMaterial(value);
                System.out.println("Wallitems Material :-" + value);

                String roomvalue = Cur.getString(Cur.getColumnIndex("Room"));
                item.setRoom(roomvalue);
                System.out.println("Wallitems Room:-" + roomvalue);

                String Insulationvalue = Cur.getString(Cur.getColumnIndex("Insulation"));
                item.setInsulation(Insulationvalue);

                System.out.println("Wallitems Insulation:-" + Insulationvalue);

                String AreaTypevalue = Cur.getString(Cur.getColumnIndex("AreaType"));
                item.setAreaType(AreaTypevalue);
                System.out.println("Wallitems AreaType:-" + AreaTypevalue);

                String qtyvalue = Cur.getString(Cur.getColumnIndex("Qty"));
                item.setQty(qtyvalue);
                System.out.println("Wallitems Qty:-" + qtyvalue);

                String damgevalue = Cur.getString(Cur.getColumnIndex("Damage"));
                item.setDamage(damgevalue);
                System.out.println("Wallitems Damage:-" + damgevalue);

                Wallitems.add(item);
            }
            while (Cur.moveToNext());
        }

        Cur.close();
        DB.close();

        adapterWall = new WalltAdapter(this, Wallitems);
        lvmenuWall.setAdapter(adapterWall);
        lvmenuWall.invalidate();


        opendatabase();

        SELECT_SQL = "SELECT * FROM TBLFloor";
        Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {
                FloorItem item = new FloorItem();

                String value = Cur.getString(Cur.getColumnIndex("Material"));
                item.setMaterial(value);
                System.out.println("Wallitems Material :-" + value);

                String roomvalue = Cur.getString(Cur.getColumnIndex("Room"));
                item.setRoom(roomvalue);
                System.out.println("Wallitems Room:-" + roomvalue);

                String Insulationvalue = Cur.getString(Cur.getColumnIndex("Insulation"));
                item.setInsulation(Insulationvalue);

                System.out.println("Wallitems Insulation:-" + Insulationvalue);

                String AreaTypevalue = Cur.getString(Cur.getColumnIndex("AreaType"));
                item.setAreaType(AreaTypevalue);
                System.out.println("Wallitems AreaType:-" + AreaTypevalue);

                String qtyvalue = Cur.getString(Cur.getColumnIndex("Qty"));
                item.setQty(qtyvalue);
                System.out.println("Wallitems Qty:-" + qtyvalue);

                String damgevalue = Cur.getString(Cur.getColumnIndex("Damage"));
                item.setDamage(damgevalue);
                System.out.println("Wallitems Damage:-" + damgevalue);

                Flooritems.add(item);
            }
            while (Cur.moveToNext());
        }

        Cur.close();
        DB.close();


        adapterFloor = new FloortAdapter(this, Flooritems);
        lvmenuFloor.setAdapter(adapterFloor);
        lvmenuFloor.invalidate();

//        adapterAreaType = new AreaTypeAdapter(this, nameitemsAreaType);
////        lvmenuAreaType.setAdapter(adapterAreaType);
////        lvmenuAreaType.invalidate();
//
//        adapterQty = new QtyAdapter(this, nameitemsQty);
////        lvmenuQty.setAdapter(adapterQty);
////        lvmenuQty.invalidate();
//
//        adapterDamage = new DamageAdapter(this, nameitemsDamage);
////        lvmenuDamage.setAdapter(adapterDamage);
////        lvmenuDamage.invalidate();
//
////        SELECT_SQL = "SELECT * FROM TbInterior";
////        Cur = DB.rawQuery(SELECT_SQL, null);
////        if (Cur != null && Cur.getCount() > 0) {
////            Cur.moveToFirst();
////            do {
////
////                String strname = Cur.getString(Cur.getColumnIndex("Insulation"));
////                String strRoom = Cur.getString(Cur.getColumnIndex("Room"));
////                String strAreaType = Cur.getString(Cur.getColumnIndex("AreaType"));
////                String strQty = Cur.getString(Cur.getColumnIndex("Qty"));
////                String strDamage = Cur.getString(Cur.getColumnIndex("Damage"));
////                String strMaterial = Cur.getString(Cur.getColumnIndex("Material"));
////                // popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);
////                System.out.println("Insulation SwipeUpActivity new:-" + strname);
////                System.out.println("Room SwipeUpActivity new:-" + strRoom);
////                System.out.println("AreaType SwipeUpActivity new:-" + strAreaType);
////                System.out.println("Qty  SwipeUpActivity new:-" + strQty);
////                System.out.println("Damage SwipeUpActivity new:-" + strDamage);
////                System.out.println("Material SwipeUpActivity new:-" + strMaterial);
////                //value++;
////            }
////            while (Cur.moveToNext());
////        }
////        Cur.close();
////        DB.close();

    }

    private void setAdpts() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, mainArray);
        spinnerMain.setAdapter(adapter);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, interiorArray);
        spinnerSub.setAdapter(adapter1);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        recyclerInterior.setLayoutManager(mLayoutManager);

        interiorAdapter = new InteriorAdapter(mContext);
        recyclerInterior.setAdapter(interiorAdapter);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(mContext);
        recyclerRoof.setLayoutManager(mLayoutManager2);

        roofAdapter = new RoofFirstAdapter(mContext);
        recyclerRoof.setAdapter(roofAdapter);

        RecyclerView.LayoutManager mLayoutManager3 = new LinearLayoutManager(mContext);
        recyclerElevation.setLayoutManager(mLayoutManager3);

        elevationAdapter = new ElevationFirstAdapter(mContext);
        recyclerElevation.setAdapter(elevationAdapter);


        List<ElevationRes> elevationRes = new ArrayList<>();
        ElevationRes elevationRes1 = new ElevationRes();
        elevationRes1.sub = elevationArray1;
        elevationRes.add(elevationRes1);
        elevationAdapter.addAll(elevationRes);

        List<RoofRes> array = new ArrayList<>();
        RoofRes roofRes = new RoofRes();
        roofRes.title = "TEST SQ";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray1;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "SHINGLES";
        roofRes.isCheckbox = true;
        roofRes.sub = roofArray2;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "CAP";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray3;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "LAYERS";
        roofRes.isCheckbox = true;
        roofRes.sub = roofArray4;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "DRIP";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray5;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "PITCH";
        roofRes.isCheckbox = true;
        roofRes.sub = roofArray6;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "IWS";
        roofRes.isCheckbox = true;
        roofRes.sub = roofArray7;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "VALLEY";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray8;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "PIPE JACKS";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray9;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "VENTS";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray10;
        array.add(roofRes);

        roofRes = new RoofRes();
        roofRes.title = "FLASH";
        roofRes.isCheckbox = false;
        roofRes.sub = roofArray11;
        array.add(roofRes);

        roofAdapter.addAll(array);

        elevationAdapter.setEventListener(new ElevationFirstAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

            }

            @Override
            public void onItemTitleClicked(int position) {
                ElevationRes data = elevationAdapter.getItem(position);
                showDialog(data.sub);
            }
        });

        roofAdapter.setEventListener(new RoofFirstAdapter.EventListener() {
            @Override
            public void onItemViewClicked(int position) {

            }

            @Override
            public void onItemTitleClicked(int position) {
                RoofRes data = roofAdapter.getItem(position);
                showDialog(data.sub);
            }
        });
    }

    boolean isElevation;

    private void setListener() {
        spinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    isElevation = false;
                    recyclerInterior.setVisibility(View.VISIBLE);
                    recyclerRoof.setVisibility(View.GONE);
                    recyclerElevation.setVisibility(View.GONE);
                    llLWH.setVisibility(View.VISIBLE);
                    btnAdd.setVisibility(View.GONE);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, interiorArray);
                    spinnerSub.setAdapter(adapter1);
                }
                if (position == 1) {
                    isElevation = false;
                    recyclerInterior.setVisibility(View.GONE);
                    recyclerRoof.setVisibility(View.VISIBLE);
                    recyclerElevation.setVisibility(View.GONE);
                    llLWH.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray);
                    spinnerSub.setAdapter(adapter1);
                }
                if (position == 2) {
                    isElevation = true;
                    recyclerElevation.setVisibility(View.VISIBLE);
                    recyclerRoof.setVisibility(View.GONE);
                    recyclerInterior.setVisibility(View.GONE);
                    llLWH.setVisibility(View.GONE);
                    btnAdd.setVisibility(View.VISIBLE);

                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray);
                    spinnerSub.setAdapter(adapter1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initArray() {
        mainArray = new ArrayList<>();
        mainArray.add("Interior");
        mainArray.add("Roof");
        mainArray.add("Elevation");

        interiorArray = new ArrayList<>();
        interiorArray.add("Living room");
        interiorArray.add("Hallway");
        interiorArray.add("Kitchen");
        interiorArray.add("Stairs");
        interiorArray.add("Bedroom 1");
        interiorArray.add("Bedroom 2");
        interiorArray.add("Bedroom 3");
        interiorArray.add("Bathroom");
        interiorArray.add("Master Bedroom");
        interiorArray.add("Master Bathroom");
        interiorArray.add("Closet");
        interiorArray.add("Office");
        interiorArray.add("Attic");
        interiorArray.add("Garage");
        interiorArray.add("Custom Text");

        elevationMainArray = new ArrayList<>();
        elevationMainArray.add("Siding");
        elevationMainArray.add("Insulation");
        elevationMainArray.add("Soffit");
        elevationMainArray.add("Fascia");
        elevationMainArray.add("Gutters");
        elevationMainArray.add("DownSpouts");
        elevationMainArray.add("Gutterguards");
        elevationMainArray.add("Shutters");
        elevationMainArray.add("Vents");
        elevationMainArray.add("Windows");
        elevationMainArray.add("Doors");
        elevationMainArray.add("AC Units");
        elevationMainArray.add("Light Fixtures");
        elevationMainArray.add("Decks");
        elevationMainArray.add("Fence");
        elevationMainArray.add("Shed");
        elevationMainArray.add("Mailbox");
        elevationMainArray.add("Paint");
        elevationMainArray.add("Custom Data");

        elevationArray1 = new ArrayList<>();
        elevationArray1.add("Vinyl");
        elevationArray1.add("Vinyl High");
        elevationArray1.add("Vinyl Premium");
        elevationArray1.add("Aluminum .19");
        elevationArray1.add("Aluminum .24");
        elevationArray1.add("Steel");
        elevationArray1.add("Wood T1-11");
        elevationArray1.add("Hardboard 6");
        elevationArray1.add("Hardboard 8");
        elevationArray1.add("Hardboard 12");
        elevationArray1.add("Fiber Cement 6");
        elevationArray1.add("Fiber Cement 8");
        elevationArray1.add("Fiber Cement 12");

        elevationArray2 = new ArrayList<>();
        elevationArray2.add("Foam Board");
        elevationArray2.add("House Wrap");
        elevationArray2.add("Foam + House Wrap");

        elevationArray3 = new ArrayList<>();
        elevationArray3.add("Metal");
        elevationArray3.add("Vinyl");
        elevationArray3.add("Wood");

        elevationArray4 = new ArrayList<>();
        elevationArray4.add("Metal 4");
        elevationArray4.add("Metal 6");
        elevationArray4.add("Metal 8");
        elevationArray4.add("Wood 4");
        elevationArray4.add("Wood 6");
        elevationArray4.add("Wood 8");
        elevationArray4.add("Vinyl 4-6");
        elevationArray4.add("Vinyl 7-10");

        elevationArray5 = new ArrayList<>();
        elevationArray5.add("5");
        elevationArray5.add("6");
        elevationArray5.add("Plastic");

        elevationArray6 = new ArrayList<>();
        elevationArray6.add("2x3");
        elevationArray6.add("3x4");
        elevationArray6.add("Plastic");

        elevationArray7 = new ArrayList<>();
        elevationArray7.add("Average");
        elevationArray7.add("High");
        elevationArray7.add("Premium");

        elevationArray8 = new ArrayList<>();
        elevationArray8.add("12x24");
        elevationArray8.add("15x43");
        elevationArray8.add("15x60");

        elevationArray9 = new ArrayList<>();
        elevationArray9.add("Octagon");
        elevationArray9.add("Rectangle");

        elevationArray10 = new ArrayList<>();
        elevationArray10.add("Reglaze 1-9 sf");
        elevationArray10.add("Reglaze 10-16 sf");
        elevationArray10.add("Reglaze 17-24 sf");

        elevationArray11 = new ArrayList<>();
        elevationArray11.add("Xterior Door");
        elevationArray11.add("Interior door");
        elevationArray11.add("Storm door");
        elevationArray11.add("Lockset");
        elevationArray11.add("Lockset w/ Deadbolt");

        elevationArray12 = new ArrayList<>();
        elevationArray12.add("Comb");
        elevationArray12.add("Cage");
        elevationArray12.add("Inspection");

        elevationArray13 = new ArrayList<>();
        elevationArray13.add("Average");
        elevationArray13.add("High");
        elevationArray13.add("Premium");

        elevationArray14 = new ArrayList<>();
        elevationArray14.add("Pwash");
        elevationArray14.add("Stain");
        elevationArray14.add("Prime");
        elevationArray14.add("Paint");
        elevationArray14.add("Pwash Dk Rail");
        elevationArray14.add("Stain Dk Rail");
        elevationArray14.add("Prime Dk Rail");
        elevationArray14.add("Paint Dk Rail");

        elevationArray15 = new ArrayList<>();
        elevationArray15.add("Privacy");
        elevationArray15.add("Board On Board");
        elevationArray15.add("Chain Link");

        elevationArray16 = new ArrayList<>();
        elevationArray16.add("Metal Gable");
        elevationArray16.add("Metal Barn");
        elevationArray16.add("Vinyl");

        elevationArray17 = new ArrayList<>();
        elevationArray17.add("Average");
        elevationArray17.add("High");
        elevationArray17.add("Premium");

        elevationArray18 = new ArrayList<>();
        elevationArray18.add("Siding");
        elevationArray18.add("Soffit");
        elevationArray18.add("Fascia");
        elevationArray18.add("Gutters");
        elevationArray18.add("DownSpouts");
        elevationArray18.add("Shutters");
        elevationArray18.add("Vents");
        elevationArray18.add("Doors");
        elevationArray18.add("Decks");
        elevationArray18.add("Fence");


        roofArray = new ArrayList<>();
        roofArray.add("Water Damage");
        roofArray.add("Wind Damage");
        roofArray.add("Hail Damage");
        roofArray.add("Mechanical Damage");
        roofArray.add("Smoke Damage");
        roofArray.add("Fire Damage");
        roofArray.add("Collateral Damage");
        roofArray.add("No Damage");
        roofArray.add("No Damage But Included");

        roofMainArray = new ArrayList<>();
        roofMainArray.add("TEST SQ");
        roofMainArray.add("Shingles");
        roofMainArray.add("Cap");
        roofMainArray.add("layers");
        roofMainArray.add("Drip ");
        roofMainArray.add("Pitch");
        roofMainArray.add("IWS");
        roofMainArray.add("Valley");
        roofMainArray.add("Pipe Jacks");
        roofMainArray.add("Vents");
        roofMainArray.add("Flash");

        roofArray1 = new ArrayList<>();
        roofArray1.add("Blank");
        roofArray1.add("25 yr 3 tab");
        roofArray1.add("30 yr lam");
        roofArray1.add("40 yr lam");
        roofArray1.add("50 yr lam");
        roofArray1.add("T-Lock");
        roofArray1.add("Shake");
        roofArray1.add("Metal");

        roofArray2 = new ArrayList<>();
        roofArray2.add("Blank");
        roofArray2.add("25 yr 3 tab");
        roofArray2.add("30 yr lam");
        roofArray2.add("40 yr lam");
        roofArray2.add("50 yr lam");
        roofArray2.add("T-Lock");
        roofArray2.add("Shake");
        roofArray2.add("Metal");

        roofArray3 = new ArrayList<>();
        roofArray3.add("Blank");
        roofArray3.add("Composite");
        roofArray3.add("Comp. w/ Ridge Vent");
        roofArray3.add("High profile");
        roofArray3.add("High Profile W/ Vent");
        roofArray3.add("Alum Ridge vent");

        roofArray4 = new ArrayList<>();
        roofArray4.add("Blank");
        roofArray4.add("1");
        roofArray4.add("2");
        roofArray4.add("3");

        roofArray5 = new ArrayList<>();
        roofArray5.add("Blank");
        roofArray5.add("None");
        roofArray5.add("ALL");
        roofArray5.add("Eave Drip");
        roofArray5.add("Eave Apron");
        roofArray5.add("Eave Apron & Rake Drip");

        roofArray6 = new ArrayList<>();
        roofArray6.add("Blank");
        roofArray6.add("1");
        roofArray6.add("2");
        roofArray6.add("3");
        roofArray6.add("4");
        roofArray6.add("5");
        roofArray6.add("6");
        roofArray6.add("7");
        roofArray6.add("8");
        roofArray6.add("9");
        roofArray6.add("10");
        roofArray6.add("11");
        roofArray6.add("12");

        roofArray7 = new ArrayList<>();
        roofArray7.add("Blank");
        roofArray7.add("Eaves");
        roofArray7.add("Eaves-Code");

        roofArray8 = new ArrayList<>();
        roofArray8.add("Blank");
        roofArray8.add("IWS");
        roofArray8.add("Open Rolled");
        roofArray8.add("Closed");
        roofArray8.add("Closed with Metal");

        roofArray9 = new ArrayList<>();
        roofArray9.add("Blank");
        roofArray9.add("1-3");
        roofArray9.add("4 Lead");
        roofArray9.add("Split");
        roofArray9.add("Flu Cap");
        roofArray9.add("HVAC Cap 5");
        roofArray9.add("HVAC Cap 6");
        roofArray9.add("HVAC Cap 8");

        roofArray10 = new ArrayList<>();
        roofArray10.add("Blank");
        roofArray10.add("Turtle");
        roofArray10.add("Turbine");
        roofArray10.add("Power Vent");
        roofArray10.add("P-Vent Cap Metal");
        roofArray10.add("P-Vent Cap Plastic");

        roofArray11 = new ArrayList<>();
        roofArray11.add("Blank");
        roofArray11.add("Step");
        roofArray11.add("Skylight cladding");
        roofArray11.add("Skylight Flashing");
        roofArray11.add("Chimney Small");
        roofArray11.add("Chimney Med");
        roofArray11.add("Chimney Lrg");
    }

    public void onBack(View view) {
        onBackPressed();
    }


    private void setTitle() {
        int value = 0;
        PopupMenu popupsubcat = new PopupMenu(this, txtTitle);

        opendatabase();

        SELECT_SQL = "SELECT * FROM tblsubcat";
        Cursor Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strname = Cur.getString(Cur.getColumnIndex("name"));
                popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);

                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

//        popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom");

        popupsubcat.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                txtTitle.setText(arg0.getTitle().toString());
                return false;
            }
        });
        popupsubcat.show();
    }

    private void opendatabase() {
        DB = SQLiteDatabase.openDatabase(claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public void onClick(View view) {
//        if (view.getId() == txtTitle.getId()) {
//            setTitle();
//        } else if (view.getId() == btnAdd.getId()) {
//            showDialog(isElevation);
//        }
    }

    Dialog dialog;

    public void showDialog(boolean isElevations) {
        dialog = new Dialog(mContext);
        dialog.setCanceledOnTouchOutside(true);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_add_option);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvDone = dialog.findViewById(R.id.tvDone);
        final Spinner spinnerMain = dialog.findViewById(R.id.spinnerMain);
        final Spinner spinnerSub = dialog.findViewById(R.id.spinnerSub);
        final TextView tvCancel = dialog.findViewById(R.id.tvCancel);

        if (isElevations) {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationMainArray);
            spinnerMain.setAdapter(adapter);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray1);
            spinnerSub.setAdapter(adapter1);

            spinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray1);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 1) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray2);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 2) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray3);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 3) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray4);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 4) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray5);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 5) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray6);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 6) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray7);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 7) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray8);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 8) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray9);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 9) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray10);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 10) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray11);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 11) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray12);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 12) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray13);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 13) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray14);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 14) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray15);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 15) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray16);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 16) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray17);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 17) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, elevationArray18);
                        spinnerSub.setAdapter(adapter);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        } else {

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofMainArray);
            spinnerMain.setAdapter(adapter);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray1);
            spinnerSub.setAdapter(adapter1);

            spinnerMain.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 0) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray1);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 1) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray2);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 2) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray3);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 3) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray4);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 4) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray5);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 5) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray6);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 6) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray7);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 7) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray8);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 8) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray9);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 9) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray10);
                        spinnerSub.setAdapter(adapter);
                    }
                    if (position == 10) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, roofArray11);
                        spinnerSub.setAdapter(adapter);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

        }

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    Dialog dialog2;

    public void showDialog(List<String> array) {
        dialog2 = new Dialog(mContext);
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setCancelable(true);
        dialog2.setContentView(R.layout.dialog_edit_option);
        dialog2.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final TextView tvDone = dialog2.findViewById(R.id.tvDone);
        final TextView tvCancel = dialog2.findViewById(R.id.tvCancel);
        final Spinner spinner = dialog2.findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.support_simple_spinner_dropdown_item, array);
        spinner.setAdapter(adapter);

        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog2.dismiss();
            }
        });

        dialog2.show();
    }

    /*private void init() {
        lbltitle = findViewById(R.id.lbltitle);
        txtBNm = findViewById(R.id.txtBNm);
        txtBSubNm = findViewById(R.id.txtBSubNm);

        btnBcancel = findViewById(R.id.btnBcancel);

        lbltitle.setOnClickListener(this);
        txtBNm.setOnClickListener(this);
        txtBSubNm.setOnClickListener(this);

//        txtBNm.setText(getIntent().getStringExtra("select"));

        btnBcancel.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_out, R.anim.left_out);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==btnBcancel.getId()) {
            onBackPressed();
        } else if (view.getId() == lbltitle.getId()) {
            setTitle();
        } else if (view.getId() == txtBNm.getId()) {
            setREI();
        } else if (view.getId() == txtBSubNm.getId()) {
            setREISubMenu();
        }
    }

    private void setREISubMenu() {
        PopupMenu popupMenu = new PopupMenu(this, txtBSubNm);

        if (txtBNm.getTag().toString().equalsIgnoreCase("1")) {
            opendatabase();
            int value = 0;
            SELECT_SQL = "SELECT * FROM tbl_interior";
            Cursor Cur = DB.rawQuery(SELECT_SQL, null);
            if (Cur != null && Cur.getCount() > 0) {
                Cur.moveToFirst();
                do {
                    String strvalue = Cur.getString(Cur.getColumnIndex("value"));
                    if (strvalue.equalsIgnoreCase("Custom Text"))
                        continue;

                    popupMenu.getMenu().add(Menu.NONE, value, Menu.NONE, strvalue);
                    value++;
                }
                while (Cur.moveToNext());
            }
            Cur.close();
            DB.close();
        } else if (txtBNm.getTag().toString().equalsIgnoreCase("2")) {
            popupMenu.getMenu().add("Blank");
            popupMenu.getMenu().add("Front Elevation");
            popupMenu.getMenu().add("Left Elevation");
            popupMenu.getMenu().add("Right Elevation");
            popupMenu.getMenu().add("Rear Elevation");
        } else {
            popupMenu.getMenu().add("Blank");
            popupMenu.getMenu().add("Front Slop");
            popupMenu.getMenu().add("Left Slop");
            popupMenu.getMenu().add("Right Slop");
            popupMenu.getMenu().add("Rear Slop");
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                txtBSubNm.setText(item.getTitle().toString());
                return false;
            }
        });
        popupMenu.show();
    }

    private void setREI() {
        PopupMenu popupMenu = new PopupMenu(this, txtBNm);
        popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "Interior");
        popupMenu.getMenu().add(Menu.NONE, 2, Menu.NONE, "Elevation");
        popupMenu.getMenu().add(Menu.NONE, 3, Menu.NONE, "Roof");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                txtBNm.setText(item.getTitle().toString());
                txtBNm.setTag(item.getItemId()+"");

                txtBSubNm.setText("Blank");
                return false;
            }
        });
        popupMenu.show();
    }

    String SELECT_SQL;
    private void setTitle() {
        int value = 0;
        PopupMenu popupsubcat = new PopupMenu(this, lbltitle);

        opendatabase();

        SELECT_SQL = "SELECT * FROM tblsubcat";
        Cursor Cur = DB.rawQuery(SELECT_SQL, null);
        if (Cur != null && Cur.getCount() > 0) {
            Cur.moveToFirst();
            do {

                String strname = Cur.getString(Cur.getColumnIndex("name"));
                popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, strname);

                value++;
            }
            while (Cur.moveToNext());
        }
        Cur.close();
        DB.close();

//        popupsubcat.getMenu().add(Menu.NONE, value, Menu.NONE, "Custom");

        popupsubcat.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem arg0) {
                lbltitle.setText(arg0.getTitle().toString());
                return false;
            }
        });
        popupsubcat.show();
    }

    private void opendatabase() {
        DB = SQLiteDatabase.openDatabase(claimDbHelper.claimdb_PATH + claimDbHelper.claimdb_NAME, null, SQLiteDatabase.OPEN_READWRITE);
    }*/
}