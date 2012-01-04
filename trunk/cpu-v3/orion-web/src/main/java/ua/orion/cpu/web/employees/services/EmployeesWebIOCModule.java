/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.cpu.web.employees.services;

/**
 *
 * @author molodec
 */
import org.apache.tapestry5.ioc.*;
import org.apache.tapestry5.services.*;
import ua.orion.cpu.core.employees.entities.Employee;
import ua.orion.cpu.core.employees.entities.EmployeePost;
import ua.orion.tapestry.menu.lib.IMenuLink;
import ua.orion.web.services.MenuLinkBuilder;

public class EmployeesWebIOCModule {

    public static void contributeOrionMenuService(MappedConfiguration<String, IMenuLink> configuration,
            MenuLinkBuilder mlb) {
        String path;

        path = "Start>Persons>Employees>Employee";
        configuration.add(path, mlb.buildCrudPageMenuLink(Employee.class, path));
        path = "Start>Persons>Employees>EmployeePost";
        configuration.add(path, mlb.buildCrudPageMenuLink(EmployeePost.class, path));
    }

    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration) {
        configuration.add(new LibraryMapping("employees", "ua.orion.cpu.web.employees"));
    }
}
