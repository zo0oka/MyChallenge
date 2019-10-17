package com.zo0okadev.mylibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import static com.zo0okadev.mylibrary.TokenUtils.sendTokenToServer;


public class MyChallenge {


    private static final String TAG = MyChallenge.class.getSimpleName();

    public static void initialize(final Context context) {

        FirebaseApp.initializeApp(context);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = "Registered Token is: " + token;
                        Log.d(TAG, msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();

                        sendTokenToServer(token);
                    }
                });
    }
}
