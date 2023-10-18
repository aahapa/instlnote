package com.refinstant.myreflibrary;

import android.app.Activity;
import android.os.RemoteException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

public class Instant_Check {

    public static Activity activity;
    public static Instant_Check myAdZOne;
    static MyInstallCallback myCallbackinstall;

    public interface MyInstallCallback {
        void Succeessfull();

        void UnSucceessfull();
    }

    public Instant_Check(Activity activity1) {
        activity = activity1;
    }

    public static Instant_Check getInstance(Activity activity1) {
        activity = activity1;
        if (myAdZOne == null) {
            myAdZOne = new Instant_Check(activity);
        }
        return myAdZOne;
    }

    public void Check_Internet_Inst(MyInstallCallback myCallbackx, String para1, String para2, String para3, String para4) {
        myCallbackinstall = myCallbackx;

        InstallReferrerClient referrerClient = InstallReferrerClient.newBuilder(activity).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                if (responseCode == InstallReferrerClient.InstallReferrerResponse.OK) {
                    try {
                        ReferrerDetails response = referrerClient.getInstallReferrer();
                        String installReferrer = response.getInstallReferrer();

                        if (installReferrer != null) {
                            if (installReferrer.contains(para1) || installReferrer.contains(para2) || installReferrer.contains(para3) || installReferrer.contains(para4)) {
                                if (myCallbackx != null) {
                                    myCallbackx.Succeessfull();
                                    myCallbackinstall = null;
                                }
                            } else {
                                if (myCallbackx != null) {
                                    myCallbackx.UnSucceessfull();
                                    myCallbackinstall = null;
                                }
                            }
                        } else {
                            if (myCallbackx != null) {
                                myCallbackx.UnSucceessfull();
                                myCallbackinstall = null;
                            }
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (myCallbackx != null) {
                        myCallbackx.UnSucceessfull();
                        myCallbackinstall = null;
                    }
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Handle the case where the service is disconnected
            }
        });

    }
}
