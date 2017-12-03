package com.example.elena.sportshop;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFrg extends Fragment {

    View v;
    EditText etShopName, etShopAddress, etShopPhone;
    Button btnAddShopInDB;

    DBHelper dbh;

    public AddShopFrg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v= inflater.inflate(R.layout.add_shop_fragment, container, false);

        etShopName= (EditText)v.findViewById(R.id.etShopName);
        etShopAddress= (EditText)v.findViewById(R.id.etShopAddress);
        etShopPhone= (EditText)v.findViewById(R.id.etShopPhone);

        btnAddShopInDB= (Button)v.findViewById(R.id.btnAddShopInDB);

        btnAddShopInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dbh = new DBHelper(getContext());
                SQLiteDatabase db= dbh.getWritableDatabase();

                ContentValues cv = new ContentValues();
                
                cv.clear();
                cv.put("shopName", etShopName.getText().toString());
                cv.put("shopAddress", etShopAddress.getText().toString());
                cv.put("shopPhone", etShopPhone.getText().toString());

                db.insert("shops", null, cv);
            }
        });

        return  v;
    }

}
