package ua.orion.core;

import ua.orion.core.utils.Defense;

/**
 * Информация о библиотеки модели
 * @author user
 */
public class ModelLibraryInfo {
    private final String libraryName;
    private final String libraryPackage;
    private final boolean tapestryComponentLibrary;

    public ModelLibraryInfo(String libraryName, String libraryPackage, boolean tapestryComponentLibrary) {
        this.libraryName = Defense.notBlank(libraryName, "libraryName");
        this.libraryPackage = Defense.notBlank(libraryPackage, "libraryPackage");
        this.tapestryComponentLibrary = tapestryComponentLibrary;
    }

    public ModelLibraryInfo(String libraryName, String libraryPackage) {
        this.libraryName = Defense.notBlank(libraryName, "libraryName");
        this.libraryPackage = Defense.notBlank(libraryPackage, "libraryPackage");
        this.tapestryComponentLibrary = false;
    }

    public String getLibraryName() {
        return libraryName;
    }

    public String getLibraryPackage() {
        return libraryPackage;
    }

    public boolean isTapestryComponentLibrary() {
        return tapestryComponentLibrary;
    }

    @Override
    public String toString() {
        return "ModelLibraryContribution{" + "libraryName=" + libraryName +
                ", libraryPackage=" + libraryPackage +
                ", tapestryComponentLibrary=" + tapestryComponentLibrary + '}';
    }

}
