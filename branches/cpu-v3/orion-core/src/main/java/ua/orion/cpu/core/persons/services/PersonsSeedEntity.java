package ua.orion.cpu.core.persons.services;

import java.util.HashSet;
import java.util.Set;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.core.annotations.OrderLibrary;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;
import ua.orion.core.utils.DateTimeUtils;
import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.cpu.core.persons.PersonsSymbols;
import ua.orion.cpu.core.persons.entities.*;
import ua.orion.cpu.core.security.entities.Acl;
import ua.orion.cpu.core.security.entities.SubjectType;

/**
 *
 * @author kgp
 */
@OrderLibrary("after:" + OrgUnitsSymbols.ORG_UNITS_LIB)
public class PersonsSeedEntity {
    
    public PersonsSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(PersonsSymbols.PERSONS_LIB));
//        if (testData) {
//
//            //---Списки доступа----------
//            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseReader",
//                    SubjectType.ROLE, "Person:read,menu"));
//            es.findUniqueOrPersist(new Acl("kis.Licensing.LicenseAppender",
//                    SubjectType.ROLE, "Person:read,insert,update,menu"));
//            //---Характер жительства
//            NatureResidence nr_Sm = es.findUniqueOrPersist(new NatureResidence("Сільська місцевість", "с.м."));
//            NatureResidence nr_M = es.findUniqueOrPersist(new NatureResidence("Місто(крім обласних центрів)", "м."));
//            NatureResidence nr_Oc = es.findUniqueOrPersist(new NatureResidence("Обласний центр", "о.ц."));
//            NatureResidence nr_S = es.findUniqueOrPersist(new NatureResidence("Столиця", "ст."));
//            //---Страны
//            Country country_ukraine =
//                    es.findUniqueOrPersist(new Country("Україна", "Українська",
//                    "Київ", "Президентсько-парламентська республіка", "Гривня",
//                    "До складу України входять 27 адміністративних одиниць"));
//            es.findUniqueOrPersist(new Country("Чехія"));
//            es.findUniqueOrPersist(new Country("Вірменія"));
//            es.findUniqueOrPersist(new Country("Дагестан"));
//            es.findUniqueOrPersist(new Country("Грузія"));
//            es.findUniqueOrPersist(new Country("Росія"));
//            //---Регионы
//            Region reg_donbas = es.findUniqueOrPersist(new Region("Донбас", country_ukraine, null));
//            //---Область
//            District dist_lugansk = es.findUniqueOrPersist(new District("Луганська область", reg_donbas, null));
//            //---Місто
//            City city_lugansk = es.findUniqueOrPersist(new City("Луганськ", country_ukraine, reg_donbas, dist_lugansk, nr_Oc, null, null));
//            //---Гражданство
//            Citizenship czship_ukr = es.findUniqueOrPersist(new Citizenship("Українське", country_ukraine));
//            //---Пол
//            Sex male = es.findUniqueOrPersist(new Sex("Чоловічий", "ч."));
//            Sex female = es.findUniqueOrPersist(new Sex("Жіночий", "ж."));
//            es.findUniqueOrPersist(new Sex("Не вказано", ""));
//            //---Пасспорт
//            Passport pass_person = es.findUniqueOrPersist(new Passport("СЮ", "983213245", DateTimeUtils.createCalendar(16, 10, 2007), "Нов.-Волинським МРВ УМВС України в Житомирській обл."));
//            //---Адреса
//            Set address_person = new HashSet();
//            address_person.add(es.findUniqueOrPersist(new Address("32123", country_ukraine, reg_donbas, dist_lugansk, city_lugansk, "ул. Новостроєна", DateTimeUtils.createCalendar(14, 6, 2009), "295-93-42")));
//            address_person.add(es.findUniqueOrPersist(new Address("31425", country_ukraine, reg_donbas, dist_lugansk, city_lugansk, "ул. Іванова", DateTimeUtils.createCalendar(3, 4, 2010), "232-33-56")));
//            //---Персоны
//            Person pers_person = new Person("Станислав", "Олександрович", "Івановнич", DateTimeUtils.createCalendar(14, 6, 1991), male, czship_ukr, "1234567891234", pass_person, DateTimeUtils.createCalendar(6, 8, 2010), "");
//            pers_person.setAddress(address_person);
//            es.findUniqueOrPersist(pers_person);
//            
//        }
    }
}
