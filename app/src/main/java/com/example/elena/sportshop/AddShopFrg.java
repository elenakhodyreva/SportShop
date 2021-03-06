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
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddShopFrg extends Fragment {

    private View v;
    private EditText etShopName, etShopAddress, etShopPhone;
    private Button btnAddShopInDB;

    private DBHelper dbh;

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

        // here just adding a new shop into the database, table shops
        btnAddShopInDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh = new DBHelper(getContext());
                SQLiteDatabase db= dbh.getWritableDatabase();

                if(!etShopName.getText().toString().isEmpty()
                        && !etShopAddress.getText().toString().isEmpty()
                        && !etShopPhone.getText().toString().isEmpty())
                {
                    ContentValues cv = new ContentValues();

                    cv.clear();


                    cv.put("shopName", etShopName.getText().toString());
                    cv.put("shopAddress", etShopAddress.getText().toString());
                    cv.put("shopPhone", etShopPhone.getText().toString());

                    db.insert("shops", null, cv);
                }
                else
                    Toast.makeText(getContext(),"Введите корректные данные!", Toast.LENGTH_SHORT)
                            .show();

            }
        });

        return  v;
    }

}
