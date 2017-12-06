package com.example.elena.sportshop;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShowClothesFrg extends Fragment {

    View v;

    private Spinner spnrShowColor, spnrShowBrand, spnrShowCategory, spnrShowSport, spnrShowShop;
    private EditText etCost;
    private Button btnShow;


    private String clr= "";
    private String brnd="";
    private String ctgr="";
    private String sprt= "";
    private String shp="";
    private int cost=0;

    private String[] colorNames = { "", "Красный", "Оранжевый", "Желтый", "Зеленый", "Синий", "Сиреневый",
            "Коричневый", "Серый", "Черный" };
    private String [] brandNames= {"", "adidas", "nike", "reebok", "asics", "saucony", "garmin", "polar"};
    private String [] categoryNames={"", "одежда", "обувь", "аксессуары"};
    private String [] sportNames={"", "бег", "футбол", "фитнес", "гимнастика", "скейтборд"};

    private String[] shopNames ={"", "tandem", "mega", "park house"};

    private ResultFragment resultFragment;

    public ShowClothesFrg() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.show_clothes_fragment, container, false);
        setWidgets();

        return  v;
    }

    private void setWidgets() {

        spnrShowColor= (Spinner)v.findViewById(R.id.spnrShowColor);
        spnrShowBrand= (Spinner)v.findViewById(R.id.spnrShowBrand);
        spnrShowCategory= (Spinner)v.findViewById(R.id.spnrShowCategory);
        spnrShowSport= (Spinner)v.findViewById(R.id.spnrShowSport);
        spnrShowShop= (Spinner)v.findViewById(R.id.spnrShowShop);

        etCost= (EditText)v.findViewById(R.id.etCost);

        // адаптер colors
        ArrayAdapter<String> adapterColors = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, colorNames);
        adapterColors.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrShowColor.setAdapter(adapterColors);

        // adapter Brands
        ArrayAdapter<String> adapterBrands = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, brandNames);
        adapterBrands.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrShowBrand.setAdapter(adapterBrands);

        //adapter Categories
        ArrayAdapter<String> adapterCategories = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, categoryNames);
        adapterCategories.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrShowCategory.setAdapter(adapterCategories);

        //adapter Sport
        ArrayAdapter<String> adapterSport = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, sportNames);
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrShowSport.setAdapter(adapterSport);

        //adapter Shop
        ArrayAdapter<String> adapterShop = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_item, shopNames);
        adapterSport.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnrShowShop.setAdapter(adapterShop);


        spnrShowColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                clr= colorNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrShowBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                brnd= brandNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrShowCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ctgr= categoryNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrShowSport.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sprt= sportNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnrShowShop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                shp= shopNames[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnShow= (Button)v.findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAllItems();
            }
        });

    }

    // here we set necessary parameters for displaying and pass they into the result fragment
    private void showAllItems() {
        if(!etCost.getText().toString().isEmpty()) {
            cost= Integer.parseInt(etCost.getText().toString());
        }
        else
            cost=0;

        Bundle arg= new Bundle();
        arg.putString("color", clr);
        arg.putString("brand", brnd);
        arg.putString("category", ctgr);
        arg.putString("sport", sprt);
        arg.putString("shop", shp);
        arg.putInt("cost", cost);

        resultFragment= new ResultFragment();
        resultFragment.setArguments(arg);

        getFragmentManager().beginTransaction().replace(R.id.lContainer, resultFragment).
                addToBackStack(null).commit();

    }
}
