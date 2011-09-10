package org.ohm.lebetter.spring.service;


import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.springframework.mail.MailException;

import java.util.HashMap;
import java.util.Map;

public interface MailManager {

    public static final String CONSULT_RQ = "consult_rq";
    public static final String CONSULT_RS = "consult_rs";

    public static final String EXPERT_RQ = "expert_rq";
    public static final String EXPERT_RS = "expert_rs";

    public static final String ACTIVATE_INVITE = "activate_invite";
    public static final String NEW_USER = "new_user";
    public static final String FRIEND_PROPOSE = "friend_propose";
    public static final String FRIEND_ACCEPT = "friend_accept";
    public static final String CHANGE_PASSWD = "change_passwd";

    public static final String NEW_MESSAGE = "new_message";

    public static final String NEW_ORDER = "new_order";
    public static final String NEW_MANAGER = "new_manager";
    public static final String NEW_ORDER_CUST = "new_order_cust";
    public static final String NEW_ORDER_SUB = "new_order_sub";

    public static final String CHANGE_ORDER = "change_order";
    public static final String CHANGE_ORDER_CUST = "change_order_cust";
    public static final String CHANGE_ORDER_SUB = "change_order_sub";
    public static final String SHOP_APPLICATION = "shop_application";
    public static final String SHOP_APPLICATION_OK = "shop_application_ok";
    public static final String SHOP_APPLICATION_MANAGER = "shop_application_manager";

    public static final String ORDER_WORKFLOW = "order_workflow";
    public static final String YML_IMPORT = "yml_import";

    public static final String MESSAGE = "message";

    public static final String NEWS_SUBSCRIBE = "news_subscribe";
    public static final String NEWS_UNSUBSCRIBE = "news_unsubscribe";
    public static final String NEWS = "news";

    public void sendMailMessage(String[] rcptEmails,
                                String templateKey,
                                Map<String, Object> params)
            throws MailException;

    public void sendMailMessage(UserEntity user,
                                String templateKey,
                                HashMap<String, Object> params)
            throws MailException;
}
