package com.example.elena.sportshop;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private StartFragment startFragment;

    /** Called when the activity is first created. */
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // go to the start fragment
        if(startFragment== null)
            startFragment= new StartFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.lContainer, startFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        switch (id) {
            case R.id.begin:

                if(startFragment== null)
                    startFragment= new StartFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.lContainer,
                        startFragment).commit();
                break;

            case R.id.exit:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Закрыть приложение?");
                alertDialogBuilder
                        .setMessage("Нажмите да, чтобы выйти")
                        .setCancelable(false)
                        .setPositiveButton("Да",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(1);
                                    }
                                })

                        .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
        }

        return true;
    }
}
