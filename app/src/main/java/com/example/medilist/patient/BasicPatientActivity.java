package com.example.medilist.patient;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medilist.LoginActivity;
import com.example.medilist.R;
import com.example.medilist.doctor.AboutUsActivity;
import com.example.medilist.doctor.BasicActivity;
import com.example.medilist.doctor.DocterUser;
import com.example.medilist.doctor.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BasicPatientActivity extends AppCompatActivity {
   RecyclerView recyclerView;
   ListrxAdapter adapter;
   List<ListDrRx> listDrRxes;
   FirebaseAuth auth;
   DatabaseReference refRx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_patient);
        getSupportActionBar().setTitle("Patient Dashboard");
        auth = FirebaseAuth.getInstance();
        refRx = FirebaseDatabase.getInstance().getReference().child("Patient").child(auth.getCurrentUser().getUid()).child("Rx");
        setcomp();
    }

    private void setcomp() {

        recyclerView=findViewById(R.id.rvrxlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        listDrRxes = new ArrayList<>();
        loadRx();
    }

    private void loadRx() {
        refRx.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ss : dataSnapshot.getChildren()) {
                        String drname =ss.getValue(ListDrRx.class).getDrName();
                        String day = ss.getValue(ListDrRx.class).getDay();
                        String month = ss.getValue(ListDrRx.class).getMonth();
                        String year = ss.getValue(ListDrRx.class).getYear();
                        String uri = ss.getValue(ListDrRx.class).getRxURI();
                        listDrRxes.add(new ListDrRx(drname,day,month,year,uri));
                    }
                    adapter = new ListrxAdapter(BasicPatientActivity.this,listDrRxes);
                    recyclerView.setAdapter(adapter);
                }
                else {
                    Toast.makeText(BasicPatientActivity.this, "Data not exist", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.profile){
            startActivity(new Intent(BasicPatientActivity.this, ProfilePatientActivity.class));
        }else  if(item.getItemId()==R.id.aboutus){
            startActivity(new Intent(BasicPatientActivity.this, AboutUsPatientActivity.class));
        }else  if(item.getItemId()==R.id.signout){
            auth.signOut();
            finish();
            startActivity(new Intent(BasicPatientActivity.this, LoginActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent a= new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
