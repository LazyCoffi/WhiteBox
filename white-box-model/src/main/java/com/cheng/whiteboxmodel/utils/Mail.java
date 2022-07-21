package com.cheng.whiteboxmodel.utils;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

public class Mail {

    public static String senderAddress = "gitlaob@163.com";
    public static String senderAccount = "gitlaob";
    public static String senderPassword = "KYHNQMXXBPCQRKLR";

    public static MimeMessage getMimeMessage(Session session, String vcode, String reciverAddress) throws Exception {
        MimeMessage msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(senderAddress));
        msg.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(reciverAddress));
        msg.setSubject("WhiteBox 邮箱验证码", "UTF-8");

        msg.setContent("您的邮箱验证码为:&nbsp;&nbsp;<b>" + vcode + "</b><p>请在5分钟内输入，超时将失效哦！</p><p>-----ps 打死也别把验证码告诉别人哦！</p>",
                "text/html;charset=UTF-8");
        msg.setSentDate(new Date());
        return msg;
    }

    public static void sendMail(String yzm, String reciverAddress) throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.smtp.host", "smtp.163.com");
        Session session = Session.getInstance(props);
        session.setDebug(true);
        Message msg = null;
        try {
            msg = getMimeMessage(session, yzm, reciverAddress);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Transport transport = session.getTransport();
        transport.connect(senderAccount, senderPassword);
        assert msg != null;
        transport.sendMessage(msg, msg.getAllRecipients());
        transport.close();
    }
}