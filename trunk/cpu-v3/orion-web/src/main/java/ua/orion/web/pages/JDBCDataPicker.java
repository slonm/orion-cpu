/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.pages;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.tapestry5.ioc.annotations.Inject;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.JDBCUtils;
import ua.orion.cpu.core.persons.entities.Citizenship;
import ua.orion.cpu.core.persons.entities.Passport;
import ua.orion.cpu.core.persons.entities.Person;
import ua.orion.cpu.core.persons.entities.Sex;
import ua.orion.cpu.core.students.entities.AcademicGroup;
import ua.orion.cpu.core.students.entities.Student;

/**
 *
 * @author molodec
 */
public class JDBCDataPicker {

    @Inject
    private EntityService es;

    public Object onSubmitFromGetStudentsForm() throws SQLException, IOException {
        String user = "";
        String password = "";
        String url = "jdbc:firebirdsql://172.16.1.1/zismg";
        String sql = "SELECT * FROM \"Students_Not_Arch\"";
        List<List<String>> data = JDBCUtils.getDataFromDB(url, sql, user, password, null, JDBCUtils.OPPERATION_GET_DATA_MATRIX);
        for (List<String> dataRow : data) {
            Sex sex = es.findUniqueOrPersist(new Sex(dataRow.get(3), dataRow.get(4)));
            Citizenship citizenship = es.findUniqueOrPersist(new Citizenship(dataRow.get(5)));
            Passport passport = es.findUniqueOrPersist(new Passport());
//            es.findUniqueOrPersist(new Student(null, null, null, sql, password, url, null, sex, citizenship, sql, null, null, user));
        }
        return this;
    }
}
