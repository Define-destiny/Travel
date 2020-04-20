package it.home.travel.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.internet.MimeBodyPart;

public class Mail
{
    private String from; // 发件人地址
    private String password; // 发件人密码
    // 收件人，以及收件人接收方式的映射
    private Map<String, RecipientType> recipientsMap = new HashMap<String, RecipientType>();
    private String subject; // 邮件主题
    private List<File> attachments = new ArrayList<File>(); // 附件列表
    private String body; // 邮件正文
    private RecipientType type = RecipientType.TO; // 默认的邮件接收类型

    public Mail() {}

    /**
     * Construct a Mail using the specified email address and password.
     * @param from the sender's address eg. hello@163.com
     * @param password the password.
     */
    public Mail(String from, String password)
    {
        this.from = from;
        this.password = password;
    }

    /**
     * Construct a Mail.
     * @param from the sender's address eg. hello@163.com
     * @param password  the password.
     * @param recipients an array of recipients.
     * @param type the {@link RecipientType}
     * @param subject the subject.
     * @param body the body.
     * @param attachments a list of attachments.
     */
    public Mail(String from, String password, String[] recipients, RecipientType type, String subject, String body, File...attachments)
    {
        this(from, password);

        // 添加接收者，及其接收方式
        for (String recipient : recipients)
        {
            if (!this.recipientsMap.containsKey(recipient))
            {
                this.recipientsMap.put(recipient, type);
            }
        }

        this.subject = subject;
        this.body = body;

        for (File file : attachments)
        {
            this.attachments.add(file);
        }
    }


    /**
     * Send the email.
     * @throws IOException
     * @throws MessagingException
     */
    public void send() throws MessagingException, IOException
    {
        File[] files = new File[attachments.size()];
        this.attachments.toArray(files);

        send(from, password, recipientsMap, subject, body, files);
    }

    /**
     * Send the email to specified recipients and recipient type, such as TO, CC, BCC.
     * @param recipients the recipients
     * @param type the type
     * @throws MessagingException
     * @throws IOException
     */
    public void sendTo(String[] recipients, RecipientType type) throws MessagingException, IOException
    {
        Map<String, RecipientType> recipientsMap = new HashMap<String, RecipientType>();
        // 添加接收者，及其接收方式
        for (String recipient : recipients)
        {
            if (!this.recipientsMap.containsKey(recipient))
            {
                recipientsMap.put(recipient, type);
            }
        }

        File[] files = new File[attachments.size()];
        this.attachments.toArray(files);
        send(this.from, this.password, this.recipientsMap, this.subject, this.body, files);
    }

    /**
     * Send an email.
     * @param from the sedner' s email address.
     * @param password the password.
     * @param recipientsMap a map of the recipients and type.
     * @param subject the subject of the email.
     * @param body the body of the email.
     * @param attachments one or more attachments can be attached to the email.
     * @throws MessagingException
     * @throws IOException
     */
    public void send(String from, String password, Map<String, RecipientType> recipientsMap,
                     String subject, String body, File...attachments)
            throws MessagingException, IOException
    {
        if (from == null || password == null)
            throw new MessagingException("From address and password can not be null.");

        Session session = getSession(from, password);
        MimeMessage msg = new MimeMessage(session);
        MimeMultipart contentList = new MimeMultipart(); // 邮件内容，包括正文和附件
        MimeBodyPart bodyPart = new MimeBodyPart(); // 邮件正文
        contentList.addBodyPart(bodyPart);
        msg.setContent(contentList);


        // 设置此次发送的发件人
        InternetAddress fromAddr = new InternetAddress(from);
        msg.setFrom(fromAddr); // 设置发件人

        if (recipientsMap == null || recipientsMap.keySet().size() <= 0)
            throw new MessagingException("Provide at least one recipient");

        if (body == null)
            body = "";

        if (subject == null)
            subject = "";

        // 设置邮件接收者
        setRecipients(msg, recipientsMap);

        // 设置邮件主题
        msg.setSubject(subject);

        // 设置邮件正文
        bodyPart.setContent(body, "text/html;charset=utf-8");

        // 添加附件
        if (attachments != null)
        {
            for (File file : attachments)
            {
                addAttachment(contentList, file);
            }
        }

        // 发送
        Transport.send(msg);
    }

    /**
     * Set the sender' s email address.
     * @param from the address. eg. hello@163.com
     * @param password the password.
     */
    public void setFrom(String from, String password)
    {
        this.from = from;
        this.password = password;
    }

    /**
     * 为msg设置接收人
     * @throws MessagingException
     */
    private void setRecipients(MimeMessage msg, Map<String, RecipientType> recipientsMap) throws MessagingException
    {
        RecipientType type = null;
        for (String recipient : recipientsMap.keySet())
        {
            type = recipientsMap.get(recipient);
            msg.addRecipients(type, recipient);
        }
    }

    /**
     * Add a recipient.
     * @param address the recipient' s email address.
     * @param type the RecipientType
     * @throws MessagingException
     */
    public void addRecipient(String address, RecipientType type)
    {
        if (!this.recipientsMap.containsKey(address))
        {
            this.recipientsMap.put(address, type);
        }
    }

    /**
     *  Add a recipient using the default RecipientType<br/>
     * @param address the recipient' s email address.
     * @throws MessagingException
     */
    public void addRecipient(String address)
    {
        if (!this.recipientsMap.containsKey(address))
        {
            this.recipientsMap.put(address, this.type);
        }
    }

    /**
     * Get the Session using specified email address and password.
     * @param from the sender' s email address
     * @param password the password
     * @return the Session.
     */
    private Session getSession(final String from, final String password)
    {
        Properties props = new Properties();
        final int index = from.indexOf("@");
        String host = from.substring(index + 1);
        props.setProperty("mail.host", "smtp." + host); // 设置邮件服务器
        props.setProperty("mail.smtp.auth", "true"); // 需要验证身份

        Authenticator auth = new Authenticator()
        {
            @Override
            protected PasswordAuthentication getPasswordAuthentication()
            {
                return new PasswordAuthentication(from.substring(0, index), password);
            }
        };

        return Session.getInstance(props, auth);
    }

    /**
     *  Set the body of the email.
     * @param body the mail body.
     */
    public void setMailBody(String body)
    {
        this.body = body;
    }

    /**
     * Set the subject of the mail.
     * @param subject the subject.
     */
    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    /**
     * add attachments.
     * @param contentList
     * @param file
     * @throws IOException
     * @throws MessagingException
     */
    private void addAttachment(MimeMultipart contentList, File file) throws IOException, MessagingException
    {
        MimeBodyPart attachment = new MimeBodyPart();
        attachment.attachFile(file);
        attachment.setFileName(MimeUtility.encodeText(file.getName())); // 设置显示的文件名

        contentList.addBodyPart(attachment);
    }

    /**
     *  Add a attachment.
     * @param file the file to be attached.
     * @throws IOException
     * @throws MessagingException
     */
    public void addAttachment(File file) throws IOException, MessagingException
    {
        this.attachments.add(file);
    }

    public static void main(String[] args) throws IOException, MessagingException {
        String from = "1395444343@qq.com";
        String password = "glurdslsdfoobacj";

        String[] to = {"Define_destiny@126.com"};

        String subject = "Test MailUtils";
        String body = "";

        Mail mail = new Mail(from,password, to, RecipientType.TO, subject, body);
        mail.setMailBody("你好，我是正文"); // 设置正文
        mail.send();
        System.out.println("邮件发送成功。");
    }
}

