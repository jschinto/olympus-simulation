package com.olympus.simulation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ElementActivity extends AppCompatActivity {

    Element element;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_element);

        Intent fromIntent = getIntent();
        element = (Element) fromIntent.getSerializableExtra("element");



        if (element.equals(Element.ELEMENT_CLIENT)) {
            
        } else if (element.equals(Element.ELEMENT_PROCEDURE)) {

        } else if (element.equals(Element.ELEMENT_PROCEDUREROOM)) {

        } else if (element.equals(Element.ELEMENT_SCOPE)) {

        } else if (element.equals(Element.ELEMENT_SCOPETYPE)) {

        }


    }
}
