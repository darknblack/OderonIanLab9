    package com.oderon.ian;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

    public class MainActivity extends AppCompatActivity {

    DatabaseReference db;

    EditText eName, eAge, eGender;
    TextView tName, tAge, tGender;
    Button bSearch;

    ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseDatabase.getInstance().getReference("student");

        eName = findViewById(R.id.etName);
        eAge = findViewById(R.id.etAge);
        eGender = findViewById(R.id.etGender);

        tName = findViewById(R.id.rName);
        tAge = findViewById(R.id.rAge);
        tGender = findViewById(R.id.rGender);

        bSearch = findViewById(R.id.bSearch);

//        bSearch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast("Hey");
//            }
//        });
    }

    public void search(View v) {
        final String name = beautifyTextField(eName).toLowerCase();

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ss : dataSnapshot.getChildren()) {
                    Person p = ss.getValue(Person.class);
                    String person_name = p.getName().toLowerCase();

                    if(!person_name.equals(name))
                        continue;

                    else {
                        tName.setText(p.getName());
                        tAge.setText(p.getAge());
                        tGender.setText(p.getGender());
                        Toast("Record found...");
                        return;
                    }
                }

                Toast("Record not found...");
                tName.setText("");
                tAge.setText("");
                tGender.setText("");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        };

        db.addValueEventListener(listener);
    }

    public void save(View v) {
        String name, age, gender, key;

        name = beautifyTextField(eName);
        age = beautifyTextField(eAge);
        gender = beautifyTextField(eGender);

        key = db.push().getKey();

        db.child(key).setValue(new Person(name, age, gender));

        Toast("Record added.....");
    }

    protected String beautifyTextField(EditText et) {
        return et.getText().toString().trim();
    }
    protected void Toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
