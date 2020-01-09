package com.example.pestpro;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pestpro.Retrofit.IUploadAPI;
import com.example.pestpro.Retrofit.RetrofitClient;
import com.example.pestpro.Utils.Common;
import com.example.pestpro.Utils.IUploadCallbacks;
import com.example.pestpro.Utils.ProgressRequestBody;
import com.squareup.picasso.Picasso;

import java.io.File;

import me.biubiubiu.justifytext.library.JustifyTextView;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.security.AccessController.getContext;

public class UploadActivity extends AppCompatActivity implements IUploadCallbacks {

    private static final int PICK_FILE_REQUEST=1000;

    IUploadAPI mService;
    Button btnUpload,chooseBtn;
    ImageView imageView;
    Uri selectedFileUri;
    TextView textView;
    EditText name;

    Toolbar myToolbar;
    LayoutInflater layoutInflater;
    TextView titleBar;

    ProgressDialog dialog;

    Bitmap b;
    String fromCamera="null";

    String editName;

    private IUploadAPI getAPIUpload(){
        return RetrofitClient.getClient().create(IUploadAPI.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        myToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbar2);
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
            titleBar.setText(getResources().getString(R.string.app_name));
            if (actionBar != null) {
                actionBar.setCustomView(action_view);
            }
        }

        imageView=findViewById(R.id.preview);
        textView=findViewById(R.id.upload_head);
        name=findViewById(R.id.editName);
        btnUpload=findViewById(R.id.upload_btn);
        chooseBtn=findViewById(R.id.choose_btn);

        fromCamera="null";
        //byteArray=null;
      /*  if(getIntent().hasExtra("image"))
        {
            b= BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"),0,getIntent().getByteArrayExtra("image").length);
            imageView.setImageBitmap(b);

        }
*/
        if(getIntent().hasExtra("imagePath"))
        {
            fromCamera=getIntent().getStringExtra("imagePath");
            b=BitmapFactory.decodeFile(fromCamera);
        }


        //create mService
        mService=getAPIUpload();

        if(fromCamera.equals("null"))
        {
            chooseFile();
        }
        else{
            textView.setText(getResources().getString(R.string.upload_head2));
            //imageView.setClickable(false);
            //add the info from camera
            b=BitmapFactory.decodeFile(fromCamera);
            imageView.setImageBitmap(b);
            //Toast.makeText(UploadActivity.this, "ByteArray recieved", Toast.LENGTH_SHORT).show();
            selectedFileUri= Uri.parse(Uri.decode(fromCamera));
        }

        chooseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });


        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editName=name.getText().toString();
                if(editName.compareTo("")==0)
                {
                    editName="Default Name";
                }
                uploadFile();
            }
        });

    }

    private void uploadFile() {
        if(selectedFileUri!=null) {
            dialog = new ProgressDialog(UploadActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setMessage("Scanning......");
            dialog.setIndeterminate(false);
            dialog.setMax(100);
            dialog.setCancelable(false);
            dialog.show();

            File file = null;
            try {
                if(fromCamera.equals("null"))
                {
                    file = new File(Common.getFilePath(UploadActivity.this,selectedFileUri));
                }else{
                    file=new File(fromCamera);
                    //File(fromcamera)
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            if (file != null) {
                final ProgressRequestBody requestBody = new ProgressRequestBody(file, UploadActivity.this);

                final MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        mService.uploadFile(body)
                                .enqueue(new Callback<String>() {
                                    @Override
                                    public void onResponse(Call<String> call, Response<String> response) {
                                        dialog.dismiss();

                                        if(!response.isSuccessful())
                                        {
                                            Toast.makeText(UploadActivity.this, "Response not successful! Retry!!", Toast.LENGTH_SHORT).show();
                                        }

                                        //String image_processed_link = new StringBuilder("http://192.168.0.103:5000/" +
                                         //       response.body().replace("\"", "")).toString();

                                        //Picasso.get().load(image_processed_link)
                                         //       .into(imageView);

                                        String result=response.body();

                                        //Toast.makeText(UploadActivity.this, "Detected!!", Toast.LENGTH_SHORT).show();

                                        Intent start=new Intent(UploadActivity.this,ResultActivity.class);
                                        start.putExtra("imagePath",selectedFileUri.toString());
                                        start.putExtra("code",result);
                                        start.putExtra("name",editName);
                                        start.putExtra("fromCamera",fromCamera);
                                        startActivity(start);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<String> call, Throwable t) {
                                        dialog.dismiss();
                                        Toast.makeText(UploadActivity.this, "Server" + t.getMessage(), Toast.LENGTH_SHORT).show();
                                        Toast.makeText(UploadActivity.this, "", Toast.LENGTH_SHORT).show();


                                        Intent start=new Intent(UploadActivity.this,MainActivity.class);
                                        startActivity(start);
                                        finish();

                                    }
                                });
                    }
                }).start();

            }
        }else{
            Toast.makeText(UploadActivity.this, "Cannot upload this file!", Toast.LENGTH_SHORT).show();

        }
    }


    private void chooseFile() {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_FILE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode== Activity.RESULT_OK)
        {
            if(requestCode==PICK_FILE_REQUEST)
            {
                if(data!=null)
                {
                    selectedFileUri=data.getData();
                    if(selectedFileUri!=null && !selectedFileUri.getPath().isEmpty()) {
                        imageView.setImageURI(selectedFileUri);
                        textView.setText(getResources().getString(R.string.upload3));
                    }else{
                        Toast.makeText(UploadActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(UploadActivity.this, "Error in request", Toast.LENGTH_LONG).show();
                }
            }
            /*else{
                if(selectedFileUri!=null && !selectedFileUri.getPath().isEmpty()) {
                    imageView.setImageURI(selectedFileUri);
                    textView.setText("Selected Image. To change again click on the image.\n");
                }else{
                    Toast.makeText(UploadActivity.this, "File not found", Toast.LENGTH_SHORT).show();
                }
            }*/
        }else if(resultCode==RESULT_CANCELED){
            Toast.makeText(UploadActivity.this, "Cancelled result", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this,MainActivity.class));
            finish();
        }

    }


    @Override
    public void onProgressUpdate(int percent) {
        dialog.setProgress(percent);
    }

    @Override
    public void onFinish() {
        dialog.setProgress(100);
    }

}
