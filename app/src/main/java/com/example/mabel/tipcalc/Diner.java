package com.example.mabel.tipcalc;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by mabel on 10/15/2014.
 */
public class Diner {

    public EditText etName;
    public EditText etFirstOrder;
    public Button ibAddOrder;
    public TextView tvName;
    public TextView tvSplitBill;
    public ArrayList<EditText> orderList;
    public double total;

 //create constructor and pass possible args in there

    public Diner(EditText et1, EditText et2, Button ib, TextView tv1,TextView tv2 ) {
        etName = et1;
        etFirstOrder = et2;
        ibAddOrder = ib;
        tvName = tv1;
        tvSplitBill= tv2;

        //order List stuff
        //init orderList inside the Diner constructor class since each orhder list belogns to diner
        orderList = new ArrayList<EditText>();
        orderList.add(etFirstOrder);

        //setting up the total variable <-- keeps track of particular diner's total bill
        total=0.0;
    }

    //dynamically change text dp on text in another element(the edit text elem)
    public void setName()
    {

        tvName.setText(etName.getText().toString());
    }

    //Creating method that adds new orders ot diner's order list
    public void newOrder(EditText newOrder) { //takes EditText as arg and adds it to orderList
        orderList.add(newOrder);
    }


    //Create the total method
    public void updateTotal(EditText toBeUpdated) {
        total = 0.0;
        toBeUpdated.setText("$" + String.format("%,.2f", editTextToDouble(toBeUpdated)));
        for (int i = 0; i < orderList.size(); i++) {
            total += editTextToDouble(orderList.get(i));
        }
    }

    //a "repeat" method: allows updateTotal to dynamic change based on solo Tip change
   // public void updateTotal(double tip) {
   //     updateTotal(etFirstOrder,tip);

   // }

    //remove $ and ,
    public double editTextToDouble(EditText et) {
        double db = 0.0;
        db = Double.parseDouble(et.getText().toString().replace("$", "").replace(",", ""));
        return db;
    }

    //adds tax to indiv owner's bill calc
    public void setTotalText(double tip, double tax) {
        tvSplitBill.setText("$" + String.format("%,.2f", total * (1 + tip) + total * tax));
    }

}
