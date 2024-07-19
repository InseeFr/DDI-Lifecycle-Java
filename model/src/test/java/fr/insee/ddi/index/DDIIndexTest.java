package fr.insee.ddi.index;

import fr.insee.ddi.exception.DuplicateIdException;
import fr.insee.ddi.lifecycle33.datacollection.SequenceType;
import fr.insee.ddi.lifecycle33.group.ResourcePackageType;
import fr.insee.ddi.lifecycle33.instance.DDIInstanceDocument;
import fr.insee.ddi.lifecycle33.instance.DDIInstanceType;
import fr.insee.ddi.lifecycle33.logicalproduct.VariableSchemeType;
import fr.insee.ddi.lifecycle33.logicalproduct.VariableType;
import fr.insee.ddi.lifecycle33.reusable.AbstractIdentifiableType;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Set;

import static fr.insee.ddi.utils.DDIUtils.getIdValue;
import static fr.insee.ddi.utils.DDIUtils.setIdValue;
import static org.junit.jupiter.api.Assertions.*;

public class DDIIndexTest {

    @Test
    void indexDDIInstanceDocument() {
        //
        DDIInstanceDocument ddiInstanceDocument = DDIInstanceDocument.Factory.newInstance();
        DDIInstanceType ddiInstanceType = DDIInstanceType.Factory.newInstance();
        setIdValue(ddiInstanceType, "instance-id");
        ResourcePackageType resourcePackageType = ResourcePackageType.Factory.newInstance();
        setIdValue(resourcePackageType, "resource-package-id");
        VariableSchemeType variableSchemeType = VariableSchemeType.Factory.newInstance();
        setIdValue(variableSchemeType, "variable-scheme-id");
        VariableType variableType1 = VariableType.Factory.newInstance();
        setIdValue(variableType1, "variable-1-id");
        VariableType variableType2 = VariableType.Factory.newInstance();
        setIdValue(variableType2, "variable-2-id");
        //
        variableSchemeType.getVariableList().add(variableType1);
        variableSchemeType.getVariableList().add(variableType2);
        resourcePackageType.getVariableSchemeList().add(variableSchemeType);
        ddiInstanceType.getResourcePackageList().add(resourcePackageType);
        ddiInstanceDocument.setDDIInstance(ddiInstanceType);

        //
        DDIIndex ddiIndex = new DDIIndex();
        ddiIndex.indexDDI(ddiInstanceDocument);

        //
        assertTrue(ddiIndex.containsId("instance-id"));
        assertTrue(ddiIndex.containsId("resource-package-id"));
        assertTrue(ddiIndex.containsId("variable-scheme-id"));
        assertTrue(ddiIndex.containsId("variable-1-id"));
        assertTrue(ddiIndex.containsId("variable-2-id"));
        //
        assertNull(ddiIndex.get("foo-id"));
        //
        compareDDIObjects(variableType1, ddiIndex.get("variable-1-id"));
        compareDDIObjects(variableType2, ddiIndex.get("variable-2-id"));
        //
        compareDDIObjects(ddiInstanceType, ddiIndex.getParent("resource-package-id"));
        compareDDIObjects(resourcePackageType, ddiIndex.getParent("variable-scheme-id"));
        compareDDIObjects(variableSchemeType, ddiIndex.getParent("variable-1-id"));
        compareDDIObjects(variableSchemeType, ddiIndex.getParent("variable-2-id"));
    }

    /** Utility for this test class. */
    private static void compareDDIObjects(AbstractIdentifiableType expectedObject, AbstractIdentifiableType actualObject) {
        assertEquals(expectedObject.getClass(), actualObject.getClass());
        assertEquals(getIdValue(expectedObject), getIdValue(actualObject));
    }

    @Test
    void indexDDIObject() {
        //
        VariableSchemeType variableSchemeType = VariableSchemeType.Factory.newInstance();
        setIdValue(variableSchemeType, "variable-scheme-id");
        VariableType variableType1 = VariableType.Factory.newInstance();
        setIdValue(variableType1, "variable-1-id");
        VariableType variableType2 = VariableType.Factory.newInstance();
        setIdValue(variableType2, "variable-2-id");
        //
        variableSchemeType.getVariableList().add(variableType1);
        variableSchemeType.getVariableList().add(variableType2);

        //
        DDIIndex ddiIndex = new DDIIndex();
        ddiIndex.indexDDIObject(variableSchemeType);

        //
        assertEquals(Set.of("variable-scheme-id", "variable-1-id", "variable-2-id"), ddiIndex.getIndex().keySet());
    }

    @Test
    void duplicateIdentifiers() {
        //
        VariableSchemeType variableSchemeType = VariableSchemeType.Factory.newInstance();
        setIdValue(variableSchemeType, "variable-scheme-id");
        VariableType variableType1 = VariableType.Factory.newInstance();
        setIdValue(variableType1, "variable-1-id");
        VariableType variableType2 = VariableType.Factory.newInstance();
        setIdValue(variableType2, "variable-1-id");
        //
        variableSchemeType.getVariableList().add(variableType1);
        variableSchemeType.getVariableList().add(variableType2);

        assertThrows(DuplicateIdException.class, () -> new DDIIndex().indexDDIObject(variableSchemeType));
    }

    @Test
    void typedGet() {
        //
        VariableSchemeType variableSchemeType = VariableSchemeType.Factory.newInstance();
        setIdValue(variableSchemeType, "variable-scheme-id");
        VariableType variableType1 = VariableType.Factory.newInstance();
        setIdValue(variableType1, "variable-id");
        variableSchemeType.getVariableList().add(variableType1);
        //
        DDIIndex ddiIndex = new DDIIndex();
        ddiIndex.indexDDIObject(variableSchemeType);
        //
        assertInstanceOf(VariableType.class, ddiIndex.get("variable-id", VariableType.class));
        assertThrows(NoSuchElementException.class, () -> ddiIndex.get("foo-id", VariableType.class));
        assertThrows(ClassCastException.class, () -> ddiIndex.get("variable-id", SequenceType.class));
    }

    @Test
    void typedGetParent() {
        //
        VariableSchemeType variableSchemeType = VariableSchemeType.Factory.newInstance();
        setIdValue(variableSchemeType, "variable-scheme-id");
        VariableType variableType1 = VariableType.Factory.newInstance();
        setIdValue(variableType1, "variable-id");
        variableSchemeType.getVariableList().add(variableType1);
        //
        DDIIndex ddiIndex = new DDIIndex();
        ddiIndex.indexDDIObject(variableSchemeType);
        //
        assertInstanceOf(VariableSchemeType.class, ddiIndex.getParent("variable-id", VariableSchemeType.class));
    }

}
