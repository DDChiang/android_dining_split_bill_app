package com.example.mabel.tipcalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity implements View.OnFocusChangeListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {
    private TableLayout mainTable;
    private Button addDinerButton;
    //button from orig user
    private Button addButton1;
    private EditText firstCustomer;
    private EditText amount1of1;
    private TextView textSplit1;
    private TextView textSplit1Dollar;
    private ArrayList<Diner>dinerList;
    private double tipPercentValue;
    private EditText tipPercent;
    private SeekBar tipSlider;
    private double grandTotal;  //created on the fly to hold values
    private double totalTip;    //created on the fly to hold values
    private TextView textGTotal;
    private TextView tipDollar;
    private double tax;
    private EditText taxDollar;
    private double taxPercentValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get ids
        mainTable = (TableLayout)findViewById(R.id.mainTable);
        firstCustomer = (EditText)findViewById(R.id.firstCustomer);
        amount1of1 = (EditText)findViewById(R.id.amount1of1);
        textSplit1 = (TextView)findViewById(R.id.textSplit1);
        textSplit1Dollar = (TextView) findViewById(R.id.textSplit1Dollar);
        addButton1 = (Button)findViewById(R.id.addButton1);
        addDinerButton = (Button)findViewById(R.id.addDinerButton);
        tipPercentValue = 0.15;
        tipPercent = (EditText)findViewById(R.id.tipPercent);
        tipSlider = (SeekBar)findViewById(R.id.tipSlider);
        grandTotal = 0.0;
        totalTip= 0.0;
        textGTotal = (TextView)findViewById(R.id.textGTotal);
        tipDollar = (TextView)findViewById(R.id.tipDollar);
        tax =0.0;
        taxDollar=(EditText)findViewById(R.id.taxDollar);
        taxPercentValue = 0.0;

        dinerList = new ArrayList<Diner>();
        //create and add our first/orig diner into the list
        Diner diner = new Diner(firstCustomer, amount1of1, addButton1, textSplit1, textSplit1Dollar);
        dinerList.add(diner);

        //set listeners
        addButton1.setOnClickListener(this);
        addDinerButton.setOnClickListener(this);
        firstCustomer.setOnFocusChangeListener(this);
        amount1of1.setOnFocusChangeListener(this);      //add listener to all the edit texts * 1. the first order for orig diner 2. new orders for orig diner   (in add Order code)
                                                    //3. first and subsequent new orders for all other added diners   (in addDiner code -- also in add order code(orig + new diner go 2gether))
        tipSlider.setOnSeekBarChangeListener(this);
        tipPercent.setOnFocusChangeListener(this);
        taxDollar.setOnFocusChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==addDinerButton) {
            TableRow row1 = new TableRow(this);
            EditText et1 = new EditText(this);
            et1.setText("Customer");
            et1.setSelectAllOnFocus(true);
            et1.setOnFocusChangeListener(this);
            et1.setGravity(firstCustomer.getGravity());
            et1.setInputType(firstCustomer.getInputType());
            et1.setLayoutParams(firstCustomer.getLayoutParams());
            et1.setWidth(firstCustomer.getWidth());
            EditText et2 = new EditText(this);
            et2.setText("$0.00");
            et2.setSelectAllOnFocus(true);
            et2.setGravity(amount1of1.getGravity());
            et2.setInputType(amount1of1.getInputType());
            et2.setLayoutParams(amount1of1.getLayoutParams());
            et2.setWidth(amount1of1.getWidth());
            et2.requestFocus();
            //set listener for each new order EditTexts
            et2.setOnFocusChangeListener(this);
            Button ib = new Button(this);
            //set to listen to ib
            ib.setOnClickListener(this);
            //
            row1.addView(et1);
            row1.addView(et2);
            row1.addView(ib);

            //adding "order row indexes" and forcing table to adjust diner list to account for extra space nec
            int rowIndex = 1;
            for (int i = 0; i < dinerList.size(); i++) {
                rowIndex += dinerList.get(i).orderList.size();
            }
            mainTable.addView(row1, rowIndex);

            TableRow row2 = new TableRow(this);
            TextView tv1 = new TextView(this);
            tv1.setText(et1.getText().toString());
            tv1.setGravity(textSplit1.getGravity());
            TextView tv2 = new TextView(this);
            tv2.setText(et2.getText().toString());
            tv2.setGravity(textSplit1Dollar.getGravity());
            row2.addView(tv1);
            row2.addView(tv2);

            //add a "row2" for bill splitting portion
            // and adjust bill splitting rows on bottom
            //and making sure rows get added after accounting for extra space taken up by additional orders
            mainTable.addView(row2, rowIndex + dinerList.size()+11);

            Diner diner = new Diner(et1,et2, ib, tv1, tv2);
            //add diner to continuing ArrayList of diners
            dinerList.add(diner);
            //give et2 a focus whenever user makes new Diner
            et2.requestFocus();
        }

        else {
            for (int i = 0; i < dinerList.size(); i++) {
                if (v == dinerList.get(i).ibAddOrder) {  //check to see if clicked button belongs to specific diner
                    TableRow row3 = new TableRow(MainActivity.this);
                    EditText emptyEditText = new EditText(MainActivity.this);   //dummy to help with spacing: takes up first colunm so newOrder can snuggle to col2
                    emptyEditText.setVisibility(View.INVISIBLE);      // set dummy invisible
                    EditText newOrder = new EditText(MainActivity.this);    //what we really care about. set newOrder's props aftr
                    newOrder.setText("$0.00");
                    newOrder.setSelectAllOnFocus(true);
                    newOrder.setInputType(amount1of1.getInputType());
                    newOrder.setGravity(amount1of1.getGravity());
                    newOrder.setLayoutParams(amount1of1.getLayoutParams());
                    newOrder.setWidth(amount1of1.getWidth());
                    newOrder.requestFocus();
                    newOrder.setOnFocusChangeListener(MainActivity.this);
                    row3.addView(emptyEditText);
                    row3.addView(newOrder);
                    //previous line: mainTable.addView(row3, dinerList.size()+1);
                    int rowIndex2 = 1;
                    for (int j=0; j<=i; j++) {
                        rowIndex2 += dinerList.get(j).orderList.size();
                    }
                    mainTable.addView(row3, rowIndex2);
                    //Add the order to the correct Diner's orderList
                    dinerList.get(i).newOrder(newOrder);

                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v==tipPercent && hasFocus == false) {
            //parse through stuff
            tipPercentValue = Double.parseDouble(((EditText)v).getText().toString().replace("%", ""));
            if (tipPercentValue>50) {
                tipPercentValue = 50.0;
            }
            System.out.println("WE ARE ABOUT TO CHANGE THE TIP");
            tipSlider.setProgress((int) tipPercentValue);
            if (tipPercentValue>0.5) {  //squash bug
                tipPercentValue = tipPercentValue/100;
            }
            System.out.println("WE HAVE CHANGED THE TIP");
        }
        else if (v==taxDollar && hasFocus == false) {
            //grabs value from the EditText v== "taxDollar". parse through. remove weird stuff
            tax = Double.parseDouble(((EditText)v).getText().toString().replace("$","").replace(",",""));
            //format tax number
            ((EditText)v).setText("$" + String.format("%,.2f", tax));
            //recalculate grand total
            calcGrandTotal();

        }
        else {
            for (int i = 0; i < dinerList.size(); i++) {
                if (v == dinerList.get(i).etName && hasFocus == false)
                {   //v is a View that recently lost or gained focus.... also used a get method
                    dinerList.get(i).setName();
                }
                else {
                    for (int j = 0; j < dinerList.get(i).orderList.size(); j++) {
                        if (v == dinerList.get(i).orderList.get(j) && hasFocus == false) {
                            dinerList.get(i).updateTotal((EditText) v);
                            calcGrandTotal();
                        }
                    }
                }
             }
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar ==tipSlider) {
            tipPercentValue = progress;
            tipPercent.setText(String.format("%.0f",tipPercentValue)+"%");
            tipPercentValue=tipPercentValue/100;
            System.out.println("WE HAVE ADJUSTED THE TIP PROPERLY");
            calcGrandTotal();
        }


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    // Not needed.
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    // Not needed.
    }

    //Method to calc Grand Total
    public void calcGrandTotal() {
        grandTotal = 0.0;
        for (int i=0; i<dinerList.size(); i++) {
            grandTotal += dinerList.get(i).total;
        }
        //everytime we call calcGrandTotal, we recalc owner's own bill(including tax) with new meth: "setTotalText"
        taxPercentValue = tax / grandTotal;
         for (int i = 0; i < dinerList.size(); i++) {
        dinerList.get(i).setTotalText(tipPercentValue, taxPercentValue);
         }
        totalTip = grandTotal * tipPercentValue;
        tipDollar.setText("$" + String.format("%,.2f", totalTip));
        textGTotal.setText("$" + String.format("%,.2f", grandTotal + totalTip + tax));
    }



}
