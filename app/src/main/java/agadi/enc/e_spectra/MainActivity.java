package agadi.enc.e_spectra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecyclerView RView;

    private DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());



        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notices");



        RView = (RecyclerView) findViewById(R.id.view);
        RView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        RView.setLayoutManager(layoutManager);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    protected void onStart(){
        super.onStart();

        final FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<NoticeModel, NoticeViewHolder>(
                NoticeModel.class, R.layout.customview, NoticeViewHolder.class, databaseReference


        ) {
            @Override
            protected void populateViewHolder(NoticeViewHolder viewHolder, NoticeModel model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDescription());
                viewHolder.setImage(getApplicationContext(), model.getImage());
                viewHolder.sekey(model.getPushKey(), model.getIdKey());



            }
        };
        RView.setAdapter(firebaseRecyclerAdapter);

    }


    public static class  NoticeViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public NoticeViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setTitle(String post){


            TextView post_title = (TextView) mView.findViewById(R.id.viewtitle);
            post_title.setText(post);

        }

        public void setDesc(String Desc){
            TextView post_descp = (TextView) mView.findViewById(R.id.viewdecs);
            post_descp.setText(Desc);

        }

        public void setImage(Context cnt,String imagejson){


            ImageView post_image = (ImageView) mView.findViewById(R.id.viewimage);


            Glide.with(cnt).load(imagejson).thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(post_image);





        }
        public void sekey(final String key, final String id){


            ImageButton post = (ImageButton) mView.findViewById(R.id.delete);
            post.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference deletebase = FirebaseDatabase.getInstance().getReference().child("Notices");
                    deletebase.child(key).removeValue();
                    StorageReference referenc = FirebaseStorage.getInstance().getReference().child("Notices/");
                    referenc.child(id+"/").delete();
                }
            });

        }

    }




    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add) {
            startActivity(new Intent(MainActivity.this, UploadAct.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_notice_wall) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        } else if (id == R.id.nav_admin_login) {

        } else if (id == R.id.nav_ques_papers) {

        } else if (id == R.id.nav_about_us) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
