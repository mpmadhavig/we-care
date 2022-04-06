package com.project.wecare.screens.newClaimForm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.project.wecare.R;
import com.project.wecare.database.claims.ClaimDatabaseManager;
import com.project.wecare.database.claims.ClaimManager;
import com.project.wecare.models.Claim;
import com.project.wecare.models.Evidence;
import com.project.wecare.screens.viewClaims.ViewClaimsListActivity;

import java.util.Date;

public class Claim4Activity extends AppCompatActivity {

    private StorageReference reference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim4);

        Claim claim = ClaimManager.getInstance().getCurrentClaim();
        claim.setDate(new Date());
        submitClaim(claim);
    }

    public void submitClaim(Claim claim){
        ClaimDatabaseManager.getInstance().addClaim(claim,
                new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Claim4Activity.this, "Successfully submitted the claim.", Toast.LENGTH_LONG).show();
                        claim.setState(1);
                        //Todo : Save the claim in local device

                        ClaimManager.getInstance().setCurrentClaim(null);
                        Intent intent = new Intent(Claim4Activity.this, ViewClaimsListActivity.class);
                        intent.putExtra("regNumber", claim.getOwnVehicleRegNumber());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                },
                new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Claim4Activity.this, "Submission failed. Stored locally ", Toast.LENGTH_LONG).show();
                    }
                });
    }
}