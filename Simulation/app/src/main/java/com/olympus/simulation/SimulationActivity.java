package com.olympus.simulation;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SimulationActivity extends AppCompatActivity implements View.OnClickListener {

    Simulation_Manager simulation_manager;
    Tag currentClicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);
        simulation_manager = new Simulation_Manager(0,100,1);
        currentClicked = null;
    }

    public void clickPlusIcon(View view) {
        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        buttonAddDelete.setText("Add");

        if (view.getId() == R.id.plusIconWaiting) {
            TextView headerView =  findViewById(R.id.textInfo_Name);
            headerView.setText("Client");

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

            textData1.setVisibility(View.VISIBLE);
            editData1.setVisibility(View.VISIBLE);
            textData2.setVisibility(View.VISIBLE);
            editData2.setVisibility(View.VISIBLE);
            textData3.setVisibility(View.VISIBLE);
            editData3.setVisibility(View.VISIBLE);

            textData1.setText("Procedure:");
            editData1.setText("");

            textData2.setText("Arrival Time:");
            editData2.setText("");

            textData3.setText("Amount to Add:");
            editData3.setText("");

            textData4.setVisibility(View.INVISIBLE);
            editData4.setVisibility(View.INVISIBLE);
            textData5.setVisibility(View.INVISIBLE);
            editData5.setVisibility(View.INVISIBLE);
            textData6.setVisibility(View.INVISIBLE);
            editData6.setVisibility(View.INVISIBLE);

            findViewById(R.id.buttonEdit).setVisibility(View.INVISIBLE);
            /*
            LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
            ImageView clientImage = new ImageView(getApplicationContext());
            clientImage.setImageResource(R.drawable.client);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(78, 100);
            clientImage.setLayoutParams(layoutParams);
            linearLayoutClients.addView(clientImage);
            */
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

            textData1.setVisibility(View.VISIBLE);
            editData1.setVisibility(View.VISIBLE);
            textData2.setVisibility(View.VISIBLE);
            editData2.setVisibility(View.VISIBLE);

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
    }

    public void onAddDeleteClick(View view){
        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        String buttonName = buttonAddDelete.getText().toString();

        if(buttonName.equals("Add"))
        {
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
            else if (header.equals("Client")){
                //Remove the setting of procedure here once you can set it from edittext
                Procedure procedure = new Procedure("Upper", 10, 100);
                int arrivalTime = 0;
                int amount = 0;
                try {
                    /*
                    EditText editData1 = findViewById(R.id.editData1);
                    Get Procedure from EditText
                    */
                    EditText editData2 = findViewById(R.id.editData2);
                    arrivalTime = Integer.parseInt(editData2.getText().toString());
                    EditText editData3 = findViewById(R.id.editData3);
                    amount = Integer.parseInt(editData3.getText().toString());
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }
                if (arrivalTime < 0 || amount <= 0 || amount >= 101) {
                    Toast.makeText(getApplicationContext(), "Invalid Data Entered!", Toast.LENGTH_LONG).show();
                    return;
                }

                for(int i = 0; i < amount; i++) {
                    Client client = new Client(procedure, arrivalTime);
                    simulation_manager.addClient(client);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);
                    ImageView clientImage = new ImageView(getApplicationContext());
                    clientImage.setImageResource(R.drawable.client);

                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(78, 100);
                    clientImage.setLayoutParams(layoutParams);
                    clientImage.setOnClickListener(this);

                    int index = simulation_manager.getClientNum() - 1;
                    Tag tag = new Tag(index, "Client");

                    clientImage.setTag(tag);

                    //roomImage.setTag(0, new Integer(index));
                    //roomImage.setTag(1,"Procedure Room");

                    linearLayoutClients.addView(clientImage);
                }
            }
        }
        else if(buttonName.equals("Delete")){
            if(currentClicked == null){
                return;
            }
            else
            {
                int index = currentClicked.index;
                String type = currentClicked.type;

                if(type.equals("Procedure Room")){
                    simulation_manager.deleteProcedureRoom(index);

                    LinearLayout linearLayoutRooms = findViewById(R.id.LinearLayoutRooms);

                    View room = linearLayoutRooms.getChildAt(simulation_manager.getProcedureRoomNum());

                    room.setVisibility(View.GONE);
                }
                else if(type.equals("Client")){
                    simulation_manager.deleteClient(index);

                    LinearLayout linearLayoutClients = findViewById(R.id.LinearLayoutClients);

                    View client = linearLayoutClients.getChildAt(simulation_manager.getClientNum());

                    client.setVisibility(View.GONE);
                }

                currentClicked = null;
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


    //TODO: Error occurs if you rapidly click on delete and an element in the procedure room list.
    public void onClick (View view) {

        findViewById(R.id.buttonEdit).setVisibility(View.VISIBLE);

        Button buttonAddDelete = findViewById(R.id.buttonAddDelete);
        buttonAddDelete.setText("Delete");

        Tag tag = (Tag)view.getTag();
        currentClicked = tag;
        int index = tag.index;
        String type = tag.type;

        if (type.equals("Procedure Room")) {
            TextView headerView =  findViewById(R.id.textInfo_Name);
            headerView.setText("Procedure Room");

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
        else if(type.equals("Client")){
            TextView headerView =  findViewById(R.id.textInfo_Name);
            headerView.setText("Client");

            Client client = simulation_manager.getClient(index);

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

            textData1.setText("Procedure:");
            editData1.setText(""+client.getProcedure().getName());

            textData2.setText("Arrival Time:");
            editData2.setText(""+client.getArrivalTime());

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
