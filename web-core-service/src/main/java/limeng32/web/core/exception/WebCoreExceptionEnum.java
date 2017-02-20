package limeng32.web.core.exception;

public enum WebCoreExceptionEnum {

	CaptchaTextIsNull("生成验证码图片的字符为null"), FailWriteCaptchaStream("写验证码图片的流失败"), ConnotFindToken(
			"Token失效，请稍后尝试"), FailToSendMail("无法发送邮件"), FailToDownloadForIO("因IO异常无法下载");

	private final String description;

	private WebCoreExceptionEnum(String description) {
		this.description = description;
	}

	public String description() {
		return description;
	}
}
