package com.commonsware.cwac.camera.demo.activities;

import android.app.Dialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.commonsware.cwac.camera.demo.other.Constants;
import com.example.claimmate.R;

public class addclaimname extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclaimname);

        addClaim();
    }


    private void addClaim()
    {
        final Dialog dialog = new Dialog(addclaimname.this);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.popup_add_claim);

        final EditText edtClaimName = dialog.findViewById(R.id.edtClaimName);
        // final EditText edtShortName = dialog.findViewById(R.id.edtShortName);

        dialog.findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(edtClaimName.getText().toString().trim().equalsIgnoreCase(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Name",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Constants.addclaimname = edtClaimName.getText().toString().trim();
                    finish();
                }

            }
        });
        dialog.show();
    }



}
