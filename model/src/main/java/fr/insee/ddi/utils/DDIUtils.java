package fr.insee.ddi.utils;

import fr.insee.ddi.lifecycle33.reusable.AbstractIdentifiableType;
import fr.insee.ddi.lifecycle33.reusable.IDType;

/**
 * Utility class that provide some methods for DDI objects.
 */
public class DDIUtils {

    private DDIUtils() {}

    /**
     * Returns a better representation than the "toString" method for a DDI object.
     * @param ddiObject A DDI object.
     * @return String representation of the object.
     */
    public static String ddiToString(Object ddiObject) {
        String className = ddiObject.getClass().getSimpleName();
        if (! (ddiObject instanceof AbstractIdentifiableType ddiIdentifiableObject))
            return className;
        if (ddiIdentifiableObject.getIDList().isEmpty())
            return className + "[id=null]";
        return className + "[id=" + ddiIdentifiableObject.getIDArray(0).getStringValue() + "]";
    }

    /**
     * Returns the identifier of the given DDI object.
     * @param ddiIdentifiableObject A DDI identifiable object.
     * @return String value of the object identifier.
     */
    public static String getIdValue(AbstractIdentifiableType ddiIdentifiableObject) {
        if (ddiIdentifiableObject.getIDList().isEmpty())
            return null;
        return ddiIdentifiableObject.getIDArray(0).getStringValue();
    }

    /**
     * Sets the identifier value of the given DDI object.
     * @param ddiIdentifiableObject A DDI identifiable object.
     * @param id String identifier value.
     */
    public static void setIdValue(AbstractIdentifiableType ddiIdentifiableObject, String id) {
        if (ddiIdentifiableObject.getIDList().isEmpty())
            ddiIdentifiableObject.getIDList().add(IDType.Factory.newInstance());
        ddiIdentifiableObject.getIDArray(0).setStringValue(id);
    }

}
