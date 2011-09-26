package org.ohm.lebetter.spring.service.impl;

import org.apache.velocity.VelocityContext;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.MailManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.service.MailEngine;
import org.room13.mallcore.spring.service.impl.I18nBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.mail.MailException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.w3c.dom.Document;
import org.w3c.tidy.Tidy;

import javax.mail.internet.MimeMessage;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MailManagerImpl
        extends MailEngine
        implements MailManager, InitializingBean {

    private static final RMLogger log = new RMLogger(MailManagerImpl.class);
    public static final Pattern TITLE_CUTTER = Pattern.compile("\\<title\\>(.*)\\<\\/title\\>");

    protected Tidy tidy;
    protected XPath xPath;
    protected XPathExpression attachmentExpr;

    protected String vmTemplatesPath;
    protected RussianCurrrencyNumberTool currrencyNumberTool = new RussianCurrrencyNumberTool();

    protected I18nBean i18n;

    @Override
    public void afterPropertiesSet() throws Exception {
        tidy = new Tidy();
        tidy.setInputEncoding("UTF-8");
        tidy.setXHTML(false);
        tidy.setErrout(new PrintWriter(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                // do nothing
            }
        }));

        xPath = XPathFactory.newInstance().newXPath();

        attachmentExpr = xPath.compile(".//link[@type='attachment']");
    }

    @Required
    public void setVmTemplatesPath(String vmTemplatesPath) {
        this.vmTemplatesPath = vmTemplatesPath;
    }

    public void setI18n(I18nBean i18n) {
        this.i18n = i18n;
    }

    @Override
    public void sendMailMessage(final String[] rcptEmails,
                                String templateKey,
                                Map<String, Object> params)
            throws MailException {

        if (log.isDebugEnabled()) {
            log.debug("Sending email to " + arr2string(rcptEmails, ";") + " using template " + templateKey);
        }

        InputStream is = getResourseInputStream(templateKey);

        if (is == null) {
            log.debug("Email sending failed due to errors");
            return;
        }

        String messageBody;
        final String subject;

        try {
            log.debug("Preparing email text...");
            StringWriter stringWriter = new StringWriter();
            params.put("number", currrencyNumberTool);
            params.put("locale", new Locale("ru", "RU"));
            if (params.get("i18n") == null) {
                params.put("i18n", i18n);
            }
            velocityEngine.evaluate(new VelocityContext(params), stringWriter,
                                    "LOG", new InputStreamReader(is, "UTF-8"));
            messageBody = stringWriter.toString();

            Document msgXml = tidy.parseDOM(new StringReader(messageBody), null);

            subject = getTitle(messageBody);

            stringWriter.close();
        } catch (Exception e) {
            log.error("", e);
            return;
        }


        // Удаляем собственные теги, например, сведения о вложениях
        final String mBody = cleanMessageBody(messageBody);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                log.debug("Create message object...");
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage, false);
                message.setText(mBody, true);
                message.setSubject(subject == null ? "Lebetter.ru" : subject);
                message.setReplyTo(defaultFrom);
                message.setFrom(defaultFrom);
                message.setTo(rcptEmails);
                log.debug("Message object created");
            }
        };

        send(preparator);
        log.debug("Email was sent");

    }

    protected String cleanMessageBody(String messageBody) {
        return messageBody.
                replaceAll("\\<link\\s.*type=['\"]attachment['\"].*\\</link\\>", "").
                replaceAll("\\<link\\s.*type=['\"]attachment['\"].*?/\\>", "");
    }

    public String getTitle(String messageBody) {
        log.debug("Cut title from html message");
        Matcher matcher = TITLE_CUTTER.matcher(messageBody);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    public void send(final MimeMessagePreparator msg) throws MailException {
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (mailSender) {
                        log.debug("Mail sender thread pool action " +
                                  System.identityHashCode(msg));

                        mailSender.send(msg);

                        log.debug("Mail sender thread pool action completed " +
                                  System.identityHashCode(msg));
                    }
                } catch (MailPreparationException e) {
                    log.error("", e);
                } catch (Exception e) {
                    log.debug("", e);
                }
            }
        });

    }

    @Override
    public void sendMailMessage(UserEntity user,
                                String templateKey,
                                HashMap<String, Object> params)
            throws MailException {

        if (user == null) {
            log.error("Sending email to null user");
            return;
        }

        String email = user.getAddress().getEmail();

        if (email == null) {
            log.error("No user email for " + user.getName());
            return;
        }
        sendMailMessage(new String[]{email}, templateKey, params);
    }

    private String arr2string(String[] strs, String separator) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strs.length; i++) {
            if (i != 0) {
                sb.append(separator);
            }
            sb.append(strs[i]);
        }
        return sb.toString();
    }

    protected InputStream getResourseInputStream(String templateName) {
        try {
            if (vmTemplatesPath.startsWith("classpath:")) {
                String classPathPath = vmTemplatesPath.substring(10);
                if (!classPathPath.endsWith("/")) {
                    classPathPath += "/";
                }
                while (classPathPath.startsWith("/") && classPathPath.length() > 1) {
                    classPathPath = classPathPath.substring(1);
                }

                return getClass().getClassLoader().
                        getResource(classPathPath + templateName + ".vm").openStream();
            } else {
                String fsPath = vmTemplatesPath.startsWith("file:") ?
                                vmTemplatesPath.substring(5) : vmTemplatesPath;
                if (!fsPath.endsWith("/") && !fsPath.endsWith(File.separator)) {
                    fsPath += File.separator;
                }
                return new FileInputStream(fsPath + templateName + ".vm");
            }
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    public static class RussianCurrrencyNumberTool {
        public String currency(Object obj) {
            if (!(obj instanceof Number) ||
                ((Number) obj).floatValue() <= 0) {
                return null;
            }

            Float price = ((Number) obj).floatValue();

            NumberFormat nf = new DecimalFormat("000");
            int cop = (int) (price * 100 - price.intValue() * 100);
            int rub = price.intValue();
            int log1000 = (int) (Math.log(rub) / Math.log(1000));
            StringBuffer sb = new StringBuffer();
            for (int i = log1000; i >= 0; i--) {
                int pow = (int) Math.pow(1000, i);
                String record = i == log1000 ? ("" + rub / pow) : nf.format(rub / pow);
                sb = sb.append(record).append('\u00a0');
                rub %= pow;
            }

            return sb.toString() + "руб." + (cop == 0 ? "" : "\u00a0" + cop + "\u00a0коп.");
        }
    }

}
