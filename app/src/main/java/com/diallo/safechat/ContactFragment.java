package com.diallo.safechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;
import static com.diallo.safechat.MainActivity.EMAIL;

public class ContactFragment extends Fragment {

    private ListView listView;
    public static ArrayList<HashMap<String, String>> listContact = new ArrayList<>();

    public static ContactFragment newInstance(){
        ContactFragment fragment = new ContactFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact , container, false);

        listView = (ListView) view.findViewById(R.id.listContact);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String emailContact = ((TextView) view.findViewById(R.id.emailContact)).getText().toString();
                ((FragmentActivity) getActivity()).setToConversationFragment(emailContact);
            }

        });

        ListAdapter adapter = new SimpleAdapter(getActivity(),
                listContact,
                R.layout.contact_row,
                new String[] {"emailContact"},
                new int[] {R.id.emailContact});
        listView.setAdapter(adapter);

        return view;
    }
}
