package ua.orion.core.services;

import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.jpa.EntityManagerManager;
import ua.orion.core.LibraryOrientedBeansFactory;

/**
 * Инициализатор персистентного хранилища
 * При вызове run создает все бины для инициализации персистентного хранилища
 * с именем типа бина в формате libraryRootPackage.services.libraryName+SeedEntity.
 * Эти объекты, в свою очередь, инициализируют хранилище.
 * @author sl
 */
public class SeedEntity extends LibraryOrientedBeansFactory implements Runnable {

    private EntityManagerManager transactionManager;

    public SeedEntity(ModelLibraryService resolver, ObjectLocator locator,
            EntityManagerManager transactionManager) {
        super(resolver, locator, "services", null, "SeedEntity");
        this.transactionManager = transactionManager;
    }

    @Override
    public void run() {
        this.create();
        transactionManager.getEntityManager("default").getTransaction().commit();
    }
}
