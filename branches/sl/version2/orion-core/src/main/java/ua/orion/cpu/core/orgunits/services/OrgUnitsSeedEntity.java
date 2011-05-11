package ua.orion.cpu.core.orgunits.services;

import ua.orion.cpu.core.orgunits.OrgUnitsSymbols;
import ua.orion.cpu.core.orgunits.entities.Chair;
import org.apache.tapestry5.ioc.annotations.Symbol;
import ua.orion.cpu.core.OrionCPUSymbols;
import ua.orion.cpu.core.entities.SubSystem;
import ua.orion.core.services.EntityService;

/**
 *
 * @author sl
 */
//@AfterLibrary(OrgUnitsSymbols.ORG_UNITS)
public class OrgUnitsSeedEntity {
    public OrgUnitsSeedEntity(@Symbol(OrionCPUSymbols.TEST_DATA) boolean testData,
            EntityService es) {
        SubSystem subSystem = es.findUniqueOrPersist(new SubSystem(OrgUnitsSymbols.ORG_UNITS_LIB));
        if (testData) {
        //---Кафедры, выполняющие обучение по лицензиям----------
            Chair kafPIT = es.findUniqueOrPersist(new Chair("кафедра програмування та інформаційних технологій", "КПІТ"));
            Chair kafEICPHS = es.findUniqueOrPersist(new Chair("кафедра управління навчальними закладами та педагогіки вищої школи", "КУНЗПВШ"));
            Chair kafSAVM = es.findUniqueOrPersist(new Chair("кафедра системного аналізу та вищої математики", "КСАВМ"));
        }
    }
}
