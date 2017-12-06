package com.example.elena.sportshop;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddClothesFrg extends Fragment {

    View  v;

    AddShopFrg addShopFrg;

    private Spinner spnrColors, spnrBrand, spnrCategory, spnrSport, spnrShop;
    private Button btnAdd, btnAddShop;
    private EditText etItem, etCost, etCount;

    private DBHelper dbh;
    private Cursor cursor;
    private SimpleCursorAdapter simpleCursorAdapter;

    private int colorPos=-1;
    private int brandPos=-1;
    private int categoryPos= -1;
    private int sportPos= -1;
    private int shopPos= -1;

    private String[] colorNames = { "", "Красный", "Оранжевый", "Желтый", "Зеленый", "Синий", "Сиреневый",
            "Коричневый", "Серый", "Черный" };
    private String [] brandNames= {"", "adidas", "nike", "reebok", "asics", "saucony", "garmin", "polar"};
    private String [] categoryNames={"", "одежда", "обувь", "аксессуары"};
    private String [] sportNames={"", "бег", "футбол", "фитнес", "гимнастика", "скейтборд"};

    public AddClothesFrg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.add_clothes_fragment, container, false);
        setWidgets();
        return  v;
    }

    private void setWidgets() {
        //spinners
        spnrColors= (Spinner)v.findViewById(R.id.spnrColors);
        spnrBrand= (Spinner)v.findViewById(R.id.spnrBrand);
        spnrCategory= (Spinner)v.findViewById(R.id.spnrCategory);
        spnrSport= (Spinner)v.findViewById(R.id.spnrSport);
        spnrShop= (Spinner)v.findViewById(R.id.spnrShop);

        //buttons
        btnAdd= (Button)v.findViewById(R.id.btnAdd);
        btnAddShop=(Button)v.findViewById(R.id.btnAddShop);

        //editText
        etItem= (EditText)v.findViewById(R.id.etItem);
        etCost= (EditText)v.findViewById(R.id.etCost);
        etCount= (EditText)v.findViewById(R.id.etCount);

        // адаптер colors
        ArrayAdapter<String> adapterColors = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, colorNames);
        adapterColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrColors.setAdapter(adapterColors);

        // adapter Brands
        ArrayAdapter<String> adapterBrands = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, brandNames);
        adapterBrands.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrBrand.setAdapter(adapterBrands);

        //adapter Categories
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrCategory.setAdapter(adapterCategories);

        //adapter Sport
        ArrayAdapter<String> adapterSport = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, sportNames);
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrSport.setAdapter(adapterSport);


        spnrColors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                colorPos= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryPos= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brandPos= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sportPos= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //shop spinner, shops we get from database
        dbh = new DBHelper(getContext());
        SQLiteDatabase db = dbh.getReadableDatabase();

        String sqlQuery = "SELECT shops.shopName, shops.shopAddress, shops.idsps as _id "
                + "FROM shops";
        cursor = db.rawQuery(sqlQuery, null);
        getActivity().startManagingCursor(cursor);

        String from[] = {"shopName", "shopAddress"};
        int to[] = {R.id.tvShopName, R.id.tvShopAddress};

        simpleCursorAdapter = new SimpleCursorAdapter(getContext(), R.layout.shop_spinner_item,
                cursor,
                from, to, 0);

        spnrShop.setAdapter(simpleCursorAdapter);

        spnrShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shopPos= (position+1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                    addItem();
            }
        });

        //if that shop doesn't exist we need to add a new shop, that's why we go to the addShop frg
        btnAddShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addShopFrgOpen();
            }
        });
    }

    private void addItem() {

        if(!etCost.getText().toString().isEmpty() && !etCount.getText().toString().isEmpty()) {
            dbh= new DBHelper(getContext());
            ContentValues cv = new ContentValues();
            SQLiteDatabase db = dbh.getWritableDatabase();

            cv.clear();
            cv.put("name", etItem.getText().toString());
            cv.put("colorId", colorPos);
            cv.put("brandId", brandPos);
            cv.put("categoryId", categoryPos);
            cv.put("sportId", sportPos);
            cv.put("shopId", shopPos);

            cv.put("cost", Integer.parseInt(etCost.getText().toString()));
            cv.put("count", Integer.parseInt(etCount.getText().toString()));

            long rowID= db.insert("clothes", null, cv);
            Toast.makeText(getContext(), "Успешно добавлен товар с id= "+ rowID, Toast.LENGTH_SHORT)
                    .show();
        }
        else
            Toast.makeText(getContext(),"Введите корректные данные!", Toast.LENGTH_SHORT)
                    .show();
    }

    private void addShopFrgOpen() {
        if(addShopFrg== null)
            addShopFrg= new AddShopFrg();
        getFragmentManager().beginTransaction().replace(R.id.lContainer, addShopFrg).commit();
    }
}
