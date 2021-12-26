package multiverse.androidapp.multiverse.database.webDatabase.webServices;

import android.content.Context;

import java.util.function.Supplier;

import multiverse.androidapp.multiverse.R;
import multiverse.androidapp.multiverse.database.webDatabase.HttpService;
import multiverse.androidapp.multiverse.database.webDatabase.WebServiceResponse;
import multiverse.androidapp.multiverse.model.webModel.notification.NotificationListResponseWebModel;
import multiverse.androidapp.multiverse.model.webModel.util.ListRequestWebModel;

public class NotificationWebService {

    private NotificationWebService() {

    }

    public static WebServiceResponse<NotificationListResponseWebModel> getNotificationList(final ListRequestWebModel request, final Context context) {
        return ApiCaller.callApi(new Supplier<ApiResponse<NotificationListResponseWebModel>>() {
            @Override
            public ApiResponse<NotificationListResponseWebModel> get() {return HttpService.post(context.getString(R.string.network_notification_get), request, NotificationListResponseWebModel.class, context);
            }
        }, context);
    }
}
