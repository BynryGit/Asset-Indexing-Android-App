package com.aia.services;

import com.aia.db.DatabaseManager;
import com.aia.models.NotificationCard;
import com.aia.utility.CommonUtility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFireBaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);
        NotificationCard notificationCard = new NotificationCard();
        notificationCard.message = remoteMessage.getNotification().getBody();
        notificationCard.title = remoteMessage.getNotification().getTitle();
        notificationCard.date = CommonUtility.getCurrentDate();
        notificationCard.is_read = "false";
        DatabaseManager.saveNotification(this, notificationCard);
    }
}

