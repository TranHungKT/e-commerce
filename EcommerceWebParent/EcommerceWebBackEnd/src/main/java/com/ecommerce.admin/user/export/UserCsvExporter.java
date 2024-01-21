package com.ecommerce.admin.user.export;

import com.ecommerce.admin.user.export.AbstractExporter;
import com.ecommerce.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {

    public void export(List<User> listUser, HttpServletResponse httpServletResponse) throws IOException {
        super.setResponseHeader(httpServletResponse, "text/csv", ".csv");

        ICsvBeanWriter csvWriter = new CsvBeanWriter(httpServletResponse.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"User ID", "E-mail", "First Name", "Last Name", "Roles", "Enabled"};
        String[] fieldMapping = {"id", "email", "firstName", "lastName", "roles", "enabled"};

        csvWriter.writeHeader(csvHeader);

        for (User user : listUser) {
            csvWriter.write(user, fieldMapping);
        }

        csvWriter.close();
    }
}
