package agadi.enc.e_spectra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class UploadAct extends AppCompatActivity {

    private ImageButton ImageChose;
    private static final int GALLERY_REQUEST = 1;
    private EditText postname;

    private Button Submit;
    private Uri imageuri = null;
    private StorageReference reference;
    private ProgressDialog progress;
    private DatabaseReference databasereff;
    private String uuid;
    private Spinner spinner;

    private static final String[]paths = {"All","3rd Sem", "4th Sem", "5th Sem", "6th Sem","7th Sem","8th Sem" };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        reference = FirebaseStorage.getInstance().getReference();
        databasereff = FirebaseDatabase.getInstance().getReference().child("Notices");





        ImageChose = (ImageButton) findViewById(R.id.imageButton);
        postname = (EditText) findViewById(R.id.title);
        spinner = (Spinner)findViewById(R.id.sem);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UploadAct.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        Submit = (Button) findViewById(R.id.uploadbtn);
        progress = new ProgressDialog(this);
        ImageChose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPosting();

            }
        });
    }

    private void startPosting() {
        progress.setMessage("Sending Notice...");

        uuid = UUID.randomUUID().toString();

        final String titleval = postname.getText().toString().trim();
        final String descval = spinner.getSelectedItem().toString().trim();
        if (!TextUtils.isEmpty(titleval) || imageuri != null){
            progress.show();


            StorageReference filepath = reference.child("Notices").child(uuid).child(imageuri.getLastPathSegment());
            filepath.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Uri downloadurl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newpost = databasereff.push();

                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = sdf.format(c.getTime());

                    newpost.child("title").setValue(titleval);
                    newpost.child("Description").setValue(descval);
                    newpost.child("Image").setValue(downloadurl.toString());
                    newpost.child("Time").setValue(String.valueOf(strDate.toString()));
                    String key = newpost.getKey();

                    newpost.child("PushKey").setValue(key);
                    newpost.child("IdKey").setValue(uuid);



                    progress.dismiss();
                    startActivity(new Intent(UploadAct.this, MainActivity.class));

                }
            });





        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            imageuri = data.getData();
            ImageChose.setImageURI(imageuri);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UploadAct.this, MainActivity.class));

    }


}
