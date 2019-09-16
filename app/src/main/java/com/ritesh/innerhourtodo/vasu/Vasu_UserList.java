package com.ritesh.innerhourtodo.vasu;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ritesh.innerhourtodo.R;

import java.util.List;

public class Vasu_UserList extends ArrayAdapter<Vasu_User> {

    private Activity context;
    //list of users
    List<Vasu_User> vasuUsers;

    public Vasu_UserList(Activity context, List<Vasu_User> vasuUsers) {
        super(context, R.layout.vasu_layout_user_list, vasuUsers);
        this.context = context;
        this.vasuUsers = vasuUsers;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.vasu_layout_user_list, null, true);
        //initialize
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textviewemail = (TextView) listViewItem.findViewById(R.id.textviewemail);
        TextView textviewnumber = (TextView) listViewItem.findViewById(R.id.textviewnumber);

        //getting user at position
        Vasu_User Vasu_User = vasuUsers.get(position);
        //set user name
        textViewName.setText(Vasu_User.getUsername());
        //set user email
        textviewemail.setText(Vasu_User.getUseremail());
        //set user mobilenumber
        textviewnumber.setText(Vasu_User.getUsermobileno());

        return listViewItem;
    }
}