package com.durmusakman.loginregister;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public  class MyOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent,
                               View view, int pos, long id) {
        Toast.makeText(parent.getContext(), "Item is " +
                parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView parent) {
        // Do nothing.
    }
}

