package limeng32.web.core.service;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;

import limeng32.web.core.exception.WebCoreException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:core-service.xml")
public class WebEmailServiceTest {

	private GreenMail greenMail;

	@Autowired
	private WebEmailService webEmailService;

	@Autowired
	ThirdVelocityEmailService thirdVelocityEmailService;

	public static final String sendFrom = "test@limeng32.com";

	@Before
	public void startMailServer() {
		greenMail = new GreenMail(ServerSetupTest.SMTP);
		greenMail.start();
	}

	@Test
	@IfProfileValue(name = "VOLATILE", value = "true")
	public void testSendMail() throws WebCoreException, InterruptedException, MessagingException {
		String sendTo = "test2@limeng32.com";
		String subject = "Test Subject";
		String htmlText = "<h3>Test</h3>";
		webEmailService.sendMail(sendTo, subject, htmlText);

		greenMail.waitForIncomingEmail(2000, 1);

		Message[] msgs = greenMail.getReceivedMessages();
		assertEquals(1, msgs.length);
		assertEquals(subject, msgs[0].getSubject());
		// assertEquals(htmlText, GreenMailUtil.getBody(msgs[0]).trim());
		assertEquals(sendFrom, (msgs[0].getFrom()[0]).toString());
		assertEquals(sendTo, (msgs[0].getRecipients(RecipientType.TO)[0]).toString());
	}

	@Test
	@IfProfileValue(name = "DEV", value = "true")
	public void testSendMail2() throws WebCoreException, InterruptedException, MessagingException {
		String sendTo = "limeng32@live.cn";
		String subject = "Test中文";
		String htmlText = "<h3>你好，這是一封測試郵件!</h3>";
		webEmailService.sendMail(sendTo, subject, htmlText);
	}

	@Test
	@IfProfileValue(name = "DEV", value = "true")
	public void testSendVelocityMail() throws WebCoreException, InterruptedException, MessagingException {
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("systemName", "科技评奖系统");
		model.put("userName", "曲飚");
		model.put("changePasswordUrl", "http://limeng32.com");
		thirdVelocityEmailService.sendEmail(model, "中国联通科技评奖系统", "limeng32/web/core/service/changePassword.vm",
				new String[] { "limeng32@live.cn" }, new String[] {});

	}

	@After
	public void stopMailServer() {
		greenMail.stop();
	}

}
