package fr.insee.ddi.utils;

import fr.insee.ddi.lifecycle33.instance.DDIInstanceType;
import fr.insee.ddi.lifecycle33.reusable.IDType;
import fr.insee.ddi.lifecycle33.reusable.ValueType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DDIUtilsTest {

    @Test
    void ddiIdentifiableObjectToString() {
        //
        String fooId = "foo-id";
        DDIInstanceType ddiInstanceType = DDIInstanceType.Factory.newInstance();
        ddiInstanceType.getIDList().add(IDType.Factory.newInstance());
        ddiInstanceType.getIDArray(0).setStringValue(fooId);
        //
        assertEquals("DDIInstanceTypeImpl[id=foo-id]", DDIUtils.ddiToString(ddiInstanceType));
    }

    @Test
    void ddiIdentifiableObjectToString_idNotSet() {
        //
        DDIInstanceType ddiInstanceType = DDIInstanceType.Factory.newInstance();
        //
        assertEquals("DDIInstanceTypeImpl[id=null]", DDIUtils.ddiToString(ddiInstanceType));
    }

    @Test
    void nonIdentifiableObjectToString() {
        //
        ValueType valueType = ValueType.Factory.newInstance();
        //
        assertEquals("ValueTypeImpl", DDIUtils.ddiToString(valueType));
    }

    @Test
    void getAndSetIdValueTest() {
        //
        DDIInstanceType ddiInstanceType = DDIInstanceType.Factory.newInstance();
        DDIUtils.setIdValue(ddiInstanceType, "foo-id");
        assertEquals("foo-id", DDIUtils.getIdValue(ddiInstanceType));
        // apply the set id method on an object that already have one
        DDIUtils.setIdValue(ddiInstanceType, "bar-id");
        assertEquals("bar-id", DDIUtils.getIdValue(ddiInstanceType));
    }

}
