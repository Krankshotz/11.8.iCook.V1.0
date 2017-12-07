package com.example.jordan.icook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
//THIS CLASS ALLOWS THE USER TO CREATE RECIPE
public class RecipeActivity extends AppCompatActivity {
    DatabaseRecipe myDb;
    DatabaseHelper myPb;
    EditText editName, editIngredient1, editQuantity1,
            editIngredient2, editQuantity2,
            editIngredient3, editQuantity3,
            editIngredient4, editQuantity4,
            editIngredient5, editQuantity5,
            editInstruction;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recipe);
        myDb = new DatabaseRecipe(this);
        myPb = new DatabaseHelper(this);

        editName = findViewById(R.id.editText_Name);
        editIngredient1 = findViewById(R.id.editText_Ingredient1);
        editQuantity1 = findViewById(R.id.editText_Quantity1);
        editIngredient2 = findViewById(R.id.editText_Ingredient2);
        editQuantity2 = findViewById(R.id.editText_Quantity2);
        editIngredient3 = findViewById(R.id.editText_Ingredient3);
        editQuantity3 = findViewById(R.id.editText_Quantity3);
        editIngredient4 = findViewById(R.id.editText_Ingredient4);
        editQuantity4 = findViewById(R.id.editText_Quantity4);
        editIngredient5 = findViewById(R.id.editText_Ingredient5);
        editQuantity5 = findViewById(R.id.editText_Quantity5);
        editInstruction = findViewById(R.id.editText_Instruction);
        btnAdd = findViewById(R.id.button_Add);
        //whatICanCook();



        //MEssageBox HelpWindow
        Button pantryInfo = findViewById(R.id.infoPantrybuttonRA);
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
        });

        //Creates Listener to Open new Activity, this is the top right button for home PA = pantry activity
        ImageButton ButtonhomeRA = findViewById(R.id.homeButtonRA);
        ButtonhomeRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonhomePA = new Intent(RecipeActivity.this, MainActivity.class);
                startActivity(ButtonhomePA);
            }
        });
        //Creates Listener to Open new Activity, this is the top left button for recipe
        ImageButton ButtonrecipeRA = findViewById(R.id.recipeButtonRA);
        ButtonrecipeRA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ButtonrecipeRA = new Intent(RecipeActivity.this, ListRecipe.class);
                startActivity(ButtonrecipeRA);
            }
        });
        //listener to add more ingredients

        AddData();
        //DeleteData();
    }


    public void AddData(){
        btnAdd.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Will insert data if there is data input in the text fields

                        boolean isInserted = myDb.insertData(
                                editName.getText().toString(),  //add remove all spaces and non alpha chars
                                editIngredient1.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""),
                                editQuantity1.getText().toString(),
                                editIngredient2.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""),
                                editQuantity2.getText().toString(),
                                editIngredient3.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""),
                                editQuantity3.getText().toString(),
                                editIngredient4.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""),
                                editQuantity4.getText().toString(),
                                editIngredient5.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""),
                                editQuantity5.getText().toString(),
                                editInstruction.getText().toString().replaceAll("[^A-Za-z]+", "").toLowerCase().replaceAll("\\s+",""));
                        if (isInserted == true) {
                            Toast.makeText(RecipeActivity.this, "Data Inserted", Toast.LENGTH_LONG).show();
                            // This following block of code sets text field to blank after user inputs data
                            editName.setText("");
                            editIngredient1.setText("");
                            editQuantity1.setText("");
                            editIngredient2.setText("");
                            editQuantity2.setText("");
                            editIngredient3.setText("");
                            editQuantity3.setText("");
                            editIngredient4.setText("");
                            editQuantity4.setText("");
                            editIngredient5.setText("");
                            editQuantity5.setText("");
                            editInstruction.setText("");
                            new AlertDialog.Builder(RecipeActivity.this)
                                    .setTitle("Recipe Added")
                                    .setNegativeButton("Add Another", null)
                                    .setPositiveButton("View Recipes",new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick (DialogInterface dialogInterface, int i){
                                            Intent pantry = new Intent(RecipeActivity.this, ListRecipe.class);
                                            startActivity(pantry);
                                        }
                                    }).create().show();

                        }

                        else {
                            Toast.makeText(RecipeActivity.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );


    }

    /*private void whatICanCook() {
        Button btView = findViewById(R.id.whaticancook);
        btView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent btView = new Intent(RecipeActivity.this, what_i_can_cook.class);
                startActivity(btView);
            }
        });
    }*/


   /* public void DeleteData(){ //NOT IN USE
        btnDelete.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        Integer deletedRows = myDb.deleteData(editName.getText().toString());
                        if(deletedRows > 0){
                            Toast.makeText(RecipeActivity.this, "Data Deleted", Toast.LENGTH_LONG).show();
                            editName.setText("");
                        }else{
                            Toast.makeText(RecipeActivity.this, "Data not Deleted", Toast.LENGTH_LONG).show();
                        }
                    }

                }
        );
    }*/
    public void showMessage (String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

}

