package com.example.elena.sportshop;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
