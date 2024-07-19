# DDI Lifecycle Java

Java library that contains [DDI Lifecycle 3.3](https://ddialliance.org/Specification/DDI-Lifecycle/3.3/) 
classes generated from XSD sources.

Classes are generated using [Apache XMLBeans](https://xmlbeans.apache.org/documentation/index.html).

This library offers deserialization / serialization for DDI objects, using methods of the XMLBeans API.

## Get started

### Requirements

Java 17 or above.

### [Get the dependency](https://mvnrepository.com/artifact/fr.insee.ddi/ddi-lifecycle)

Maven:

```xml
<dependency>
    <groupId>fr.insee.ddi</groupId>
    <artifactId>ddi-lifecycle</artifactId>
    <version>1.0.0</version>
</dependency>
```

Gradle:

```kotlin
implementation("fr.insee.ddi:ddi-lifecycle:1.0.0")
```

### Deserialize / serialize DDI objects

Deserialization example:

```java
String xmlDDI = """
        <DDIInstance xmlns="ddi:instance:3_3"
                     xmlns:r="ddi:reusable:3_3"
                     xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                     xsi:schemaLocation="ddi:instance:3_3 https://www.ddialliance.org/Specification/DDI-Lifecycle/3.3/XMLSchema/instance.xsd">
           <r:Agency>fr.insee</r:Agency>
           <r:ID>example-id</r:ID>
           <r:Version>1</r:Version>
        </DDIInstance>
        """;
DDIInstanceDocument ddiInstanceDocument = DDIInstanceDocument.Factory.parse(xmlDDI);
DDIInstanceType ddiInstanceType = ddiInstanceDocument.getDDIInstance();
```

The `parse(...)` method accepts various input types (url, file, input stream, etc.)

Each DDI Lifecycle object has a "Document" class and a "Type" class created by XMLBeans. 
Document classes are wrappers to deserialize or serialize xml content (with the root node). 
Type classes are those to get or set values in java.

To serialize a DDI object, just call the `toString()` on any DDI document object.

Deserialization/serialization is not restricted to the `DDIInstance` object. 
This example can be applied to any DDI Lifecycle object: `CodeList`, `Sequence`, `QuestionItem`, `Variable`, etc.

Useful link: [DDI model documentation](https://ddialliance.github.io/ddimodel-web/DDI-L-3.3/)

## Other features

### Indexing of a DDI object

You can index the objects contained within a DDI object.

```java
DDIIndex ddiIndex = new DDIIndex();
// Perform indexing
ddiIndex.indexDDIObject(someDDIObject);
// Get an object within the index
AbstractIdentifiableType innerObject = ddiIndex.get("some-inner-object-id");
// The result can be typed
VariableType variable = ddiIndex.get("some-variable-id", VariableType.class);
// Get the parent object in the hierarchy
VariableSchemeType variableScheme = ddiIndex.getParent("some-variable-id", VariableScheme.class);
```

### Utilities

XMLBeans API is pretty verbose, a utility class of the lib offers some QOE methods:

```java
VariableType variable = VariableType.Factory.newInstance();
DDIUtils.setIdValue(variable, "foo-id");
DDIUtils.getIdValue(variable) // "foo-id"
DDIUtils.ddiToString(variable) // "VariableTypeImpl[id=foo-id]"
```

## Requests

If you have a question, request or bug report, feel free to open an issue.
