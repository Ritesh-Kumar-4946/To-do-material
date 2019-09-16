package com.ritesh.innerhourtodo.vasu;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ritesh.innerhourtodo.R;

import java.util.ArrayList;
import java.util.List;

public class Vasu_MainActivity extends AppCompatActivity {


    //initialize
    EditText editTextName, editTextEmail, editTextNumber;

    Button buttonAddUser;
    ListView listViewUsers;


    //a list to store all the Vasu_User from firebase database
    List<Vasu_User> vasuUsers;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vasu_activity_main);

        // method for find ids of views
        findViews();

        // to maintian click listner of views
        initListner();
    }


    private void findViews() {
        //getRefrance for user table
        databaseReference = FirebaseDatabase.getInstance().getReference("vasuUsers");

        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        listViewUsers = (ListView) findViewById(R.id.listViewUsers);
        buttonAddUser = (Button) findViewById(R.id.buttonAddUser);
        //list for store objects of user
        vasuUsers = new ArrayList<>();
    }

    private void initListner() {
        //adding an onclicklistener to button
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addUser()
                //the method is defined below
                //this method is actually performing the write operation
                addUser();
            }
        });

        // list item click listener
        listViewUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Vasu_User Vasu_User = vasuUsers.get(i);
                CallUpdateAndDeleteDialog(Vasu_User.getUserid(), Vasu_User.getUsername(), Vasu_User.getUseremail(), Vasu_User.getUsermobileno());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous Vasu_User list
                vasuUsers.clear();

                //getting all nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting Vasu_User from firebase console
                    Vasu_User Vasu_User = postSnapshot.getValue(Vasu_User.class);
                    //adding Vasu_User to the list
                    vasuUsers.add(Vasu_User);
                }
                //creating Userlist adapter
                Vasu_UserList UserAdapter = new Vasu_UserList(Vasu_MainActivity.this, vasuUsers);
                //attaching adapter to the listview
                listViewUsers.setAdapter(UserAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CallUpdateAndDeleteDialog(final String userid, String username, final String email, String monumber) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.vasu_update_dialog, null);
        dialogBuilder.setView(dialogView);
        //Access Dialog views
        final EditText updateTextname = (EditText) dialogView.findViewById(R.id.updateTextname);
        final EditText updateTextemail = (EditText) dialogView.findViewById(R.id.updateTextemail);
        final EditText updateTextmobileno = (EditText) dialogView.findViewById(R.id.updateTextmobileno);
        updateTextname.setText(username);
        updateTextemail.setText(email);
        updateTextmobileno.setText(monumber);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateUser);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteUser);
        //username for set dialog title
        dialogBuilder.setTitle(username);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        // Click listener for Update data
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = updateTextname.getText().toString().trim();
                String email = updateTextemail.getText().toString().trim();
                String mobilenumber = updateTextmobileno.getText().toString().trim();
                //checking if the value is provided or not Here, you can Add More Validation as you required

                if (!TextUtils.isEmpty(name)) {
                    if (!TextUtils.isEmpty(email)) {
                        if (!TextUtils.isEmpty(mobilenumber)) {
                            //Method for update data
                            updateUser(userid, name, email, mobilenumber);
                            b.dismiss();
                        }
                    }
                }

            }
        });

        // Click listener for Delete data
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Method for delete data
                deleteUser(userid);
                b.dismiss();
            }
        });
    }

    private boolean updateUser(String id, String name, String email, String mobilenumber) {
        //getting the specified Vasu_User reference
        DatabaseReference UpdateReference = FirebaseDatabase.getInstance().getReference("vasuUsers").child(id);
        Vasu_User Vasu_User = new Vasu_User(id, name, email, mobilenumber);
        //update  Vasu_User  to firebase
        UpdateReference.setValue(Vasu_User);
        Toast.makeText(getApplicationContext(), "Vasu_User Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteUser(String id) {
        //getting the specified Vasu_User reference
        DatabaseReference DeleteReference = FirebaseDatabase.getInstance().getReference("vasuUsers").child(id);
        //removing Vasu_User
        DeleteReference.removeValue();
        Toast.makeText(getApplicationContext(), "Vasu_User Deleted", Toast.LENGTH_LONG).show();
        return true;
    }


    private void addUser() {


        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String mobilenumber = editTextNumber.getText().toString().trim();


        //checking if the value is provided or not Here, you can Add More Validation as you required

        if (!TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(email)) {
                if (!TextUtils.isEmpty(mobilenumber)) {

                    //it will create a unique id and we will use it as the Primary Key for our Vasu_User
                    String id = databaseReference.push().getKey();
                    //creating an Vasu_User Object
                    Vasu_User Vasu_User = new Vasu_User(id, name, email, mobilenumber);
                    //Saving the Vasu_User
                    databaseReference.child(id).setValue(Vasu_User);

                    editTextName.setText("");
                    editTextNumber.setText("");
                    editTextEmail.setText("");
                    Toast.makeText(this, "Vasu_User added", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Please enter a mobilenumber", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Please enter a Email", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }
}