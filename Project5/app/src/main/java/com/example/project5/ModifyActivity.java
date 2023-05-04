package com.example.project5;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModifyActivity extends AppCompatActivity {
    private ExpenseManager myExpenseManager = ExpenseManager.getInstance();
    Spinner typeSpinner;
    private TextView max_textView;
    private EditText amountEditText;
    private Button modifyButton;
    private EditText newTypeEditText;
    private EditText newAmountEditText;
    private Button addTypeButton;
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modify_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        typeSpinner = findViewById(R.id.typeSpinner);
        max_textView = (TextView)findViewById(R.id.max_tv);
        amountEditText = (EditText)findViewById(R.id.amountEditText);
        modifyButton = (Button)findViewById(R.id.modify_button);
        newTypeEditText = (EditText)findViewById(R.id.editTextNewType);
        addTypeButton = (Button) findViewById(R.id.add_newtype_button);
        newAmountEditText = (EditText)findViewById(R.id.editTextNewamount);

        update_adapter();

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                // Update the TextView with the selected item
                for (Type type: myExpenseManager.getExpenseTypes()){
                    if (type.getTypeName().equals(typeSpinner.getSelectedItem().toString())){
                        max_textView.setText("<= "+String.valueOf(type.getMaxVal())+"$");
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // todo: goto back activity from here

                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void modify_pressed(View view){
        try {
            String str_selectedType = typeSpinner.getSelectedItem().toString();
            double selectedMax = Double.parseDouble(String.valueOf(amountEditText.getText()));
            for (Type t: myExpenseManager.getExpenseTypes()){
                if (t.getTypeName().equals(str_selectedType)){
                    t.setMaxVal(selectedMax);
                    max_textView.setText("<= "+String.valueOf(selectedMax)+"$");
                    for (Expense expense: myExpenseManager.getExpenseHashMap().get(t.getTypeName())){
                        expense.getType().setMaxVal(selectedMax);
                    }
                    break;
                }
            }
            Toast toast = Toast.makeText(this, "Actualizado correctamente.", Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e){
            Toast toast = Toast.makeText(this, "Error. No ingreso nada.", Toast.LENGTH_LONG);
            toast.show();
        }
        amountEditText.setText("");

    }
    public void add_pressed(View view){
        try {
            String newType = newTypeEditText.getText().toString();
            double newAmount = Double.parseDouble(newAmountEditText.getText().toString());
            Type mNewType = new Type(newType, newAmount);
            myExpenseManager.addExpenseType(mNewType);
            myExpenseManager.getExpenseHashMap().put(newType, new ArrayList<>());
            update_adapter();
            Toast toast = Toast.makeText(this, "Ingresado correctamente.", Toast.LENGTH_LONG);
            toast.show();
        } catch (Exception e){
            Toast toast = Toast.makeText(this, "Error. No ingreso nada.", Toast.LENGTH_LONG);
            toast.show();
        }
        newTypeEditText.setText("");
        newAmountEditText.setText("");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    public void update_adapter(){
        List<String> expenseTypes = myExpenseManager.getExpenseTypes().stream().map(x->x.getTypeName()).collect(Collectors.toList());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, expenseTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
    }
}
