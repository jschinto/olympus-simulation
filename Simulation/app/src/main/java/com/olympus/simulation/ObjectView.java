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
        } else if (object instanceof  ManualCleaningStation) {
            type = "Sink";
        } else if (object instanceof  Technician) {
            type = "Technician";
        } else if (object instanceof Reprocessor) {
            type = "Reprocessor";
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
            imageView.setImageResource(R.drawable.nurse);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Doctor")) {
            imageView.setImageResource(R.drawable.doctor);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Sink")) {
            imageView.setImageResource(R.drawable.sink_color);
            LayoutParams params = new LayoutParams(125, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Technician")) {
            imageView.setImageResource(R.drawable.technician);
            LayoutParams params = new LayoutParams(70, 100);
            imageView.setLayoutParams(params);
        } else if (type.equals("Reprocessor")) {
            imageView.setImageResource(R.drawable.reprocessor);
            LayoutParams params = new LayoutParams(125, 100);
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
            Scope scope = (Scope) object;
            if (scope.getHolding() != null) {
                this.changeImage(R.drawable.technician_scope);
                changeText("Technician " + scope.getHolding().getId());
            } else {
                this.changeImage(R.drawable.scope_color);
                changeText("");
            }
            addLine("Scope " + scope.getId());
            if (scope.getState() == State_Scope.STATE_TRAVEL) {
                addLine("Room " + scope.getRoom().getId());
            } else if (scope.getState() == State_Scope.STATE_FREE) {
                addLine(scope.getType().getName());
            } else if (scope.getState() == State_Scope.STATE_INROOM) {
                addLine("WAITING");
            } else if (scope.getState() == State_Scope.STATE_USE) {
                addLine("USE");
            } else if (scope.getState() == State_Scope.STATE_DIRTY) {
                addLine("DIRTY");
            } else if (scope.getState() == State_Scope.STATE_RETURNING) {
                addLine("RETURNING");
            } else if (scope.getState() == State_Scope.STATE_DONE) {
                addLine("DONE CLEANING");
            } else if (scope.getState() == State_Scope.STATE_TOREPROCESS) {
                addLine("TO REPROCESSOR");
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
            Nurse nurse = (Nurse)object;
            if (nurse.getState() == State.STATE_TRAVEL) {
                changeText("Nurse");
                if (nurse.getDestination() != null) {
                    Element element = nurse.getDestination();
                    if (element instanceof ProcedureRoom) {
                        ProcedureRoom procedureRoom = (ProcedureRoom)element;
                        addLine("Room "+procedureRoom.getId());
                    }
                }
            }
        } else if (type.equals("Doctor")) {
            Doctor doctor = (Doctor)object;
            changeText("Doctor " + doctor.getId());
            if (doctor.getState() == State.STATE_TRAVEL) {
                if (doctor.getDestination() != null) {
                    Element element = doctor.getDestination();
                    if (element instanceof ProcedureRoom) {
                        ProcedureRoom procedureRoom = (ProcedureRoom)element;
                        addLine("Room "+procedureRoom.getId());
                    }
                }
            } else if (doctor.getState() == State.STATE_WAIT) {
                ArrayList<Procedure> procedures = doctor.getProcedures();
                for (int i=0; i < procedures.size(); i++) {
                    addLine(procedures.get(i).getName());
                }
            }
        } else if (type.equals("Sink")) {
            changeOrientation(HORIZONTAL);
            ManualCleaningStation sink = (ManualCleaningStation)object;
            changeText("Sink " + sink.getId());
            if (sink.getCurrentScope () != null) {
                addLine("Scope " + sink.getCurrentScope().getId());
                if (sink.getCurrentScope().getHolding() != null) {
                    addLine("Technician "+sink.getCurrentScope().getHolding().getId());
                }
            }
        } else if (type.equals("Technician")) {
            Technician technician = (Technician)object;
            if (technician.getState() == State.STATE_TRAVEL) {
                changeText("Technician");
                if (technician.getDestination() != null) {
                    Element element = technician.getDestination();
                    if (element instanceof ProcedureRoom) {
                        ProcedureRoom procedureRoom = (ProcedureRoom)element;
                        addLine("Room "+procedureRoom.getId());
                    } else if (element instanceof  ManualCleaningStation) {
                        ManualCleaningStation manualCleaningStation = (ManualCleaningStation)element;
                        addLine("Sink "+manualCleaningStation.getId());
                    } else if (element instanceof Scope) {
                        Scope scope = (Scope)element;
                        addLine("Scope "+scope.getId());
                    }
                }
            }
        } else if (type.equals("Reprocessor")) {
            Reprocessor reprocessor = (Reprocessor)object;
            changeText("Reprocessor " + reprocessor.getId());
            addLine("Scope Count: " + reprocessor.getNumScopes());
            if (reprocessor.getState() == State_Scope.STATE_CLEANING) {
                addLine("CLEANING");
            } else {
                addLine("WAITING");
            }
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
