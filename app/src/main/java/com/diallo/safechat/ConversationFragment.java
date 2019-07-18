package com.diallo.safechat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ConversationFragment extends Fragment {

    private ArrayList<HashMap<String, String>> listMessages = new ArrayList<>();
    private ListView listView;
    private EditText editTextChatbox;
    private SimpleAdapter adapter = null;

    private String emailContactString = null;

    public static ConversationFragment newInstance(){
        ConversationFragment fragment = new ConversationFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_conversation , container, false);

        listView = (ListView) view.findViewById(R.id.listMessage);
        Button sendMessage = (Button) view.findViewById(R.id.sendMessage);
         editTextChatbox = (EditText) view.findViewById(R.id.editTextChatbox);

        getMesages();

        adapter = new SimpleAdapter (getActivity().getBaseContext(), listMessages, R.layout.message_row,
                new String[] {"message", "date", "expediteur"}, new int[] {R.id.messageConversation, R.id.date, R.id.expediteur});
        listView.setAdapter(adapter);

        sendMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String emailContact = getActivity().getIntent().getExtras().getString("emailContact");
                String message = editTextChatbox.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                String date = simpleDateFormat.format(currentTime);

                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                Map<String, Object> mapMessage = new HashMap<>();

                mapMessage.put("emailUser", MainActivity.EMAIL);
                mapMessage.put("emailContact", emailContact);
                mapMessage.put("message", message);
                mapMessage.put("date", date);
                database.collection("Message").add(mapMessage);
                editTextChatbox.setText("");
                ((SimpleAdapter) adapter).notifyDataSetChanged();
            }
        });
        ((SimpleAdapter) adapter).notifyDataSetChanged();
        return view;
    }

    private void getMesages() {
        emailContactString = getActivity().getIntent().getStringExtra("emailContact");
        final FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Message")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        HashMap<String, String> element;

                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.get("emailUser").toString().equals(MainActivity.EMAIL) ||
                                        document.get("emailUser").toString().equals(emailContactString) &&
                                                document.get("emailContact").toString().equals(MainActivity.EMAIL) ||
                                        document.get("emailContact").toString().equals(emailContactString)){
                                    element = new HashMap<>();
                                    element.put("message", document.get("message").toString());
                                    element.put("date", document.get("date").toString());
                                    element.put("expediteur", "De: "+document.get("emailUser").toString());
                                    listMessages.add(element);
                                }
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
