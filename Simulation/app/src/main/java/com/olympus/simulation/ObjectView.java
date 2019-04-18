package com.olympus.simulation;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;


public class ObjectView extends LinearLayout {

    private Object object;
    private String type;

    private ImageView imageView;
    private TextView textView;


    public ObjectView(Object object, Context context) {
        this(object, context,null);
    }

    public ObjectView(Object object, Context context, AttributeSet attrs) {
        this(object, context, attrs,0);
    }

    public ObjectView(Object object, Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.object = object;
        if (object instanceof Client) {
            type = "client";
        } else if (object instanceof Scope) {
            type = "scope";
        } else if (object instanceof  ProcedureRoom) {
            type = "procedureRoom";
        }
        else  {
            type = null;
        }
        init();
    }

    private void init() {
        inflate(getContext(),R.layout.object_view,this);

        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        if (type == null) {
            return;
        }

        if (type.equals("client")) {
            imageView.setImageResource(R.drawable.client);
        } else if (type.equals("scope")) {
            imageView.setImageResource(R.drawable.scope);
        } else if (type.equals("procedureRoom")) {
            imageView.setImageResource(R.drawable.procedure_room);
        }

        update();

    }

    public boolean contains(Object object) {
        if (object == null || this.object == null) {
            return false;
        } else {
            return this.object.equals(object);
        }
    }

    public void update() {
        if (type == null) {
            return;
        }

        if (type.equals("client")) {

            Client client = (Client)object;
            if (client.getState() == State.STATE_WAIT) {
                changeText("WAIT:");
                ArrayList<Procedure> procedureList = client.getProcedureList();
                changeText(procedureList.get(0).getName());
                for (int i=1; i < procedureList.size(); i++) {
                    addLine(procedureList.get(i).getName());
                }
            } else if (client.getState() == State.STATE_TRAVEL) {
                changeText("TRAVEL:" + String.valueOf(client.getProcedureRoom().getTravelTime()));
            } else if (client.getState() == State.STATE_DONE) {
                changeText("DONE");
            } else if (client.getState() == State.STATE_OPERATION) {
                changeText("OPERATION");
            } else {
                changeText("BORGER");
            }
        } else if (type.equals("scope")) {
            Scope scope = (Scope)object;
            if (scope.getState() == State_Scope.STATE_TRAVEL) {

            }
        } else {
            changeText("NO TYPE");
        }
    }

    public void changeText(String text) {
        textView.setText(text);
    }

    public void addLine(String line) {
        String text = textView.getText().toString();
        text+= "\n" + line;
        textView.setText(text);
    }

    public void changeImage(@DrawableRes int drawable) {
        imageView.setImageResource(drawable);
    }


    public void changeOrientation(int orientation) {
        if (orientation == VERTICAL) {
            this.setOrientation(VERTICAL);
            imageView.setPadding(0,0,0,-10);
            textView.setPadding(10,0,0,0);
        } else if (orientation == HORIZONTAL) {
            this.setOrientation(HORIZONTAL);
            imageView.setPadding(0,0,-30,0);
            textView.setPadding(0,20,0,0);
        }
    }
}
