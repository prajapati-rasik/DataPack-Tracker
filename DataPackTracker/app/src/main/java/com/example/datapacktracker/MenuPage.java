package com.example.datapacktracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import Emergency_DataBank.emergency_databank;
import towerNavigation.findTower;

public class MenuPage extends AppCompatActivity {

    DrawerLayout drawerLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView drawer = findViewById(R.id.drawer);

        drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer(drawerLayout);
            }
        });

        View databank = findViewById(R.id.databank_menu);
        View towerFinder = findViewById(R.id.find_tower);
        View data_calculation = findViewById(R.id.data_usage);

        databank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPage.this, emergency_databank.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        towerFinder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuPage.this, findTower.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

    }

    private static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }


}