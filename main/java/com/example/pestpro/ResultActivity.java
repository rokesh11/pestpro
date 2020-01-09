package com.example.pestpro;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pestpro.Retrofit.IUploadAPI;
import com.example.pestpro.Retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends AppCompatActivity {

    Toolbar myToolbar;
    LayoutInflater layoutInflater;
    TextView titleBar;

    TextView result;
    //IUploadAPI mService;
    String code,fromCamera=null,name;

    Uri FilePathUri;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    StorageTask uploadTask;
    ProgressDialog progressDialog ;



    private IUploadAPI getAPIUpload(){
        return RetrofitClient.getClient().create(IUploadAPI.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        myToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar5);
        if(myToolbar!=null) {
            setSupportActionBar(myToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowCustomEnabled(true);
            }

            layoutInflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View action_view = layoutInflater.inflate(R.layout.custom_action_bar, null);
            titleBar = action_view.findViewById(R.id.head_bar);
            titleBar.setText(getResources().getString(R.string.result));
            if (actionBar != null) {
                actionBar.setCustomView(action_view);
            }
        }

        result=findViewById(R.id.result);

        if(getIntent().hasExtra("imagePath"))
        {
            FilePathUri=Uri.parse(getIntent().getStringExtra("imagePath"));
        }

        if(getIntent().hasExtra("fromCamera"))
        {
            fromCamera=getIntent().getStringExtra("fromCamera");
        }

        if(getIntent().hasExtra("code"))
        {
            code=getIntent().getStringExtra("code");
        }

        if(getIntent().hasExtra("name"))
        {
            name=getIntent().getStringExtra("name");
        }


        storageReference = FirebaseStorage.getInstance().getReference("Images");
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
        progressDialog = new ProgressDialog(ResultActivity.this);

        if(uploadTask!=null&&uploadTask.isInProgress())
        {
            Toast.makeText(ResultActivity.this, "Upload in Progress", Toast.LENGTH_SHORT).show();
        }else{
            UploadImage();
        }

        result.setText(code);
    }

    public String GetFileExtension(Uri uri) {

        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri)) ;

    }


    public void UploadImage() {

        if (FilePathUri != null) {

            progressDialog.setTitle("Fetching Result...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            StorageReference storageReference2;

            if(!fromCamera.equals("null"))
            {
                FilePathUri=Uri.fromFile(new File(fromCamera));
                storageReference2 = storageReference.child(System.currentTimeMillis() + ".jpg");
            }
            else
            {
                storageReference2 = storageReference.child(System.currentTimeMillis() + ".jpg");
            }

            uploadTask=storageReference2.putFile(FilePathUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String imageUrl = uri.toString();
                                            //createNewPost(imageUrl);
                                            String TempImageName = "Disease: "+code;
                                            String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());


                                            Toast.makeText(getApplicationContext(), "Image Uploaded Successfully ", Toast.LENGTH_LONG).show();
                                            @SuppressWarnings("VisibleForTests")
                                            uploadinfo imageUploadInfo  = new uploadinfo(TempImageName,imageUrl,currentDateTimeString,name);
                                            String ImageUploadId = databaseReference.push().getKey();
                                            databaseReference.child(ImageUploadId).setValue(imageUploadInfo);

                                        }
                                    });
                                }
                            }

                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Image Not Uploaded to server! ", Toast.LENGTH_LONG).show();
                        }
                    });
        }
        else {

            Toast.makeText(ResultActivity.this, "Please Select Image or Add Image Name", Toast.LENGTH_LONG).show();

        }
    }

}
