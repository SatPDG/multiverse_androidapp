package multiverse.androidapp.multiverse.model.webModel.notification;

import java.util.List;

public class NotificationListResponseWebModel {

    public List<UserNotificationResponseWebModel> userNotificationList;
    public List<ConversationNotificationResponseWebModel> conversationNotificationList;
    public int count;
    public int offset;
    public int totalSize;

}
