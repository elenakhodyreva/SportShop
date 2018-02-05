package com.example.elena.sportshop;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements View.OnClickListener {

    private View v;
    private Button btnAdmin, btnShopper;
    private AddClothesFrg addClothesFragment;
    private ShowClothesFrg showClothesFragment;

    private DBHelper dbh;
    private Cursor cursor;

    private String TAG= "";
    String resultOfQuery="";
    String sqlQuery="";

    String id, name, color, catID, brandID, sportID, category, sport, shopName,
            shopAddress, shopPhone, colorID;


    public StartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.start_fragment, container, false);
        setWidgets();
        return  v;
    }

    private void setWidgets() {
        btnAdmin= (Button)v.findViewById(R.id.btnAdmin);
        btnShopper= (Button)v.findViewById(R.id.btnShopper);

        btnAdmin.setOnClickListener(this);
        btnShopper.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // if user is administrator, only admin can add clothes
            case R.id.btnAdmin:

                dbh = new DBHelper(getContext());
                SQLiteDatabase db = dbh.getReadableDatabase();

                sqlQuery = "SELECT * FROM categories UNION SELECT * FROM brands";
                cursor = db.rawQuery(sqlQuery, null);

                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){

                        do {
                            catID= cursor.getString(cursor.getColumnIndex("category_id")).toString();
                            category= cursor.getString(cursor.getColumnIndex("categoryName"));

                            resultOfQuery+= catID+" "+ category+"\n";
                        }
                        while (cursor.moveToNext());


                    }
                }

                TAG= "union";
                Log.d(TAG, resultOfQuery);

                //intersect
                sqlQuery= "SELECT idb FROM brands where idb in" +
                            "(SELECT idc FROM colors)";

                cursor = db.rawQuery(sqlQuery, null);

                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){

                        do {
                            brandID= cursor.getString(cursor.getColumnIndex("idb")).toString();
                            resultOfQuery+= brandID+"\n";
                        }
                        while (cursor.moveToNext());


                    }
                }

                TAG= "intersect";
                Log.d(TAG, resultOfQuery);

                //запрос на разность таблиц:
                sqlQuery= "SELECT idc FROM colors where idc not in" +
                        "(SELECT idb FROM brands)";

                cursor = db.rawQuery(sqlQuery, null);

                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){

                        do {
                            colorID= cursor.getString(cursor.getColumnIndex("idc")).toString();
                            resultOfQuery+= colorID+"\n";
                        }
                        while (cursor.moveToNext());


                    }
                }

                TAG= "разность";
                Log.d(TAG, resultOfQuery);


                //запрос-декартово произведение таблиц:
                sqlQuery= "SELECT * FROM categories, sport";

                cursor = db.rawQuery(sqlQuery, null);

                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){

                        do {
                            catID= cursor.getString(cursor.getColumnIndex("category_id")).toString();
                            category= cursor.getString(cursor.getColumnIndex("categoryName")).toString();
                            sportID= cursor.getString(cursor.getColumnIndex("ids")).toString();
                            sport= cursor.getString(cursor.getColumnIndex("sportName")).toString();

                            resultOfQuery+= catID+" "+ category+" "+ sportID+ " "+ sport +"\n";
                        }
                        while (cursor.moveToNext());


                    }
                }

                TAG= "декартово произведение";
                Log.d(TAG, resultOfQuery);

                //запрос-проекция таблицы
                sqlQuery= "SELECT shopName, shopAddress, shopPhone FROM shops";
                cursor = db.rawQuery(sqlQuery, null);
                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){
                        do {

                            shopName= cursor.getString(cursor.getColumnIndex("shopName")).toString();
                            shopAddress= cursor.getString(cursor.getColumnIndex("shopAddress")).toString();
                            shopPhone= cursor.getString(cursor.getColumnIndex("shopPhone")).toString();

                            resultOfQuery+= shopName+", "+ shopAddress+", "+ shopPhone+"\n";
                        }
                        while (cursor.moveToNext());


                    }
                }

                TAG= "проекция";
                Log.d(TAG, resultOfQuery);


                //запрос-соединение таблиц
                sqlQuery= "SELECT * FROM clothes, colors where clothes.colorId= colors.idc";
                cursor = db.rawQuery(sqlQuery, null);
                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){
                        do {

                            id= cursor.getString(cursor.getColumnIndex("_id")).toString();
                            name= cursor.getString(cursor.getColumnIndex("name")).toString();
                            colorID= cursor.getString(cursor.getColumnIndex("idc")).toString();
                            color= cursor.getString(cursor.getColumnIndex("colorName")).toString();

                            resultOfQuery+= id+" "+ name+" "+ colorID + " "+ color+"\n";
                        }
                        while (cursor.moveToNext());
                    }
                }

                TAG= "соединение таблиц";
                Log.d(TAG, resultOfQuery);


                //having count
                sqlQuery= "SELECT * FROM clothes, colors where clothes.colorId= colors.idc" +
                        " GROUP BY clothes.colorId HAVING COUNT(*)>1";
                cursor = db.rawQuery(sqlQuery, null);
                resultOfQuery="";
                if(cursor!= null)
                {
                    if(cursor.moveToFirst()){
                        do {

                            color= cursor.getString(cursor.getColumnIndex("colorName")).toString();

                            resultOfQuery+=color+"\n";
                        }
                        while (cursor.moveToNext());
                    }
                }

                TAG= "having count";
                Log.d(TAG, resultOfQuery);



                if(addClothesFragment== null)
                    addClothesFragment= new AddClothesFrg();
                getFragmentManager().beginTransaction().replace(R.id.lContainer, addClothesFragment)
                        .commit();

                break;

            // if user is customer, customer can overlook and buy clothes
            case R.id.btnShopper:

                if(showClothesFragment== null)
                    showClothesFragment= new ShowClothesFrg();
                getFragmentManager().beginTransaction().replace(R.id.lContainer,
                        showClothesFragment).commit();

                break;
        }
    }
}
