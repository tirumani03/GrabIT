package com.codex.saratchandra.grabit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by SaratChandra on 6/21/2016.
 */
public class FragmentProfile extends Fragment {
    TextView profilename;
    SharedPreferences credcheck;
    UsersDatabaseHandler udb;
    String first,email,phone,education,interests;
    ArrayAdapter<String> profileadapter;
    ImageView dp;
    public FragmentProfile getInstance(int position) {
        FragmentProfile profileFragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putInt("position", position);
        profileFragment.setArguments(args);
        return profileFragment;
    }
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View profileview=inflater.inflate(R.layout.fragment_profile, container, false);
        udb=new UsersDatabaseHandler(getActivity());
         profilename=(TextView)profileview.findViewById(R.id.profilename);
        dp=(ImageView)profileview.findViewById(R.id.dp);
        dp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                     dispatchTakePictureIntent();
            }
        });
        credcheck = getActivity().getSharedPreferences("cred", 0);
        if (credcheck.getString("user","").contentEquals(""))
        {                  profilename.setText("Login");
        }
         else{
            String  userprofilename =credcheck.getString("user","Unknown data");
            User user;
           user= udb.getDetails(userprofilename);
            first=user.getfirst();
            email=user.getemail();
            phone=user.getphone();
            education=user.geteducation();
            interests=user.getinterests();
            profilename.setText(first);
            String[] userdata={first,email,phone,education,interests,"Logout"};
            final ListView profilelist=(ListView)profileview.findViewById(R.id.profilelist);
            profilelist.setAdapter(profileadapter=new ArrayAdapter<String>(getActivity(),
                    android.R.layout.simple_list_item_1, userdata));
            profilelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position)
                    {
                        case 0:
                            AlertDialog.Builder dialogname =new AlertDialog.Builder(getActivity());
                            dialogname.setTitle("Enter new name");
                            final EditText new_name = new EditText(getActivity());
                            dialogname.setView(new_name);
                            dialogname.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!new_name.getText().toString().isEmpty())
                                    udb.updatefirst(first,new_name.getText().toString());
                                }
                            });
                            dialogname.show();
                            break;
                        case 1:
                            AlertDialog.Builder dialogemail =new AlertDialog.Builder(getActivity());
                            dialogemail.setTitle("Enter new email");
                            final EditText new_email = new EditText(getActivity());
                            dialogemail.setView(new_email);
                            dialogemail.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!new_email.getText().toString().isEmpty())
                                    udb.updateemail(email,new_email.getText().toString());
                                }
                            });
                            dialogemail.show();
                            break;
                        case 2:
                            AlertDialog.Builder dialogphone =new AlertDialog.Builder(getActivity());
                            dialogphone.setTitle("Enter new phone number");
                            final EditText new_phone = new EditText(getActivity());
                            dialogphone.setView(new_phone);
                            dialogphone.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!new_phone.getText().toString().isEmpty())
                                    udb.updatephone(phone,new_phone.getText().toString());
                                }
                            });
                            dialogphone.show();
                            break;
                        case 3:
                            AlertDialog.Builder dialogeducation =new AlertDialog.Builder(getActivity());
                            dialogeducation.setTitle("Enter new name");
                            final EditText new_education = new EditText(getActivity());
                            dialogeducation.setView(new_education);
                            dialogeducation.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!new_education.getText().toString().isEmpty())
                                    udb.updateeducation(education,new_education.getText().toString());
                                }
                            });
                            dialogeducation.show();
                            break;
                        case 4:
                            AlertDialog.Builder dialoginterest =new AlertDialog.Builder(getActivity());
                            dialoginterest.setTitle("Enter new name");
                            final EditText new_interest = new EditText(getActivity());
                            dialoginterest.setView(new_interest);
                            dialoginterest.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if(!new_interest.getText().toString().isEmpty())
                                    udb.updateinterest(interests,new_interest.getText().toString());
                                }
                            });
                            dialoginterest.show();
                            break;
                        case 5:
                            AlertDialog.Builder dialog =new AlertDialog.Builder(getActivity());
                            dialog.setTitle("Are you sure");
                            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SharedPreferences.Editor edit = credcheck.edit();
                                    edit.remove("user");
                                    edit.remove("pass");
                                    edit.commit();
                                    Intent go=new Intent(getActivity(),Login.class);
                                    startActivity(go);
                                    getActivity().finish();
                                }
                            });
                            dialog.show();
                            break;

                    }

                }
            });

        }
        return profileview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == getActivity().RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            dp.setImageBitmap(imageBitmap);
        }
    }
}
