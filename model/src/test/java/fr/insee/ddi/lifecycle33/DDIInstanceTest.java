package fr.insee.ddi.lifecycle33;

import fr.insee.ddi.lifecycle33.instance.DDIInstanceDocument;
import fr.insee.ddi.lifecycle33.instance.DDIInstanceType;
import fr.insee.ddi.lifecycle33.reusable.IDType;
import org.apache.xmlbeans.XmlException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DDIInstanceTest {

    @Test
    void createDDIInstanceObject() {
        //
        String fooId = "foo-id";
        //
        DDIInstanceType ddiInstanceType = DDIInstanceType.Factory.newInstance();
        ddiInstanceType.getIDList().add(IDType.Factory.newInstance());
        ddiInstanceType.getIDArray(0).setStringValue(fooId);
        //
        assertEquals(fooId, ddiInstanceType.getIDArray(0).getStringValue());
    }

    @Test
    void parseDDIInstanceObject() throws XmlException, IOException {
        //
        DDIInstanceDocument ddiInstanceDocument = DDIInstanceDocument.Factory.parse(
                this.getClass().getClassLoader().getResourceAsStream("ddi-instance.xml"));
        DDIInstanceType ddiInstanceType = ddiInstanceDocument.getDDIInstance();
        //
        assertEquals(1, ddiInstanceType.getIDList().size());
        assertEquals(1, ddiInstanceType.getAgencyList().size());
        assertEquals(1, ddiInstanceType.getVersionList().size());
        assertEquals("example-id", ddiInstanceType.getIDArray(0).getStringValue());
        assertEquals("fr.insee", ddiInstanceType.getAgencyArray(0));
        assertEquals("1", ddiInstanceType.getVersionArray(0));
    }

}
