package com.anmol.customerapp.Services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by anmol on 2017-08-12.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("tokens").child(auth.getCurrentUser().getUid());
        reference.child("token").setValue(refreshToken);
    }
}
