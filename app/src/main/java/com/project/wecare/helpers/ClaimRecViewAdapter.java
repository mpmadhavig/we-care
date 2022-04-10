package com.project.wecare.helpers;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.project.wecare.R;
import com.project.wecare.interfaces.ItemClickListener;
import com.project.wecare.models.Claim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ClaimRecViewAdapter extends RecyclerView.Adapter<ClaimRecViewAdapter.ViewHolder> {

    private ArrayList<Claim> claims = new ArrayList<>();
    private ItemClickListener clickListener;

    public ClaimRecViewAdapter() {}

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.claim_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Date date = claims.get(position).getDate();
        holder.textView.setText(" • " + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(date.getTime()));
        Log.d("Wecare", "Claim state: "+ String.valueOf(claims.get(position).getState() ));
        if (claims.get(position).getState()==0){
            holder.resubmitButton.setVisibility(View.VISIBLE);
        }
        if (claims.get(position).getState()==1){
            holder.resubmitButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setClaims(ArrayList<Claim> claims){
        this.claims = claims;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textView;
        private final FloatingActionButton resubmitButton;
        private final FloatingActionButton viewButton;

        public ViewHolder(@NonNull View view){
            super(view);
            textView = view.findViewById(R.id.claimName);
            resubmitButton = view.findViewById(R.id.btn_resubmit);
            viewButton = view.findViewById(R.id.btn_view);

            resubmitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onButtonClick(view, getAbsoluteAdapterPosition());
                }
            });

            viewButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (clickListener != null) clickListener.onClick(view, getAbsoluteAdapterPosition());
                }
            });
        }

        public boolean isConnected(){
            try{
                String command = "ping -c 1 google.com";
                return Runtime.getRuntime().exec(command).waitFor() == 0;
            }catch(Exception e){
                Log.d("Wecare", e.toString());
                return false;
            }
        }


//        public void submitClaimToDatabase(Claim claim){
//
//            ClaimDatabaseManager.getInstance().addClaim(claim,
//                    new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {// Upload photos to firebase storage and store the remote uri
//                            uploadPhotos(claim, 0);
//                            uploadPhotos(claim, 1);
//                            uploadPhotos(claim, 2);
//                        }
//                    },
//                    new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Claim4Activity.this, "Submission failed. Stored locally ", Toast.LENGTH_LONG).show();
//                        }
//                    });
//
//
//        }
//
//
//        public void uploadPhotos ( Claim claim, int evidenceType){
//            //regNumber/claimId/ownVehicleEvidence
//
//            String regNumber = claim.getOwnVehicleRegNumber();
//            String claimId = claim.getClaimId();
//
//            ArrayList<Evidence> evidences;
//            String evidenceName ;
//
//            if(evidenceType == 0){
//                //own vehicle damage
//                evidences = claim.getOwnVehicleDamageEvidences();
//                evidenceName = "ownVehicleDamageEvidences";
//            }else if(evidenceType == 1){
//                //other vehicle damage
//                evidences  = claim.getOtherVehicleDamageEvidences();
//                evidenceName = "otherVehicleDamageEvidences";
//            }else{
//                //property damage
//                evidences  = claim.getPropertyDamageEvidences();
//                evidenceName = "propertyDamageEvidences";
//            }
//
//
//            for (int i =0 ; i< evidences.size(); i++){
//
//                Evidence e = evidences.get(i);
//                Uri uri = Uri.fromFile(new File(e.getImagePath()));
//
//                StorageReference imageRef = storageReference.child(regNumber).child(claimId).child(evidenceName).child(uri.getLastPathSegment());
//                UploadTask uploadTask = imageRef.putFile(uri);
//                uploadTask.addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("Wecare", "Error : "+ e.toString());
//                        Toast.makeText(Claim4Activity.this, "File Upload Failed", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>(){
//                            @Override
//                            public void onSuccess(Uri uri){
//                                Uri downloadUri=uri;
//                                String remoteUri=downloadUri.toString();
//                                e.setRemoteUri(remoteUri);
//                                Log.d("Wecare","Remote uri : "+remoteUri);
//
//
//                                if(evidenceType==0){
//                                    //If own vehicle damage, increment the count of uploaded images
//                                    claim.incOwnVehicleEvidenceUploadedCount();
//
//                                }else if(evidenceType==1){
//                                    //If other vehicle damage, increment the count of uploaded images
//                                    claim.incOtherVehicleEvidenceUploadedCount();
//                                }else{
//                                    //If property damage, increment the count of uploaded images
//                                    claim.incPropertyEvidenceUploadedCount();
//                                }
//
//                                ClaimDatabaseManager.getInstance().addEvidence(claim.getClaimId(),evidenceName, e,
//                                        new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                if(claim.isAllEvidencesSubmitted()){
//                                                    Toast.makeText(Claim4Activity.this,"All evidence files Uploaded Successfully",Toast.LENGTH_SHORT).show();
//                                                    claim.setState(1);
//
//                                                    //Redirect
//                                                    Intent intent=new Intent(Claim4Activity.this,ViewClaimsListActivity.class);
//                                                    intent.putExtra("regNumber",claim.getOwnVehicleRegNumber());
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                    startActivity(intent);
//                                                }
//                                            }
//                                        },
//                                        new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                            }
//                                        });
//
//                            }
//                        });
//                    }
//                });
//            }
//        }
    }
}
