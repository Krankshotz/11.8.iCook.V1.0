package com.example.jordan.icook;

/**
 * Created by edske on 10/29/2017.
 */

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Henry on 10/21/2017.
 */

public class ListPantry extends AppCompatActivity {
    private static final String TAG = "ListPantry";
    DatabaseHelper myDb;
    EditText text1;
    EditText text2;
    EditText editQuantity;  //copied from pantryActivity to edit quanity of
    int items = 400; //for pantryCompare function, easy to manipulate the size of pantry array.

    FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_pantry);
        text1 = findViewById(R.id.food_Header);
        text2 = findViewById(R.id.quantity_header);
        text1.setEnabled(false);
        text2.setEnabled(false);
        editQuantity = findViewById(R.id.editText_Quantity);  //copied from pantryActivity
        myDb = new DatabaseHelper(this);
        ImageButton ButtonrecipePL = findViewById(R.id.recipeButtonPL);
        btnAdd = findViewById(R.id.btn_AddItems);
        Button ApprovedFoodList = findViewById(R.id.Food_List);
        ImageButton ButtonhomePL = findViewById(R.id.homeButtonPL);
        Button pantryInfo = findViewById(R.id.infoPantrybutton4);

        pantryInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                android.app.AlertDialog.Builder infoDialog = new android.app.AlertDialog.Builder(ListPantry.this);
                infoDialog.setMessage("To delete Pantry items: Tap and hold the item you would like to delete.");
                infoDialog.setCancelable(true);
                infoDialog.setPositiveButton("OK", null);
                infoDialog.setTitle("Pantry Loadouts");
                infoDialog.show();
            }
        });

        //Creates Listener to Open new Activity, this is the top right button for home PA = pantry activity
        ButtonhomePL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonhomePL = new Intent(ListPantry.this, MainActivity.class);
                startActivity(ButtonhomePL);
            }
        });
        //Creates Listener to Open new Activity, this is the top left button for recipe
        ButtonrecipePL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonrecipePL = new Intent(ListPantry.this, ListRecipe.class);
                startActivity(ButtonrecipePL);
            }
        });
        //creates listener to open Approved Food List center bottom of pantry list
        ApprovedFoodList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ApprovedFoodList = new Intent(ListPantry.this, ApprovedFoodListActivity.class);
                startActivity(ApprovedFoodList);
            }
        });
/////////////////////
/// TEST CASE////////
/////////////////////
      /*  if(MainActivity.test == 1) {
            myDb.insertData("eggs", 2);
            MainActivity.test++;
        }*/
//////////////////////////////////////////////////////////////////////////////////////////////////

        addItem();
        pantryCompare();
        populateListViewFromDB();

        listViewItemLongClick();

    }

    private void populateListViewFromDB(){
        Cursor cursor = myDb.getAllRows();

        //Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]
                {DatabaseHelper.COL_1, DatabaseHelper.COL_2};
        int[] toViewIDs = new int[]
                {R.id.textViewItem, R.id.textViewItemQuantity};
        // Create Adapter to column of the DB onto element in the UI
        SimpleCursorAdapter myCursorAdaptor = new SimpleCursorAdapter(
                this,     //Context
                R.layout.item_layout,    //Row Layout template
                cursor,                  //cursor
                fromFieldNames,          // DB col names
                toViewIDs                // View IDs to put data in
        );
        ListView myList = findViewById(R.id.listViewPantry);
        myList.setAdapter(myCursorAdaptor);
    }
 //the floating add button will to a different screen that let you add items.
    public void addItem(){
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent PantryAdd = new Intent(ListPantry.this, PantryActivity.class);
                startActivity(PantryAdd);
            }
        });
    }

    private void listViewItemLongClick(){  //Creates feature, a long hold will delete the item
        ListView myList = findViewById(R.id.listViewPantry);
        myList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long id){
                myDb.deleteRow(id);
                populateListViewFromDB();
                return false;
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



    //Creates Pantry Comparisons, if items have == Names, merge the quantity
    private void pantryCompare() {
        int x = 0;          //iteration uses for array
        int tempQty = 0;    //holds temp pantry quantity and adds to previious pantry arrays if strings of = values are found
        String pantryTemp;        //holds temp pantry item
        String arrayStr[] = new String[items];  //Holds all Pantry Items temporarily and then reprints.
        int arrayInt[] = new int[items];        //temp pantry Qty item
        for(int reset = 0; reset < items; reset++) //resets, may not necessary
        {
            arrayInt[reset] = 0;
            arrayStr[reset] = "";
        }

        Cursor cursor = myDb.getAllRows();  //creates a placement to iterate through all pantry items
        if(cursor.moveToFirst())            //test to make sure data is present
            do {
                //Toast.makeText(getBaseContext(), "Test2: ",Toast.LENGTH_SHORT).show();
                pantryTemp = cursor.getString(1);     //gets string Pantry item
                tempQty = cursor.getInt(2);     //gets Pantry Qty
                int flag = 0;                     //flag flips to 1 in the for loop if the value grabbed is a duplicate, therefore doesnt incremenet the array
                for(int xx = 0; xx < x; xx++) {
                    if (pantryTemp.contentEquals(arrayStr[xx])) {
                        //Toast.makeText(getBaseContext(), "Test3: ", Toast.LENGTH_SHORT).show();
                        //  arrayStr[x] = temp;
                        arrayInt[xx] = tempQty + arrayInt[xx];  //add Qtys together
                        flag = 1;                               //Flag, dont create another array because the item was a duplicate
                    }
                }
                if (flag == 0) {
                    //Toast.makeText(getBaseContext(), "Test2: ",Toast.LENGTH_SHORT).show();
                    arrayStr[x] = pantryTemp;      //Insert Pantry item
                    arrayInt[x] = tempQty;  //insert
                    x++;                    //Move to next array element
                }
                flag = 0;
            }while(cursor.moveToNext());

        myDb.deleteAll();    //Deletes all data from DB
        for(int tester = 0; tester <= x; tester++)
        {
            if(arrayInt[tester] == 0);  //Some values are set to 0 with no real data, so we don't want to print that.
            else {
                //Toast.makeText(getBaseContext(), "Test1: ", Toast.LENGTH_SHORT).show();
                myDb.insertData(arrayStr[tester], arrayInt[tester]);  //Re-inserts all data to DB pantry.
            }
        }
        cursor.close();
    }
    //--------------End of Pantry Comparison-------------------------------

    //FOR GESTURES, So Swipes Open New Activites. Shortcuts :)
   /* @Override
    public boolean onTouchEvent(MotionEvent event){
        this.gestureObject.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    //For Gesture Object Class
    class LearnGesture extends GestureDetector.SimpleOnGestureListener{
        //Simple Gesture Listener for what we want to do, this opens the pantry
        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event2.getX() > event1.getX()){
                //Here is the code for what you want the swipe to do for Left to Right
                Intent openPantry = new Intent(ListPantry.this, MainActivity.class);
                finish(); //Ends current activities Actions
                startActivity(openPantry);
            }
            else
            if(event2.getX() < event1.getX()){
                //Here is the code for what you want the swipe to do for Right to Left
                Intent openPantry = new Intent(ListPantry.this, ListRecipe.class);
                finish(); //Ends current activities Actions
                startActivity(openPantry);
            }
            return true;
        }

    }*/
}