package ua.orion.core.services;

import javax.persistence.EntityTransaction;
import org.apache.tapestry5.ioc.ObjectLocator;
import org.apache.tapestry5.jpa.EntityManagerManager;
import org.apache.tapestry5.jpa.EntityManagerSource;
import ua.orion.core.LibraryOrientedBeansFactory;
import ua.orion.core.utils.IOCUtils;

/**
 * Инициализатор персистентного хранилища
 * При вызове run создает все бины для инициализации персистентного хранилища
 * с именем типа бина в формате libraryRootPackage.services.libraryName+SeedEntity.
 * Эти объекты, в свою очередь, инициализируют хранилище.
 * @author sl
 */
public class SeedEntity extends LibraryOrientedBeansFactory implements Runnable {

    private EntityManagerManager entityManagerManager;
    private EntityManagerSource emSource;

    public SeedEntity(ModelLibraryService resolver, ObjectLocator locator,
            EntityManagerManager entityManagerManager,
            final EntityManagerSource emSource) {
        super(resolver, locator, "services", null, "SeedEntity");
        this.entityManagerManager = entityManagerManager;
        this.emSource = emSource;
    }

    @Override
    public void run() {
        EntityTransaction transaction = entityManagerManager.getEntityManager(IOCUtils.getDefaultPersistenceUnitName(emSource)).getTransaction();
        transaction.begin();
        try {
            this.create();
        } catch (final RuntimeException e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }

            throw new RuntimeException("SeedEntity exception", e);
        }
        if (transaction != null && transaction.isActive()) {
            transaction.commit();
        }
    }
}
