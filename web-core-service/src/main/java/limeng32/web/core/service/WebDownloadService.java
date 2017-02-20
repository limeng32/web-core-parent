package limeng32.web.core.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import com.aliyun.oss.model.OSSObject;

import limeng32.web.core.exception.WebCoreException;
import limeng32.web.core.exception.WebCoreExceptionEnum;

@Service
public class WebDownloadService {

	public void download(OSSObject ossObject, HttpServletResponse response, String fileName) throws WebCoreException {
		response.setContentType("application/x-msdownload");
		response.setContentLength(((int) (ossObject.getObjectMetadata().getContentLength())));
		try {
			response.setHeader("Content-Disposition",
					"attachment;filename=" + new String(fileName.getBytes("utf-8"), "iso-8859-1"));
		} catch (UnsupportedEncodingException e) {
			response.setHeader("Content-Disposition", "attachment;filename=oddNameFile");
		}
		try {
			OutputStream myout = response.getOutputStream();
			long k = 0;
			byte[] b = new byte[1024];
			BufferedInputStream buff = new BufferedInputStream(ossObject.getObjectContent());
			while (k < ossObject.getObjectMetadata().getContentLength()) {
				int j = buff.read(b, 0, 1024);
				k += j;
				myout.write(b, 0, j);
			}
			myout.flush();
		} catch (IOException e) {
			throw new WebCoreException(WebCoreExceptionEnum.FailToDownloadForIO.toString());
		}
	}

}
