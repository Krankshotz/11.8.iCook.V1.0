package com.example.jordan.icook;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorTreeAdapter;

/**
 * Created by edske on 11/17/2017.
 */

public class ListRecipe extends AppCompatActivity {
    private static final String TAG = "ListRecipe";
    private Cursor cur;
    DatabaseHelper pantryDb;
    DatabaseRecipe myDb;
    EditText text1;
    FloatingActionButton btnAdd;
    int[] RecipeChecks = new int[400]; //create of arry up to 399 if array1 is == 5 show arrray Recipe
    int count = 0; //for Recipe Checks
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_recipe);
        text1 = findViewById(R.id.myRecipe);
        text1.setEnabled(false);
        btnAdd = findViewById(R.id.btn_AddRecipeRL);
        myDb = new DatabaseRecipe(this);
        pantryDb = new DatabaseHelper(this);
        //MEssageBox HelpWindow
        Button pantryInfo = findViewById(R.id.infoRecipebuttonRL);
        pantryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder infoDialog = new android.app.AlertDialog.Builder(ListRecipe.this);
                infoDialog.setMessage("To Delete Recipes, press and hold the item you wish to delete.\n\nTo add a recipe click the + icon located " +
                        "at the lower right hand corner.\n\nFor more information on the recipe, tap the name.");
                infoDialog.setCancelable(true);
                infoDialog.setPositiveButton("OK", null);
                infoDialog.setTitle("Adding Recipes");
                infoDialog.show();
            }
        });
        compareRecipesToPantry();
        populateListView();
        MenuButtons();
        addItem();
        ListViewItemClick();
        deleteLongClick();
    }

    public void compareRecipesToPantry(){
        Cursor pantryC = pantryDb.getAllRows();  //Cursor creates a copy of the DTB we can iterate through
        Cursor recipeC = myDb.getAllData();
        count = 0; //reset count every time;
        for(int reset = 0; reset < 399; reset++)  //Loops through entire array
            RecipeChecks[reset] = 0;              //Resets all checked values to 0, as recipe ingredients may have changed.
        while(recipeC.moveToNext()){  //Goes until end of Recipes
            if(pantryC.moveToFirst())
                do {
                    for (int x = 2; x < 11; x = x + 2)
                        if (recipeC.getString(x).equals(pantryC.getString(1)));
                            RecipeChecks[count]++;  //incrememnts means it found the ingredient
                }while(pantryC.moveToNext());
            count++;  //increment to next recipe window
        }
    }

    private void MenuButtons(){
        /*Button pantryInfo = findViewById(R.id.infoPantrybuttonRL);
        pantryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder infoDialog = new android.app.AlertDialog.Builder(RecipeActivity.this);
                infoDialog.setMessage("Enter ingredients using the full name, as the database will match your ingredients you have inputted" +
                        "\n\nSorry at this time you are limited to 5 ingredients");
                infoDialog.setCancelable(true);
                infoDialog.setPositiveButton("OK", null);
                infoDialog.setTitle("Adding Recipes");
                infoDialog.show();
            }
        });*/

        //Creates Listener to Open new Activity, this is the top right button for home PA = pantry activity
        ImageButton ButtonhomeRA = findViewById(R.id.homeButtonRL);
        ButtonhomeRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonhomePA = new Intent(ListRecipe.this, MainActivity.class);
                startActivity(ButtonhomePA);
            }
        });

        //Creates Listener to Open new Activity, this is the top left button for recipe
        ImageButton ButtonrecipeRA = findViewById(R.id.recipeButtonRL);
        ButtonrecipeRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonrecipeRA = new Intent(ListRecipe.this, ListPantry.class);
                startActivity(ButtonrecipeRA);
            }
        });

    }





    private void populateListView() {
        Cursor cursor = myDb.getAllData();

        //Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]
                {DatabaseRecipe.COL_2};
        int[] toViewIDs = new int[]
                {R.id.listHeader};
        // Create Adapter to column of the DB onto element in the UI
        SimpleCursorAdapter myCursorAdaptor = new SimpleCursorAdapter(
                this,     //Context
                R.layout.recipe_group,    //Row Layout template
                cursor,                  //cursor
                fromFieldNames,          // DB col names
                toViewIDs                // View IDs to put data in
        );
        ListView myList = findViewById(R.id.ListViewRecipe);
        myList.setAdapter(myCursorAdaptor);
    }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Clicking on one of the recipe will bring the user to a new activity that displays the ingredients, description//
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////
   public void ListViewItemClick(){
        ListView myList =findViewById(R.id.ListViewRecipe);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                detail(i);
            }
        });
    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void detail(int i){
        cur = myDb.getAllData();
        String id = "";
        String name = "";
        String i1 = "";
        String q1= "";
        String i2 = "";
        String q2 = "";
        String i3 = "";
        String q3 = "";
        String i4 = "";
        String q4 = "";
        String i5 = "";
        String q5 = "";
        String ins = "";
        if (cur!=null && cur.getCount()>0 && cur.moveToFirst()){
            cur.moveToPosition(i);
            id = cur.getString(0);
            name = cur.getString(1);
            i1 = cur.getString(2);
            q1 = cur.getString(3);
            i2 = cur.getString(4);
            q2 = cur.getString(5);
            i3 = cur.getString(6);
            q3 = cur.getString(7);
            i4 = cur.getString(8);
            q4 = cur.getString(9);
            i5 = cur.getString(10);
            q5 = cur.getString(11);
            ins = cur.getString(12);

        }
        Intent iIntent = new Intent(this, RecipePullUp.class);
        iIntent.putExtra("COL_1", id);
        iIntent.putExtra("COL_2", name);
        iIntent.putExtra("COL_3", i1);
        iIntent.putExtra("COL_4", q1);
        iIntent.putExtra("COL_5", i2);
        iIntent.putExtra("COL_6", q2);
        iIntent.putExtra("COL_7", i3);
        iIntent.putExtra("COL_8", q3);
        iIntent.putExtra("COL_9", i4);
        iIntent.putExtra("COL_10", q4);
        iIntent.putExtra("COL_11", i5);
        iIntent.putExtra("COL_12", q5);
        iIntent.putExtra("COL_13", ins);
        setResult(RESULT_OK, iIntent);
        startActivity(iIntent);
    }
//////////////////////////////////////////////////////////
////ADD BUTTON, Will add the recipe to the recipe list////
//////////////////////////////////////////////////////////
    public void addItem(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RecipeAdd = new Intent(ListRecipe.this, RecipeActivity.class);
                startActivity(RecipeAdd);
            }
        });
    }

    private void deleteLongClick(){  //Creates feature, a long hold will delete the item
        ListView myList = findViewById(R.id.ListViewRecipe);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id){
                myDb.deleteRow(id);
                populateListView();
                return false;
            }
        });
    }



}
