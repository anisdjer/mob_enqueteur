/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pfe.v4;

import static com.pfe.v4.gcm.CommonUtilities.SENDER_ID;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.pfe.v4.entities.Enqueteur;
import com.pfe.v4.gcm.ServerUtilities;
import com.pfe.v4.utils.Utils;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    private static final String TAG = "GCMIntentService";

    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, getString(R.string.gcm_registered));
        Log.e("GCMIntentService 48", "register id:"+registrationId);
        ServerUtilities.register(context, registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, getString(R.string.gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, ""+Enqueteur.id);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
    	
        Log.i(TAG, "Received message" + System.currentTimeMillis());
        
        String message = intent.getExtras().getString("message");
        
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, message);
        // notifies user
        
        
        generateNotification(context, message);
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        com.pfe.v4.gcm.CommonUtilities.displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @SuppressWarnings("deprecation")
	private static void generateNotification(Context context, String message) {
    	
        int icon = R.drawable.icon_enquete;
        long when = System.currentTimeMillis();
        
        com.pfe.v4.entities.Notification notif = new com.pfe.v4.entities.Notification(message, when);
        Utils.db.saveNotif(notif);
        
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        Notification notification = new Notification(icon, message, when);
        
        String title = context.getString(R.string.app_name);
        
        Intent notificationIntent = new Intent(context, NotificationActivity.class);
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        
        
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        try {
            Uri notification1 = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notification1);
            r.play();
        } catch (Exception e) {}
        
        notificationManager.notify(0, notification);
    }

}
