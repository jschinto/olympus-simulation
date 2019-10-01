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
        } else if (object instanceof  Nurse) {
            type = "Nurse";
        } else if (object instanceof Doctor) {
            type = "Doctor";
        } else  {
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
            imageView.setImageResource(R.drawable.client_color);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("scope")) {
            imageView.setImageResource(R.drawable.scope_color);
            LayoutParams params = new LayoutParams(100, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("procedureRoom")) {
            imageView.setImageResource(R.drawable.procedure_room_color);
            LayoutParams params = new LayoutParams(125, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Nurse")) {
            imageView.setImageResource(R.drawable.client_color);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Doctor")) {
            imageView.setImageResource(R.drawable.client_color);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
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

        //client
        if (type.equals("client")) {

            Client client = (Client)object;
            changeText("Patient "+client.getId());
            if (client.getState() == State.STATE_WAIT) {
                ArrayList<Procedure> procedureList = client.getProcedureList();
                for (int i=0; i < procedureList.size(); i++) {
                    addLine(procedureList.get(i).getName());
                }
            } else if (client.getState() == State.STATE_TRAVEL) {
                addLine("Room "+client.getProcedureRoom().getId());
            } else if (client.getState() == State.STATE_DONE) {
                addLine("DONE");
            } else if (client.getState() == State.STATE_OPERATION) {
                addLine("OPERATION");
            }
            else {
                addLine("ERROR");
            }

         //scope
        } else if (type.equals("scope")) {
            Scope scope = (Scope)object;
            changeText("Scope "+scope.getId());
            if (scope.getState() == State_Scope.STATE_TRAVEL) {
                addLine("Room "+scope.getRoom().getId());
            } else if (scope.getState() == State_Scope.STATE_FREE) {
                addLine(scope.getType().getName());
            } else if (scope.getState() == State_Scope.STATE_USE) {
                addLine("USE");
            } else if (scope.getState() == State_Scope.STATE_DIRTY) {
                addLine("DIRTY");
            }
            else {
                addLine("ERROR");
            }

        //procedure room
        } else if (type.equals("procedureRoom")) {
            ProcedureRoom procedureRoom = (ProcedureRoom)object;
            changeText("Room "+procedureRoom.getId());
            if (procedureRoom.isAvailable()) {
                addLine("Available");
            } else if (procedureRoom.isOccupied()) {
                addLine("Patient "+procedureRoom.getClient().getId());
            } else if (!procedureRoom.isOccupied() && !procedureRoom.isAvailable()) {
                addLine("Cleaning");
            }
            else {
                addLine("ERROR");
            }

            ArrayList<Scope> scopes = procedureRoom.getScopeList();
            for (int i=0; i < scopes.size(); i++) {
                addLine(scopes.get(i).getType().getName());
            }
        } else if (type.equals("Nurse")) {
            changeText("Number of Nurses: ");
        } else if (type.equals("Doctor")) {
            changeText("Number of Doctors: ");
    }
        else {
            addLine("TYPE INVALID");
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

    public void changeObject(Object o) {
        this.object = o;
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
        update();
    }


    public void changeOrientation(int orientation) {
        if (orientation == VERTICAL) {
            this.setOrientation(VERTICAL);
            imageView.setPadding(10,0,0,-10);
            textView.setPadding(10,0,0,0);
        } else if (orientation == HORIZONTAL) {
            this.setOrientation(HORIZONTAL);
            imageView.setPadding(-30,0,-30,0);
            textView.setPadding(0,20,30,0);
        }
    }
}
