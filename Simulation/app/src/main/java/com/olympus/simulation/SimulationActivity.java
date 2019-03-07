package com.olympus.simulation;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SimulationActivity extends AppCompatActivity implements View.OnClickListener {

    Simulation_Manager simulation_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        simulation_manager = new Simulation_Manager(0,100,1);
    }

    public void clickPlusIcon(View view) {
        if (view.getId() == R.id.plusIconWaiting) {
            LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
            ImageView clientImage = new ImageView(getApplicationContext());
            clientImage.setImageResource(R.drawable.client);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(78, 100);
            clientImage.setLayoutParams(layoutParams);
            linearLayoutClients.addView(clientImage);
            //TODO: add backend client here
        }
        else if (view.getId() == R.id.plusIconProcedure) {
            TextView headerView =  findViewById(R.id.textInfo_Name);
            headerView.setText("Procedure Room");

            TextView textData1 = findViewById(R.id.textData1);
            EditText editData1 = findViewById(R.id.editData1);
            TextView textData2 = findViewById(R.id.textData2);
            EditText editData2 = findViewById(R.id.editData2);
            TextView textData3 = findViewById(R.id.textData3);
            EditText editData3 = findViewById(R.id.editData3);
            TextView textData4 = findViewById(R.id.textData4);
            EditText editData4 = findViewById(R.id.editData4);
            TextView textData5 = findViewById(R.id.textData5);
            EditText editData5 = findViewById(R.id.editData5);
            TextView textData6 = findViewById(R.id.textData6);
            EditText editData6 = findViewById(R.id.editData6);

            textData1.setText("Travel Time:");
            editData1.setText("");

            textData2.setText("Cooldown Time:");
            editData2.setText("");

            textData3.setVisibility(View.INVISIBLE);
            editData3.setVisibility(View.INVISIBLE);
            textData4.setVisibility(View.INVISIBLE);
            editData4.setVisibility(View.INVISIBLE);
            textData5.setVisibility(View.INVISIBLE);
            editData5.setVisibility(View.INVISIBLE);
            textData6.setVisibility(View.INVISIBLE);
            editData6.setVisibility(View.INVISIBLE);

            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);

        }
        else if (view.getId() == R.id.buttonAddDelete) {
            TextView headerView =  findViewById(R.id.textInfo_Name);
            String header = headerView.getText().toString();
            //press add button for procedure room
            if (header.equals("Procedure Room")) {
                int travelTime = 0;
                int cooldownTime = 0;
                try {
                    EditText editData1 = findViewById(R.id.editData1);
                    travelTime = Integer.parseInt(editData1.getText().toString());
                    EditText editData2 = findViewById(R.id.editData2);
                    cooldownTime = Integer.parseInt(editData2.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (cooldownTime <= 0 || travelTime <= 0) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                ProcedureRoom procedureRoom = new ProcedureRoom(travelTime, cooldownTime);
                simulation_manager.addProcedureRoom(procedureRoom);

                LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);
                ImageView roomImage = new ImageView(getApplicationContext());
                roomImage.setImageResource(R.drawable.procedure_room);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
                roomImage.setLayoutParams(layoutParams);
                roomImage.setPadding(10, 0, 0, 0);
                roomImage.setOnClickListener(this);

                int index = simulation_manager.getProcedureRoomNum() - 1;
                Tag tag = new Tag(index, "Procedure Room");

                roomImage.setTag(tag);

                //roomImage.setTag(0, new Integer(index));
                //roomImage.setTag(1,"Procedure Room");

                linearLayoutRooms.addView(roomImage);
            }
        }
    }



    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.addClient) {
        }
        return super.onOptionsItemSelected(item);
    }

    public void onClick (View view) {
        Tag tag = (Tag)view.getTag();
        int index = tag.index;
        String type = tag.type;

        if (type.equals("Procedure Room")) {
            ProcedureRoom room = simulation_manager.getProcedureRoom(index);

            TextView textData1 = findViewById(R.id.textData1);
            EditText editData1 = findViewById(R.id.editData1);
            TextView textData2 = findViewById(R.id.textData2);
            EditText editData2 = findViewById(R.id.editData2);
            TextView textData3 = findViewById(R.id.textData3);
            EditText editData3 = findViewById(R.id.editData3);
            TextView textData4 = findViewById(R.id.textData4);
            EditText editData4 = findViewById(R.id.editData4);
            TextView textData5 = findViewById(R.id.textData5);
            EditText editData5 = findViewById(R.id.editData5);
            TextView textData6 = findViewById(R.id.textData6);
            EditText editData6 = findViewById(R.id.editData6);

            textData1.setText("Travel Time:");
            editData1.setText(""+ room.getTravelTime());

            textData2.setText("Cooldown Time:");
            editData2.setText(""+ room.getCooldownTime());

            textData3.setVisibility(View.INVISIBLE);
            editData3.setVisibility(View.INVISIBLE);
            textData4.setVisibility(View.INVISIBLE);
            editData4.setVisibility(View.INVISIBLE);
            textData5.setVisibility(View.INVISIBLE);
            editData5.setVisibility(View.INVISIBLE);
            textData6.setVisibility(View.INVISIBLE);
            editData6.setVisibility(View.INVISIBLE);
        }
    }
}
