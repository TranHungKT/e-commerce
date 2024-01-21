package com.ecommerce.admin.user.export;

import com.ecommerce.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AbstractExporter {
    public void setResponseHeader( HttpServletResponse httpServletResponse, String contentType, String extension) throws IOException {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String timestamp = dateFormatter.format(new Date());
        String fileName = "user_" + timestamp + extension;

        httpServletResponse.setContentType(contentType);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        httpServletResponse.setHeader(headerKey, headerValue);
    }
}
