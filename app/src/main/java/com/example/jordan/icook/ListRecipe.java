package com.example.jordan.icook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by edske on 11/17/2017.
 */

public class ListRecipe extends AppCompatActivity {
    private static final String TAG = "ListRecipe";
    DatabaseHelper pantryDb;
    DatabaseRecipe myDb;
    FloatingActionButton btnAddRecipe;

    EditText text1;
    int[] RecipeChecks = new int[400]; //create of arry up to 399 if array1 is == 5 show arrray Recipe
    int count = 0; //for Recipe Checks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recipe);
        text1 = findViewById(R.id.myRecipe);
        text1.setEnabled(false);
        myDb = new DatabaseRecipe(this);
        pantryDb = new DatabaseHelper(this);
        btnAddRecipe = findViewById(R.id.btn_AddItemsRL);
        //Creates Listener to Open new Activity, this is the top right button for home PA = pantry activity
        ImageButton ButtonhomeRL = findViewById(R.id.homeButtonRL);
        ButtonhomeRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonhomeRL = new Intent(ListRecipe.this, MainActivity.class);
                startActivity(ButtonhomeRL);
            }
        });
        //Creates Listener to Open new Activity, this is the top left button for recipe
        ImageButton ButtonPantryRL = findViewById(R.id.recipeButtonRL);
        ButtonPantryRL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonPantryRL = new Intent(ListRecipe.this, ListPantry.class);
                startActivity(ButtonPantryRL);
            }
        });
        addRecipe(); //Floating Button to Add another recupe, brings up new activity
        compareRecipesToPantry();
        populateListView();
    }

    //the floating add button will to a different screen that let you add items.
    public void addRecipe(){
        btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PantryAdd = new Intent(ListRecipe.this, RecipeActivity.class);
                startActivity(PantryAdd);
            }
        });
    }

    public void compareRecipesToPantry(){
        Cursor pantryC = pantryDb.getAllRows();  //Cursor creates a copy of the DTB we c;an iterate through
        Cursor recipeC = myDb.getAllData();
        count = 0; //reset count every time;
        for(int reset = 0; reset < 399; reset++)  //Loops through entire array
            RecipeChecks[reset] = 0;              //Resets all checked values to 0, as recipe ingredients may have changed.
        while(recipeC.moveToNext()){              //Goes until end of Recipes
            for (int x = 2; x < 11; x = x + 2) {  //INCREMENTS BY 2 BECAUSE IT IS CHECKING THE THE INGREDIENTS WHICH ARE EVERY OTHER COL
                if (pantryC.moveToFirst())
                    do {
                        if (recipeC.getString(x).equals(pantryC.getString(1)) || recipeC.getString(x).isEmpty()) {
                            RecipeChecks[count]++;                  //incrememnts means it found the ingredient
                            break;
                        }
                    } while (pantryC.moveToNext());
            }
            count++;  //increment to next recipe window
        }
    }

    private void populateListView() {

        Cursor cur = myDb.getAllData();
        //HEADER
                String[] groupField = new String[]{DatabaseRecipe.COL_2};
                //push Header into the listHeader textField
                int[] toGroup = new int[]{R.id.listHeader};
                //Cursor ingredients
                String[] childField = new String[]{DatabaseRecipe.COL_3, DatabaseRecipe.COL_4, DatabaseRecipe.COL_5, DatabaseRecipe.COL_6,
                        DatabaseRecipe.COL_7, DatabaseRecipe.COL_8, DatabaseRecipe.COL_9, DatabaseRecipe.COL_10,
                        DatabaseRecipe.COL_11, DatabaseRecipe.COL_12, DatabaseRecipe.COL_13};
                //push cursor to certain textField
                int[] toChild = new int[]{R.id.ig1, R.id.qu1, R.id.ig2, R.id.qu2,
                        R.id.ig3, R.id.qu3, R.id.ig4, R.id.qu4,
                        R.id.ig5, R.id.qu5, R.id.instruction};
                SimpleCursorTreeAdapter setAdapter = new RecipeExpandableListAdpator(cur, this, R.layout.recipe_group, R.layout.recipe_childrenlist,
                        groupField, toGroup, childField, toChild);
                ExpandableListView expandableListView = findViewById(R.id.lvExp);
                expandableListView.setAdapter(setAdapter);
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
///////////////////////////////////////////
/// Adapter that makes the list expands  //
///////////////////////////////////////////
    public class RecipeExpandableListAdpator extends SimpleCursorTreeAdapter{

        public RecipeExpandableListAdpator(Cursor cursor, Context context, int groupLayout,
                                           int childLayout, String[] groupFrom, int[] groupTo, String[] childrenFrom,
                                           int[] childrenTo) {
            super(context, cursor, groupLayout, groupFrom, groupTo,
                    childLayout, childrenFrom, childrenTo);
        }
        @Override
        protected Cursor getChildrenCursor(Cursor groupCursor) {
            int columnIndex = groupCursor.getColumnIndex(DatabaseRecipe.COL_1);
            return myDb.getAllData();
        }



    }



}
